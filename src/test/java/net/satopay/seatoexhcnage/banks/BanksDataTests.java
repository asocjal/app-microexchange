package net.satopay.seatoexhcnage.banks;

import bittech.lib.utils.json.JsonBuilder;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.satopay.satoexchange.banks.bots.BanksData;

/**
 * Unit test for simple App.
 */
public class BanksDataTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public BanksDataTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(BanksDataTests.class);
	}
	
	public void testLipny() {
		
	}

	public void testBasic() throws Exception {

		BanksData banksData = BanksData.readFromConfig();
//		
//		BankData bt = new BankData();
//		bt.usr = "userId";
//		bt.pwd = "userPassword";
//		
//		banksData.banks.put("tmobile", bt);
		
		System.out.println(JsonBuilder.build().toJson(banksData));
	}

}
