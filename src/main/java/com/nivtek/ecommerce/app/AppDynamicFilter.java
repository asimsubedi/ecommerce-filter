package com.nivtek.ecommerce.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class AppDynamicFilter {
	
	private static boolean hasFilters = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		HashMap<String, String> filterTodo = new HashMap<String, String>();

		String userInputs = getFilterInputs();

		List<String> filterParams = Arrays.asList(userInputs.trim().split(","));

		for (String str : filterParams) {
			String[] keyvalue = str.split(":");
			filterTodo.put(keyvalue[0], keyvalue[1]);
		}

		// generate our filterquery
		String filterQuery = createQueryBasedOnInput(filterTodo);
		
		System.out.println("filter query is: \n" + filterQuery);

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		@SuppressWarnings("rawtypes")
		Query query = session.createQuery(filterQuery);

		@SuppressWarnings("unchecked")
		List<Product> products = query.list();

		System.out.println(products.size() + " Items Found!! \n" + products);

		session.close();
		sessionFactory.close();

	}

	/**
	 * @return userInput Filter String
	 */
	private static String getFilterInputs() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("======================");
		System.out.println(
				"enter filters with comma separated key value pairs\neg. brand:lg,rating:4,pricerange:300-600,mincapacity:35");
		System.out.println("======================");

		System.out.print("Enter Your Desired Filters: ");
		String inputFilters = scanner.next();
		scanner.close();

		return inputFilters;
	}

	/**
	 * @param filterTodo
	 * @return String which is filter Query we use in session.createQuery();
	 */
	private static String createQueryBasedOnInput(HashMap<String, String> filterTodo) {
		
		String FILTER_QUERY = "FROM Product ";
		
		StringBuilder sbldr = new StringBuilder(FILTER_QUERY);

		for (@SuppressWarnings("rawtypes")
		Map.Entry m : filterTodo.entrySet()) {

			// if filterBy Brand is present:
			if (m.getKey().equals("brand")) {
				
				sbldr = checkIfFilters(sbldr);

				sbldr.append(" brand in " + m.getValue());

			}

			// if filterBy rating is present:
			if (m.getKey().equals("rating")) {

				sbldr = checkIfFilters(sbldr);
				sbldr.append(" rating >= " + m.getValue());

			}

			// if filterBy Price is present:
			if (m.getKey().equals("pricerange")) {

				sbldr = checkIfFilters(sbldr);

				String[] priceRange = m.getValue().toString().split("-");

				sbldr.append(" price between " + priceRange[0] + " AND " + priceRange[1]);

			}

			// if filterBy capacity is present:
			if (m.getKey().equals("mincapacity")) {

				sbldr = checkIfFilters(sbldr);

				sbldr.append(" capacity >= " + m.getValue());

			}

		}

		return sbldr.toString();
	}

	private static StringBuilder checkIfFilters(StringBuilder sbldr) {
		if (hasFilters)
			sbldr.append(" AND ");

		else {
			sbldr.append(" WHERE ");
			hasFilters = true;

		}
		return sbldr;
	}
}