package com.brass-bullet.jsputils.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import com.sun.xml.parser.Resolver;
import org.xml.sax.InputSource;
import java.sql.Connection;
import java.util.Vector;
import java.util.StringTokenizer;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.brass-bullet.dbutils.xml.Database;
import com.brass-bullet.dbutils.xml.Table;
import com.brass-bullet.dbutils.xml.Field;
import com.brass-bullet.dbutils.xml.DatabaseParser;
import com.brass-bullet.dbutils.ConnectionPool;

public class RowAddServlet extends HttpServlet
{
  private Database database;
  private ConnectionPool pool;
  
  public void init() throws ServletException
  {
    pool = (ConnectionPool)getServletContext().getAttribute("uws.eleceng.webdb.ConnectionPool");
    String xmlurl = getInitParameter("DatabaseSpec");
    if (xmlurl!=null)
    {
      try
      {
        InputSource input = Resolver.createInputSource(getServletContext().getResource(xmlurl),true);
        DatabaseParser parser = new DatabaseParser();
        database=parser.parse(input);
        log("Database details loaded");
      }
      catch (Exception err)
      {
        log("Error loading database specification");
        ServletException e = new ServletException("Error loading database specification");
      }
    }
    else
    {
      log("No spec given");
      ServletException e = new ServletException("Parameter DatabaseSpec not specified");
    }
  }
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
  	doPost(request,response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
  	Connection dbcon = pool.getConnection();
    String redirect = request.getParameter("failed");
    if (dbcon!=null)
    {
    	String tablename = request.getParameter("table");
    	if (tablename!=null)
    	{
    	  Table table = database.getTable(tablename);
    	  if (table!=null)
    	  {
        	String fieldlist = "";
    	    String insertlist = "";
    	    for (int loop=0; loop<table.getAllFieldCount(); loop++)
    	    {
    	      String val = request.getParameter(table.getAllField(loop).getName());
    	      if (val!=null)
    	      {
    	        fieldlist=fieldlist+table.getAllField(loop).getName()+",";
    	        if (table.getAllField(loop).isString())
    	        {
    	          insertlist=insertlist+"'"+val+"'";
    	        }
    	        else
    	        {
    	          insertlist=insertlist+val;
    	        }
    	        insertlist=insertlist+",";
    	      }
    	    }
    	    if (insertlist.length()>0)
    	    {
          	fieldlist=fieldlist.substring(0,fieldlist.length()-1);
    	      insertlist=insertlist.substring(0,insertlist.length()-1);
    	    }
					String query = "INSERT INTO "+tablename+" ("+fieldlist+") VALUES ("+insertlist+");";
          log("Trying query - "+query);
          try
          {
          	dbcon.createStatement().executeUpdate(query);
    				redirect = request.getParameter("success");
    	    }
    	    catch (SQLException error)
    	    {
    	      log(error.getMessage());
    	    }      
    	  }
    	  else
    	  {
    	    log("Invalid table specified");
    	  }
    	}
    	else
    	{
    	  log("Table not specified");
    	}
      pool.releaseConnection(dbcon);
    }
    else
    {
    	log("No database connection available");
    }
    if (redirect==null)
    {
    	redirect="/";
    }
    String passthru = request.getParameter("passthru");
    if (passthru!=null)
    {
    	redirect=redirect+"?";
    	String current;
      String value;
      StringTokenizer passlist = new StringTokenizer(passthru,",");
      while (passlist.hasMoreTokens())
      {
				current=passlist.nextToken();
				value=request.getParameter(current);
        if (value!=null)
        {
					redirect=redirect+current+"="+value+"&";
        }
      }
      redirect=redirect.substring(0,redirect.length()-1);
    }
    RequestDispatcher dispatch = request.getRequestDispatcher(redirect);

    dispatch.forward(request,response);
  }

}
