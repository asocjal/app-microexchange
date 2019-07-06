package net.satopay.satoexchange.commands;

import bittech.lib.protocol.Command;

public class CheckForFiatsReceivedCommand extends Command<CheckForFiatsReceivedRequest, CheckForFiatsReceivedResponse> {

	public static CheckForFiatsReceivedCommand createStub() {
		return new CheckForFiatsReceivedCommand("");
	}
	
	public CheckForFiatsReceivedCommand(final String title) {
		this.request = new CheckForFiatsReceivedRequest(title);
	}

}
