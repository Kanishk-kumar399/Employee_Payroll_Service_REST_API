package com.employeepayrollrestassured;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class EmployeePayrollRestAPITest 
{
	@Before
	public void setup()
	{
		RestAssured.baseURI="http://localhost";
		RestAssured.port=3000;
	}
	public EmployeePayrollData[] getEmployeeList()
	{
		Response response=RestAssured.get("/employees");
		System.out.println("Employee Payroll Entries In JSONServer:\n"+response.asString());
		EmployeePayrollData[] arrayOfEmployees=new Gson().fromJson(response.asString(),EmployeePayrollData[].class);
		return arrayOfEmployees;
	}
    @Test
    public void givenEmployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount()
    {
        EmployeePayrollData[] arrayOfEmployees=getEmployeeList();
        EmployeePayrollREST_IOService employeePayrollREST_IOService;
        employeePayrollREST_IOService=new EmployeePayrollREST_IOService(Arrays.asList(arrayOfEmployees));
        long entries=employeePayrollREST_IOService.countREST_IOEntries();
        Assert.assertEquals(2,entries);
    }
}
