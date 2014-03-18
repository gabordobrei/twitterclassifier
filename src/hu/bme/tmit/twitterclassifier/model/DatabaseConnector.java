package hu.bme.tmit.twitterclassifier.model;

import hu.bme.tmit.twitterclassifier.logger.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseConnector {

	private static Logger log = new Logger(DatabaseConnector.class);
	private static DataSource dataSource;

	static {
		try {
			dataSource = (DataSource) new InitialContext()
					.lookup("jndifordbconc");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			log.e(e.getMessage());
		}

	}

}
