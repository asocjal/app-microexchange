package net.satopay.satoexchange.ln;

public interface InvoicePaidEvent {
	
	public void onPay(String invoice);

}
