package net.satopay.satoexchange.core;

import java.math.BigDecimal;

import bittech.lib.utils.Config;
import bittech.lib.utils.exceptions.StoredException;
import net.satopay.satoexchange.PriceCalculator;
import net.satopay.satoexchange.bankbots.BankBotsModule;
import net.satopay.satoexchange.bankbots.BankTxReceivedEvent;
import net.satopay.satoexchange.fiat.Banks;
import net.satopay.satoexchange.fiat.Payments;
import net.satopay.satoexchange.fiat.Payments.Payment;
import net.satopay.satoexchange.hub.HubModule;
import net.satopay.satoexchange.ln.Ln;

public class CoreModule implements AutoCloseable, BankTxReceivedEvent {

	public final SatoexConfig satoexConfig;
	public final BankBotsModule bankBotsModule;
	public final Banks banks;
	public final PriceCalculator priceCalculator;
	public final Payments payments;
	public final Ln ln;
	public final HubModule hubModule;

	public CoreModule() {
		try {
			satoexConfig = Config.getInstance().getEntry("satoex", SatoexConfig.class);
			bankBotsModule = new BankBotsModule(false);
			banks = Banks.load();
			priceCalculator = new PriceCalculator(banks);
			payments = Payments.load();
			ln = new Ln();
			hubModule = new HubModule(satoexConfig.name, satoexConfig.domain, banks.getActiveBanks());

			ln.registerInvoicePaidListener(payments);
			bankBotsModule.registerBankTxReceivedListener(this);
		} catch (Exception ex) {
			throw new StoredException("Cannot run core module", ex);
		}
	}

	@Override
	public void close() {
		hubModule.close();
		ln.close();
		bankBotsModule.close();
	}

	@Override
	public void onBankTxReceived(String title, BigDecimal amount) {
		if (payments.hasPayment(title)) {
			Payment p = payments.received(title, amount);
			ln.payInvoice(p.lnInvocie, p.calculation.satoshis);
		}
	}

}
