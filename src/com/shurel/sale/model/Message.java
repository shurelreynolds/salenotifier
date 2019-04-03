//*******************************************************************//
// Message.class																		
//																						
// 																					
//*******************************************************************//
/**
 * @version 1.0
 * 
 * 1/4/2019
 * 
 * @author Shurel Reynolds (shurel_reynolds@yahoo.com)
 */
package com.shurel.sale.model;

import java.time.*;

import com.shurel.sale.MessageUtil;

abstract class Message extends MessageUtil {
	/**
	 * the body of the message
	 * 
	 */

	private String message;

	/**
	 * Initializes with message
	 * 
	 * @param message
	 *            the body of this Message
	 */

	public Message(String message) {
		this.message = message;
		this.timeStamp = Instant.now().getEpochSecond();
	}

	/**
	 * This method returns the contents of this message.
	 * 
	 * @return 
	 *            the contents of this message
	 */

	public String getMessage() {
		return this.message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return getTime() + " - " + getMessage();
	}

}