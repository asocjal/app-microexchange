package net.satopay.satoexchange.core;

import java.math.BigDecimal;

import net.satopay.satoexchange.PriceCalculator;
import net.satopay.satoexchange.bankbots.BankBotsModule;
import net.satopay.satoexchange.bankbots.BankTxReceivedEvent;
import net.satopay.satoexchange.fiat.Banks;
import net.satopay.satoexchange.fiat.Payments;
import net.satopay.satoexchange.fiat.Payments.Payment;
import net.satopay.satoexchange.ln.Ln;

public class CoreModule implements AutoCloseable, BankTxReceivedEvent {

	public final BankBotsModule bankBotsModule = new BankBotsModule(false);
	public final PriceCalculator priceCalculator = PriceCalculator.load();
	public final Payments payments = Payments.load();
	public final Banks banks = Banks.load();
	public final Ln ln = new Ln();
	
	public CoreModule() {
		ln.registerInvoicePaidListener(payments);
		bankBotsModule.registerBankTxReceivedListener(this);
	}

	@Override
	public void close() {
		ln.close();
		bankBotsModule.close();
	}

	@Override
	public void onBankTxReceived(String title, BigDecimal amount) {
		if(payments.hasPayment(title)) {
			Payment p = payments.received(title, amount);
			ln.payInvoice(p.lnInvocie, p.calculation.satoshis);
		}
	}
	
}
