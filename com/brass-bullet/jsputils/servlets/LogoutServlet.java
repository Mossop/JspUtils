package com.brass-bullet.jsputils.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class LogoutServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    request.getSession().removeAttribute("uws.eleceng.webdb.SecureBean.securityinfo");
    response.sendRedirect("/index.jsp");
  }
}
