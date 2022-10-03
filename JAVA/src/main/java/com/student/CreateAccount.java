package com.student;



import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adventnet.authentication.util.AuthUtil;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;

@WebServlet("/create-account")
public class CreateAccount extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String loginName = request.getParameter("logInName"), firstName = request.getParameter("firstName"), middleName = request.getParameter("middleName"), lastName = request.getParameter("lastName"), serviceName = "System";
		String accAdminProfile = "Profile 1", password = request.getParameter("password"), passwordProfile = "Profile 1";
		int role = Integer.parseInt(request.getParameter("role"));
		
		try
		{
			Persistence persistence = (Persistence) BeanUtil.lookup("Persistence"); 
			DataObject dobj = persistence.constructDataObject();
			Row userRow = new Row("AaaUser");
			userRow.set("FIRST_NAME", firstName);
			userRow.set("MIDDLE_NAME", middleName); // optional
			userRow.set("LAST_NAME", lastName); // optional
			dobj.addRow(userRow);
	
			Row loginRow = new Row("AaaLogin");
			loginRow.set("NAME", loginName);
			dobj.addRow(loginRow);
	
			Row accRow = new Row("AaaAccount");
			accRow.set("SERVICE_ID", AuthUtil.getServiceId(serviceName));
			accRow.set("ACCOUNTPROFILE_ID", AuthUtil.getAccountProfileId(accAdminProfile));
			dobj.addRow(accRow);
	
			Row passwordRow = new Row("AaaPassword");
			passwordRow.set("PASSWORD", password);
			passwordRow.set("PASSWDPROFILE_ID", AuthUtil.getPasswordProfileId(passwordProfile));
			dobj.addRow(passwordRow);
	
			Row accPassRow = new Row("AaaAccPassword");
			accPassRow.set("ACCOUNT_ID", accRow.get("ACCOUNT_ID"));
			accPassRow.set("PASSWORD_ID", passwordRow.get("PASSWORD_ID"));
			dobj.addRow(accPassRow);
	
			// to assign roles - optional
			Row authRoleRow = new Row("AaaAuthorizedRole");
			authRoleRow.set("ACCOUNT_ID", accRow.get("ACCOUNT_ID"));
			authRoleRow.set("ROLE_ID", role);
			dobj.addRow(authRoleRow);
	
			// to permit this user to create another user - optional
			int noOfSubAccounts = -1; // -1 to create unlimited users, 0 - for no user, n - to create n user accounts
			Row accOwnerProfileRow = new Row("AaaAccOwnerProfile");
			accOwnerProfileRow.set("ACCOUNT_ID", accRow.get("ACCOUNT_ID"));
			accOwnerProfileRow.set("ALLOWED_SUBACCOUNT", noOfSubAccounts);
			dobj.addRow(accOwnerProfileRow);
	
			AuthUtil.createUserAccount(dobj);
			out.print("Account created successfully...");
		}
		catch(Exception e){
			out.print(e);
		}
	}
}
