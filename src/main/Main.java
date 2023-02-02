package main;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SellerDao sellerDao = DaoFactory.createSellerDao();
		Department department = new Department(2,null);
		
		
		//teste do find by id
//		Seller seller = sellerDao.findById(3);
//		System.out.println(seller);
		
		
		
		//teste do find by department
//		List<Seller> sellers = new ArrayList();
//		sellers = sellerDao.findByDepartment(2);
//		for(Seller seller : sellers) {
//			System.out.println(seller);
//		}
		
		
		
		//teste do find all
//		List<Seller> sellers = new ArrayList();	
//		sellers = sellerDao.findAll();
//		for(Seller seller : sellers) {
//			System.out.println(seller);
//		}
		
		
		
		//teste do insert
//		Seller seller = new Seller();
//		seller.setName("hallan");
//		seller.setEmail("hallan@email.com");
//		seller.setBirthdate(new Date());
//		seller.setBaseSalary(6000.0);
//		seller.setDepartment(department);
//		sellerDao.insert(seller);
//		System.out.println("Inserted! New Id: " + seller.getId());
		
		
		
		//teste update
//		Seller seller = sellerDao.findById(9);
//		seller.setName("Hallan Kayo");
//		sellerDao.update(seller);
//		System.out.println("Updated!");
		
		
		
		//teste deleteById
//		int id = 9;
//		sellerDao.deleteById(id);

	}

}
