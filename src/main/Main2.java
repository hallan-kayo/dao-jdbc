package main;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Main2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Department department = new Department();
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		int id = 3;
		department = departmentDao.findById(id);
		System.out.println(department);

	}

}
