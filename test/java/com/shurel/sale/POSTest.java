package com.shurel.sale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
 
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import static junit.framework.TestCase.fail;
import org.junit.rules.ExpectedException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import com.shurel.sale.observer.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class POSTest{

    
	private static MessageProcessor messageProcessor;
	private static String input[]={ 	"apple at 60p",
			"cherry at £1.20",
			"apple at 50p",
			"wine at 80p",
			"cherry at 20p",
			"flour at £2.50",
			"egg at £1.55",
			"cheese at £.20",
			"sausage at £2.50",
			"ham at £1.55",
			"rice at £.20"};
	

	private static SaleObserver saleObserver = new SaleObserver();
	

@BeforeClass
    public static void initMessageProcessor() {
		messageProcessor= new MessageProcessor();
		messageProcessor.register(saleObserver);
}

@Before
    public void beforeEachTest() {
 
 }

@After
    public void afterEachTest() {
	//saleObserver.getSaleList().clear();
        	System.out.println("End Testing...");
    }
	


@Test
public void a_processItem(){
	boolean b=messageProcessor.processMessage("apple at £1");
	assertTrue(b);

	b=messageProcessor.processMessage("add 10p apple");
	assertTrue(b);

	System.out.println("Total Price Apple: "+messageProcessor.getTotalPrice("apple"));

	b=messageProcessor.processMessage("add 15% apple");
	assertTrue(b);
	System.out.println("Total Price Apple after 15% : "+messageProcessor.getTotalPrice("apple"));
}

 @Test
public void b_reportTen(){
	for(String s:input)
		messageProcessor.processMessage(s);
		assertNotSame(messageProcessor.getLastReport(),"Nothing to Report");
	
	}

@Test 
public void c_quantity(){
	boolean b=messageProcessor.processMessage("20 sales of coconuts at 50p each");
	assertTrue(b);
	assertTrue(messageProcessor.getDistinctSaleByProductType().contains("coconut"));
	assertTrue(messageProcessor.getTotalPrice("coconut").equals("£10.00"));
	}


@Test 
public void d_invalidInput()throws SaleException{
	try{
	messageProcessor.parseMessage("oh well");
	fail();
	}catch(SaleException se)
	{
		assertThat(se.getMessage(), is("Invalid Message"));
	}
	}

@Test 
public void e_invalidAdjustment()throws SaleException{
	try{
	messageProcessor.parseMessage("add 10p grape");
	fail();
	}catch(SaleException se)
	{
		assertThat(se.getMessage(), is("No Such Product Type"));
		}
	}

@Test 
public void f_invalidAdjustmentShortcut()throws SaleException{
	try{
	messageProcessor.parseMessage("+ 40p lemon");
	fail();
	}catch(SaleException se)
	{
		assertThat(se.getMessage(), is("No Such Product Type"));
		}
	}

 @Test
public void g_fiftyInput(){
	
	for(int i=0;i<5;i++)
	for(String s:input)
		messageProcessor.processMessage(s);
		assertTrue(messageProcessor.onPause());
	
	}

}