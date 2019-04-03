#### SaleNotifier
		- maintains a list of Sale objects
		- maintains a list of Adjustment objects
		- keeps track of the number of messages received
		- pauses after number of messages received
		- reports messages received at specified interval
		- reports all Adjustments
		- keeps track of the last message received
		- processes and parses messages
		- gives the total sale
		- gives a detail report
		- tails sales

##### Observers - listeners to incoming messages
		- maintains a list of SaleObserver objects
		- registers, unregister and notifies SaleObserver objects

##### ProductType - parameter
		- gives the amount for a specified product type
		- gives the total for a specified product type
		- gives a list of distinct product types

##### Message Formats: A sale of 1 apple at 20p:
		apple at 20p
		20 sales of apples at 50p each
		mango at £1.50
		
##### Adjustment Formats:
		add 20p apples
		+ 10p cherry
		subtract 60p cabbage
		- 15p jam
		* 10% onion
		* 10p oranges

##### Error Handling
Messages are checked during parsing for validity
