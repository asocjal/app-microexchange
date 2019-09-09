package net.satopay.satoexchange.banks.bots;

import java.math.BigDecimal;
import java.util.List;

public interface Bank {
	
	public void login();
	public List<BankTx> getTransactions();
	
	public void onReceived(String title, BigDecimal amount);
		

}
