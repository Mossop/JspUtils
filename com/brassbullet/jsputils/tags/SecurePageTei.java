package com.brassbullet.jsputils.tags;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

public class SecurePageTei extends TagExtraInfo {
  
  public SecurePageTei()
  {
    super();
  }

  public VariableInfo[] getVariableInfo(TagData data)
  {
		VariableInfo info = new VariableInfo("authUser","java.lang.String",true,VariableInfo.NESTED);
    VariableInfo[] back = { info };
    return back;
  }

  public boolean isValid(TagData data)
  {
    return true;
  }
}

