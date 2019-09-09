package net.satopay.seatoexhcnage.banks;

import java.math.BigDecimal;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.satopay.satoexchange.banks.bots.BankBotsModule;
import net.satopay.satoexchange.banks.bots.BankTxReceivedEvent;

/**
 * Unit test for simple App.
 */
public class TMobileTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public TMobileTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TMobileTests.class);
	}
	
	public void testLipny() {
		
	}

	public void testBasic() throws Exception {

		try(BankBotsModule banks = new BankBotsModule(false)) {
			banks.registerBankTxReceivedListener(new BankTxReceivedEvent() {

				@Override
				public void onBankTxReceived(String title, BigDecimal amount) {
					System.out.println("Received: " + title + " -> " + amount);
				}
				
			});
			Thread.sleep(1000000);
		}
	}

}
