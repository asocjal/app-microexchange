package net.satopay.satoexchange;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import bittech.lib.utils.JsonFile;
import bittech.lib.utils.Require;
import bittech.lib.utils.Utils;
import bittech.lib.utils.exceptions.StoredException;
import bittech.lib.utils.json.JsonBuilder;
import net.satopay.satoexchange.fiat.Banks;

public class PriceCalculator {

	public static class Calculation {
		public String id;
		public int satoshis;
		public Map<String, BigDecimal> prices;// Calculation per bank
	}
	
	public static class Data extends JsonFile {

		private Map<String, Calculation> caluclatons = new HashMap<String, Calculation>();
		
		@Override
		public void onLoad() {
			// Nothing more
		}

	}
	
	private Data data;

	

	public static final BigDecimal btcPriceZl = new BigDecimal(41000);
	public static final BigDecimal satPriceZl = btcPriceZl.divide(new BigDecimal("100000000"));
	private static final String fileName = ".satoex/calculations.json";
	
	private final Banks banks;
	
	public PriceCalculator(final Banks banks) {
		this.data = Data.load(fileName, Data.class);
		this.banks = Require.notNull(banks, "banks");
	}
	
//	public static PriceCalculator load() {
//		File fl = new File(fileName);
//		if(fl.exists() == false) {
//			return new PriceCalculator();
//		}
//		
//		try {
//			try (FileReader fr = new FileReader(fl)) {
//				return JsonBuilder.build().fromJson(fr, PriceCalculator.class);
//			}
//		} catch (Exception ex) {
//			throw new StoredException("Cannot load calculations from file " + fileName, ex);
//		}
//	}
//	
//	private synchronized void save() {
//		try {
//			try (FileWriter fw = new FileWriter(new File(fileName))) {
//				JsonBuilder.build().toJson(this, fw);
//			}
//		} catch (Exception ex) {
//			throw new StoredException("Cannot save calculations to file " + fileName, ex);
//		}
//	}

	public synchronized Calculation calculate(String calculationId, int satoshis) {
		
		Calculation calc = new Calculation();
		calc.id = calculationId;
		calc.satoshis = Require.inRange(satoshis, 0, 1000000, "satoshis");
		
		BigDecimal price = satPriceZl.multiply(new BigDecimal(satoshis), new MathContext(2));
		
		calc.prices = new HashMap<String, BigDecimal>();
		for(String bankId : banks.getActiveBanks()) {
			calc.prices.put(bankId, price);
		}

		data.caluclatons.put(calc.id, calc);
		data.save();
		return Utils.deepCopy(calc, Calculation.class);
	}

//	public synchronized void remove(String id) {
//		if (caluclatons.remove(id) == null) {
//			throw new StoredException("No such price calculation: " + id, null);
//		}
//		save();
//	}

	public synchronized Calculation get(String id) {
		Calculation calc = data.caluclatons.get(id);
		if (calc == null) {
			throw new StoredException("No such price calculation: " + id, null);
		}
		return calc;
	}
}
