package com.techelevator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Application {
	private static final String MAIN_MENU_DISPLAY_VENDING_ITEMS = "Display Vending Items";
	private static final String MAIN_MENU_PURCHASE = "Purchase";
	private static final String MAIN_MENU_EXIT = "Exit";
	private static final String SALES_REPORT = "!!Sales Report";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_DISPLAY_VENDING_ITEMS, MAIN_MENU_PURCHASE, MAIN_MENU_EXIT, SALES_REPORT,};
	private static final String FEED_MONEY = "Feed Money";
	private static final String SELECT_PRODUCT = "Select Product";
	private static final String FINISH_TRANSACTION = "Complete Purchase";
	private static final String[] PURCHASE_MENU_OPTIONS = {FEED_MONEY, SELECT_PRODUCT, FINISH_TRANSACTION};
	private String[] activeMenu = MAIN_MENU_OPTIONS;

	private boolean running = false;  //may have deleted this before accidentally ?
	private VendingMachine vm;
	//private Item it; // the original version had Inv.Loader, but NOT Part
	public Map<String, List<Item>> inventory;

	public static void main(String[] args) {
		Application app = new Application();
	        app.run();
	}

	private void run() {
		this.vm = new VendingMachine();
		inventory = new HashMap<>();
		//this.it = new Item();  // turned off b/c orig did not include new Part
// DEBUG ONLY
		//vm.timeStamp();
		//vm.logTransaction();
		Item purchaseItem = null;
		//vm.salesReport();
		vm.loadStockList(vm); //note this used to fire off load in the il (? Im not sure if the (vm) argument is needed now

		vm.loadInventory(vm); // need to ?? REBUILD this in original form



//      CREATING WORKING MENU
		Scanner userInput = new Scanner(System.in);
//TODO	Output a welcome screen
		System.out.println("---------------- Welcome to the Vendo-Matic 800 ----------------");
		System.out.println("----------------------------------------------------------------");
		System.out.println("--------- Please make a selection from the menu below. ---------");

		// --------------------------------- Initiate Menu System -----------------------------

		this.running = true;

		while (running) {

			displayMenu();
			System.out.print("Please select an option: ");

			Integer userEntry = 0;
			try {
				userEntry = Integer.parseInt(userInput.nextLine());

				if (userEntry > activeMenu.length || userEntry < 1 ) {
					userEntry = 1;
				}
			} catch (NumberFormatException nfe) {
				userEntry = 1;
			}
			String optionChosen = activeMenu[userEntry - 1];

			switch (optionChosen) {
				case MAIN_MENU_DISPLAY_VENDING_ITEMS:
					//System.out.println(vm.stockList);
					checkInventory();
					break;

				case MAIN_MENU_PURCHASE:
					this.activeMenu = PURCHASE_MENU_OPTIONS;
					System.out.println("\n");
					break;

				case MAIN_MENU_EXIT:
					System.out.println("\n Have a nice day! Goodbye.");
					this.running = false;
					break;

				case SALES_REPORT:
					//vm.salesReport();

					//System.out.println();
//TODO Write sales report
					break;

				case FEED_MONEY:
					vm.feedMoney();
					break;

				case SELECT_PRODUCT:
					checkInventory();
					vm.selectItemFromList();
					break;

				case FINISH_TRANSACTION: //add in method for getting change and returning to main menu
					vm.completePurchase();


					break;
				//default:  public void main(){
				//	System.out.println("back in menu");
				//}
			}

		}// NOTE:  After whatever case action happens above, control returns to this spot afterwards.

}


 	private void printStockList() { //not currently used but it works!!

		System.out.println("Slot  " + " Name  " + "Price  " + "QTY");

		for(Map.Entry<String, Item> stockItem : vm.stockList.entrySet()){
			//System.out.println( stockItem.getKey() + ": " + stockItem.getValue().toString());
			System.out.println(stockItem.getValue().getSlot() + "     " + stockItem.getValue().getItemName() + "      " + stockItem.getValue().getPrice());
		}
		System.out.println("-----------------------------------");
	}
// -----------------------------------------------------------------------------------------------------------------
	private void checkInventory(){
		System.out.println();
		System.out.println();
		System.out.println("Slot  |" + " Name    | "   +   "Price | "   +   "QTY ");
//TODO Continue to refine the list displayed - formatting is rudimentary and hard to read
		for(Map.Entry<String, List<Item>> slotItems : vm.inventory.entrySet()){

			String itemName = vm.stockList.get(slotItems.getKey()).getItemName();
			Double tempItemPrice = vm.stockList.get(slotItems.getKey()).getPrice();

//			System.out.println( "Slot:  " + slotItems.getKey() + ") has " + slotItems.getValue().size() + " " + itemName );
			System.out.println(slotItems.getKey() + ":   " + itemName + "  -  " + tempItemPrice + "  - " + slotItems.getValue().size());
			//System.out.printf("%"  slotItems.getKey() + ":   " + itemName + "  -  " + tempItemPrice + "  - " + slotItems.getValue().size());
		}
		System.out.println("----------------------------------------------------------------");
		System.out.println();

	}
	
// ------------------------------------------------------------------------------------------------------------------
	public void displayMenu () {

		for (int i = 0; i < activeMenu.length; i++ ) {

			String menuOptionNumber = (i + 1) + ") ";
			if (!activeMenu[i].startsWith("!!")) {
				System.out.println(menuOptionNumber + activeMenu[i]);
			}

		}
		System.out.println("\n");

	}


}