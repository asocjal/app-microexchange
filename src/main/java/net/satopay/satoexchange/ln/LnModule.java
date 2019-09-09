package net.satopay.satoexchange.ln;

public class LnModule implements AutoCloseable {
	
	public final Ln ln;
	
	public LnModule() {
		ln = new Ln();
	}
		
	public void registerInvoicePaidListener(InvoicePaidEvent observer) {
		ln.registerInvoicePaidListener(observer);
	}
	
	public void payInvoice(final String invoice, final long amount) {
		ln.payInvoice(invoice, amount);
	}
	
	public void verifyInvoice(String invoice, long requiredAmount) {
		ln.verifyInvoice(invoice, requiredAmount);
	}

	@Override
	public void close() {
		ln.close();	
	}
}
