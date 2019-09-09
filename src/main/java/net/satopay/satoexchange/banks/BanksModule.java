package net.satopay.satoexchange.banks;

import java.util.List;

import bittech.lib.utils.exceptions.StoredException;
import net.satopay.satoexchange.banks.Banks.Bank;
import net.satopay.satoexchange.banks.bots.BankBotsModule;
import net.satopay.satoexchange.banks.bots.BankTxReceivedEvent;
import net.satopay.satoexchange.banks.bots.BanksData.BankData;

public class BanksModule implements AutoCloseable {

	public final BankBotsModule bankBotsModule;
	public final Banks banks;

	public BanksModule() {
		try {
			bankBotsModule = new BankBotsModule(false);
			banks = Banks.load();
		} catch (Exception ex) {
			throw new StoredException("Cannot run core module", ex);
		}
	}
	
	public List<String> getActiveBanks() {
		return banks.getActiveBanks();
	}
	
	public void registerBankTxReceivedListener(BankTxReceivedEvent listener) {
		bankBotsModule.registerBankTxReceivedListener(listener);
	}
	
	public Bank getBank(String id) {
		return banks.getBank(id);
	}
	
	public BankData getBankDetails(String id) {
		return bankBotsModule.getBank(id);
	}

	@Override
	public void close() {
		bankBotsModule.close();		
	}
	

}
