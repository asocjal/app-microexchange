package net.satopay.satoexchange.web;

import bittech.lib.protocol.Command;
import bittech.lib.protocol.Listener;
import bittech.lib.protocol.common.NoDataResponse;
import bittech.lib.utils.Require;
import bittech.lib.utils.exceptions.StoredException;
import lib.satopay.CalcFiatPriceCommand;
import lib.satopay.CalcFiatPriceResponse;
import net.satopay.satoexchange.PriceCalculator.Calculation;
import net.satopay.satoexchange.commands.dev.FiatsReceivedCommand;
import net.satopay.satoexchange.core.CoreModule;
import net.satopay.satoexchange.fiat.Payments.Payment;
import net.satopay.satoexchange.web.commands.GetInfoCommand;
import net.satopay.satoexchange.web.commands.GetInfoResponse;
import net.satopay.satoexchange.web.commands.GetPaymentStatusCommand;
import net.satopay.satoexchange.web.commands.NewPaymentCommand;
import net.satopay.satoexchange.web.commands.NewPaymentResponse;

public class ApiListener implements Listener, AutoCloseable {
	
	private final CoreModule coreModule;
	
	public ApiListener(final CoreModule coreModule) {
		this.coreModule = Require.notNull(coreModule, "coreModule");
	}
	
	@Override
	public Class<?>[] getListeningCommands() {
		return new Class<?>[] { GetInfoCommand.class, CalcFiatPriceCommand.class, NewPaymentCommand.class,
				FiatsReceivedCommand.class, GetPaymentStatusCommand.class };
	}

	@Override
	public String[] getListeningServices() {
		return null;
	}

	@Override
	public void commandReceived(String fromServiceName, Command<?, ?> command) throws StoredException {

		if (command instanceof GetInfoCommand) {
			GetInfoCommand cmd = (GetInfoCommand) command;
			cmd.response = new GetInfoResponse("SUPERSAT 23", "mailtoadmin@gmail.com");
		} else if (command instanceof CalcFiatPriceCommand) {
			CalcFiatPriceCommand cmd = (CalcFiatPriceCommand) command;
			Calculation calc = coreModule.priceCalculator.calculate(cmd.getRequest().bankId, cmd.getRequest().satoshis);
			cmd.response = new CalcFiatPriceResponse(calc.price, calc.id);
		} else if (command instanceof FiatsReceivedCommand) {
			FiatsReceivedCommand cmd = (FiatsReceivedCommand) command;
			Payment p = coreModule.payments.received(cmd.getRequest().title);
			coreModule.ln.payInvoice(p.lnInvocie, p.calculation.satoshis);
			cmd.response = new NoDataResponse();
		} else if (command instanceof NewPaymentCommand) {
			NewPaymentCommand cmd = (NewPaymentCommand) command;
			Calculation calc = coreModule.priceCalculator.get(Require.notNull(cmd.getRequest().calculationId, "calculationId"));
			coreModule.ln.verifyInvoice(cmd.getRequest().lnInvoice, calc.satoshis);
			Payment payment = coreModule.payments.newPayment(calc, Require.notNull(cmd.getRequest().lnInvoice, "lnInvoice"));
			cmd.response = new NewPaymentResponse();
			cmd.response.amount = payment.calculation.price;
			cmd.response.bank = coreModule.banks.getBank(payment.calculation.bankId);
			cmd.response.timeoutSec = payment.timeoutSec;
			cmd.response.title = payment.id;
		} else if (command instanceof GetPaymentStatusCommand) {
			GetPaymentStatusCommand cmd = (GetPaymentStatusCommand) command;
			cmd.response = coreModule.payments.getStatus(cmd.getRequest().title);
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
