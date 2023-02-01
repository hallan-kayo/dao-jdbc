package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbExceptions;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	//a classe depende da conexão com o banco de dados
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
			
			//verificando se há dados encontrados.
			if(result.next()) {
				
				Department department = instanceDepartment(result);
				Seller seller = instanceSeller(result,department);
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
	
	@Override
	public List<Seller> findByDepartment(Integer id){
		
		List<Seller> sellers = new ArrayList<>();
		
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			statement = this.connection.prepareStatement(
					"SELECT seller.* , department.Name AS DepName " 
					+"FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name"

			);
			
			statement.setInt(1, id);
			result = statement.executeQuery();
			
			while(result.next()) {
				
				//mapeando o departamento
				Department department = mapDepartment(result);
				Seller seller = instanceSeller(result, department);
				
				sellers.add(seller);
				
			}
			return sellers;
		}
		catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(result);
		}
	}
	
	
	
	
	
	private Department mapDepartment(ResultSet result) throws SQLException {
		
		//mapear os departamentos para não salvar vários iguais
		Map<Integer, Department> map = new HashMap<>();
		
		Department department = map.get(result.getInt("DepartmentId")); //pegando o Id
		
		if (department == null) {
			department = instanceDepartment(result); //se o departamento não existir, instancia um
			map.put(result.getInt("DepartmentId"), department); //coloca o departamento na instância
		}
		return department;
	}

	private Department instanceDepartment(ResultSet result) throws SQLException {
		Department department = new Department();
		department.setId(result.getInt("DepartmentId"));
		department.setName(result.getString("depName"));
		return department;
	}

	private Seller instanceSeller(ResultSet result, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(result.getInt("Id"));
		seller.setName(result.getString("Name"));
		seller.setEmail(result.getString("Email"));
		seller.setBirthdate(result.getDate("BirthDate"));
		seller.setBaseSalary(result.getDouble("BaseSalary"));
		seller.setDepartment(department);
		return seller;
	}
}