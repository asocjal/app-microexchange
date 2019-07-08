package net.satopay.satoexchange.commands;

import java.math.BigDecimal;

import bittech.lib.protocol.Response;
import net.satopay.satoexchange.fiat.Banks.Bank;

public class NewPaymentResponse implements Response {

	public Bank bank;
	public BigDecimal amount;
	public String title;
	public int timeoutSec;
	
	public NewPaymentResponse() {

	}

}
