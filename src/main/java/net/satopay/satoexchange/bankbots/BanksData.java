package net.satopay.satoexchange.bankbots;

import java.util.Map;

import bittech.lib.utils.Config;
import bittech.lib.utils.exceptions.StoredException;

public class BanksData {
	
	public static class BankData {		
		public String login;
		public String password;
		public String accountNum;
		public String payee;
	}
	
	private Map<String, BankData> banks;
	
	private BanksData() {}
	
	public static BanksData readFromConfig() {
		return Config.getInstance().getEntry("banksData", BanksData.class);
	}
	
	public BankData get(String name) {
		try {
		BankData bd = banks.get(name);
		if(bd == null) {
			throw new Exception("No such bank: " + name);
		}
		return bd;
		} catch(Exception ex) {
			throw new StoredException("Cannot get bank data for name " + name, ex);
		}
	}
	
	public String getAccountNum(String id) {
		BankData bd = get(id);
		return bd.accountNum;
	}

}
