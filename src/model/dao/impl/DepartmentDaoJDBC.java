package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbExceptions;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection connection;
	private PreparedStatement statement = null;
	private ResultSet result = null;
	

	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department department) {
		
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
		
		try {
			
			statement = this.connection.prepareStatement(
					"UPDATE department "
					+ "SET Name = ?"
					+ "WHERE Id = ?"
					);
			statement.setString(1, department.getName());
			statement.setInt(2, department.getId());
			
			statement.executeUpdate();
			System.out.println("Updated sucessfull!");
			
		}catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}finally {
			DB.closeStatement(statement);
		}
	}

	@Override
	public void deleteById(Integer id) {
			
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		//verificando se existe um departamento o Id passado.
		//colocar em um método separado
		if(this.findById(id) == null) {
			System.out.println("Id not found.");
			return;
		}
		
		//fazendo varredura nos vendedores, verificando se há vendedores vinculados a este departamento
		List<Seller> linkedSellersToDepartment = sellerDao.findByDepartment(id);
		
		//se não houver vínculos, apaga o departamento
		if(linkedSellersToDepartment.isEmpty()) {
			try {
				this.connection.setAutoCommit(false);
				statement = this.connection.prepareStatement(
						"DELETE FROM department "
						+ "WHERE department.Id = ?"
						);
				
				statement.setInt(1, id);
				statement.executeUpdate();
				this.connection.commit();
				System.out.println("Deleted Sucessfull.");
			}
			catch(SQLException e) {
				try {
					this.connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				throw new DbExceptions(e.getMessage());
			}
			finally {
				DB.closeStatement(statement);
			}
			return;
		}
		
		//se houver vínculos 
		for(Seller seller : linkedSellersToDepartment) {
			Department department = this.findById(id);
			department.setId(this.findByName("UnknowDepartment").getId());
			seller.setDepartment(department);
			sellerDao.update(seller);
		}
		this.deleteById(id);
		
		
			
		
	}

	@Override
	public Department findById(Integer id) {
		
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

	public Department findByName(String name) {
		
		try {
			statement = this.connection.prepareStatement(
					"SELECT department.Id,department.Name FROM department WHERE department.Name = ?"
					);
			statement.setString(1, name);
			result = statement.executeQuery();
			
			if(result.next()) {
				Department department = instanceDepartment(result);
				return department;
				
			}
			return null;
		}catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}
		finally {
			DB.closeResultSet(result);
			DB.closeStatement(statement);
		}
		
	}
	
	@Override
	public List<Department> findAll() {
		
		List<Department> departments = new ArrayList<>();
		
		try {
			
			statement = this.connection.prepareStatement(
					"SELECT * FROM department"
					);
			
			result = statement.executeQuery();
			while(result.next()) {
				Department department = this.instanceDepartment(result);
				departments.add(department);
			}
			return departments;
			
		}catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(result);
		}
	}

	private Department instanceDepartment(ResultSet result) throws SQLException {
		
		Department department = new Department();
		department.setId(result.getInt("Id"));
		department.setName(result.getString("Name"));
		
		return department;
		
	}

	
	
}
