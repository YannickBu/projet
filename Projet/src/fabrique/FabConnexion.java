package fabrique;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabConnexion {


	private static FabConnexion FabConnexion;
	private Connection c;
	
	/**
	 * Constructeur de la fabrique construction
	 */
	private FabConnexion(){
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			c = DriverManager.getConnection("jdbc:hsqldb:file:projet;shutdown=true","sa","");
		} catch (SQLException e) {
			System.out.println(" ca n'a pas fonctionnee");
		} catch (ClassNotFoundException e){
			
		}
	}
	
	/**
	 * Methode qui nous permet d'avoir une connexion
	 * @return connexion
	 */
	public static Connection getConnexion(){
		if(FabConnexion == null){
			FabConnexion = new FabConnexion();
		}
		return FabConnexion.c;
	}
	
	/**
	 * Methode qui nous permet d'arreter la connexion
	 * @throws SQLException
	 */
	public static void closeConnexion() throws SQLException{
		if(FabConnexion != null){
			FabConnexion.c.close();
			FabConnexion = null;
		}
	}
}
