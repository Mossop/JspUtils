package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;

public class NotNullTag extends TagSupport
{
  private String value;
  private String ifnull;
  
  public NotNullTag()
  {
    super();
    value=null;
    ifnull=null;
  }
  
  public String getValue()
  {
    return value;
  }
  
  public void setValue(String Value)
  {
    value=Value;
  }
  
  public String getIfnull()
  {
    return ifnull;
  }
  
  public void setIfnull(String Ifnull)
  {
    ifnull=Ifnull;
  }
  
  public int doStartTag() throws JspTagException
  {
    if (value==null)
    {
      if (ifnull!=null)
      {
        try
        {
          JspWriter out = pageContext.getOut();
          out.print(ifnull);
        }
        catch (Exception e)
        {
        }
      }
      return SKIP_BODY;
    }
    else
    {
      return EVAL_BODY_INCLUDE;
    }
  }
}
