package com.student;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.*;
import com.es.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

@WebServlet("/get-marks")
public class GetMarks extends HttpServlet {
	PrintWriter out;
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		out = response.getWriter();
		JSONObject json = new JSONObject();
		
		try {	
			User user = User.getObj();
			checkSession(request.getParameter("index"), user.getUser());

			json.put("user", user.getUser());
			json.put("marks", getData("MarkDetails"));
			json.put("students", getData("StudentDetail"));
			user.setUser("");

			out.print(json);
		} catch (Exception e) {
			out.print(e);
		}
	}

	JSONArray getData(String table) throws DataAccessException{
		JSONArray jArray = new JSONArray();
		Row r = null;
		DataObject d = null;
		Iterator i = null;
		
		d = DataAccess.get(table, (Criteria)null);
		i = d.getRows(table);
		
		while(i.hasNext()) {
			r = (Row) i.next();
			jArray.add(r.getAsJSON());
		}
		
		return jArray;
	}

	public void checkSession(String index, String loginName){
		System.out.println("checking Session : "+loginName);
		Criteria criteria1 = new Criteria(new Column("AaaLogin", "NAME"),loginName, QueryConstants.EQUAL),
				criteria2 = new Criteria(new Column("AaaAccSession", "STATUS"),"ACTIVE", QueryConstants.EQUAL),
				criteria = criteria1.and(criteria2);
		Persistence per;
		LogManager log = new LogManager(index);
		long time;
		try {
			per = (Persistence) BeanUtil.lookup("Persistence");
			DataObject d = per.getForPersonality("AccountDOForSSO", criteria);
			time =  (Long) d.getRow("AaaAccSession").get("OPENTIME");
			System.out.println("Log-in time : " + time);
			if(!(Boolean) log.checkLog("time", time)){
				JSONObject jLog = new JSONObject();
				jLog.put("userId", loginName);
				jLog.put("operation", "LOGIN");
				jLog.put("studentId", "");
				jLog.put("time", time);
				log.putLog(jLog);

				jLog = new JSONObject();
				jLog.put("userId", loginName);
				jLog.put("operation", "VIEW");
				jLog.put("studentId", "");
				jLog.put("time", time);
				log.putLog(jLog);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}
