package com.nivtek.ecommerce.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.nivtek.ecommerce.entity.Brand;
import com.nivtek.ecommerce.entity.Category;
import com.nivtek.ecommerce.entity.Product;


/**
 * @author AsimSubedi
 *
 */
public class HibernateUtil {

	private final static SessionFactory SESSION_FACTORY;

	// This is a static block and it executes only once
	// this is singleton. We create single sesion factory object for hibernate
	// application
	static {

		// configuration object is used to bootstrap hibernate
		Configuration configuration = new Configuration();

		// loads hibernate.cfg.xml file from classath
		configuration.configure();

		configuration.addAnnotatedClass(Product.class);
		configuration.addAnnotatedClass(Category.class);
		configuration.addAnnotatedClass(Brand.class);

		// register the service. we are starting essential hibernate SErvice
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();

		//
		SESSION_FACTORY = configuration.buildSessionFactory(serviceRegistry);

	}

	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}

}
