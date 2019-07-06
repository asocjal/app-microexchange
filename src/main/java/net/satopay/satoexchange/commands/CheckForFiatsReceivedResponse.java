package net.satopay.satoexchange.commands;

import bittech.lib.protocol.Response;
import bittech.lib.utils.Require;

public class CheckForFiatsReceivedResponse implements Response {

	public final boolean done;
	
	public CheckForFiatsReceivedResponse(final boolean done) {
		this.done = Require.notNull(done, "done");
	}

}
