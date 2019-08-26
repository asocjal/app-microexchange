package net.satopay.satoexchange.core;

import net.satopay.satoexchange.PriceCalculator;
import net.satopay.satoexchange.fiat.Banks;
import net.satopay.satoexchange.fiat.Payments;
import net.satopay.satoexchange.ln.Ln;

public class CoreModule implements AutoCloseable {

	public final PriceCalculator priceCalculator = PriceCalculator.load();
	public final Payments payments = Payments.load();
	public final Banks banks = Banks.load();
	public final Ln ln = new Ln();
	
	public CoreModule() {
		ln.registerInvoicePaidListener(payments);
	}

	@Override
	public void close() {
		ln.close();
	}
	
}
