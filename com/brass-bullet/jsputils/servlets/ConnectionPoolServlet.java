package com.brass-bullet.jsputils.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import com.brass-bullet.jsputils.SecureBean;
import com.brass-bullet.dbutils.ConnectionPool;

public class ConnectionPoolServlet extends HttpServlet
{
	private ConnectionPool pool;

  public void init() throws ServletException
  {
    pool = new ConnectionPool();
    getServletContext().setAttribute("uws.eleceng.webdb.ConnectionPool",pool);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    RequestDispatcher dispatch = request.getRequestDispatcher("/include/mainstart.jsp?pageTitle=Connection%20Pool%20Status");

    dispatch.include(request,response);

    PrintWriter out = response.getWriter();

    out.println("<TABLE>");
    out.println("<TR><TD><B>Connections in pool:</B></TD><TD>"+String.valueOf(pool.getTotalConnections())+"</TD></TR>");
    out.println("<TR><TD><B>Connections in use:</B></TD><TD>"+String.valueOf(pool.getInUse())+"</TD></TR>");
		out.println("<FORM METHOD=\"POST\">");
    out.print("<TR><TD><B>Minimum number of connections:</B></TD><TD>");
    out.println("<INPUT TYPE=\"TEXT\" NAME=\"connections\" VALUE=\""+String.valueOf(pool.getConnections())+"\"></TD></TR>");
		out.print("<TR><TD><B>Allow extra connections:</B></TD><TD><INPUT TYPE=\"CHECKBOX\" NAME=\"allowextra\" VALUE=\"yes\"");
    if (pool.getAllowExtra())
    {
    	out.print(" CHECKED");
    }
    out.println("></TD></TR>");
    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"CENTER\"><INPUT TYPE=\"SUBMIT\" VALUE=\"Apply\"></TD></TR>");
    out.println("</FORM>");
    out.println("</TABLE>");

    dispatch = request.getRequestDispatcher("/include/mainend.jsp");

    dispatch.include(request,response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
  	String Allowed = request.getParameter("allowextra");
    if ((Allowed!=null)&&(Allowed.equals("yes")))
    {
    	pool.setAllowExtra(true);
    }
    else
    {
    	pool.setAllowExtra(false);
    }
    try
    {
    	int newconns = Integer.parseInt(request.getParameter("connections"));
      pool.setConnections(newconns);
    }
    catch (NumberFormatException e)
    {
    }
  	doGet(request,response);
  }

  public void destroy()
  {
  	getServletContext().removeAttribute("uws.eleceng.webdb.ConnectionPool");
  	super.destroy();
  }
}
