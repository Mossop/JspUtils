package com.brass-bullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

public class SqlQueryTei extends TagExtraInfo {
  
  public SqlQueryTei()
  {
    super();
  }

  public VariableInfo[] getVariableInfo(TagData data)
  {
    String idValue = data.getAttributeString("id");

    if (idValue == null)
    {
      return new VariableInfo[0];
    }
    else
    {
      VariableInfo info = new VariableInfo(idValue,"java.sql.ResultSet",true,VariableInfo.AT_END);
      VariableInfo[] back = { info };
      return back;
    }
  }

  public boolean isValid(TagData data)
  {
    return true;
  }
}

