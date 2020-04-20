/**
 * 
 */
package com.nivtek.ecommerce.app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.nivtek.ecommerce.entity.Brand;
import com.nivtek.ecommerce.entity.Category;
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
		
		Brand brandLg = session.get(Brand.class, 1);
		Brand brandSamsung = session.get(Brand.class, 2);
//		Brand brandNokia = session.get(Brand.class, 3);
		
		Category categoryAC = session.get(Category.class, 1);
		
		Product productInsert = new Product("Nokia ko ac rahexa", 402, 2, new Brand("nokia"), categoryAC, "35");
		
		session.beginTransaction();
		
		session.save(productInsert);
		
		session.getTransaction().commit();
		
		session.close();
		sessionFactory.close();

	}

}
