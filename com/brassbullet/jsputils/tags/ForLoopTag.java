package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.sql.ResultSet;

public class ForLoopTag extends BodyTagSupport
{
  private int loop;
  private int start;
  private int end;
  
  public ForLoopTag()
  {
    super();
  }
  
  public int getLoop()
  {
    return loop;
  }
  
  public String getStart()
  {
    return Integer.toString(start);
  }
  
  public void setStart(String Start)
  {
    start=Integer.parseInt(Start);
  }
  
  public String getEnd()
  {
    return Integer.toString(end);
  }
  
  public void setEnd(String End)
  {
    end=Integer.parseInt(End);
  }
  
  public String toString()
  {
    return Integer.toString(loop);
  }
  
  public int doStartTag() throws JspTagException
  {
    loop=start;
    if (start>end)
    {
      return SKIP_BODY;
    }
    else
    {
      return EVAL_BODY_TAG;
    }
    
  }
  
  public void doInitBody() throws JspTagException
  {
    pageContext.setAttribute(getId(),this);
  }
  
  public int doAfterBody() throws JspTagException
  {
    try
    {
      BodyContent body = getBodyContent();
      body.writeOut(getPreviousOut());
      body.clear();
      loop++;
      if (loop<=end)
      {
        return EVAL_BODY_TAG;
      }
      else
      {
        return SKIP_BODY;
      }
    }
    catch (Exception e)
    {
      return SKIP_BODY;
    }
  }
  
  public int doEndTag() throws JspTagException
  {
    pageContext.removeAttribute(getId());
    return EVAL_PAGE;
  }
}
