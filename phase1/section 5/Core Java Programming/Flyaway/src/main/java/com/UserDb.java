package com;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class UserDb {
	private DataSource dataSource;
	

	
	public UserDb(DataSource theDataSource) {
	         dataSource=theDataSource;
	}
	
    
	  private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
         try {
      	   if(myRs !=null) {
      		   myRs.close();
      	   }
      	   if(myStmt !=null) {
      		   myStmt.close();
      	   }
      	   if(myRs !=null) {
      		   myRs.close();
      	   }
      	   
         }
         catch (Exception e){
      	 e.printStackTrace();
         }		
	}
	  
	
	public void registerUser(User theUser) throws Exception {
		
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
		
			
				myConn = dataSource.getConnection();
				
		    
				
				String sql="INSERT into customers "+ "(first_name,last_name,email,phno,user_password) " + "values(?,?,?,?,?)";
				myStmt = myConn.prepareStatement(sql);
				
			
				
				myStmt.setString(1,theUser.getFname());
				myStmt.setString(2,theUser.getLname());
				myStmt.setString(3,theUser.getEmail());
				myStmt.setString(4,theUser.getPhoneno());
				myStmt.setString(5,theUser.getPassword());
			
	
				myStmt.execute();
	        }
		 finally {
			
				close(myConn,myStmt,null);
				}
		  }
	

	public String validateLogin(User theUser) throws Exception {
	
				Connection myConn = null;
				PreparedStatement myStmt = null;
				ResultSet myRs = null;
				String pass=null;
				
				try {
					
					
						myConn = dataSource.getConnection();
						
				   
						
						String sql="SELECT user_password from customers WHERE email=?";
						myStmt = myConn.prepareStatement(sql);
						
				
						
						myStmt.setString(1,theUser.getEmail());
					
					
						
						myRs=myStmt.executeQuery();
						
						if(myRs.next())
						{
							pass=myRs.getString("user_password");
							return pass;
						}
						else {
							return null;
						}
			        }
				 finally {
					
						close(myConn,myStmt,null);
						}
				  }

	public String validateAdminLogin(AdminUser admin) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		String pass=null;
		
		try {
			
			
				myConn = dataSource.getConnection();
				
		    
				
				String sql="SELECT a_password from admin WHERE a_username=?";
				myStmt = myConn.prepareStatement(sql);
				
			
				
				myStmt.setString(1,admin.getUsername());
			
			
				
				myRs=myStmt.executeQuery();
				
				if(myRs.next())
				{
					pass=myRs.getString("a_password");
					return pass;
				}
				else {
					return null;	
					
				}
	        }
		 finally {
			
				close(myConn,myStmt,null);
				}
		  }

	public User getProfileDetails(String email) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		try {		
				myConn = dataSource.getConnection();				
				String sql="SELECT * from customers WHERE email=?";
				myStmt = myConn.prepareStatement(sql);				
				myStmt.setString(1,email);				
				myRs=myStmt.executeQuery();
				
				if(myRs.next())
				{
					String firstName = myRs.getString("first_name");
					String lastName = myRs.getString("last_name");
					String email2 = myRs.getString("email");
					String phno = myRs.getString("phno");
					
					User u = new User(firstName,lastName,email2,phno,null);
					System.out.println("Gathered User Details!");
					return u;
				}
				else {
					
					System.out.println("NOT FOUND!!!");
					return null;
				}
	        }
		 finally {
			
				close(myConn,myStmt,null);
				}
	}

}
