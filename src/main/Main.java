package main;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Department dep = new Department(1, "eletronics");
		
		System.out.println(dep);
		
		Seller seller = new Seller(12, "halan", "halan@email.com", new Date(), 3000.0, dep);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println(seller);

	}

}
