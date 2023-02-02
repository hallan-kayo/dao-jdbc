package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DbExceptions;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection connection;

	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Department department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			statement = this.connection.prepareStatement(
					"SELECT Id,Name FROM department "
					+ "WHERE department.id = ?"
					);
			
			statement.setInt(1, id);
			result = statement.executeQuery();
			
			if(result.next()) {
				Department department = this.instanceDepartment(result);
				return department;
			}
			return null;
					
		}
		catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}
	}

	@Override
	public List<Department> findAll(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	private Department instanceDepartment(ResultSet result) throws SQLException {
		
		Department department = new Department();
		department.setId(result.getInt("Id"));
		department.setName(result.getString("Name"));
		
		return department;
		
	}

	
	
}
