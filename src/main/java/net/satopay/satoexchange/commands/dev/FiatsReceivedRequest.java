package net.satopay.satoexchange.commands.dev;

import bittech.lib.protocol.Request;
import bittech.lib.utils.Require;

public class FiatsReceivedRequest implements Request {

	public String title;

	public FiatsReceivedRequest(final String title) {
		this.title = Require.notNull(title, "title");
	}
	

}
