package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import com.brassbullet.jsputils.SecureBean;

public class SecureSectionTag extends TagSupport
{
  private String users;
  private String groups;
  private String notusers;
  private String notgroups;
  
  public SecureSectionTag()
  {
    super();
    users=null;
    groups=null;
  }
  
  public void setUsers(String Users)
  {
    users=Users;
  }
  
  public String getUsers()
  {
    return users;
  }
  
  public void setGroups(String Groups)
  {
    groups=Groups;
  }
  
  public String getGroups()
  {
    return groups;
  }
  
  public void setNotusers(String Notusers)
  {
    notusers=Notusers;
  }
  
  public String getNotusers()
  {
    return notusers;
  }
  
  public void setNotgroups(String Notgroups)
  {
    notgroups=Notgroups;
  }
  
  public String getNotgroups()
  {
    return notgroups;
  }
  
  public int doStartTag() throws JspTagException
  {
    SecureBean secure = (SecureBean)pageContext.findAttribute("uws.eleceng.webdb.SecureBean.securityinfo");
    if (secure!=null)
    {
      if ((users==null)&&(groups==null)&&(notusers==null)&&(notgroups==null))
      {
        return EVAL_BODY_INCLUDE;
      }
      else
      {
        if (((secure.isUser(users))||(secure.inGroup(groups)))&&(!((secure.isUser(notusers))||(secure.inGroup(notgroups)))))
        {
          return EVAL_BODY_INCLUDE;
        }
        else
        {
          return SKIP_BODY;
        }
      }
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
