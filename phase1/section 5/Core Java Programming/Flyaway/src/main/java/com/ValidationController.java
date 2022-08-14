package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.mysql.cj.protocol.x.SyncFlushDeflaterOutputStream;


/**
 * Servlet implementation class ValidationControllerServlet
 */


public class ValidationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private UserDb userDb;	
	
	
	@Resource(name="jdbc/flight_booking")
	private DataSource dataSource; 
	
	
	@Override 
	public void init() throws ServletException {
		
		super.init();
		
		try {
			userDb = new UserDb(dataSource);
			
		}
		catch (Exception e){
			throw new ServletException(e) ;
	    }
	  }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		       try {		    	   
		            
					String theCommand = request.getParameter("command");
					
					
					if (theCommand==null) {
						theCommand="SIGNUP";
					}
					
				    switch(theCommand){
				    
				    case "SIGNUP":
				    	registerUser(request,response);
				    	break;
				    case "LOGIN":
				    	validateLogin(request,response);
				        break;
				    case "ADMIN":
				    	adminLogin(request,response);				    
				    default:
				    	validateLogin(request,response);			      
				    }	
				    
				} catch (Exception e) {
					System.out.println("Exception Occured: "+e);
				}
			}

	private void adminLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		       
		
				String username=request.getParameter("username");
				String password=request.getParameter("password");
				
		
				AdminUser admin = new AdminUser(username,password);
				
				
					
				String dbPassword=userDb.validateAdminLogin(admin);
				RequestDispatcher rd= null;
				PrintWriter out=response.getWriter();
				
				if(password.equals(dbPassword))
				{
					
					HttpSession session=request.getSession(); 
					   if(session.isNew()) {
				        	System.out.println("New Session Created:" +session.getId());
				        }
					
					session.setAttribute("username",username);			
					
					rd= request.getRequestDispatcher("admin_home.jsp");
					out.println("<script>document.getElementById('error').innerHTML=''; </script>");
					rd.forward(request, response);
				}
				    
				else {
					rd=request.getRequestDispatcher("admin_login.html");
					rd.include(request, response); 
					out.println("<script>document.getElementById('error').innerHTML='Invalid UserName or Password!'; </script>");
				}		
			}

	private void validateLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		
		
		User theUser = new User(email,password);
		
		
			
		String dbPassword=userDb.validateLogin(theUser);
		RequestDispatcher rd= null;
		PrintWriter out=response.getWriter();
		
		
		
		if(password.equals(dbPassword))
		{
		
			User profileDetails = userDb.getProfileDetails(email);
			
			HttpSession session=request.getSession(); 
	        if(session.isNew()) {
	        	System.out.println("New Session Created:" +session.getId());
	        }			
			
			session.setAttribute("USER_DETAILS", profileDetails);
			session.setAttribute("username",profileDetails.getFname());
			session.setAttribute("user_email", email);
			rd= request.getRequestDispatcher("home.jsp");
			out.println("<script>document.getElementById('error').innerHTML=''; </script>");
			rd.forward(request, response);
		}
		    
		else {

			rd=request.getRequestDispatcher("login.html");			
			rd.include(request, response); 
			out.println("<script>document.getElementById('error').innerHTML='Invalid UserName or Password!'; </script>");
		}		
	}

	private void registerUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		String phno=request.getParameter("phno");
		String password=request.getParameter("password");
		
	
		User theUser = new User(firstName,lastName,email,phno,password);
		
		
			
		userDb.registerUser(theUser);
		System.out.println("User Registered Succesfully!");
		
		
		HttpSession session=request.getSession(); 
        if(session.isNew()) {
        	System.out.println("New Session Created:" +session.getId());
        }
		session.setAttribute("username",firstName);
		session.setAttribute("user_email", email);
			
		RequestDispatcher rd= request.getRequestDispatcher("home.jsp");
		rd.forward(request, response);
	}
	
	
	
}

