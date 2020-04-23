package com.nivtek.ecommerce.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

	private SessionFactory sessionFactory;
	private Session session;
	private CriteriaBuilder builder;
	private CriteriaQuery<Product> criteriaQuery;
	private Root<Product> root;
	private List<Predicate> predicates;

	/**
	 * This is the main method. Nothing special just calling getInputFilter Method
	 * and then converting that string into hashmap of filter criteria and filter
	 * query,Then calling filteredProducts() method that does all the action
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		AppDynamicFilter app = new AppDynamicFilter();
		Map<String, String> filterTodo = new HashMap<String, String>();

		String userInputs = getFilterInputs();

		// Convert The userInput into filterTodo as key-value pair
		filterTodo = Arrays.asList(userInputs.trim().split(",")).stream().map(str -> str.split(":"))
				.collect(Collectors.toMap(str -> str[0], str -> str[1]));

		app.getFilteredProducts(filterTodo);

	}

	/**
	 * This method is just asking for the filter needs from user using the scanner
	 * and returning it as string. nothing else.
	 * 
	 * @return userInput Filter String
	 */
	private static String getFilterInputs() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("========================================");
		System.out.println(" Enter the filters with comma separated key value pairs\n"
				+ " eg. brand:lg,rating:4,pricerange:300-600,mincapacity:35");
		System.out.println("========================================");

		System.out.print("Enter Your Desired Filters: ");
		String inputFilters = scanner.next();
		scanner.close();

		return inputFilters;
	}

	/**
	 * This method is handling everything from creating session, predicates and
	 * doing query and printing all the query result products
	 * 
	 * @param filterTodo
	 */
	private void getFilteredProducts(Map<String, String> filterTodo) {
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();

		builder = session.getCriteriaBuilder();
		criteriaQuery = builder.createQuery(Product.class);

		root = criteriaQuery.from(Product.class);

		predicates = new ArrayList<Predicate>();
		criteriaQuery.select(root);

		generatePredicatesBasedonCondition(filterTodo);

		criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

		Query<Product> queryC = session.createQuery(criteriaQuery);
		List<Product> products = queryC.list();

		System.out.println("\n[ " + products.size() + " ] Items Found!!");
		products.forEach(System.out::println);

		session.close();
		sessionFactory.close();
	}

	/**
	 * @param filterTodo
	 * @return String which is filter Query we use in session.createQuery();
	 */
	private void generatePredicatesBasedonCondition(Map<String, String> filterTodo) {

		for (@SuppressWarnings("rawtypes")
		Map.Entry m : filterTodo.entrySet()) {

			// if filterBy Brand is present:

			if (m.getKey().equals("brand")) {
				predicates.add(builder.and(root.get("brand").in(m.getValue())));

			}

			// if filterBy rating is present:

			if (m.getKey().equals("rating")) {
				predicates.add(builder.ge(root.get("rating"), Integer.parseInt((String) m.getValue())));

			}

			// if filterBy Price is present:

			if (m.getKey().equals("pricerange")) {

				String[] priceRange = m.getValue().toString().split("-");
				predicates.add(builder.ge(root.get("price"), Float.parseFloat(priceRange[0])));
				predicates.add(builder.le(root.get("price"), Float.parseFloat(priceRange[1])));

			}

			// if filterBy capacity is present:
			if (m.getKey().equals("mincapacity")) {
				predicates.add(builder.ge(root.get("capacity"), Integer.parseInt((String) m.getValue())));

			}

		}
	}

}
