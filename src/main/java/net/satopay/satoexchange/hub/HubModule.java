package net.satopay.satoexchange.hub;

import bittech.lib.protocol.Connection;
import bittech.lib.protocol.Node;
import bittech.lib.utils.Config;
import bittech.lib.utils.Require;
import bittech.lib.utils.exceptions.StoredException;
import lib.satopay.Exchange;
import lib.satopay.RegisterExchangeCommand;
import net.satopay.satoexchange.banks.BanksModule;
import net.satopay.satoexchange.core.CoreModule;
import net.satopay.satoexchange.core.SatoexConfig;

public class HubModule implements AutoCloseable {

	private final Node node;
	private final Connection connection;
	private final ApiListener apiListener;

	public HubModule(final CoreModule coreModule, final BanksModule banksModule) {
		try {
			Require.notNull(coreModule, "coreModule");
			
			SatopayConnection satopayConnectionConf = Config.getInstance().getEntry("satopay", SatopayConnection.class);
			SatoexConfig satoexConf = Config.getInstance().getEntry("satoex", SatoexConfig.class);
			
			node = new Node("Microex" + (long) (Math.random() * Long.MAX_VALUE));
			connection = node.connectWithReconnect("Microex" + (long) (Math.random() * Long.MAX_VALUE),
					satopayConnectionConf.host, satopayConnectionConf.port);
			
			apiListener = new ApiListener(coreModule);
			node.registerListener(apiListener);
			
			Exchange exchange = new Exchange();
			exchange.active = satopayConnectionConf.active;
			exchange.name = Require.notEmpty(satoexConf.name, "exchange name");
			exchange.domain = Require.notEmpty(satoexConf.domain, "exchange domain");
			exchange.banksSupported = Require.notNull(banksModule.getActiveBanks(), "banksSupported");
			RegisterExchangeCommand cmd = new RegisterExchangeCommand(exchange);
			connection.execute(cmd);
			if (cmd.getError() != null) {
				throw cmd.getError().toException();
			}
		} catch (Exception ex) {
			throw new StoredException("Cannot create hub module", ex);
		}
	}

	@Override
	public void close() {
		if(node != null) {
			node.close();
		}
	}

}
