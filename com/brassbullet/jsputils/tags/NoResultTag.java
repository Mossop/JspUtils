package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.sql.ResultSet;

public class NoResultTag extends TagSupport
{
  private String query;
  private ResultSet results;
  
  public NoResultTag()
  {
    super();
  }
  
  public String getQuery()
  {
    return query;
  }
  
  public void setQuery(String Query)
  {
    query=Query;
  }
  
  public int doStartTag() throws JspTagException
  {
    try
    {
      results=(ResultSet)pageContext.findAttribute(getQuery());
      Object test = results.getObject(1);
      if (test!=null)
      {
        return SKIP_BODY;
      }
      else
      {
        return EVAL_BODY_INCLUDE;
      }
    }
    catch (Exception e)
    {
      return SKIP_PAGE;
    }
  }

}