package com.brass-bullet.jsputils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.StringTokenizer;

public class SecureBean
{
  private String username;
  private Vector groups;
  private ConnectionPool pool;

  public SecureBean(ConnectionPool Pool)
  {
    super();
    pool=Pool;
    username=null;
    groups=null;
  }
  
  public String getUser()
  {
    return username;
  }
  
  public boolean authenticate(String User, String Password)
  {
    boolean valid = false;
    try
    {
      Connection con = pool.getConnection();
      if (con!=null)
      {
        ResultSet result = con.createStatement().executeQuery("SELECT * FROM SECUREUSERS WHERE USER='"+User+"' AND PASSWORD=PASSWORD('"+Password+"');");
      	if (result.next())
      	{
      	  username=User;
      	  valid=true;
      	  groups=new Vector(5);
      	  ResultSet groupresult = con.createStatement().executeQuery("SELECT GROUPNAME FROM SECUREGROUPS WHERE USER='"+User+"';");
      	  while (groupresult.next())
      	  {
      	    groups.addElement(groupresult.getString(1).toUpperCase());
      	  }
      	}
      	else
      	{
      	  valid=false;
      	}
      	pool.releaseConnection(con);
      }
      else
      {
      System.err.println("(SecureBean) Error authenticating - no database connection");
      valid=false;
      }
    }
    catch (SQLException e)
    {
      System.err.println("(SecureBean) SQLException - "+e.getMessage());
      valid=false;
    }
    return valid;
  }
  
  public boolean isUser(String users)
  {
    if (username!=null)
    {
      boolean valid = false;
      if (users!=null)
      {
        if (users.equals("*"))
        {
          valid=true;
        }
        else
        {
          StringTokenizer splitter = new StringTokenizer(users,",");
          while (splitter.hasMoreTokens())
          {
            if (username.equalsIgnoreCase(splitter.nextToken()))
            {
              valid=true;
            }
          }
        }
      }
      return valid;
    }
    else
    {
      return false;
    }
  }
  
  public boolean inGroup(String group)
  {
    if (username!=null)
    {
      boolean valid = false;
      if (group!=null)
      {
        if (group.equals("*"))
        {
          valid=true;
        }
          else
        {
          StringTokenizer splitter = new StringTokenizer(group,",");
          while (splitter.hasMoreTokens())
          {
            if (groups.contains(splitter.nextToken().toUpperCase()))
            {
              valid=true;
            }
          }
        }
      }
      return valid;
    }
    else
    {
      return false;
    }
  }
}
