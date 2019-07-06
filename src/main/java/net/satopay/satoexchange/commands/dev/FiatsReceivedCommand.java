package net.satopay.satoexchange.commands.dev;

import bittech.lib.protocol.Command;
import bittech.lib.protocol.common.NoDataResponse;

public class FiatsReceivedCommand extends Command<FiatsReceivedRequest, NoDataResponse> {

	public static FiatsReceivedCommand createStub() {
		return new FiatsReceivedCommand("");
	}
	
	public FiatsReceivedCommand(final String paymentId) {
		this.request = new FiatsReceivedRequest(paymentId);
	}

}
