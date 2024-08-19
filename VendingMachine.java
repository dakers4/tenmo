package com.techelevator;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.*;

import java.io.FileOutputStream;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class VendingMachine {
    public Map<String, Item> stockList;
    public Map<String, List<Item>> inventory;// putting this back in
    //public Map<String, BigDecimal> priceList;  //Deactivated ..for new map to handle JUST slot and penny price
    public ArrayList inventoryList; //This is new - was not part of declared variables in original loader// leaving this in but as of Saturday morning not using it

    private static final String FILE_LOCATION = "vendingmachine.csv";
    private static final String SALES_LOG_LOCATION = "saleslog.txt";
    private static final String ACTIVITY_LOG_LOCATION = "log.txt";

    private Double currentMoneyProvided = 0.00;
    private String customerCurrentSelection = "";
    List<Item> logSalesList = new ArrayList<Item>();
    final private Integer VM_SLOT_CAPACITY = 5;
    // logging variables below
    private static final DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    private static final String LOG_ENTRY_FORMAT = "%s: %s";
    //experimental routing
    private boolean logicSwitchA;


    // logging end
    public VendingMachine() {

        stockList = new HashMap<>();
        inventory = new HashMap<>();
    }

    public Item purchase(String slot) {
        Item result = null;
        if (this.inventory.get(slot).size() > 0) {
            result = this.inventory.get(slot).remove(0);
        }
        logSalesList.add(result); //debug

        String logFeed = this.stockList.get(customerCurrentSelection).getItemName() + " " + String.valueOf(this.stockList.get(customerCurrentSelection).getPrice()) + " " + String.valueOf(currentMoneyProvided);
        logTransaction(logFeed);
        //below is for debug1
        //if(customerCurrentSelection.equals("D4")) {//Debug: when you buy a D4 it cuts you straight to sales report. NEEED to remoe this when finished testing.
        //    System.out.println("match sending to SalesLog");
        //    salesReport();
        //}



        return result; // result is sent to log List but not returned to anywhere.
        // below is testing for access to values in the log and in the stocklist

        //System.out.println("from using get customer key like above but to get slot" + this.stockList.get(customerCurrentSelection).getSlot());
        //System.out.println("test for stocklist pull: " + this.stocklisl.get;

//
//            int productCounter = 0;
//
//            for (int s = 0; s < logSalesList.size(); s ++) {
//                if (product == logSalesList.get(s).getSlot()) {
//                    productCounter ++;
//                }



    }


    public void feedMoney() {  // this should return the customers change due

        System.out.println("How much would you like to deposit (in whole dollars): ");
        Scanner userInput = new Scanner(System.in);
        String strMoneyFedIn = userInput.nextLine();
        Double moneyFedIn = Double.parseDouble(strMoneyFedIn);

        if (moneyFedIn <= 0.00 || (moneyFedIn > 0.00 && moneyFedIn < 1.00)) {
            System.out.println("Amount cannot be negative, and must be in whole dollars.  Please try again.");
            //moneyFedIn = 0.00;
            Scanner userInput1 = new Scanner(System.in);
            String strMoneyFedIn1 = userInput1.nextLine();
            moneyFedIn = Double.parseDouble(strMoneyFedIn1);
        }
        currentMoneyProvided += moneyFedIn;

        String logFeed = "FEED MONEY: " + String.valueOf(moneyFedIn) + " " + String.valueOf(currentMoneyProvided);
        logTransaction(logFeed);

        String printCustomerBalance = String.format("Current money provided: $%.2f", currentMoneyProvided);
        System.out.println(printCustomerBalance + "\n" + "Enter (2) to Select the refreshment you'd like to purchase." + "\n");

    }




    public void selectItemFromList() {
//TODO  I would really like a way for the user to select multiple items - but would there then have to
//TODO  be a way to also remove from cart or "start over"?
        String printCustomerBalance = String.format("Current money provided: $%.2f", currentMoneyProvided);
        System.out.println(printCustomerBalance);
        System.out.println("What Item do you want to purchase? Please enter slot number: ");
        //TODO If a product has already been selected, replace the above output with "You selected <product name>; Enter (3) to complete your purchase"
        //TODO test with crazy inputs to see if errFlag logic works
        boolean errFlag = false;
        Scanner userSlotSelection = new Scanner(System.in);
        String usersSelection = userSlotSelection.nextLine().toUpperCase();

        if (!inventory.containsKey(usersSelection)) {
            errFlag = true;
            System.out.println("Please enter a valid slot number");
        }

        if (!errFlag && this.inventory.get(usersSelection).size() <= 0) {
            System.out.println("This item is OUT OF STOCK.  Please make another selection.");
        }
        customerCurrentSelection = usersSelection;
        System.out.println("You selected " + this.inventory.get(customerCurrentSelection).get(0) + "   Please select (3) to Complete Purchase" + "\n");
    }

    //TODO cycle though to test THIS out of stock warning (there is also one in another method).
    public void completePurchase() {// Selection (3) sends us hered
        if (this.stockList.get(customerCurrentSelection).getPrice() <= currentMoneyProvided) {
            System.out.println("You've selected " + this.inventory.get(customerCurrentSelection).get(0));

            currentMoneyProvided -= this.stockList.get(customerCurrentSelection).getPrice();

            switch (this.stockList.get(customerCurrentSelection).getItemType()) {

                case "Chip":
                    System.out.println("Crunch Crunch, Yum!");
                    break;
                case "Drink":
                    System.out.println("Glug Glug, Yum!");
                    break;
                case "Gum":
                    System.out.println("Chew Chew, Yum!");
                    break;
                case "Candy":
                    System.out.println("Munch Munch, Yum!");
                default:
                    System.out.println();
                    //TODO It would be GREAT to:  right after we show the glug glug output, we ask for input "would you like to purchase somethign else?  If no, execute refund and send them to the main menu; if yes, they still have their money balance and could make more purchases!!
            }
            System.out.println(" ____________________________________________");
            // call method to remove from inventory
            purchase(this.stockList.get(customerCurrentSelection).getSlot());

            System.out.println("Your purchase is complete, enjoy!");
            refundInCoins();  //move this to if loop - true that no more purchases

        } else {
            System.out.println("Please feed more money in order to make this purchase:");
            feedMoney();
        }


    }

    public void refundInCoins() { //may need to change to BigDecimal

        // coin machine//
        int pennyPriceRefund = (int) (100 * currentMoneyProvided);
        int chgDue = pennyPriceRefund;
        final int QUARTER = 25;
        final int DIME = 10;
        final int NICKEL = 5;

        //compute change//
        int Qcount = chgDue / QUARTER;
        chgDue -= (Qcount * QUARTER);
        int Dcount = chgDue / DIME;
        chgDue -= (Dcount * DIME);
        int Ncount = (chgDue / NICKEL);


        String printChangeDueLineFormat = String.format("Your refund is $%.2f", currentMoneyProvided);

        System.out.println(printChangeDueLineFormat);
        System.out.println("Here are: " + Qcount + " Quarters, " + Dcount + " Dimes and " + Ncount + " Nickels.  Don't spend it all in one place..." + "\n");

        String giveChange = "GIVE CHANGE: " + String.valueOf(currentMoneyProvided) + " " + "0.00";
        logTransaction(giveChange);

        currentMoneyProvided = 0.00;  //need to reset balance to 0 after refund!

//TODO Instructions say that after this the user needs to return to the main menu - right now it is the purchase menu


    }


    public void loadStockList(VendingMachine vm) {
        Map<String, Item> result = new HashMap<>();

        try {
            File dataFile = new File(FILE_LOCATION);

            try (Scanner fileScanner = new Scanner(dataFile)) {

                while (fileScanner.hasNext()) {

                    String[] data = fileScanner.nextLine().split("\\|");

                    String slotLocation = data[0];

                    Item newItem = new Item(data[0], data[1], Double.parseDouble(data[2]), data[3]);

                    result.put(slotLocation, newItem);
                }
            }
            vm.stockList = result;

        } catch (IOException ex) {
            System.out.println("file read problem:" + ex.getMessage());
        }
    }


//___________________________________________________________________________________________________________________

    public void loadInventory(VendingMachine vm) {
        Map<String, List<Item>> result = new HashMap<>();
        List<Item> slotItemList;  // this was binPartList in original

        try {
            File dataFile = new File(FILE_LOCATION);

            try (Scanner fileScanner = new Scanner(dataFile)) {

                while (fileScanner.hasNext()) {

                    String[] data = fileScanner.nextLine().split("\\|");

                    String slot = data[0];  //does slot need to be a class variable?

                    //int count = Integer.parseInt(data[4]); we don't have this, it was the number of each item.  In our case this is always 5;  Consider a constant here VM_SLOT_CAPACITY = 5?

                    slotItemList = new ArrayList<>();

                    for (int i = 0; i < VM_SLOT_CAPACITY; i++) {
                        slotItemList.add(new Item(data[0], data[1], Double.parseDouble(data[2]), data[3]));
                    }
                    result.put(slot, slotItemList);
                }
            }
            vm.inventory = result;

        } catch (IOException iox) {
            System.out.println(iox.getMessage());
        }

    }





    public String timeStamp() {

        String ts = "";

        LocalDateTime dateTimeNow = LocalDateTime.now();
        ts = dateTimeNow.format(LOG_DATE_FORMAT);

        return ts;
    }




    public void logTransaction(String svar) {

        try (PrintWriter loge = new PrintWriter(new FileOutputStream("C:/Users/Student/workspace/mod-1-capstone-team-1/Logfile.txt", true))) {

            loge.println(String.format(LOG_ENTRY_FORMAT, timeStamp(), svar));

        } catch (IOException iox) {
            //ALLIGATOR CATCH: NOT A GOOD PRACTICE! FOR DEMO ONLY
        }
    }



    public void salesReport() {
        Scanner sales = new Scanner(System.in);
        System.out.println("Would you like to generate the Sales Report? (Y)es or (N)o ?");
        String userInput = sales.nextLine().toUpperCase();
//TODO the below kepp in for now for testing, while we make the sales report loop work.
//        System.out.println("----------below from stocklist-------get slot key A2--++++------------------");
//        System.out.println(this.stockList.get("A2").getItemName());
//        System.out.println(logSalesList.size());
//        System.out.println(logSalesList.get(0).getItemName());
//        System.out.println(logSalesList.get(1).getItemName());


        Double saleTotalforLog = 0.00;
        // Using keySet() to get all keys
        Set<String> keys = stockList.keySet();

        // Iterating over the keys and printing associated values
        for (String key : keys) {
            int soldCounter = 0;
            String tempNameforLoop = "";
            Double salesDollars = 0.00;
            for (int s = 0; s < logSalesList.size(); s++){
                if(key.equals(logSalesList.get(s).getSlot())) {
                    soldCounter++;
                    salesDollars += logSalesList.get(s).getPrice();
                    tempNameforLoop = logSalesList.get(s).getItemName();
                }
            }
        //Below for testing purposes only
        String stringForSalesLog = (tempNameforLoop + " |"+  String.valueOf(soldCounter) + "|" + String.valueOf(salesDollars));
        saleTotalforLog += salesDollars;
            try (PrintWriter sr = new PrintWriter(new FileOutputStream("C:/Users/Student/workspace/mod-1-capstone-team-1/SalesReport.txt", true))) {
                sr.println(stringForSalesLog);

            } catch (IOException ioex) {
                System.out.println(ioex.getMessage());

            }


        }

    }




    }
//
//        for (logSalesList.size()) {
//            System.out.println(logSalesList);
//        }
//        //start generating sales report
//        for (int i = 0; i < stockList.size(); i++ ) {
//            String product = this.stockList.get(customerCurrentSelection).getSlot();
//
//            int productCounter = 0;
//
//            for (int s = 0; s < logSalesList.size(); s ++) {
//                if (product == logSalesList.get(s).getSlot()) {
//                    productCounter ++;
//                }
//            }
//            String salesByProduct = String.valueOf(this.stockList.get(customerCurrentSelection).getItemName()) + " | " + String.valueOf(productCounter);
//            System.out.println(salesByProduct);
//        }

//        if (userInput == "Y") {
//            System.out.println("Ready for report");
//
//
//
//        }






//___________________________________________________________________________________________________________________
    // The method below was created to load details into a List but the list could not be persistent withtin or outside the Class so rewriting to load per the example PartsCounter
    // IF THE REWRITE FROM SATURDAY MORNING DOES NOT WORK THEN CONSIDER CREATING A NEW UTILITY LOADER CLASS AS IN TEH ORIGINAL EXAMPLE
//    public void loadInventoryArray(VendingMachine vm) { //SATURDAY AM - COMMENTING THIS OUT AND GOING BACK TO STOCK LOADER
//
//        List<Item> inventoryList;
//        try {
//            File dataFile = new File(FILE_LOCATION);
//            //List<String> inventoryArrayList;
//
//            try (Scanner fileScanner = new Scanner(dataFile)) {
//                inventoryList = new ArrayList<>();
//                while (fileScanner.hasNext()) {
//                    String[] data = fileScanner.nextLine().split("\\|");
//
//                    for (int i = 0; i < 5; i++) {
//                        inventoryList.add(new Item(data[0], data[1], Double.parseDouble(data[2]), data[3]));
//                    }
//                }
//            }
//            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            System.out.println(inventoryList);
//
//            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            } catch (IOException x) {
//                System.out.println(x.getMessage());
//            }
//        }

