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
import javax.sql.DataSource;

/**
 * Servlet implementation class AdminControllerServlet
 */
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private AdminDb adminDb;
	
	
	@Resource(name="jdbc/flight_booking")
	private DataSource dataSource; 
	
	
	@Override 
	public void init() throws ServletException {
		super.init();
		
		try {
			adminDb = new AdminDb(dataSource);
		}
		catch (Exception e){
			throw new ServletException(e) ;
	    }
	  }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 try {
	            
				String theCommand = request.getParameter("command");
				
			
				
			    switch(theCommand){
			    
			    case "CHANGE":
			    	changePassword(request,response);
			    	break;
			    case "VIEWSRCLOC":
			    	viewSrcLocationList(request,response);
			        break;
			    case "VIEWDESTLOC":
			    	viewDestLocationList(request,response);	
			    	break;
			    case "VIEWAIRLINE":
			    	viewAirlines(request,response);
			    case "VIEWFLIGHT":
			    	viewFlights(request,response); 
			    case "ADDFLIGHT":
			    	addFlight(request,response);  
			    default:
			    	 System.out.println("Invalid Command!");			      
			    }	
			    
			} catch (Exception e) {
				System.out.println("Exception: "+e);
			}
		}


	private void addFlight(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fno = request.getParameter("flightID");
		String al = request.getParameter("airline");
		String src = request.getParameter("src");
		String dest = request.getParameter("dest");
		String arr = request.getParameter("arr");
		String dep = request.getParameter("dep");
		String dur = request.getParameter("dur");
		String ap = request.getParameter("ap");
		int dur_min = Integer.parseInt(request.getParameter("dur_min"));
		int fare = Integer.parseInt(request.getParameter("fare"));
		int seats = Integer.parseInt(request.getParameter("seats"));
		int stops = Integer.parseInt(request.getParameter("stops"));
		adminDb.addFlight(fno,al,src,dest,arr,dep,dur,dur_min,fare,seats,stops,ap);
		RequestDispatcher rd=request.getRequestDispatcher("admin_flights.jsp");
		rd.forward(request, response);
		
		
	}

	private void viewFlights(HttpServletRequest request, HttpServletResponse response) throws Exception, IOException{
		List<Flight> flights = adminDb.getLFlightDetailsList();
		request.setAttribute("F_LIST", flights);
		RequestDispatcher rd=request.getRequestDispatcher("admin_flights.jsp");
		rd.forward(request, response);
		
	}

	private void viewAirlines(HttpServletRequest request, HttpServletResponse response) throws Exception, IOException {
		List<String> airlines = adminDb.getAirlineList();
		request.setAttribute("AIR_LIST", airlines);
		RequestDispatcher rd=request.getRequestDispatcher("admin_airlines.jsp");
		rd.forward(request, response);		
	}

	private void viewDestLocationList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		List<String> dest = adminDb.getDestinationLocationList();
		request.setAttribute("DEST_LIST", dest);
		RequestDispatcher rd=request.getRequestDispatcher("admin_places_dest.jsp");
		rd.forward(request, response);
		
	}

	private void viewSrcLocationList(HttpServletRequest request, HttpServletResponse response) throws SQLException, Exception, IOException {
		List<String> src = adminDb.getSourceLocationList();
		request.setAttribute("SRC_LIST", src);
		RequestDispatcher rd=request.getRequestDispatcher("admin_available_place.jsp");
		rd.forward(request, response);
	}	
	
	private void changePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String oldpassword=request.getParameter("password1");
		String newpassword=request.getParameter("password2");
		String username=request.getParameter("username");
		boolean success=adminDb.changePassword(oldpassword,newpassword,username);
		RequestDispatcher rd= null;
		PrintWriter out=response.getWriter();
		
		if(success) {
			System.out.println("Successfully Changed Password");
			rd= request.getRequestDispatcher("admin-profile.jsp");
			rd.include(request, response);
			out.println("<script>document.getElementById('success').innerHTML='Password Changed Successfully!'; </script>");
	}
		else {
			System.out.println("Incorrect Password");
			rd= request.getRequestDispatcher("admin-profile.jsp");
			rd.include(request, response);
			out.println("<script>document.getElementById('error').innerHTML='Incorrect Password!'; </script>");			
		}

	}

}
