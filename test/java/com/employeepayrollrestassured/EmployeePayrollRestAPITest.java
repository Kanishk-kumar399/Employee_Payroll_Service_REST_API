package com.employeepayrollrestassured;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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
    @Test
    public void givenNewEmployeeWhenAddedShouldMatch201ResponseAndcount()
    {
    	EmployeePayrollService employeePayrollService;
    	EmployeePayrollData[] arrayOfEmployees=getEmployeeList();
    	employeePayrollService=new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
    	EmployeePayrollData employeePayrollData=new EmployeePayrollData(4,"bhinav",2223223.0);
    	Response response=addEmployeeToJsonServer(employeePayrollData);
    	int HTTPstatusCode=response.getStatusCode();
    	Assert.assertEquals(201,HTTPstatusCode);
    	employeePayrollData=new Gson().fromJson(response.asString(),EmployeePayrollData.class);
    	employeePayrollService.addEmployeeToPayrollUsingRestAPI(employeePayrollData);
    	long entries=employeePayrollService.countREST_IOEntries();
    	Assert.assertEquals(4,entries);
    }
    public Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
		String employeeJson=new Gson().toJson(employeePayrollData);
		RequestSpecification request=RestAssured.given();
		request.header("Content-Type","application/json");
		request.body(employeeJson);
		return request.post("/employees");
	}
}
