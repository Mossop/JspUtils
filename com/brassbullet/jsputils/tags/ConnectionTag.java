package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import com.brassbullet.dbutils.ConnectionPool;
import java.sql.Connection;

public class ConnectionTag extends TagSupport
{
  private Connection con;
  private ConnectionPool pool;

  public ConnectionTag()
  {
    super();
    con=null;
    pool=null;
  }
  
  public int doStartTag() throws JspTagException
  {
    try
    {
    	pool = (ConnectionPool)pageContext.findAttribute("uws.eleceng.webdb.ConnectionPool");
      if (pool!=null)
      {
      	con=pool.getConnection();
      	if (con!=null)
      	{
      	  pageContext.setAttribute(getId(),con);
      	  return EVAL_BODY_INCLUDE;
      	}
      	else
      	{
      	  return SKIP_BODY;
      	}
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
    if ((con!=null)&&(pool!=null))
    {
      pageContext.removeAttribute(getId());
      pool.releaseConnection(con);
    }
    return EVAL_PAGE;
  }
}
