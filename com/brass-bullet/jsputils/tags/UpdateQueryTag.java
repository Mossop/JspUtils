package com.brass-bullet.jsputils.tags;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.sql.Connection;

public class UpdateQueryTag extends BodyTagSupport
{
  private String connection;
  private Connection con;

  public UpdateQueryTag()
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
      con.createStatement().executeUpdate(query);
      body.clear();
    }
    catch (Exception e)
    {
      return SKIP_BODY;
    }
    return SKIP_BODY;
  }
}
