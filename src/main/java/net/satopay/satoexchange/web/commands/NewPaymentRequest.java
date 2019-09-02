package net.satopay.satoexchange.web.commands;

import bittech.lib.protocol.Request;
import bittech.lib.utils.Require;

public class NewPaymentRequest implements Request {

	public String calculationId;
	public String bankId;
	public String lnInvoice;

	public NewPaymentRequest(final String calculationId, final String bankId, final String lnInvoice) {
		this.calculationId = Require.notNull(calculationId, "calculationId");
		this.bankId = Require.notNull(bankId, "bankId");
		this.lnInvoice = Require.notNull(lnInvoice, "lnInvoice");
	}

}
