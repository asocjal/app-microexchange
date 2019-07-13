package net.satopay.satoexchange.web.commands;

import bittech.lib.protocol.Command;

public class NewPaymentCommand extends Command<NewPaymentRequest, NewPaymentResponse> {

	public static NewPaymentCommand createStub() {
		return new NewPaymentCommand("", "");
	}
	
	public NewPaymentCommand(final String calculationId, final String lnInvoice) {
		this.request = new NewPaymentRequest(calculationId, lnInvoice);
	}

}
