package net.satopay.satoexchange.commands;

import bittech.lib.protocol.Request;
import bittech.lib.utils.Require;

public class NewPaymentRequest implements Request {

	public String calculationId;
	public String lnInvoice;

	public NewPaymentRequest(final String calculationId, final String lnInvoice) {
		this.calculationId = Require.notNull(calculationId, "calculationId");
		this.calculationId = Require.notNull(lnInvoice, "lnInvoice");
	}

}
