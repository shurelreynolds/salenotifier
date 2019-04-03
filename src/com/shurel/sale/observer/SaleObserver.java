//*******************************************************************//
// SaleObserver.class																
//																							
// This class observes new Sale and Adjustments								
//*******************************************************************//
/**
 * @version 1.0
 * 
 * 1/4/2019
 * 
 * @author Shurel Reynolds (shurel_reynolds@yahoo.com)
 */

package com.shurel.sale.observer;

import java.util.ArrayList;
import java.util.List;
import com.shurel.sale.model.Adjustment;
import com.shurel.sale.model.Sale;

public class SaleObserver implements MessageObserver<Sale, Adjustment> {
	/**
	 * used to store the list of Sales observed by all SaleObservers.
	 */
	private static List<Sale> saleList = new ArrayList<>();
	/**
	 * used to store the list of Adjustments observed by all SaleObservers.
	 */
	private static List<Adjustment> adjustmentList = new ArrayList<>();

	/**
	 * This method will be called when there is a Sale update.
	 * 
	 * @param sale
	 *            a Sale
	 */

	public void update(Sale sale) {
		System.out.println("Last Sale: " + sale);

	}

	/**
	 * This method will be called when there is an Adjustment made on a Sale.
	 * 
	 * @param adjustment
	 *            a Adjustment
	 */

	public void update(Adjustment adjustment) {
		System.out.println("Last Adjustment: " + adjustment);

	}

	/**
	 * @return the list of Sale object observed by all SaleObservers.
	 */
	public static List<Sale> getSaleList() {
		return saleList;
	}

	/**
	 * @return the list of Sale object observed by all SaleObservers.
	 */
	public static List<Adjustment> getAdjustmentList() {
		return adjustmentList;
	}

}