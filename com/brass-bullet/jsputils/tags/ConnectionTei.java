package com.brass-bullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

public class ConnectionTei extends TagExtraInfo {
  
  public ConnectionTei()
  {
    super();
  }

  public VariableInfo[] getVariableInfo(TagData data)
  {
    String idValue = data.getId();

    if (idValue == null)
    {
      return new VariableInfo[0];
    }
    else
    {
      VariableInfo info = new VariableInfo(idValue,"java.sql.Connection",true,VariableInfo.NESTED);
      VariableInfo[] back = { info };
      return back;
    }
  }

  public boolean isValid(TagData data)
  {
    return true;
  }
}

