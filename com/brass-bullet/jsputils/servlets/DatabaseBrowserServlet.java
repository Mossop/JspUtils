package com.brass-bullet.jsputils.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import com.brass-bullet.dbutils.ConnectionPool;
import com.brass-bullet.dbutils.xml.Database;
import com.brass-bullet.dbutils.xml.Table;
import com.brass-bullet.dbutils.xml.Field;
import com.brass-bullet.dbutils.xml.Reference;
import com.brass-bullet.dbutils.xml.DatabaseParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import com.sun.xml.parser.Resolver;
import org.xml.sax.InputSource;

public class DatabaseBrowserServlet extends HttpServlet
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
  	PrintWriter out = response.getWriter();

    String path = request.getRequestURI();

    StringTokenizer pathlist = new StringTokenizer(path,"/");

    out.println("<HTML>");
    out.println("<HEAD><TITLE>Database Browser v1.00</TITLE></HEAD>");
    out.println("<BODY>");
    if ((pathlist.hasMoreTokens())&&(pathlist.nextToken().equals("database")))
    {
    	if (pathlist.hasMoreTokens())
      {
      	Table table = database.getTable(pathlist.nextToken());
        if (table!=null)
        {
          if (pathlist.hasMoreTokens())
          {
          	Field field = table.getAllField(pathlist.nextToken());
            if (field!=null)
            {
            	out.println("<H1>"+table.getName()+"."+field.getName()+"</H1>");
              out.println("<TABLE>");
              if (field.getComment()!=null)
              {
              	out.println("<TR><TD><B>Comment:</B></TD><TD>"+field.getComment()+"</TD></TR>");
              }
              out.println("<TR><TD><B>Type:</B></TD><TD>"+field.getType()+"</TD></TR>");
              out.print("<TR><TD><B>References:</B></TD><TD>");
              for (int loop=0; loop<field.getReferencesCount(); loop++)
              {
              	Field referenced = field.getReferences(loop).getDestination();

                out.print("<A HREF=\"/database/"+referenced.getParent().getName()+"/"+referenced.getName()+"\">");
              	out.print(referenced.getParent().getName()+"."+referenced.getName());
                out.print("</A>");
                if ((loop+1)<field.getReferencesCount())
                {
                	out.print(", ");
                }
              }
              out.println("</TD></TR>");
              out.print("<TR><TD><B>Referenced By:</B></TD><TD>");
              for (int loop=0; loop<field.getReferencedbyCount(); loop++)
              {
              	Field referenced = field.getReferencedby(loop).getSource();

                out.print("<A HREF=\"/database/"+referenced.getParent().getName()+"/"+referenced.getName()+"\">");
              	out.print(referenced.getParent().getName()+"."+referenced.getName());
                out.print("</A>");
                if ((loop+1)<field.getReferencedbyCount())
                {
                	out.print(", ");
                }
              }
              out.println("</TD></TR>");
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
          	for (int loop=0; loop<table.getAllFieldCount(); loop++)
          	{
          		out.println("<LI><A HREF=\"/database/"+table.getName()+"/"+table.getAllField(loop).getName()+"\">View "+table.getAllField(loop).getName()+"</A></LI>");
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
      	for (int loop=0; loop<database.getTableCount(); loop++)
        {
        	out.println("<LI><A HREF=\"/database/"+database.getTable(loop).getName()+"\">View "+database.getTable(loop).getName()+"</A></LI>");
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