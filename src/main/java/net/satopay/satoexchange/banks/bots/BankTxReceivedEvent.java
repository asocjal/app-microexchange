package net.satopay.satoexchange.banks.bots;

import java.math.BigDecimal;

public interface BankTxReceivedEvent {

	public void onBankTxReceived(String title, BigDecimal amount);
}
