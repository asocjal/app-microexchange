package net.satopay.satoexchange.web.commands;

import bittech.lib.protocol.Command;
import bittech.lib.protocol.common.NoDataRequest;

public class GetInfoCommand extends Command<NoDataRequest, GetInfoResponse> {

	public static GetInfoCommand createStub() {
		return new GetInfoCommand();
	}
	
	public GetInfoCommand() {
		this.request = new NoDataRequest();
	}

}
