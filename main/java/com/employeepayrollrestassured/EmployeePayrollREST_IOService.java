package com.employeepayrollrestassured;

import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollREST_IOService 
{
	List<EmployeePayrollData> employeePayrollDataList;
    public EmployeePayrollREST_IOService(List<EmployeePayrollData> employeeList) {
		employeePayrollDataList=new ArrayList<>(employeeList);
	}
	public long countREST_IOEntries() {
		return employeePayrollDataList.size();
	}
}
