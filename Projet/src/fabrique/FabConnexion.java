package fabrique;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabConnexion {


	private static FabConnexion FabConnexion;
	private Connection c;
	
	/**
	 * Constructeur de FabConnexion
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
	 * Methode qui permet de creer la connexion
	 * @return connexion
	 */
	public static Connection getConnexion(){
		if(FabConnexion == null){
			FabConnexion = new FabConnexion();
		}
		return FabConnexion.c;
	}
	
	/**
	 * Methode qui permet d'arreter une connexion
	 * @throws SQLException
	 */
	public static void closeConnexion() throws SQLException{
		if(FabConnexion != null){
			FabConnexion.c.close();
			FabConnexion = null;
		}
	}
}
