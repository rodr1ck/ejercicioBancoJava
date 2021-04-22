package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DBConnection {
	Connection con;
	PreparedStatement pstmt;
	
	String url ="jdbc:mysql://localhost:3306/trialdb";
	
	String driver = "com.mysql.cj.jdbc.Driver";
	String user = "root";
	String pwd = "";

	public DBConnection() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,user,pwd);
			System.out.println("The connection is created");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createBankAccountTable() {
		String query = "create table bankaccount (number int, name varchar(10), initial_balance int, min_balance int)";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.execute();
			System.out.println("Created bank account table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void createTransactionsTable() {
		String query = "create table transactions (number int, date datetime, type varchar(10), amount int)";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.execute();
			System.out.println("Created transactions table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 public void createAccount(int number, String name, int initial_balance, int min_balance) { 
		 String query = "insert into bankaccount(number, name, initial_balance, min_balance) values(?,?,?,?)"; 
		 try { 
			 pstmt = con.prepareStatement(query); 
			 pstmt.setInt(1,number);
			 pstmt.setString(2,name); 
			 pstmt.setInt(3,initial_balance);
			 pstmt.setInt(4,min_balance);
			 int rows = pstmt.executeUpdate(); 
			 System.out.println("Inserted"+ rows + "rows");
		 } catch (SQLException e) { 
			 e.printStackTrace(); }
	  }
	 
	 public ResultSet balanceEnquiry(int number) { 
		 ResultSet rs = null; 
		 String query = "select initial_balance from bankaccount where number=?"; 
		 try { 
			 pstmt = con.prepareStatement(query); 
			 pstmt.setInt(1,number); 
			 rs = pstmt.executeQuery();
			 } catch (SQLException e) { 
				  e.printStackTrace();
				 } 
		 return rs; 
		 }
	 
	 public ResultSet minimumBalance(int account_number) { 
		 ResultSet rs = null; 
		 String query = "select min_balance from bankaccount where number=?"; 
		 try { 
			 pstmt = con.prepareStatement(query); 
			 pstmt.setInt(1,account_number); 
			 rs = pstmt.executeQuery();
			 } catch (SQLException e) { 
				  e.printStackTrace();
				 } 
		 return rs; 
		 }
	 
	 public void deposit(int number, java.sql.Date date, String type, int amount) { 
		 String query = "insert into transactions(number, date, type, amount) values(?,?,?,?)"; 
		 try { 
			 pstmt = con.prepareStatement(query); 
			 pstmt.setInt(1,number);
			 pstmt.setDate(2, date);
			 pstmt.setString(3,type); 
			 pstmt.setInt(4,amount); 
			 int rows = pstmt.executeUpdate(); 
			 System.out.println("Inserted"+ rows + "rows");
			 
			 ResultSet rs = balanceEnquiry(number);
			 rs.next();
			 int initial_balance = rs.getInt(1); // 1 is the column number in rs. Only one is returned
			 System.out.println("Initial balance: " + initial_balance);
			
			 int newBalance = initial_balance+amount;
			 int rs1 = 0;
			 String updateBalanceQuery = "UPDATE bankaccount SET initial_balance = ? where number=?"; 	 
				pstmt = con.prepareStatement(updateBalanceQuery);
				pstmt.setInt(1,newBalance);
				pstmt.setInt(2,number);
				rs1 = pstmt.executeUpdate(); 
				 
		 } catch (SQLException e) { 
			 e.printStackTrace(); }
	  
	  }

	 public void withdrawal(int number, Date date, String type, int amount) { 
		 String query = "insert into transactions(number, date, type, amount) values(?,?,?,?)"; 
		 try { 
			 pstmt = con.prepareStatement(query); 
			 pstmt.setInt(1,number);
			 pstmt.setDate(2, (java.sql.Date) date);
			 pstmt.setString(3,type); 
			 pstmt.setInt(4,amount); 
			 int rows = pstmt.executeUpdate(); 
			 System.out.println("Inserted"+ rows + "rows");
			 
			 ResultSet rs = balanceEnquiry(number);
			 rs.next();
			 int initial_balance = rs.getInt(1);
			 System.out.println("Initial balance: " + initial_balance);
			
			 int newBalance = initial_balance-amount;
			 int rs1 = 0;
			 String updateBalanceQuery = "UPDATE bankaccount SET initial_balance = ? where number=?"; 	 
				pstmt = con.prepareStatement(updateBalanceQuery);
				pstmt.setInt(1,newBalance);
				pstmt.setInt(2,number);
				rs1 = pstmt.executeUpdate(); 
	  
		 } catch (SQLException e) { 
			 e.printStackTrace(); }
	 }
	 
}
