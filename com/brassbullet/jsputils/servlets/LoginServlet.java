package com.brassbullet.jsputils.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import com.brassbullet.jsputils.SecureBean;
import com.brassbullet.dbutils.ConnectionPool;

public class LoginServlet extends HttpServlet
{
	private ConnectionPool pool;

  public void init() throws ServletException
  {
    pool = (ConnectionPool)getServletContext().getAttribute("uws.eleceng.webdb.ConnectionPool");
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String requested=request.getRequestURI();
    if (request.getQueryString()!=null)
    {
      requested=requested+"?"+request.getQueryString();
    }
    request.getSession(true).setAttribute("uws.eleceng.webdb.LoginServlet.requestedpage",requested);
    
    
    RequestDispatcher dispatch = request.getRequestDispatcher("/include/login.jsp");
    
    dispatch.forward(request,response);
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    SecureBean secure = (SecureBean)request.getSession(true).getAttribute("uws.eleceng.webdb.SecureBean.securityinfo");

    if (secure==null)
    {
      secure = new SecureBean(pool);
    }
    
    String username = request.getParameter("username");
    String password = request.getParameter("password");    
    
    boolean valid = false;
    
    if ((username!=null)&&(password!=null))
    {
      valid=secure.authenticate(username,password);
    }
    
    if (valid)
    {
      request.getSession(true).setAttribute("uws.eleceng.webdb.SecureBean.securityinfo",secure);
      String requestedpage = (String)request.getSession(true).getAttribute("uws.eleceng.webdb.LoginServlet.requestedpage");
          
      if ((requestedpage==null)||(requestedpage.equals("/login")))
      {
        requestedpage="/secure/index.jsp";
      }
      
      response.sendRedirect(requestedpage);
    }
    else
    {
      RequestDispatcher dispatch = request.getRequestDispatcher("/include/badlogin.jsp");
    
      dispatch.forward(request,response);
    }
  }
}
