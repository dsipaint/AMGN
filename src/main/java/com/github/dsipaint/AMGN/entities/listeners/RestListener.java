package com.github.dsipaint.AMGN.entities.listeners;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//listener interface for receiving and handling REST requests made to /api/plugin/{pluginname}
public interface RestListener extends IListener
{
    public Object handleRequest(HttpServletRequest request, HttpServletResponse response);
}
