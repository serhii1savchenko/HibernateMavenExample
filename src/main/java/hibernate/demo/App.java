package hibernate.demo;

import java.util.Date;
import java.util.logging.Level;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import hibernate.demo.entity.User;

public class App {

	static User userObj;
	static Session sessionObj;
	static SessionFactory sessionFactoryObj;

	private static SessionFactory buildSessionFactory() {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
		Configuration configObj = new Configuration();
		configObj.configure("hibernate.cfg.xml");
		ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build(); 
		sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
		return sessionFactoryObj;
	}

	public static void main(String[] args) {
		System.out.println(".......Hibernate Maven Example.......");
		try {
			
			sessionObj = buildSessionFactory().openSession();
			sessionObj.beginTransaction();

			for(int i=1; i<=5; i++) {
				userObj = new User();
				userObj.setUsername("User " + i);
				userObj.setCreatedBy("Administrator");
				userObj.setCreatedDate(new Date());
				
				sessionObj.save(userObj);
			}

			// Committing To The Database 
			sessionObj.getTransaction().commit();
			
			System.out.println(".......Records Saved Successfully.......");
			
			System.out.println(".......Getting User From The Database.......");
			User newUser = new User();
			int userId = 1;
			newUser = (User)sessionObj.get(User.class, new Integer(userId));
			if(newUser != null) {
				System.out.println(newUser.getUserid()+" "+newUser.getUsername());
			}

			// This should return null
			userId = 100500;
			newUser = (User)sessionObj.get(User.class, new Integer(userId));
			if(newUser == null) {
				System.out.println("No User with such id!");
			}
			
		} catch(Exception exception) {
			if(null != sessionObj.getTransaction()) {
				System.out.println(".......ERROR!... Transaction Is Being Rolled Back.......");
				sessionObj.getTransaction().rollback();
			}
			exception.printStackTrace();
		} finally {
			if(sessionObj != null) {
				sessionObj.close();
			}
		}
	}
}