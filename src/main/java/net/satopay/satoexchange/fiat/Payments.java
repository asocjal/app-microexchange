package net.satopay.satoexchange.fiat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import bittech.lib.utils.Require;
import bittech.lib.utils.exceptions.StoredException;
import bittech.lib.utils.json.JsonBuilder;
import net.satopay.satoexchange.PriceCalculator.Calculation;

public class Payments {

	public static class Payment {
		public String id;
		public Calculation calculation;
		public String lnInvocie;
		public int timeoutSec;
	}

	private final Map<String, Payment> waitingPayments = new HashMap<String, Payment>();
	private final Map<String, Payment> donePayments = new HashMap<String, Payment>();
//	private final Map<String, Payment> canceledPayments = new HashMap<String, Payment>();
	private static final String fileName = "payments.json";

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

	public synchronized Payment newPayment(Calculation calculation, String lnInvoice) {
		Payment p = new Payment();
		p.id = "pay_" + (long) (Math.random() * Long.MAX_VALUE);
		p.calculation = Require.notNull(calculation, "calculation");
		p.lnInvocie = Require.notEmpty(lnInvoice, "lnInvoice"); // TODO: Validate invoice and compare with calculation
		p.timeoutSec = 10 * 60;
		waitingPayments.put(p.id, p);
		save();
		return p;
	}

	public synchronized void received(String id) {
		Payment p = waitingPayments.remove(id);
		if (p == null) {
			throw new StoredException("No payment with id " + id, null);
		}
		donePayments.put(p.id, p);
		save();
	}

	public synchronized String getStatus(String id) {
		if(waitingPayments.get(id) != null) {
			return "Waiting for bank transfer...";
		} else if(donePayments.get(id) != null) {
			return "Succeeded! Fiat funds received :)";
		}
		throw new StoredException("No such payment id: " + id, null);
	}

}
