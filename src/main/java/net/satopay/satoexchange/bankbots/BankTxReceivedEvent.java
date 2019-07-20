package net.satopay.satoexchange.bankbots;

import java.math.BigDecimal;

public interface BankTxReceivedEvent {

	public void onBankTxReceived(String title, BigDecimal amount);
}
