package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

public class ForAllTei extends TagExtraInfo {
  
  public ForAllTei()
  {
    super();
  }

  public VariableInfo[] getVariableInfo(TagData data)
  {
    String idValue = data.getAttributeString("query");

    if (idValue == null)
    {
      return new VariableInfo[0];
    }
    else
    {
      VariableInfo info = new VariableInfo(idValue,"java.sql.ResultSet",true,VariableInfo.NESTED);
      VariableInfo[] back = { info };
      return back;
    }
  }

  public boolean isValid(TagData data)
  {
    return true;
  }
}

