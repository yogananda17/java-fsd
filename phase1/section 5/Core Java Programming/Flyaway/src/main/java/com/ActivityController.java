package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class ActivityControllerServlet
 */
public class ActivityController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private BookingDb bookingDb;
	
	
	@Resource(name="jdbc/flight_booking")
	private DataSource dataSource; // Creating handle

	
	
	@Override 
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		try {
			bookingDb  = new BookingDb(dataSource);
		}
		catch (Exception e){
			throw new ServletException(e) ;
	    }
	  }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try { 
			
			
			 HttpSession session=request.getSession(false);
			 RequestDispatcher rd=null;
			 PrintWriter out=response.getWriter();
			 
			 if(session==null) {
				 System.out.println("User not logged in!");
			    	rd=request.getRequestDispatcher("login.html");			
					rd.include(request, response); 
					out.println("<script>document.getElementsById('loginalert').innerHTML='Please Login to Continue'; </script>");
			 }
         
			String theCommand = request.getParameter("command");
			
			
		    switch(theCommand){
		    
		    case "ORDERS":
		    	viewOrderDetails(request,response);
		    	break;
		    case "DETAILS":
		    	viewUserDetails(request,response);
		        break;  			        
		    default:
		    	System.out.println("Coudn't find COMMAND Param value");		
		    	break;
		    }	
		    
		} catch (Exception e) {
			System.out.println("Exception: "+e);
		}
	}

	private void viewUserDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd= request.getRequestDispatcher("user_details.jsp");	
		rd.forward(request, response);
	}

	private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		HttpSession session=request.getSession(false);
		
		String email= (String) session.getAttribute("user_email");
		
		List<BookingDetails> bd=bookingDb.getOrderDetails(email);
		request.setAttribute("ORDER_LIST", bd);
		RequestDispatcher rd= request.getRequestDispatcher("my-orders.jsp");	
		rd.forward(request, response);
	}
		}
