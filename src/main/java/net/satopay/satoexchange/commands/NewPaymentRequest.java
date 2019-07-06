package net.satopay.satoexchange.commands;

import bittech.lib.protocol.Request;
import bittech.lib.utils.Require;

public class NewPaymentRequest implements Request {

	public String calculationId;

	public NewPaymentRequest(final String calculationId) {
		this.calculationId = Require.notNull(calculationId, "calculationId");
	}

}
