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

	// filters on rating, capacity, brand and price [difficulty in fetching brand as separate table :( ]
	private static final String FILTER_QUERY = "from Product P where P.rating >= :rating AND P.capacity = :capacity AND P.price < :price AND P.brand = :brand_id";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.print("Filter By Rating: Enter Your Desired Min Rating: [1-5] ONLY: ");
		int inputRating = scanner.nextInt();

		System.out.print("Filter By Brand Name: [LG -1 , Samsung-2, Nokia-3]: ");
		int inputBrand = scanner.nextInt();

		System.out.print("Filter By Price Lower Than: ");
		float inputPrice = scanner.nextFloat();

		System.out.print("Filter By Capacity: [35, 25, 45, 55]: ");
		String inputCapacity = scanner.next();

		scanner.close();

		System.out.println("\n== Your Chosen filters are: == \nRating: " + inputRating + "\nBrand: " + inputBrand
				+ "\nCapacity: " + inputCapacity + "\nPriceLessThan: " + inputPrice);

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		Query query = session.createQuery(FILTER_QUERY);
		query.setParameter("rating", inputRating);
		query.setParameter("capacity", inputCapacity);
		query.setParameter("price", inputPrice);
		query.setParameter("brand_id", inputBrand);

		List<Product> results = query.list();

		System.out.println("== " + results.size() + " RESULTS FOUND ==");

		for (int index = 0; index < results.size(); index++) {
			System.out.println(results.get(index));

		}

		session.close();
		sessionFactory.close();

	}

}
