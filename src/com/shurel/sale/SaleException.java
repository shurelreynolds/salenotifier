//*******************************************************************//
// SaleException.class																
//																							
// For exception handling															
//*******************************************************************//
/**
 * @version 1.0
 * 
 * 1/4/2019
 * 
 * @author Shurel Reynolds (shurel_reynolds@yahoo.com)
 */

package com.shurel.sale;

public class SaleException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 *            the message thrown
	 */

	public SaleException(String message) {
		super(message);
	}
}