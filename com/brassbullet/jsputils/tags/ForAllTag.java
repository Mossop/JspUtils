package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.sql.ResultSet;

public class ForAllTag extends BodyTagSupport
{
  private String query;
  private String between;
  private ResultSet results;
  
  public ForAllTag()
  {
    super();
    between=null;
  }
  
  public String getQuery()
  {
    return query;
  }
  
  public void setQuery(String Query)
  {
    query=Query;
  }
  
  public String getBetween()
  {
    return between;
  }
  
  public void setBetween(String Between)
  {
    between=Between;
  }
  
  public int doStartTag() throws JspTagException
  {
    try
    {
      results=(ResultSet)pageContext.findAttribute(getQuery());
      Object test = results.getObject(1);
      if (test==null)
      {
        return SKIP_BODY;
      }
      else
      {
        return EVAL_BODY_TAG;
      }
    }
    catch (Exception e)
    {
      return SKIP_BODY;
    }
  }
  
  public int doAfterBody() throws JspTagException
  {
    try
    {
      BodyContent body = getBodyContent();
      body.writeOut(getPreviousOut());
      body.clear();
      if (results.next())
      {
        if (between!=null)
        {
          getPreviousOut().print(between);
        }
        return EVAL_BODY_TAG;
      }
      else
      {
        return SKIP_BODY;
      }
    }
    catch (Exception e)
    {
      return SKIP_PAGE;
    }
  }
}
