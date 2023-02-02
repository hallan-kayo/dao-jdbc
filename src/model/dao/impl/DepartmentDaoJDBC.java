package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import db.DB;
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
		
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			
			statement = this.connection.prepareStatement(
					"INSERT INTO department "
					+ "(Name) "
					+ "VALUES (?)",
					Statement.RETURN_GENERATED_KEYS
					);
			statement.setString(1, department.getName());
			
			int rowsAffected = statement.executeUpdate();
			if(rowsAffected > 0) {
				result = statement.getGeneratedKeys();
				while(result.next()) {
					int id = result.getInt(1);
					System.out.println("Done! Id: " + id);
				}
			}else {
				throw new DbExceptions("unexpected error! No rows affected.");
			}
			
		}
		catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(result);
		}
		
	}

	@Override
	public void update(Department department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement statement = null;
		
		if(findById(id) == null) {
			System.out.println("Id not found.");
			return;
		}
		
		try {
			statement = this.connection.prepareStatement(
					"DELETE FROM department "
					+ "WHERE department.Id = ?"
					);
			
			statement.setInt(1, id);
			statement.executeUpdate();
			System.out.println("Deleted Sucessfull.");
		}
		catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
		
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
