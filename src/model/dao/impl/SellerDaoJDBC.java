package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		
		PreparedStatement statement = null;
		
		try {
			statement = this.connection.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES (?,?,?,?,?) ",
					Statement.RETURN_GENERATED_KEYS
					);
			statement.setString(1, seller.getName());
			statement.setString(2, seller.getEmail());
			statement.setDate(3, new java.sql.Date(seller.getBirthdate().getTime()));
			statement.setDouble(4, seller.getBaseSalary());
			statement.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = statement.executeUpdate();
			if(rowsAffected > 0 ) {
				ResultSet result = statement.getGeneratedKeys();
				while(result.next()) {
					int id = result.getInt(1);
					System.out.println("Done! Id: " + id);
				}
				DB.closeResultSet(result);
			}else {
				throw new DbExceptions("unexpected error! No rows affected.");
			}
		}
		catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
		
	}

	@Override
	public void update(Seller seller) {
		
		PreparedStatement statement = null;
		
		try {
			statement = this.connection.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId =? "
					+ "WHERE Id = ?"
					);
			statement.setString(1, seller.getName());
			statement.setString(2, seller.getEmail());
			statement.setDate(3, new java.sql.Date(seller.getBirthdate().getTime()));
			statement.setDouble(4, seller.getBaseSalary());
			statement.setInt(5, seller.getDepartment().getId());
			statement.setInt(6, seller.getId());
			
			statement.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement statement = null;
		
		if(this.findById(id) == null) {
			System.out.println("Id not found");
			return;
		}
		
		try {
			statement = this.connection.prepareStatement(
					"DELETE FROM seller "
					+ "WHERE Id = ?"
					);
			
			statement.setInt(1, id);
			
			statement.executeUpdate();
			System.out.println("Deleted sucessfull! ");
		}
		catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
		
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
	public List<Seller> findAll() {
		List<Seller> sellers = new ArrayList<>();
		
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			statement = this.connection.prepareStatement(
					"SELECT seller.* , department.Name AS DepName " 
					+"FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name"

			);
			
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