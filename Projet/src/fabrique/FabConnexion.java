package fabrique;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabConnexion {


	private static FabConnexion FabConnexion;
	private Connection c;
	
	private FabConnexion(){
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			c = DriverManager.getConnection("jdbc:hsqldb:file:projet;shutdown=true","sa","");
		} catch (SQLException e) {
			System.out.println(" ca n'a pas fonctionnee");
		} catch (ClassNotFoundException e){
			
		}
	}
	
	public static Connection getConnexion(){
		if(FabConnexion == null){
			FabConnexion = new FabConnexion();
		}
		return FabConnexion.c;
	}
	
	public static void closeConnexion() throws SQLException{
		if(FabConnexion != null){
			FabConnexion.c.close();
			FabConnexion = null;
		}
	}
}
