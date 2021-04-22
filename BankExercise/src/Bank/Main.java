package Bank;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import Bank.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {
	public static void main(String[] args) {
		
		DBConnection dbCon = new DBConnection();

		Scanner sc = new Scanner(System.in);
		
		int option = 1;
		
		while(option != 5) {
		System.out.println("Welcome to our bank. What would you like to do?");
		System.out.println("Please choose an option");
		System.out.println("1.Create account");
		System.out.println("2.Deposit");
		System.out.println("3.Withdrawal");
		System.out.println("4.Balance Enquiry");
		System.out.println("5.Exit");
		option = sc.nextInt();
		
		int number;
		String name;
		int initial_balance;
		int min_balance = 0;
		String transaction_type;
		int amount;
		
		switch(option){
		
		//Create new account
		case 1: {
			System.out.println("Please enter account number");
			number = sc.nextInt();
			System.out.println("Please enter name");
			name = sc.next();
			System.out.println("Please enter initial balance");
			initial_balance = sc.nextInt();
			System.out.println("Please enter min balance");
			min_balance = sc.nextInt();
			
			dbCon.createAccount(number, name, initial_balance, min_balance);
			 
			System.out.println("Going back to menu...");
			System.out.println("");
        	break;
		}
		
		//Deposit
		case 2: {
			
			System.out.println("Please enter your account number: ");
			number = sc.nextInt();
			
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			
			transaction_type = "deposit";
			
			System.out.println("Please enter the amount to deposit: ");
			amount = sc.nextInt();
			
			dbCon.deposit(number, sqlDate, transaction_type, amount);
			
			System.out.println("");
			System.out.println("Going back to menu...");
			System.out.println("");
        	break;
		}
		
		//Withdraw money from the bank
		case 3: {
			System.out.println("Please enter your account number: ");
			number = sc.nextInt();
			
			System.out.println("Please enter how much would you like to withdraw: ");
			int withdraw_amount = sc.nextInt();
			
			ResultSet rs = dbCon.balanceEnquiry(number); 
			
			ResultSet rs_min = dbCon.minimumBalance(number); 
			
			int current_balance = 0;
			try {
				rs.next();
				current_balance = rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				rs_min.next();
				min_balance = rs_min.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			
			if(current_balance - withdraw_amount > min_balance) {
				dbCon.withdrawal(number, sqlDate, "withdraw", withdraw_amount);
			} else {
				System.out.println("Sorry you don't have enough balance.");
			}
		
			System.out.println("");
			System.out.println("Going back to menu...");
			System.out.println("");
        	break;
		}
		
		//Balance Enquiry
		case 4: {

        	System.out.println("Please enter your account number: ");
        	number = sc.nextInt();
        	
			 ResultSet rs = dbCon.balanceEnquiry(number); 
			 try { 
				 rs.next();
				 System.out.println("you balance is: " + rs.getInt(1));
			 } catch (SQLException e) { 
				  e.printStackTrace(); }
			 
			System.out.println("");
			System.out.println("Going back to menu...");
			System.out.println("");
        	break;
		}
		default: System.out.println("Exit While");
		}
		
		}
		System.out.println("Good bye you have exited the menu");
		sc.close();
	}
}
