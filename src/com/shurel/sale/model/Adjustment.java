//*******************************************************************//
// Adjustment.class																	
//																							
// Stores information of a Sales's adjustment								
//*******************************************************************//
/**
 * @version 1.0
 * 
 * 1/4/2019
 * 
 * @author Shurel Reynolds (shurel_reynolds@yahoo.com)
 */
package com.shurel.sale.model;

public class Adjustment extends Message {
	/**
	 *
	 * the original value of th sale
	 */
	private long fromValue;

	/**
	 * Initializes an Adjustment object with a String message.
	 * 
	 * @param message
	 *            the message for this Adjustment
	 */
	public Adjustment(String message) {
		super(message);
	}

	/**
	 * Initializes an Adjustment object with a String message and the original
	 * value of the object to be adjusted.
	 * 
	 * @param message
	 *            the message for this Adjustment
	 * @param fromValue
	 *            the original value of the object to be adjusted.
	 */
	public Adjustment(String message, long fromValue) {
		super(message);
		this.fromValue = fromValue;
	}

	/**
	 * Sets the fromValue of the object being adjusted.
	 *
	 * @param fromValue
	 *            the original value of the object to be adjusted.
	 */

	public void setFrom(long fromValue) {
		this.fromValue = fromValue;
	}

	/**
	 *
	 * @return the price representation of the original value.
	 */

	public String getFromPrice() {

		return longToPrice(this.fromValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getTime() + " - " + getMessage() + "  <-\t" + (this.fromValue == 0 ? "N/A" : getFromPrice());
	}

}
