package net.satopay.satoexchange.commands;

import bittech.lib.protocol.Response;
import bittech.lib.utils.Require;

public class CheckForFiatsReceivedResponse implements Response {
	
	public final String status;
	
	public CheckForFiatsReceivedResponse(final String status) {
		this.status = Require.notNull(status, "status");
	}

}
