package net.satopay.satoexchange.web.commands;

import java.math.BigDecimal;

import bittech.lib.protocol.Response;
import net.satopay.satoexchange.banks.Banks.Bank;

public class NewPaymentResponse implements Response {

	public Bank bank;
	public BigDecimal amount;
	public String title;
	public String accountNumber;
	public String payee;
	public int timeoutSec;
	
	public NewPaymentResponse() {

	}

}
