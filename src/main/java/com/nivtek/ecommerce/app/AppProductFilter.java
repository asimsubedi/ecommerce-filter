/**
 * 
 */
package com.nivtek.ecommerce.app;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.nivtek.ecommerce.entity.Product;
import com.nivtek.ecommerce.util.HibernateUtil;

/**
 * @author AsimSubedi
 *
 */
public class AppProductFilter {

	// filters on rating, capacity, brand and price [difficulty in fetching brand as
	// separate table :( ]
	private static final String FILTER_QUERY = "FROM Product WHERE rating >= :rating AND brand = :brand AND"
			+ "  price between :minprice and :maxprice AND capacity >= :capacity";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.print("Filter By Rating: Enter Your Desired Min Rating: [1-5] ONLY: ");
		int inputRating = scanner.nextInt();

		System.out.print("Filter By Brand Name: [LG , Samsung, Nokia]: ");
		String inputBrand = scanner.next();

		System.out.print("Filter By Price Range\nEnter Lower Amount: ");
		float inputPriceMin = scanner.nextFloat();

		System.out.println("Enter Higher Amount: ");
		float inputPriceMax = scanner.nextFloat();

		System.out.print("Filter By Capacity: ");
		float inputCapacity = scanner.nextFloat();

		scanner.close();

		System.out.println("\n=== SUMMARY ===\nYour Chosen filters are: ");
		System.out.println("Rating Greater Than: " + inputRating + "\nBrand: " + inputBrand + "\nMin Price: "
				+ inputPriceMin + "\nMax Price: " + inputPriceMax + "\nCapacity: " + inputCapacity);

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		Query query = session.createQuery(FILTER_QUERY);
		
		query.setParameter("rating", inputRating);
		query.setParameter("brand", inputBrand);
		query.setParameter("minprice", inputPriceMin);
		query.setParameter("maxprice", inputPriceMax);
		query.setParameter("capacity", inputCapacity);
		

		List<Product> products = query.list();

		System.out.println(products.size() + " Items Found!! \n" + products);

		session.close();
		sessionFactory.close();

	}

}
