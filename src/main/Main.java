package main;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		//teste do find by id
//		Seller seller = sellerDao.findById(3);
//		
//		System.out.println(seller);
		
		
		//teste do find by department
//		List<Seller> sellers = new ArrayList();
//		
//		sellers = sellerDao.findByDepartment(2);
//		for(Seller seller : sellers) {
//			System.out.println(seller);
//		}
		
		//teste do find all
		List<Seller> sellers = new ArrayList();	
		sellers = sellerDao.findAll();
		for(Seller seller : sellers) {
			System.out.println(seller);
		}

	}

}
