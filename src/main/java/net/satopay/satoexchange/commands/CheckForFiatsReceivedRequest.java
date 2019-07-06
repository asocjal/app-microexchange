package net.satopay.satoexchange.commands;

import bittech.lib.protocol.Request;
import bittech.lib.utils.Require;

public class CheckForFiatsReceivedRequest implements Request {

	public String title;

	public CheckForFiatsReceivedRequest(final String title) {
		this.title = Require.notNull(title, "title");
	}
	

}
