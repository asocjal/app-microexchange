package net.satopay.satoexchange.commands;

import bittech.lib.protocol.Command;

public class NewPaymentCommand extends Command<NewPaymentRequest, NewPaymentResponse> {

	public static NewPaymentCommand createStub() {
		return new NewPaymentCommand("");
	}
	
	public NewPaymentCommand(String calculationId) {
		this.request = new NewPaymentRequest(calculationId);
	}

}
