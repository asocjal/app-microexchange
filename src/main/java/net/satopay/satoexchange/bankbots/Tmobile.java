package net.satopay.satoexchange.bankbots;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import bittech.lib.utils.Require;
import bittech.lib.utils.exceptions.StoredException;
import net.satopay.satoexchange.bankbots.BanksData.BankData;

public abstract class Tmobile implements Bank {

	final WebDriver driver;

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private BankData bankData;

//	private String userId = "secret";
//	private String passwd = "secret";
//	private String accountNumber = "secret";

	private String lastTxId = "";

	public Tmobile(final WebDriver driver, final BankData bankData) {
		this.driver = Require.notNull(driver, "driver");
		this.bankData = Require.notNull(bankData, "bankData");
	}

	public void login() {

		try {
			driver.get("https://system.t-mobilebankowe.pl/web/login");

			{ // LOGIN
				WebElement login = driver.findElement(By.xpath(
						"//*[@id=\"app\"]/div/div[2]/div[2]/div[1]/div[1]/div/div/div/div/div[2]/div/div[2]/div/div/div[3]/div[2]/input"));
				login.sendKeys(bankData.login); // send also a "\n"

				WebElement button = driver.findElement(By.xpath(
						"//*[@id=\"app\"]/div/div[2]/div[2]/div[1]/div[1]/div/div/div/div/div[3]/div/button/div/div/span/span"));
				Thread.sleep(100);
				button.click();
				Thread.sleep(1000);
			}

			{ // HASLO
				WebElement elPasswd = driver.findElement(By.xpath(
						"//*[@id=\"app\"]/div/div[2]/div[2]/div[1]/div[1]/div/div/div/div/div[2]/div/div[3]/div/div/div[3]/div[2]/input"));
				elPasswd.sendKeys(bankData.password);

				Thread.sleep(100);
				WebElement button = driver.findElement(By.xpath(
						"//*[@id=\"app\"]/div/div[2]/div[2]/div[1]/div[1]/div/div/div/div/div[3]/div/button/div/div/span"));
				button.click();
				Thread.sleep(10000);
			}

			{
				String accountNum = bankData.accountNum.replace(" ", "");
				WebElement details = driver.findElement(By.id(accountNum));
				details.click();
				Thread.sleep(10000);

//				WebElement history = driver.findElement(By.xpath("//span[text() = 'Historia']"));

			}

			runWatchThread();

		} catch (Exception ex) {
			throw new StoredException("Error while using TMobile", ex);

		}

	}

	private void runWatchThread() {
		executor.submit(() -> {
			try {
//			WebElement elHistry = driver.findElement(By.xpath("/html/body/div/div/div[6]/div/div/div/div[2]/div[3]/div/div/div/div[2]/div/div[1]/div/div/div/div[2]/div[2]/div"));

				while (true) {
					WebElement history = driver.findElement(By.xpath(
							"/html/body/div/div/div[6]/div/div/div/div[2]/div[3]/div/div/div/div[1]/div[4]/div/div/div[3]/span"));
					history.click();
					Thread.sleep(1000);

					List<WebElement> elsHistry = driver.findElements(By.className("RjVx6R"));

					String firstTxId = null;
					for (WebElement el : elsHistry) {
						String id = el.getAttribute("id");
						if (firstTxId == null) {
							firstTxId = id;
						}
						if (lastTxId.equals(id)) {
							break;
						}

						String title = el.findElement(By.className("RjVxqD")).getText();
						;
						String amountStr = el.findElement(By.className("RjVx3M")).getText();
						amountStr = amountStr.replace(',', '.');
						amountStr = amountStr.replace(" PLN", "");
						amountStr = amountStr.replace(" ", "");
						BigDecimal amount;
						try {
							amount = new BigDecimal(amountStr);
						} catch (Exception ex) {
							throw new Exception("Cannot format amount: " + amountStr);
						}
						onReceived(title, amount);

					}
					
					lastTxId = firstTxId;

					WebElement details = driver.findElement(By.xpath(
							"/html/body/div/div/div[6]/div/div/div/div[2]/div[3]/div/div/div/div[1]/div[4]/div/div/div[1]/span"));
					details.click();
					Thread.sleep(3000);
				}

			} catch (Exception ex) {
				throw new StoredException("Getting history failed", ex);
			}
			// lastValue = elFunds.getText();

//			
//			while(true) {
//				if(!lastValue.equals(elFunds.getText())) {
//					lastValue = elFunds.getText();
//					System.out.println(lastValue);
//				}
//				Thread.sleep(1000);
//			}
		});
	}

	public List<BankTx> getTransactions() {
		List<BankTx> list = new LinkedList<BankTx>();
		try {

			List<WebElement> elements = driver.findElements(By.className("RjVx6R"));

			for (WebElement el : elements) {
				BankTx bankTx = new BankTx();
				bankTx.title = el.findElement(By.className("RjVxqD")).getText();
				String amountStr = el.findElement(By.className("RjVx3M")).getText();
				amountStr = amountStr.replace(',', '.');
				amountStr = amountStr.replace(" PLN", "");
				try {
					bankTx.amount = new BigDecimal(amountStr);
				} catch (Exception ex) {
					throw new Exception("Cannot format amount: " + amountStr);
				}
				list.add(bankTx);
				// - title
				// RjVx3M - amount

//				System.out.println(el.getText());
			}

//			Thread.sleep(1000000);

		} catch (Exception ex) {
			throw new StoredException("Error while using TMobile", ex);

		}
		return list;
	}

}
