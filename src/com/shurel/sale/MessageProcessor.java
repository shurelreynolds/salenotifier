//*******************************************************************//
// MessageProcessor.class															
//																							
// Processes all incoming messages												
//*******************************************************************//
/**
 * @version 1.0
 * 
 * 1/4/2019
 * 
 * @author Shurel Reynolds (shurel_reynolds@yahoo.com)
 */
package com.shurel.sale;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import java.util.stream.Collectors;

import com.shurel.sale.observer.Notifier;
import com.shurel.sale.observer.SaleObserver;
import com.shurel.sale.model.Sale;
import com.shurel.sale.model.Adjustment;

public class MessageProcessor extends MessageUtil implements Notifier {
	/**
	 * list of SaleObservers
	 */
	private static List<SaleObserver> saleObservers;
	/**
	 * list of AdjustmentObservers
	 */

	/**
	 * keeps track of the message count received
	 */
	private static int messageCounter;
	
	/**
	 * limit on the number of messages to be processed
	 */
	private static int messageLimit=50;
	
	/**
	 * the interval for message reporting
	 */
	private static int messageInterval=10;

	/**
	 * keeps track of state of the MessageProcessor
	 */
	private boolean paused;
	/**
	 * keeps track of the lastReport
	 */

	private String lastReport = "";

	MessageProcessor() {
		saleObservers = new ArrayList<>();
	}

	@Override
	public void register(SaleObserver so) {
		if (!saleObservers.contains(so))
			saleObservers.add(so);
	}

	@Override
	public void unregister(SaleObserver so) {
		saleObservers.remove(so);
	}

	@Override
	public void notifyObservers(Adjustment adj) {
		SaleObserver.getAdjustmentList().add(adj);
		for (SaleObserver so : saleObservers) {
			so.update(adj);
		}
	}

	@Override
	public void notifyObservers(Sale sale) {
		SaleObserver.getSaleList().add(sale);
		for (SaleObserver so : saleObservers) {
			so.update(sale);
		}
	}

	/**
	 * @param productType
	 *            productType
	 * @return returns the amount for productType
	 */

	public int getAmount(String productType) {

		return SaleObserver.getSaleList().stream().filter(o -> o.getProductType().equals(productType))
				.mapToInt(Sale::getAmount).sum();

	}

	/**
	 * @param productType
	 *            productType
	 * @return returns the total value for productType as price
	 */

	public String getTotalPrice(String productType) {
		return longToPrice(getTotal(productType));
	}

	/**
	 * @return returns the total Sales
	 */
	public String getTotalSale() {

		return longToPrice(getSaleSum());
	}

	/**
	 * @param productType
	 *            productType
	 * @return returns the totoal value for productType
	 */

	public long getTotal(String productType) {

		long total = 0;

		for (Sale s : SaleObserver.getSaleList().stream().filter(o -> o.getProductType().equals(productType))
				.collect(Collectors.toList())) {

			total += s.getAmount() * s.getValue();
		}
		return total;
	}

	/**
	 * @return returns productTypes available
	 */

	public List<String> getDistinctSaleByProductType() {
		List<Sale> sl = SaleObserver.getSaleList().stream()
				.sorted((s1, s2) -> s1.getProductType().compareTo(s2.getProductType()))
				.filter(distinctByKey(p -> p.getProductType())).collect(Collectors.toList());

		List<String> ld = sl.stream().map(Sale::getProductType).collect(Collectors.toList());
		Collections.sort(ld);

		return ld;
	}

	/**
	 * @return returns the total of all Sales in long value.
	 */

	private long getSaleSum() {
		long total = 0;

		for (Sale s : SaleObserver.getSaleList()) {

			total += s.getAmount() * s.getValue();
		}
		return total;

	}

	/**
	 * shows a list of all Sales, their amount and values
	 */

	public void showFullSaleReport() {

		System.out.println("--------Full Report----------\n");
		System.out.println(getFullSaleReport());

		System.out.println("-----End of Full Report------\n\n");

	}

	/**
	 * @return returns a list of all Sales
	 */

	public String getFullSaleReport() {
		StringBuffer sb = new StringBuffer();

		List<String> productTypeList = getDistinctSaleByProductType();

		for (String productType : productTypeList) {
			sb.append(getAmount(productType) + "x\t" + capitalize(productType) + "\t\t\t" + getTotalPrice(productType)
					+ "\n");
		}

		return sb.toString();
	}

	/**
	 * @param tail
	 *            reports the last tail of Sales
	 */

	public void report(int tail) {
		this.lastReport = "Nothing to Report";
		if (SaleObserver.getSaleList().isEmpty()) {
			System.out.println(this.lastReport);
		} else {
			// Assumes that all sales have the same date
			this.lastReport = "--------" + SaleObserver.getSaleList().get(0).getDate() + "---------\n"
					+ getReport(tail);

			System.out.println(this.lastReport + "\n");

		}
	}

	/**
	 * @return returns the last report.
	 */

	public String getLastReport() {
		return this.lastReport;
	}

	/**
	 * @param tail
	 * 			the length of the tail.
	 * @return Report tail of Sales.
	 */

	public String getReport(int tail) {
		StringBuffer sb = new StringBuffer("--------Reporting---------\n");
		List<Sale> tailSail = SaleObserver.getSaleList().subList(Math.max(SaleObserver.getSaleList().size() - tail, 0),
				SaleObserver.getSaleList().size());

		for (Sale s : tailSail)
			sb.append(s + "\n");

		sb.append("Total: " + longToPrice(getSaleSum()) + "\n\n"); // $NON-NLS
		sb.append("-----End of Reporting------\n\n"); //$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Prints out all the Adjustments to the console.
	 * 
	 */
	public void showAdjustments() {
		System.out.println("--------Adjustments---------");
		for (Adjustment adj : SaleObserver.getAdjustmentList()) {
			System.out.println(adj);
		}

		System.out.println("-----End of Adjustments------\n");

	}

	/**
	 * Processes the given message and checks the state before parsing the message
	 * 
	 * @param message
	 *            the message to be processed
	 * @return boolean true if the message has successfully been processed.
	 */
	@Override
	public boolean processMessage(String message) {
		if (paused)
			return false;

		boolean b = true;
		try {

			b = parseMessage(message);

			if ((++messageCounter % messageInterval) == 0) {

				// report the last ten
				report(messageInterval);
			}
			if (messageCounter == messageLimit) {

				pauseSystem();

			}
		} catch (SaleException se) {
			System.out.println(se);
		}
		return b;
	}

	/**
	 * Parses the given message to identify whether it is a new Sale or an
	 * Adjustment
	 * 
	 * @param message
	 *            the message to be parsed
	 * @throws SaleException
	 * 				on invalid imput
	 * @return boolean returns true if the message has successfully been processed.
	 */
	public boolean parseMessage(String message) throws SaleException {
		if (message == null)
			throw new SaleException("No Message given");

		message = message.toLowerCase();
		String tokens[] = message.split(" ");

		int amnt = 1;

		switch (tokens.length) {
		// assumes it is singular
		case 3:

			/*
			 * Operations can be add, subtract, or multiply e.g Add 20p apples
			 * would instruct your application to add 20p to each sale of apples
			 * you have recorded
			 */

			Adjustment adj = new Adjustment(message);
			switch (tokens[0]) {
			case "+": //$NON-NLS-1$
			case "add": //$NON-NLS-1$
				if (tokens[1].endsWith("%")) { //$NON-NLS-1$
					if (adjustPercent(adj, tokens[2], tokens[1], '+'))
						SaleObserver.getAdjustmentList().add(adj);
				} else if (adjustMoney(adj, tokens[2], tokens[1], '+'))
					notifyObservers(adj);
				break;
			case "-": //$NON-NLS-1$
			case "subtract": //$NON-NLS-1$
				if (tokens[1].endsWith("%")) { //$NON-NLS-1$
					if (adjustPercent(adj, tokens[2], tokens[1], '-'))
						notifyObservers(adj);
				} else

				if (adjustMoney(adj, tokens[2], tokens[1], '-'))
					notifyObservers(adj);
				break;
			case "*": //$NON-NLS-1$
			case "multiply": //$NON-NLS-1$
				if (adjustMoney(adj, tokens[2], tokens[1], '*'))
					notifyObservers(adj);
				break;

			default:

				// apple at 10p
				// expects 10, 10p, 0.1, 0.1p,£1,£1.0,£.20,£0.10

				// is valid money
				if (isValidMoney(tokens[2])) {
					Sale ss = new Sale(message,tokens[0], getMoney(tokens[2]));

					notifyObservers(ss);
				}
			}
			break;
		// 20 sales of apples at 10p each.
		case 7:

			try {
				amnt = Integer.parseInt(tokens[0]);
			} catch (NumberFormatException ie) {
				throw new SaleException("Invalid Amount" + tokens[0]);
			}

			if (tokens[3].endsWith("s")) {//$NON-NLS-1$

				tokens[3] = tokens[3].substring(0, tokens[3].length() - 1);

			}
			if (isValidMoney(tokens[5])) {

				notifyObservers(new Sale(message,tokens[3], getMoney(tokens[5]), amnt));

			}

			break;
		default:
			throw new SaleException("Invalid Message");

		}
		return true;
	}

	/**
	 * Makes a percentage adjustment on the specified Sale object
	 * 
	 * @param adj
	 *            the adjustment message
	 * 
	 * @param productType
	 *            the productType to be adjustmented
	 * @param perc
	 *            the percentage amount to be adjusted
	 * 
	 * @param op
	 *            the operation of the adjustment
	 * 
	 * 
	 * @return boolean true if the percent has been successfully processed.
	 */

	private static boolean adjustPercent(Adjustment adj, String productType, String perc, char op)
			throws SaleException {
		// find a sale
		Sale sale = containsSale(productType);

		if (sale == null)
			throw new SaleException("No Such Product Type");
		adj.setFrom(sale.getValue());

		// check for percent first
		final double percent;
		try {
			percent = Double.parseDouble(perc.substring(0, perc.length() - 1));
		} catch (NumberFormatException ie) {
			throw new SaleException("Invalid Percentage Format:" + perc);
		}

		switch (op) {

		case '+':
			// add percent

			SaleObserver.getSaleList().stream().filter(p -> p.getProductType().equals(productType))
					.forEach(s -> s.setValue(addPercentage(s.getValue(), percent)));
			break;

		case '-':
			// deduct percent

			// check if it is greater than 0, only minus then
			SaleObserver.getSaleList().stream().filter(p -> p.getProductType().equals(productType)).forEach(s -> {
				try {
					s.setValue(isValidPercentDeduction(s.getValue(), percent) ? deductPercentage(s.getValue(), percent)
							: s.getValue());
				} catch (SaleException se) {
					System.out.println("Could not deduct: " + percent + "% from " + s.getValue());
				}
			});
			break;
		}

		return true;
	}

	/**
	 * @param money
	 * @exception SaleException
	 * @return returns the long value of the given String
	 */

	private static long getMoney(String money) throws SaleException {
		if (money == null)
			return 0;

		if (money.startsWith("£")) { //$NON-NLS-1$

			try {
				return (int) (Double.parseDouble(money.substring(1)) * 100);
			} catch (NumberFormatException ie) {
				throw new SaleException("Invalid Money Format:" + money);
			}
		}

		else if (money.endsWith("p")) {

			try {
				return Integer.parseInt(money.substring(0, money.length() - 1));
			} catch (NumberFormatException ie) {
				throw new SaleException("Invalid Money Format:" + money);
			}
		} else if (money.contains(".")) {

			try {
				return (int) (Double.parseDouble(money) * 100);
			} catch (NumberFormatException ie) {
				throw new SaleException("Invalid Money Format:" + money);
			}

		}

		else {

			try {
				return Integer.parseInt(money);

			} catch (NumberFormatException ie) {
				throw new SaleException("Invalid Money Format:" + money);
			}
		}

	}

	/**
	 * @param String
	 *            money
	 * @return boolean true if the given String is a valid representation of a price.
	 */

	private static boolean isValidMoney(String money) throws SaleException {
		if (money == null)
			throw new SaleException("Invalid Money Format:");

		if (money.startsWith("£")) {

			try {
				Double.parseDouble(money.substring(1));
				return true;
			} catch (NumberFormatException ie) {
				throw new SaleException("Invalid Money Format:" + money);
			}
		}

		else if (money.endsWith("p")) { //$NON-NLS-1$

			try {
				Integer.parseInt(money.substring(0, money.length() - 1));
				return true;
			} catch (NumberFormatException ie) {
				throw new SaleException("Invalid Money Format:" + money);
			}
		} else if (money.contains(".")) { //$NON-NLS-1$

			try {
				Double.parseDouble(money);
				return true;
			} catch (NumberFormatException ie) {
				throw new SaleException("Invalid Money Format:" + money);
			}
		}

		else {

			try {
				Integer.parseInt(money);
				return true;
			} catch (NumberFormatException ie) {
				throw new SaleException("Invalid Money Format:" + money); //$NON-NLS-1$
			}
		}

	}

	/**
	 * Checks for a Sale by the given productType
	 *
	 * @param the
	 *            String used to identify the Sale object
	 * @return returns Sale for the given productTyoe
	 */

	private static Sale containsSale(String productType) {

		Optional<Sale> matchingObject = SaleObserver.getSaleList().stream()
				.filter(p -> p.getProductType().equals(productType)).findFirst();

		return matchingObject.orElse(null);

	}

	/**
	 * @param number
	 * @param percent
	 * 
	 * @return true is percentage is a valid deduction.
	 */

	private static boolean isValidPercentDeduction(long number, double percent) {
		long value = (long) (number - ((percent / 100) * number));
		if (value <= 0)
			return false;

		return true;

	}

	/**
	 * @param number
	 * @param percent
	 * @exception SaleException
	 * @return the result of the percentage deduction.
	 */
	private static long deductPercentage(long number, double percent) throws SaleException {
		long value = (long) (number - ((percent / 100) * number));
		if (value <= 0)
			throw new SaleException("Percentage too large:" + value);

		return value;

	}

	/**
	 * @param adj
	 *            the Adjustment object
	 * @param productType
	 * @param money
	 * @param op
	 *            the operation to be performed
	 * @exception SaleException
	 * @return boolean true if the money has been adjusted.
	 */

	private static boolean adjustMoney(Adjustment adj, String productType, String money, char op) throws SaleException {
		// find a sale
		Sale sale = containsSale(productType);
		if (sale == null)
			throw new SaleException("No Such Product Type");

		adj.setFrom(sale.getValue());

		// check for percent first

		long pence = getMoney(money);
		switch (op) {

		case '+':
			// add

			SaleObserver.getSaleList().stream().filter(p -> p.getProductType().equals(productType))
					.forEach(s -> s.setValue(s.getValue() + pence));
			break;

		case '-':
			// deduct
			// check if it is greater than 0, only minus then
			SaleObserver.getSaleList().stream().filter(p -> p.getProductType().equals(productType))
					.forEach(s -> s.setValue(s.getValue() - pence > 0 ? s.getValue() - pence : s.getValue()));
			break;
		// multiply by amount
		case '*':
			SaleObserver.getSaleList().stream().filter(p -> p.getProductType().equals(productType))
					.forEach(s -> s.setValue(s.getValue() * pence));
			break;

		}

		return true;
	}

	/**
	 * @return boolean true if paused.
	 */

	public boolean onPause() {
		return this.paused;
	}

	/**
	 * 
	 * pauses this
	 */

	private void pauseSystem() {
		paused = true;

		// show all adjustments
		showAdjustments();
		// show full report
		showFullSaleReport();
		System.out.println("System paused");
	}

}