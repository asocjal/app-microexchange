package net.satopay.satoexchange.web.commands;

import bittech.lib.protocol.Response;
import bittech.lib.utils.Require;

public class GetPaymentStatusResponse implements Response {
	
	public String status;
	public String timeLeft;
	
	public GetPaymentStatusResponse() {
		
	}
			
	public GetPaymentStatusResponse(final String status, final String timeLeft) {
		this.status = Require.notNull(status, "status");
		this.timeLeft = Require.notNull(timeLeft, "timeLeft");
	}

}
