package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

public class SqlQueryTag extends BodyTagSupport
{
  private String connection;
  private Connection con;
  private ResultSet results;
  
  public SqlQueryTag()
  {
    super();
    connection=null;
  }
  
  public String getConnection()
  {
    return connection;
  }
  
  public void setConnection(String Connection)
  {
    connection=Connection;
  }
  
  public ResultSet getResults()
  {
    return results;
  }
  
  public int doStartTag() throws JspTagException
  {
    con=(Connection)pageContext.findAttribute(connection);
    return EVAL_BODY_TAG;
  }
  
  public int doAfterBody() throws JspTagException
  {
    BodyContent body = getBodyContent();
    try
    {
      String query = body.getString();
      results = con.createStatement().executeQuery(query);
      results.next();
      body.clear();
    }
    catch (Exception e)
    {
      return SKIP_BODY;
    }
    return SKIP_BODY;
  }
  
  public int doEndTag() throws JspTagException
  {
    pageContext.setAttribute(getId(),results);
    return EVAL_PAGE;
  }
}
