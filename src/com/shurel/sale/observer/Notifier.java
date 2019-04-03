//*******************************************************************//
// Notifier.class																	
//																						
// Interface for registering and unregistering Observers				
//*******************************************************************//
/**
 * @version 1.0
 * 
 * 1/4/2019
 * 
 * @author Shurel Reynolds (shurel_reynolds@yahoo.com)
 */
package com.shurel.sale.observer;

import com.shurel.sale.model.Sale;
import com.shurel.sale.model.Adjustment;

public interface Notifier {

	/**
	 * This method will be used to register observers for notification.
	 * 
	 * @param observer
	 *            an observer to be added
	 */
	public void register(SaleObserver observer);

	/**
	 * This method will be used to unregister observers for notification.
	 * 
	 * @param observer
	 *            an observer to be unregistered
	 */
	public void unregister(SaleObserver observer);

	/**
	 * This method will be used to notify observers of a sale.
	 * 
	 * @param sale
	 *            the object being sent for notification
	 */
	public void notifyObservers(Sale sale);

	/**
	 * This method will be used to notify observers of an adjustment.
	 * 
	 * @param adjustment
	 *            the object being sent for notification
	 */
	public void notifyObservers(Adjustment adjustment);

	/**
	 * This method will be used to process incoming messages.
	 * 
	 * @param message
	 *            the message to be processed
	 * @return boolean
	 */
	public boolean processMessage(String message);

}