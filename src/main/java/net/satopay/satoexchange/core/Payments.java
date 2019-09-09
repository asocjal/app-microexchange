package net.satopay.satoexchange.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import bittech.lib.utils.Require;
import bittech.lib.utils.exceptions.StoredException;
import bittech.lib.utils.json.JsonBuilder;
import net.satopay.satoexchange.PriceCalculator.Calculation;
import net.satopay.satoexchange.ln.InvoicePaidEvent;
import net.satopay.satoexchange.web.commands.GetPaymentStatusResponse;

public class Payments implements InvoicePaidEvent {

	private enum Status {WAIT_FOR_FIATS, DOING_LN_PAYMENT, DONE, EXPIRED};
	
	public static class Payment {
		public String id;
		public Calculation calculation;
		public String lnInvocie;
		public int timeoutSec;
		public long expireTime;
		public Status status;
	}

	private final Map<String, Payment> waitingPayments = new HashMap<String, Payment>();
	private final Map<String, Payment> donePayments = new HashMap<String, Payment>();
//	private final Map<String, Payment> canceledPayments = new HashMap<String, Payment>();
	private static final String fileName = ".satoex/payments.json";

	private Payments() {
		// TODO Auto-generated constructor stub
	}

	public static Payments load() {
		File fl = new File(fileName);
		if (fl.exists() == false) {
			return new Payments();
		}

		try {
			try (FileReader fr = new FileReader(fl)) {
				return JsonBuilder.build().fromJson(fr, Payments.class);
			}
		} catch (Exception ex) {
			throw new StoredException("Cannot load calculations from file " + fileName, ex);
		}
	}

	private synchronized void save() {
		try {
			try (FileWriter fw = new FileWriter(new File(fileName))) {
				JsonBuilder.build().toJson(this, fw);
			}
		} catch (Exception ex) {
			throw new StoredException("Cannot save calculations to file " + fileName, ex);
		}
	}
	
	private String invoiceToId(String invoice) {
		int id = invoice.hashCode();
		if(id < 0 ) {
			id = -id;
		}
		return "" + id;
	}
	
	public synchronized Payment newPayment(Calculation calculation, String lnInvoice) {
		Payment p = new Payment();
		p.id = invoiceToId(lnInvoice);
		if(waitingPayments.containsKey(p.id)) {
			return waitingPayments.get(p.id);
		}
		if(donePayments.containsKey(p.id)) {
			return donePayments.get(p.id);
		}
		p.calculation = Require.notNull(calculation, "calculation");
		p.lnInvocie = Require.notEmpty(lnInvoice, "lnInvoice"); // TODO: Validate invoice and compare with calculation
		p.timeoutSec = 10 * 60;
		p.expireTime = (new Date()).getTime() + p.timeoutSec * 1000L;
		p.status = Status.WAIT_FOR_FIATS;
		waitingPayments.put(p.id, p);
		save();
		return p;
	}

	public synchronized Payment received(String id, BigDecimal amount) {
		Payment p = waitingPayments.get(id);
		if (p == null) {
			throw new StoredException("No payment with id " + id, null);
		}
		p.status = Status.DOING_LN_PAYMENT;
//		donePayments.put(p.id, p);
		save(); // TODO: Check amount
		return p;
	}

	public synchronized boolean hasPayment(String id) {
		return waitingPayments.get(id) != null;
	}
	
	public synchronized GetPaymentStatusResponse getStatus(String id) {
		GetPaymentStatusResponse resp = new GetPaymentStatusResponse();
		Payment payment = null;
		if ((payment = waitingPayments.get(id)) != null) {
			
			long timeDelta = (payment.expireTime - new Date().getTime()) / 1000;

			if (timeDelta <= 0) {
				payment.status = Status.EXPIRED;
				resp.status = "Expired :(";
				resp.timeLeft = "Close this page and select exchange provider again";
			} else {
				if(payment.status == Status.WAIT_FOR_FIATS) {
					resp.status = "Waiting for bank transfer...";
					long min = timeDelta / 60;
					long sec = timeDelta - min * 60;
					resp.timeLeft = "" + min + " min. " + sec + " sec.";
				} else if(payment.status == Status.DOING_LN_PAYMENT) {
					resp.status = "Funds received on bank account. Sending satoshis...";
					resp.timeLeft = "It should take few seconds.";
				} else {
					throw new StoredException("Unknown status: " + payment.status, null);
				}

			}
			return resp;

		} else if (donePayments.get(id) != null) {
			resp.status = "Succeeded! Fiat funds received :)";
			resp.timeLeft = "Nothing more to do. You can close this page";
			return resp;
		}

		throw new StoredException("No such payment id: " + id, null);
	}

	@Override
	public synchronized void onPay(String invoice) {
		String id = invoiceToId(invoice);
		Payment p = waitingPayments.remove(id);
		if(p != null) {
			p.status = Status.DONE;
			donePayments.put(p.id, p);
			save();
		}
	}

}
