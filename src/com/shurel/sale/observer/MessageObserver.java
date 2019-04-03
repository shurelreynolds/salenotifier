//*******************************************************************//
// MessageObserver.class																			
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
package com.shurel.sale.observer;

import com.shurel.sale.MessageUtil;

public interface MessageObserver<S, A extends MessageUtil> {

	/**
	 * This method will be called when there is a MessageUtil update.
	 * 
	 * @param t
	 *            a MessageUtil
	 */
	public void update(S t);

}