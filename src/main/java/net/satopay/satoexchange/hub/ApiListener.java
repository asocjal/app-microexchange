package net.satopay.satoexchange.hub;

import bittech.lib.protocol.Command;
import bittech.lib.protocol.Listener;
import bittech.lib.utils.Require;
import bittech.lib.utils.exceptions.StoredException;
import bittech.lib.utils.json.JsonBuilder;
import lib.satopay.CalcFiatPriceCommand;
import lib.satopay.CalcFiatPriceResponse;
import net.satopay.satoexchange.PriceCalculator.Calculation;
import net.satopay.satoexchange.core.CoreModule;

public class ApiListener implements Listener, AutoCloseable {
	
	private final CoreModule coreModule;
	
	public ApiListener(final CoreModule coreModule) {
		this.coreModule = Require.notNull(coreModule, "coreModule");
	}
	
	@Override
	public Class<?>[] getListeningCommands() {
		return new Class<?>[] { CalcFiatPriceCommand.class };
	}

	@Override
	public String[] getListeningServices() {
		return null;
	}

	@Override
	public void commandReceived(String fromServiceName, Command<?, ?> command) throws StoredException {

		if (command instanceof CalcFiatPriceCommand) {
			CalcFiatPriceCommand cmd = (CalcFiatPriceCommand) command;
			System.out.println(" -------- Command received: " + JsonBuilder.build().toJson(cmd));
			Calculation calc = coreModule.priceCalculator.calculate(cmd.getRequest().calculationId, cmd.getRequest().amount.toSatRoundFloor());
			cmd.response = new CalcFiatPriceResponse(calc.prices);
			System.out.println("------------- CALC fat price: " + JsonBuilder.build().toJson(cmd));
		} else {
			throw new StoredException("Unsupported command type: " + command.type, null);
		}

	}

	@Override
	public void responseSent(String serviceName, Command<?, ?> command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// Nothing for now
	}

}
