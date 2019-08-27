package net.satopay.satoexchange.fiat;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import bittech.lib.utils.exceptions.StoredException;
import bittech.lib.utils.json.JsonBuilder;

public class Banks {

	public static class Bank {
		public String id;
		public boolean active;
		public String fullName;
		public String iconUrl;
		public String pageUrl;
	}

	private final Map<String, Bank> banks = new HashMap<String, Bank>();
	private static final String fileName = "banks.json";

	private Banks() {
		// TODO Auto-generated constructor stub
	}

	public static Banks load() {
		File fl = new File(fileName);
		if (fl.exists() == false) {
			return new Banks();
		}

		try {
			try (FileReader fr = new FileReader(fl)) {
				return JsonBuilder.build().fromJson(fr, Banks.class);
			}
		} catch (Exception ex) {
			throw new StoredException("Cannot load calculations from file " + fileName, ex);
		}
	}

//	private synchronized void save() {
//		try {
//			try (FileWriter fw = new FileWriter(new File(fileName))) {
//				JsonBuilder.build().toJson(this, fw);
//			}
//		} catch (Exception ex) {
//			throw new StoredException("Cannot save calculations to file " + fileName, ex);
//		}
//	}

	public synchronized Bank getBank(String id) {
		Bank b = banks.get(id);
		if (b == null) {
			throw new StoredException("No bank with id " + id, null);
		}
		return b;
	}

//	public synchronized void createBanks() {
//		{
//			Bank b = new Bank();
//			b.id = "tmobile"; // , , 
//			b.active = true;
//			b.fullName = "T-Mobile usługi bankowe";
//			b.iconUrl = "bank_icons/tmobile.png";
//			b.pageUrl = "https://www.t-mobilebankowe.pl/";
//			b.accountNumber = "01 78732 0987 0001 76876 2142";
//			banks.put(b.id, b);
//		}
//		{
//			Bank b = new Bank();
//			b.id = "bos";
//			b.active = true;
//			b.fullName = "BOŚ bank";
//			b.iconUrl = "bank_icons/bos.png";
//			b.pageUrl = "https://www.bosbank.pl/";
//			b.accountNumber = "02 78732 0987 0001 76876 2142";
//			banks.put(b.id, b);
//		}
//		{
//			Bank b = new Bank();
//			b.id = "mbank";
//			b.active = true;
//			b.fullName = "mBank";
//			b.iconUrl = "bank_icons/mbank.gif";
//			b.pageUrl = "https://www.mbank.pl/indywidualny/";
//			b.accountNumber = "03 78732 0987 0001 76876 2142";
//			banks.put(b.id, b);
//		}
//		{
//			Bank b = new Bank();
//			b.id = "millenium"; // bos, mbank, millenium
//			b.active = true;
//			b.fullName = "Millenium bank";
//			b.iconUrl = "bank_icons/millenium.png";
//			b.pageUrl = "https://www.bankmillennium.pl/";
//			b.accountNumber = "04 78732 0987 0001 76876 2142";
//			banks.put(b.id, b);
//		}
//		save();
//	}

}
