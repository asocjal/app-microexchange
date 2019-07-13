package net.satopay.satoexchange.web.commands;

import bittech.lib.protocol.Command;

public class GetPaymentStatusCommand extends Command<GetPaymentStatusRequest, GetPaymentStatusResponse> {

	public static GetPaymentStatusCommand createStub() {
		return new GetPaymentStatusCommand("");
	}
	
	public GetPaymentStatusCommand(final String title) {
		this.request = new GetPaymentStatusRequest(title);
	}

}
