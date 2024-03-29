package net.satopay.satoexchange.banks.bots;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import bittech.lib.utils.Notificator;
import net.satopay.satoexchange.banks.bots.BanksData.BankData;

public class BankBotsModule implements AutoCloseable {
	
	private WebDriver driver;
	private List<Bank> banks;
	
	private BanksData baksData = BanksData.readFromConfig();
	
	private Notificator<BankTxReceivedEvent> notificator = new Notificator<BankTxReceivedEvent>();
	
	public BankBotsModule(boolean visible) {
		initWebDriver(visible);
		
		banks = new LinkedList<Bank>();
		banks.add(new Tmobile(driver, baksData.get("tmobile")) {

			@Override
			public void onReceived(String title, BigDecimal amount) {
				notificator.notifyThem((m) -> m.onBankTxReceived(title, amount));
			}
			
		});
		
		loginToBanks();
//		startWathingThread();
	}
	
	public BankData getBank(String id) {
		return baksData.get(id);
	}
	
	public void registerBankTxReceivedListener(BankTxReceivedEvent listener) {
		notificator.register(listener);
	}
	
	private void initWebDriver(boolean visible) {
		System.setProperty("webdriver.gecko.driver", "geckodriver");

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		FirefoxBinary fb = firefoxOptions.getBinary();
		if(visible == false) {
			fb.addCommandLineOptions("--headless");
		}
		firefoxOptions.setBinary(fb);

		driver = new FirefoxDriver(firefoxOptions);
	}
	
	public void loginToBanks() {
		for(Bank bank : banks) {
			bank.login();
		}
	}
	
	public void startWathingThread() {
		for(Bank bank : banks) {
			List<BankTx> txs = bank.getTransactions();
			for(BankTx bankTx : txs) {
				System.out.println("TX: " + bankTx.title + " -> " + bankTx.amount);
			}
		}
	}

	@Override
	public void close() {
		System.out.print("Stopping banks module...");
		if(driver != null) {
			driver.quit();
		}
		System.out.println("Done");
	}

}
