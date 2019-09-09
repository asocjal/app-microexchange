package net.satopay.satoexchange.core;

import java.math.BigDecimal;

import bittech.lib.utils.Require;
import bittech.lib.utils.exceptions.StoredException;
import net.satopay.satoexchange.PriceCalculator;
import net.satopay.satoexchange.PriceCalculator.Calculation;
import net.satopay.satoexchange.banks.BanksModule;
import net.satopay.satoexchange.banks.bots.BankTxReceivedEvent;
import net.satopay.satoexchange.core.Payments.Payment;
import net.satopay.satoexchange.ln.LnModule;
import net.satopay.satoexchange.web.commands.GetPaymentStatusResponse;

public class CoreModule implements BankTxReceivedEvent, AutoCloseable {
		
	private final PriceCalculator priceCalculator;	
	private final Payments payments;

	private final LnModule lnModule;
	
	public CoreModule(BanksModule banksModule, LnModule lnModule) {
		try {			
			this.lnModule = Require.notNull(lnModule, "lnModule");
			
			payments = Payments.load();
					
			priceCalculator = new PriceCalculator(banksModule);

			lnModule.registerInvoicePaidListener(payments);
			banksModule.registerBankTxReceivedListener(this);
		} catch (Exception ex) {
			throw new StoredException("Cannot run core module", ex);
		}
	}

	
	@Override
	public void onBankTxReceived(String title, BigDecimal amount) {
		if (payments.hasPayment(title)) {
			Payment p = payments.received(title, amount);
			lnModule.payInvoice(p.lnInvocie, p.calculation.satoshis);
		}
	}
	
	public Calculation calculatePrice(String calculationId, int satoshis) {
		return priceCalculator.calculate(calculationId, satoshis);
	}
	
	public Payment fiatsReceived(String id, BigDecimal amount) {
		return payments.received(id, amount);
	}
	
	public Payment newPayment(Calculation calculation, String lnInvoice) {
		return payments.newPayment(calculation, lnInvoice);
	}
	
	public Calculation getPriceCalculation(String id) {
		return priceCalculator.get(id);
	}
	
	public GetPaymentStatusResponse getPaymentStatus(String id) {
		return payments.getStatus(id);
	}
	
	@Override
	public void close() {
		// Nothing for now
	}

}
