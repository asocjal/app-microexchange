package net.satopay.satoexchange.web.commands;

import bittech.lib.protocol.Request;
import bittech.lib.utils.Require;

public class GetPaymentStatusRequest implements Request {

	public String title;

	public GetPaymentStatusRequest(final String title) {
		this.title = Require.notNull(title, "title");
	}
	

}
