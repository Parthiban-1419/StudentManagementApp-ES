package com.student;



import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.WritableDataObject;

@WebServlet("/add-student")
public class AddStudent extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String regNumber = request.getParameter("number"), name = request.getParameter("name"), gender = request.getParameter("gender"), dob = request.getParameter("dob");
		
		try {
			
			Persistence pers = (Persistence)BeanUtil.lookup("Persistence");
			Row r = new Row("StudentDetail");
			r.set("REG_NUMBER", regNumber);
			r.set("STUDENT_NAME", name);
			r.set("GENDER", gender);
			r.set("DOB", dob);
			
			DataObject d=new WritableDataObject();
			d.addRow(r);
			d = DataAccess.add(d);	
			
			if(d.getRows("StudentDetail").hasNext()) 
				out.print("Student detail added successfully");
			else
				out.print("Unnable to add student detail");
		} catch (Exception e) {
			out.print(e);
		}
	}

}
