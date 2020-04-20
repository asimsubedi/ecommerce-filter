/**
 * This class in intended for the insertion of the Product into the database
 */
package com.nivtek.ecommerce.app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.nivtek.ecommerce.entity.Product;
import com.nivtek.ecommerce.util.HibernateUtil;

/**
 * @author AsimSubedi
 *
 */
public class AppProductInsert {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		Product insertProduct = new Product("Samsung greenway ac", 475, 4, 52, "samsung", "ac");
		
		saveProduct(session, insertProduct);

		session.close();
		sessionFactory.close();

	}

	private static void saveProduct(Session session, Product insertProduct) {
		
		session.beginTransaction();
		session.save(insertProduct);
		session.getTransaction().commit();
		
	}

}
