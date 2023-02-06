package main;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Main2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Department department = new Department();
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		
		//teste Find by Id
//		int id = 3;
//		department = departmentDao.findById(id);
//		System.out.println(department);
		
		//teste insert
		department.setName("Computer");
		departmentDao.insert(department);
		
//		teste delete by id
		int id = 4;
		departmentDao.deleteById(id);
		
		//teste update
//		department.setName("Technology and Softwares");
//		departmentDao.update(department);
		
		//teste find All
//		List<Department> departments = new ArrayList<>();
//		departments = departmentDao.findAll();
//		for( Department dep : departments) {
//			System.out.println(dep);
//		}
		
		//teste findByName
		department = departmentDao.findByName("UnknowDepartment");
		System.out.println(department);
		
	}

}
