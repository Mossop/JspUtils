package com.brass-bullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

public class DefaultPage extends TagSupport
{
  private String title;
  
  public DefaultPage()
  {
    super();
    setTitle("");
  }
  
  public void setTitle(String Title)
  {
    title=Title;
  }
  
  public int doStartTag() throws JspTagException
  {
    try
    {
      pageContext.getOut().flush();
      pageContext.include("/include/mainstart.jsp?pageTitle="+title);
    }
    catch (Exception e)
    {
      return SKIP_BODY;
    }
    
    return EVAL_BODY_INCLUDE;
  }
  
  public int doEndTag() throws JspTagException
  {
    try
    {
      pageContext.getOut().flush();
      pageContext.include("/include/mainend.jsp");
    }
    catch (Exception e)
    {
      return SKIP_PAGE;
    }
    
    return EVAL_PAGE;
  }
}
