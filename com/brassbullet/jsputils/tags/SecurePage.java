package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.RequestDispatcher;
import com.brassbullet.jsputils.SecureBean;

public class SecurePage extends TagSupport
{
  private String title;
  private String groups;
  private String users;
  private String notgroups;
  private String notusers;
  private boolean valid;
  
  public SecurePage()
  {
    super();
    setTitle("");
    valid=false;
  }
  
  public void setTitle(String Title)
  {
    title=Title;
  }
  
  public String getTitle()
  {
    return title;
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
    SecureBean secure = (SecureBean)pageContext.getAttribute("uws.eleceng.webdb.SecureBean.securityinfo",PageContext.SESSION_SCOPE);

    if (secure!=null)
    {
      if ((users==null)&&(groups==null)&&(notusers==null)&&(notgroups==null))
      {
        valid=true;
      }
      else
      {
        valid=(((secure.isUser(users))||(secure.inGroup(groups)))&&(!((secure.isUser(notusers))||(secure.inGroup(notgroups)))));
      }
    }
    else
    {
      valid=false;
    }
    
    if (valid)
    {
      try
      {
        pageContext.setAttribute("authUser",secure.getUser());
        pageContext.getOut().flush();
        pageContext.include("/include/mainstart.jsp?pageTitle="+title);
      }
      catch (Exception e)
      {
        return SKIP_BODY;
      }
    
      return EVAL_BODY_INCLUDE;
    }
    else
    {
      try
      {
        RequestDispatcher dispatch = pageContext.getServletContext().getNamedDispatcher("login");
        
        dispatch.include(pageContext.getRequest(), pageContext.getResponse());
      }
      catch (Exception e)
      {
        return SKIP_BODY;
      }
      
      return SKIP_BODY;
    }
  }
  
  public int doEndTag() throws JspTagException
  {
    if (valid)
    {
      try
      {
        pageContext.getOut().flush();
        pageContext.include("/include/mainend.jsp");
        pageContext.removeAttribute("authUser");
      }
      catch (Exception e)
      {
        return SKIP_PAGE;
      }
    
      return EVAL_PAGE;
    }
    else
    {
      return SKIP_PAGE;
    }
  }
}
