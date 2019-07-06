package net.satopay.satoexchange.commands;

import java.math.BigDecimal;

import bittech.lib.protocol.Response;

public class NewPaymentResponse implements Response {

	public String bankAccountNumber;
	public BigDecimal amount;
	public String title;
	public int timeoutSec;
	
	public NewPaymentResponse() {

	}

}
