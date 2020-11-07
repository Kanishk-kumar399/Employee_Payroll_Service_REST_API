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
	//UC4
    @Test
    public void givenEmployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount()
    {
        EmployeePayrollData[] arrayOfEmployees=getEmployeeList();
        EmployeePayrollREST_IOService employeePayrollREST_IOService;
        employeePayrollREST_IOService=new EmployeePayrollREST_IOService(Arrays.asList(arrayOfEmployees));
        long entries=employeePayrollREST_IOService.countREST_IOEntries();
        Assert.assertEquals(7,entries);
    }
    //uc1
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
    //uc2
    @Test
    public void givenMultipleEmployees_WhenAdded_ShouldMatch210ResponseAndCount()
    {
    	EmployeePayrollService employeePayrollService;
    	EmployeePayrollData[] arrayOfEmployees=getEmployeeList();
    	employeePayrollService=new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
    	EmployeePayrollData[] arrayOfEmployeePayroll= {
    			new EmployeePayrollData(5,"Ms Dhoni",7777737.0),
    			new EmployeePayrollData(6,"Sanjay Singhaniya",6265777.0),
    			new EmployeePayrollData(7,"Ronaldo Singh",9777777.0)
    	};
    	for(EmployeePayrollData employeePayrollData:arrayOfEmployeePayroll)
    	{
    		Response response=addEmployeeToJsonServer(employeePayrollData);
    		int HTTPstatusCode=response.getStatusCode();
    		Assert.assertEquals(201,HTTPstatusCode);
    		employeePayrollData=new Gson().fromJson(response.asString(),EmployeePayrollData.class);
        	employeePayrollService.addEmployeeToPayrollUsingRestAPI(employeePayrollData);
    	}
    	long entries=employeePayrollService.countREST_IOEntries();
    	Assert.assertEquals(7,entries);
    }
    //uc3
    @Test
    public void givenNewSalaryForAnyEmployee_WhenUpdated_ShouldMatch200Response()
    {
    	EmployeePayrollService employeePayrollService;
    	EmployeePayrollData[] arrayOfEmployees=getEmployeeList();
    	employeePayrollService=new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
    	employeePayrollService.updateEmployeeSalaryUsingRest_IO("Abhinav",3453545.0);
    	EmployeePayrollData employeePayrollData=employeePayrollService.getEmployeePayrollData("Abhinav");
    	String empJson=new Gson().toJson(employeePayrollData);
    	RequestSpecification request=RestAssured.given();
		request.header("Content-Type","application/json");
		request.body(empJson);
		Response response=request.put("/employees/"+employeePayrollData.getId());
		int statusCode=response.getStatusCode();
		Assert.assertEquals(200, statusCode);
    }
    //uc5
    @Test
    public void givenEmployeeToBeDeleted_WhenDeleted_ShouldMatch200Response()
    {
    	EmployeePayrollService employeePayrollService;
    	EmployeePayrollData[] arrayOfEmployees=getEmployeeList();
    	employeePayrollService=new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
    	EmployeePayrollData employeePayrollData=employeePayrollService.getEmployeePayrollData("Abhinav");
    	RequestSpecification request=RestAssured.given();
		request.header("Content-Type","application/json");
		Response response=request.delete("/employees/"+employeePayrollData.getId());
		int statusCode=response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		employeePayrollService.deleteEmployeePayroll_UsingRest_IO(employeePayrollData.getName());
		long entries=employeePayrollService.countREST_IOEntries();
    	Assert.assertEquals(6,entries);
    }
}
