package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbExceptions;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection connection;
	
	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller seller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller seller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			statement = this.connection.prepareStatement(
					"SELECT seller.* , department.Name AS depName "
					+ "FROM seller INNER JOIN department " 
					+ "ON seller.DepartmentId = department.id "
					+ "WHERE seller.Id = ?"

					);
			
			statement.setInt(1, id);
			result = statement.executeQuery();
			
			if(result.next()) {
				
				Department department = new Department();
				department.setId(result.getInt("DepartmentId"));
				department.setName(result.getString("depName"));
				
				Seller seller = new Seller();
				seller.setId(result.getInt("Id"));
				seller.setName(result.getString("Name"));
				seller.setEmail(result.getString("Email"));
				seller.setBirthdate(result.getDate("BirthDate"));
				seller.setBaseSalary(result.getDouble("BaseSalary"));
				seller.setDepartment(department);
				return seller;
			}
			return null;
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
	public List<Seller> findAll(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
