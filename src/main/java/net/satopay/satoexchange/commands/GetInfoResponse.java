package net.satopay.satoexchange.commands;

import bittech.lib.protocol.Response;
import bittech.lib.utils.Require;

public class GetInfoResponse implements Response {

	public final String name;
	public final String email;
	
	public GetInfoResponse(final String name, final String email) {
		this.name = Require.notNull(name, "name");
		this.email = email;
	}

}
