package net.satopay.satoexchange.ln;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bittech.lib.commands.ln.invoices.DecodeInvoiceCommand;
import bittech.lib.commands.ln.invoices.PayInvoiceCommand;
import bittech.lib.protocol.Connection;
import bittech.lib.protocol.Node;
import bittech.lib.utils.Btc;
import bittech.lib.utils.Notificator;
import bittech.lib.utils.exceptions.StoredException;

public class Ln implements AutoCloseable {

	public static int invoiceExpireInMinutes = 6;

	public static final class Invoice {
		public String label;
		public String bolt11;
		public String amount;
		public int expireInMinutes;
		public String diamondId;
		public long createdAt;

		public void countExpiration() {
			long now = new Date().getTime();
			long delta = now - createdAt;
			expireInMinutes = invoiceExpireInMinutes - (int) (delta / 60000);
		}
	}

	private final Node node;
	private final String nodeIp = "176.107.133.70";
	private final int port = 1546;
	private final String peerName = "ln";
	private final Connection connection;

	private final Notificator<InvoicePaidEvent> invoicePaidNotifier = new Notificator<InvoicePaidEvent>();
	
//	private final ReceivedFundsListener receivedFundsListener;

	public Ln() {
		this.node = new Node("LnNodeConnector:" + (long) Math.random());
//		this.receivedFundsListener = new ReceivedFundsListener(node, peerName);
//		this.node.registerListener(receivedFundsListener);
		connection = this.node.connectWithReconnect(peerName, nodeIp, port);
	}

	@Override
	public void close() throws Exception {
		node.close();
	}

	public void registerInvoicePaidListener(InvoicePaidEvent observer) {
		invoicePaidNotifier.register(observer);
	}
	
	public void verifyInvoice(String invoice, long requiredAmount) {
		try {
			DecodeInvoiceCommand cmd = new DecodeInvoiceCommand(invoice);
			connection.execute(cmd);
			if (cmd.error != null) {
				throw new Exception("Lightning node returned error on DecodeInvoiceCommand", cmd.error.toException());
			}
			if (cmd.getResponse().amount != null && !Btc.fromSat(requiredAmount).equals(cmd.getResponse().amount)) {
				throw new Exception("Incorrect invoice. Invoice amount is " + cmd.getResponse().amount
						+ " but it shuld be " + Btc.fromSat(requiredAmount), null);
			}
		} catch (Exception ex) {
			throw new StoredException("Invoice verification error", ex);
		}
	}

	public void payInvoice(final String invoice, final long amount) {
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
			try {
				Thread.sleep(5000);
				PayInvoiceCommand cmd = new PayInvoiceCommand(invoice, Btc.fromSat(amount), Btc.fromSat(amount));
				connection.execute(cmd);
				if (cmd.error != null) {
					throw new Exception("Lightning node returned error on PayInvoiceCommand", cmd.error.toException());
				}
				invoicePaidNotifier.notifyThem((m) -> m.onPay(invoice));
			} catch (Exception ex) {
				throw new StoredException("Invoice payment error", ex);
			}
		});
		

	}

//	public Invoice newInvoice() {
//		long amountSat = 100000;
//		
//		Invoice inv = new Invoice();
//		inv.label = "diamonds" + (long)(Math.random()*Long.MAX_VALUE);
//		inv.expireInMinutes = invoiceExpireInMinutes;
//		inv.amount = "" + Btc.fromSat(amountSat) + " = " + amountSat + " satoshis";
//		
//		InvoiceCommand invoiceCmd = new InvoiceCommand(""+amountSat*1000, inv.label, "payment", inv.expireInMinutes*60);
//		node.execute("ln", invoiceCmd);
//		if(invoiceCmd.getError() != null) {
//			throw new StoredException("Cannot create invoice", new StoredException(invoiceCmd.getError().message, null));
//		}
//		
//		inv.createdAt = new Date().getTime();
//		inv.bolt11 = invoiceCmd.getResponse().bolt11;
//		return inv;
//	}

}
