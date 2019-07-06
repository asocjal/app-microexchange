package net.satopay.satoexchange;

import bittech.lib.commans.general.PingCommand;
import bittech.lib.commans.general.PingResponse;
import bittech.lib.protocol.Command;
import bittech.lib.protocol.Listener;
import bittech.lib.protocol.common.NoDataResponse;
import bittech.lib.utils.exceptions.StoredException;
import net.satopay.satoexchange.PriceCalculator.Calculation;
import net.satopay.satoexchange.commands.CalcFiatPriceCommand;
import net.satopay.satoexchange.commands.CalcFiatPriceResponse;
import net.satopay.satoexchange.commands.CheckForFiatsReceivedCommand;
import net.satopay.satoexchange.commands.CheckForFiatsReceivedResponse;
import net.satopay.satoexchange.commands.NewPaymentCommand;
import net.satopay.satoexchange.commands.NewPaymentResponse;
import net.satopay.satoexchange.commands.dev.FiatsReceivedCommand;
import net.satopay.satoexchange.fiat.Payments;
import net.satopay.satoexchange.fiat.Payments.Payment;

public class ApiListener implements Listener {

	private final PriceCalculator priceCalculator = PriceCalculator.load();
	private final Payments payments = Payments.load();

	@Override
	public Class<?>[] getListeningCommands() {
		// TODO Auto-generated method stub
		return new Class<?>[] { PingCommand.class, CalcFiatPriceCommand.class, NewPaymentCommand.class,
				FiatsReceivedCommand.class, CheckForFiatsReceivedCommand.class };
	}

	@Override
	public String[] getListeningServices() {
		return null;
	}

	@Override
	public void commandReceived(String fromServiceName, Command<?, ?> command) throws StoredException {
		if (command instanceof PingCommand) {
			PingCommand cmd = (PingCommand) command;
			cmd.response = new PingResponse("PONGi PONGi: " + cmd.getRequest().message);
		} else if (command instanceof CalcFiatPriceCommand) {
			CalcFiatPriceCommand cmd = (CalcFiatPriceCommand) command;
			Calculation calc = priceCalculator.calculate(cmd.getRequest().bankName, cmd.getRequest().satoshis);
			cmd.response = new CalcFiatPriceResponse(calc.price, calc.id);
		} else if (command instanceof FiatsReceivedCommand) {
			FiatsReceivedCommand cmd = (FiatsReceivedCommand) command;
			payments.received(cmd.getRequest().title);
			cmd.response = new NoDataResponse();
		} else if (command instanceof NewPaymentCommand) {
			NewPaymentCommand cmd = (NewPaymentCommand) command;
			Calculation calc = priceCalculator.get(cmd.getRequest().calculationId);
			Payment payment = payments.newPayment(calc);
			cmd.response = new NewPaymentResponse();
			cmd.response.amount = payment.calculation.price;
			cmd.response.bankAccountNumber = payment.accountNum;
			cmd.response.timeoutSec = payment.timeoutSec;
			cmd.response.title = payment.id;
		} else if (command instanceof CheckForFiatsReceivedCommand) {
			CheckForFiatsReceivedCommand cmd = (CheckForFiatsReceivedCommand) command;
			cmd.response = new CheckForFiatsReceivedResponse(payments.isDone(cmd.getRequest().title));
		} else {
			throw new StoredException("Unsupported command type: " + command.type, null);
		}

	}

	@Override
	public void responseSent(String serviceName, Command<?, ?> command) {
		// TODO Auto-generated method stub

	}

}
