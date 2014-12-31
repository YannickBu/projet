package fabrique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import donnee.Salle;
import donnee.TypeSalle;
import exception.ObjetInconnuException;

public class FabTypeSalle {

	private static FabTypeSalle singleton;

	public static FabTypeSalle getInstance(){
		if(singleton==null) {
			singleton = new FabTypeSalle();
		}
		return singleton;
	}

	/**
	 * Methode qui permet de rechercher le type d'une salle par son identifiant
	 * @param idTypeSalle
	 * @return TypeSalle
	 */
	public TypeSalle rechercher(int idTypeSalle){
		TypeSalle typeSalle = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idtypesalle, typesalle FROM typesalle WHERE idtypesalle = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setInt(1, idTypeSalle);
			
			ResultSet rs = pst.executeQuery();
			
			if(!rs.next()) {
				throw new ObjetInconnuException(Salle.class.toString(), "Aucune TypeSalle a ete trouve pour l'identifiant "+idTypeSalle);
			}

			typeSalle = new TypeSalle(rs.getInt(1), rs.getString(2));
			

		} catch (SQLException e) {
			System.out.println("Echec de la recuperation du typesalle pour l'id "+idTypeSalle
					+" - "+e.getMessage());
		}		
		return typeSalle;
	}
	
	/**
	 * Methode qui permet de rechercher une salle par son type
	 * @param libTypeSalle
	 * @return typeSalle
	 */
	public TypeSalle rechercher(String libTypeSalle){
		TypeSalle typeSalle = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idtypesalle FROM typesalle WHERE typesalle = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setString(1, libTypeSalle);
			
			ResultSet rs = pst.executeQuery();
			
			if(!rs.next()) {
				throw new ObjetInconnuException(Salle.class.toString(), "Aucune TypeSalle a ete trouve pour le libelle "+libTypeSalle);
			}

			typeSalle = new TypeSalle(rs.getInt(1), libTypeSalle);
			

		} catch (SQLException e) {
			System.out.println("Echec de la recuperation du typesalle pour le libelle "+libTypeSalle
					+" - "+e.getMessage());
		}		
		return typeSalle;
	}
}
