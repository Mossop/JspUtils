package com.brass-bullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

public class ForFirstTag extends TagSupport
{
  private String forloop;
  
  public ForFirstTag()
  {
    super();
  }
  
  public String getForloop()
  {
    return forloop;
  }
  
  public void setForloop(String Forloop)
  {
    forloop=Forloop;
  }
  
  public int doStartTag() throws JspTagException
  {
    ForLoopTag parent = (ForLoopTag)pageContext.getAttribute(forloop);
    int loop = parent.getLoop();
    int start = Integer.parseInt(parent.getStart());
    if (loop==start)
    {
      return EVAL_BODY_INCLUDE;
    }
    else
    {
      return SKIP_BODY;
    }
  }
}
