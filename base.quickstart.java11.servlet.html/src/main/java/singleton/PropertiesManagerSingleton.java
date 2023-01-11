/**
 *
 */
package singleton;

import java.io.IOException;

/**
 * Singleton manager class that provides to retrieve properties
 * 
 * @author maurizio.franco@ymail.com
 *
 */
public class PropertiesManagerSingleton {

	private static java.util.Properties p;

    private static final String DEFAULT_PROPERTIES_FILE = "application_giacomo.properties";

	
	private static PropertiesManagerSingleton instance;

	private PropertiesManagerSingleton() throws IOException {
		p = new java.util.Properties();
        p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE));
		System.out.println("Istanziato costruttore privato....");
	}

	public static PropertiesManagerSingleton getInstance() throws IOException {
		if (instance == null) {
			instance = new PropertiesManagerSingleton();
			
		}
		return instance;
	}
	
	public String getProperty (String key) {
		System.out.println("Sto chiedendo il valore della chiave: " + key);
		String value = "" ;
		value = p.getProperty(key);
		System.out.println("Sto chiedendo il valore: " + value);
		return value ;
	}

}
