package net.satopay.satoexchange.web;

import java.math.BigDecimal;

import bittech.lib.protocol.Command;
import bittech.lib.protocol.Listener;
import bittech.lib.protocol.common.NoDataResponse;
import bittech.lib.utils.Require;
import bittech.lib.utils.exceptions.StoredException;
import bittech.lib.utils.json.JsonBuilder;
import lib.satopay.CalcFiatPriceCommand;
import lib.satopay.CalcFiatPriceResponse;
import net.satopay.satoexchange.PriceCalculator.Calculation;
import net.satopay.satoexchange.banks.BanksModule;
import net.satopay.satoexchange.commands.dev.FiatsReceivedCommand;
import net.satopay.satoexchange.core.CoreModule;
import net.satopay.satoexchange.core.Payments.Payment;
import net.satopay.satoexchange.ln.LnModule;
import net.satopay.satoexchange.web.commands.GetInfoCommand;
import net.satopay.satoexchange.web.commands.GetInfoResponse;
import net.satopay.satoexchange.web.commands.GetPaymentStatusCommand;
import net.satopay.satoexchange.web.commands.NewPaymentCommand;
import net.satopay.satoexchange.web.commands.NewPaymentResponse;

public class ApiListener implements Listener, AutoCloseable {
	
	private final CoreModule coreModule;
	private final LnModule lnModule;
	private final BanksModule banksModule;
	
	
	public ApiListener(CoreModule coreModule, LnModule lnModule, BanksModule banksModule) {
		this.coreModule = Require.notNull(coreModule, "coreModule");
		this.lnModule = Require.notNull(lnModule, "lnModule");
		this.banksModule = Require.notNull(banksModule, "banksModule");
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
			Calculation calc = coreModule.calculatePrice(cmd.getRequest().calculationId, cmd.getRequest().amount.toSatRoundFloor());
			cmd.response = new CalcFiatPriceResponse(calc.prices);
			System.out.println("------------- CALC fat price: " + JsonBuilder.build().toJson(cmd));
		} else if (command instanceof FiatsReceivedCommand) {
			FiatsReceivedCommand cmd = (FiatsReceivedCommand) command;
			Payment p = coreModule.fiatsReceived(cmd.getRequest().title, new BigDecimal("0"));
			lnModule.payInvoice(p.lnInvocie, p.calculation.satoshis);
			cmd.response = new NoDataResponse();
		} else if (command instanceof NewPaymentCommand) {
			NewPaymentCommand cmd = (NewPaymentCommand) command;
			Calculation calc = coreModule.getPriceCalculation(Require.notNull(cmd.getRequest().calculationId, "calculationId"));
			lnModule.verifyInvoice(cmd.getRequest().lnInvoice, calc.satoshis);
			Payment payment = coreModule.newPayment(calc, Require.notNull(cmd.getRequest().lnInvoice, "lnInvoice"));
			cmd.response = new NewPaymentResponse();
			cmd.response.amount = payment.calculation.prices.get(cmd.getRequest().bankId);
			cmd.response.bank = banksModule.getBank(cmd.getRequest().bankId);
			cmd.response.timeoutSec = payment.timeoutSec;
			cmd.response.title = payment.id;
			cmd.response.accountNumber = banksModule.getBankDetails(cmd.getRequest().bankId).accountNum;
			cmd.response.payee = banksModule.getBankDetails(cmd.getRequest().bankId).payee;
		} else if (command instanceof GetPaymentStatusCommand) {
			GetPaymentStatusCommand cmd = (GetPaymentStatusCommand) command;
			cmd.response = coreModule.getPaymentStatus(cmd.getRequest().title);
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
