package fabrique;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabConnexion {


	private static FabConnexion fabConnexion;
	private Connection c;
	
	private FabConnexion(){
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			c = DriverManager.getConnection("jdbc:hsqldb:file:projet;shutdown=true","sa","");
		} catch (SQLException e) {
			System.out.println("Erreur connexion " + e.getMessage());
		} catch (ClassNotFoundException e){
			
		}
	}
	
	public static Connection getConnexion(){
		if(fabConnexion == null){
			fabConnexion = new FabConnexion();
		}
		return fabConnexion.c;
	}
	
	public static void close() throws SQLException{
		if(fabConnexion != null){
			fabConnexion.c.close();
		}
	}
}
