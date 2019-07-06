package net.satopay.satoexchange;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import bittech.lib.utils.Require;
import bittech.lib.utils.exceptions.StoredException;
import bittech.lib.utils.json.JsonBuilder;

public class PriceCalculator {

	public static class Calculation {
		public String id;
		public String bankName;
		public int satoshis;
		public BigDecimal price;
	}

	private Map<String, Calculation> caluclatons = new HashMap<String, Calculation>();

	public static final BigDecimal btcPriceZl = new BigDecimal(41000);
	public static final BigDecimal satPriceZl = btcPriceZl.divide(new BigDecimal("100000000"));
	private static final String fileName = "calculations.json";

	private PriceCalculator() {
		// TODO Auto-generated constructor stub
	}
	
	public static PriceCalculator load() {
		File fl = new File(fileName);
		if(fl.exists() == false) {
			return new PriceCalculator();
		}
		
		try {
			try (FileReader fr = new FileReader(fl)) {
				return JsonBuilder.build().fromJson(fr, PriceCalculator.class);
			}
		} catch (Exception ex) {
			throw new StoredException("Cannot load calculations from file " + fileName, ex);
		}
	}
	
	private synchronized void save() {
		try {
			try (FileWriter fw = new FileWriter(new File(fileName))) {
				JsonBuilder.build().toJson(this, fw);
			}
		} catch (Exception ex) {
			throw new StoredException("Cannot save calculations to file " + fileName, ex);
		}
	}

	public synchronized Calculation calculate(String bankName, int satoshis) {
		Calculation calc = new Calculation();
		calc.id = "calc_" + (long) (Math.random() * Long.MAX_VALUE);
		calc.bankName = Require.notEmpty(bankName, "bankName");
		calc.satoshis = Require.inRange(satoshis, 0, 1000000, "satoshis");
		calc.price = satPriceZl.multiply(new BigDecimal(satoshis), new MathContext(2));
		caluclatons.put(calc.id, calc);
		save();
		return calc;
	}

	public synchronized void remove(String id) {
		if (caluclatons.remove(id) == null) {
			throw new StoredException("No such price calculation: " + id, null);
		}
		save();
	}

	public synchronized Calculation get(String id) {
		Calculation calc = caluclatons.get(id);
		if (calc == null) {
			throw new StoredException("No such price calculation: " + id, null);
		}
		return calc;
	}
}
