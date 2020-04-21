package com.nivtek.ecommerce.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

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

		Map<String, String> filterTodo = new HashMap<String, String>();

		String userInputs = getFilterInputs();

		// Convert The userInput into filterTodo as key-value pair
		filterTodo = Arrays.asList(userInputs.trim().split(",")).stream().map(str -> str.split(":"))
				.collect(Collectors.toMap(str -> str[0], str -> str[1]));

		// generate our filter query
		String filterQuery = createQueryBasedOnInput(filterTodo);
		System.out.println("filter query is: \n" + filterQuery);

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		@SuppressWarnings("rawtypes")
		Query query = session.createQuery(filterQuery);

		@SuppressWarnings("unchecked")
		List<Product> products = query.list();

		System.out.println("\n[ " + products.size() + " ] Items Found!!");
		products.forEach(System.out::println);

		session.close();
		sessionFactory.close();

	}

	/**
	 * @return userInput Filter String
	 */
	private static String getFilterInputs() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("========================================");
		System.out.println(
				" Enter the filters with comma separated key value pairs\n eg. brand:lg,rating:4,pricerange:300-600,mincapacity:35");
		System.out.println("========================================");

		System.out.print("Enter Your Desired Filters: ");
		String inputFilters = scanner.next();
		scanner.close();

		return inputFilters;
	}

	/**
	 * @param filterTodo
	 * @return String which is filter Query we use in session.createQuery();
	 */
	private static String createQueryBasedOnInput(Map<String, String> filterTodo) {

		String FILTER_QUERY = "FROM Product ";

		StringBuilder sbldr = new StringBuilder(FILTER_QUERY);

		for (@SuppressWarnings("rawtypes")
		Map.Entry m : filterTodo.entrySet()) {

			// if filterBy Brand is present:
			if (m.getKey().equals("brand")) {

				sbldr = checkIfFilters(sbldr);
				sbldr.append(" brand in ('" + m.getValue() + "') ");

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

	/**
	 * @param sbldr
	 * @return query with AND appended if some filter already present else append
	 *         WHERE if no filter appended yet.
	 */
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
