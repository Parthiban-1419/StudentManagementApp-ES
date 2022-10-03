package com.student;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

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
import com.es.LogManager;
import org.json.simple.JSONObject;


@WebServlet("/delete-marks")
public class DeleteMarks extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int regNumber = Integer.parseInt(request.getParameter("regNumber"));
		PrintWriter out = response.getWriter();
		JSONObject jLog = new JSONObject();
		LogManager log = new LogManager(request.getParameter("index"));
		
		Criteria criteria = new Criteria(new Column("MarkDetails", "REG_NUMBER"), regNumber, QueryConstants.EQUAL);
		try {
			DataAccess.delete(criteria);
			jLog.put("userId", request.getParameter("user"));
			jLog.put("operation", "DELETE");
			jLog.put("studentId", regNumber);
			jLog.put("time", new Date().getTime());
			log.putLog(jLog);
			out.print("Deleted uccessfully");
		} catch (DataAccessException e) {
			out.print("Unnable to delete");
		}
	}

}
