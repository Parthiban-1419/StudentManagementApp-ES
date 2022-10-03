package com.student;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;

@WebServlet("/get-role")
public class GetRole extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String name = request.getParameter("name");
		Criteria criteria = new Criteria(new Column("AaaLogin", "NAME"),name, QueryConstants.EQUAL);
		Persistence per;
		try {
			per = (Persistence)BeanUtil.lookup("Persistence");
			DataObject d =per.getForPersonality("AccountDOForSSO", criteria);
			out.print(d.getRow("AaaRole").get("NAME"));
		} catch (Exception e) {
			out.print(e);
		}
	}

}
