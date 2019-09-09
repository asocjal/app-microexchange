package net.satopay.satoexchange.web;

import bittech.lib.protocol.ListenersManager;
import lib.satopay.JsonCommandExec;
import net.satopay.satoexchange.banks.BanksModule;
import net.satopay.satoexchange.core.CoreModule;
import net.satopay.satoexchange.ln.LnModule;

public class ApiModule implements AutoCloseable {
	
	private final ListenersManager listenersManager = new ListenersManager();
	private final JsonCommandExec jsonCommandExec = new JsonCommandExec(listenersManager);
	private final ApiListener apiListener;
	
	public ApiModule(final CoreModule coreModule, final LnModule lnModule, final BanksModule banksModule) {
		apiListener = new ApiListener(coreModule, lnModule, banksModule);
		listenersManager.registerListener(apiListener);
	}
	
	public String onReceived(String message) {
		return jsonCommandExec.onReceived(message);
	}

	
	@Override
	public void close() {
		apiListener.close();
	}
	
	
	

}
