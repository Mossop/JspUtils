package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import com.brassbullet.jsputils.SecureBean;

public class InSecureSectionTag extends TagSupport
{
  public InSecureSectionTag()
  {
    super();
  }
  
  public int doStartTag() throws JspTagException
  {
    SecureBean secure = (SecureBean)pageContext.findAttribute("uws.eleceng.webdb.SecureBean.securityinfo");
    if (secure==null)
    {
      return EVAL_BODY_INCLUDE;
    }
    else
    {
      return SKIP_BODY;
    }
  }
  
  public int doEndTag() throws JspTagException
  {
    return EVAL_PAGE;
  }
}
