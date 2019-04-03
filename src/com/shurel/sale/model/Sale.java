//*******************************************************************//
// Sale.class																			
//																							
// Stores the amount, productType and value of a Sale						
//*******************************************************************//
/**
 * @version 1.0
 * 
 * 1/4/2019
 * 
 * @author Shurel Reynolds (shurel_reynolds@yahoo.com)
 */
package com.shurel.sale.model;

public class Sale extends Message {
	/**
	 * amounts to 1 by default.
	 */
	private int amount = 1;
	/**
	 * the product type
	 */
	private String productType;
	/**
	 * the value
	 */
	private long value;

	/**
	 * Initializes with message, product type and value
	 * 
	 * @param message
	 *            the original message of this Sale
	 * @param productType
	 *            the product type of this Sale
	 * @param value
	 *            value of this Sale
	 */
	public Sale(String message,String productType, long value) {
		super(message);
		this.productType = productType;
		this.value = value;

	}

	/**
	 * Initializes with message, product type, value and amount
	 * @param message
	 *            the original message of this Sale
 	 * @param productType
	 *            the product type of this Sale
	 * @param value
	 *            value of this Sale
	 * @param amount
	 *            the amount for this Sale
	 */

	public Sale(String message,String productType, long value, int amount) {
		this(message,productType, value);
		this.amount = amount;
	}

	/**
	 * @return returns the product type for this Sale
	 */

	public String getProductType() {
		return this.productType;
	}

	/**
	 * @return returns the amount for this Sale
	 */

	public int getAmount() {
		return this.amount;
	}

	/**
	 * @return returns the value as a price for this Sale
	 */

	public String getPrice() {

		return getValue() < 100 ? getValue() + "p" : "£" + formatter.format(getValue() / 100d);
	}

	/**
	 * @param value
	 *            sets the value
	 */
	public void setValue(long value) {
		this.value = value;
	}

	/**
	 * @return returns the value for this Sale
	 */

	public long getValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return getTime() + " \t " + getAmount() + "x\t" + capitalize(getProductType()) + "\t\t@ " + getPrice();
	}

}