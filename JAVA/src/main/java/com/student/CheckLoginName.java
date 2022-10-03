package com.student;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;

@WebServlet("/check-login-name")
public class CheckLoginName extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String loginName = request.getParameter("logInName");
		DataObject d = null;
		Criteria criteria = new Criteria(new Column("AaaLogin", "NAME"), loginName, QueryConstants.EQUAL);
		try {
			out.print(DataAccess.get("AaaLogin", criteria).getRows("AaaLogin").hasNext());
		} catch (DataAccessException e) {
			out.print(e);
		}
	}

}
