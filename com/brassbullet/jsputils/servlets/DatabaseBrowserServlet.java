package com.brassbullet.jsputils.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import com.brassbullet.dbutils.DbSession;
import com.brassbullet.dbutils.DbHandler;
import com.brassbullet.dbutils.xml.Database;
import com.brassbullet.dbutils.xml.Table;
import com.brassbullet.dbutils.xml.Field;
import com.brassbullet.dbutils.xml.Reference;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import com.sun.xml.parser.Resolver;
import org.xml.sax.InputSource;

public class DatabaseBrowserServlet extends HttpServlet
{
	private Database database;
	
  public void init() throws ServletException
  {
    String xmlurl = getInitParameter("DatabaseSpec");
    if (xmlurl!=null)
    {
      try
      {
      	database = Database.unmarshalDatabase(getServletContext().getResource(xmlurl));
      }
      catch (Exception err)
      {
      	err.printStackTrace();
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
  	PrintWriter out = response.getWriter();

    String path = request.getRequestURI();

    StringTokenizer pathlist = new StringTokenizer(path,"/");

    out.println("<HTML>");
    out.println("<HEAD><TITLE>Database Browser v1.00 ("+path+")</TITLE></HEAD>");
    out.println("<BODY>");
    pathlist.nextToken();
    if ((pathlist.hasMoreTokens())&&(pathlist.nextToken().equals("database")))
    {
    	if (pathlist.hasMoreTokens())
      {
      	Table table = database.getTable(pathlist.nextToken());
        if (table!=null)
        {
          if (pathlist.hasMoreTokens())
          {
          	Field field = table.getField(pathlist.nextToken());
            if (field!=null)
            {
            	out.println("<H1>"+table.getName()+"."+field.getName()+"</H1>");
              out.println("<TABLE>");
              if (field.getComment()!=null)
              {
              	out.println("<TR><TD><B>Comment:</B></TD><TD>"+field.getComment()+"</TD></TR>");
              }
              out.println("<TR><TD><B>Type:</B></TD><TD>"+field.getType()+"</TD></TR>");
              out.println("</TABLE>");
            }
            else
            {
            	out.println("<P>Invalid field specified");
            }
          }
          else
          {
            out.println("<H1>"+table.getName()+"</H1>");
        		String comment = table.getComment();
        		if (comment!=null)
        		{
        			out.println("<P>"+comment);
        		}
        		out.println("<UL>");
          	for (int loop=0; loop<table.getFields().size(); loop++)
          	{
          		out.println("<LI><A HREF=\"/database/"+table.getName()+"/"+((Field)table.getFields().get(loop)).getName()+"\">View "+((Field)table.getFields().get(loop)).getName()+"</A></LI>");
          	}
          	out.println("</UL>");
          }
        }
        else
        {
        	out.println("<P>Invalid table specified");
        }
      }
      else
      {
      	String author = database.getAuthor();
        if (author!=null)
        {
        	out.println("<P>Database designed by "+author);
        }
        String comment = database.getComment();
        if (comment!=null)
        {
        	out.println("<P>"+comment);
        }
        out.println("<UL>");
      	for (int loop=0; loop<database.getTables().size(); loop++)
        {
        	out.println("<LI><A HREF=\"/database/"+((Table)database.getTables().get(loop)).getName()+"\">View "+((Table)database.getTables().get(loop)).getName()+"</A></LI>");
        }
        out.println("</UL>");
      }
    }
    else
    {
    	out.println("<P>Error in requested URL");
    }
    out.println("</BODY>");
    out.println("</HTML>");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
  }

  public void destroy()
  {
  	super.destroy();
  }
}
