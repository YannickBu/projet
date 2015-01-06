package fabrique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import donnee.Salle;
import donnee.TypeSalle;
import exception.ObjetInconnuException;

public class FabSalle {

	private static FabSalle singleton;

	public static FabSalle getInstance(){
		if(singleton==null) {
			singleton = new FabSalle();
		}
		return singleton;
	}


	/**
	 * Recherche une salle par son id
	 * @param id
	 * @return Salle
	 * @throws ObjetInconnuException
	 */
	public Salle rechercherParId(int id) throws ObjetInconnuException{
		Salle salle = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idtypesalle, prix1h, prix2h FROM salle WHERE idsalle = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
			
			if(!rs.next()) {
				throw new ObjetInconnuException(Salle.class.toString(), "Aucune Salle a etait trouve pour l'identifiant "+id);
			}

			salle = new Salle();
			salle.setIdSalle(id);
			salle.setTypeSalle(FabTypeSalle.getInstance().rechercher(rs.getInt("idtypesalle")));
			salle.setPrixPlage1h(rs.getInt("prix1h"));
			salle.setPrixPlage2h(rs.getInt("prix2h"));
			

		} catch (SQLException e) {
			System.out.println("Echec de la recuperation de la salle pour l'id "+id
					+" - "+e.getMessage());
		}
		return salle;
	}
	

	/**
	 * Recupere lensemble des salles selon le type de la salle
	 * @param typeSalle
	 * @return liste de salle
	 * @throws ObjetInconnuException
	 */
	public List<Salle> rechercher(String typeSalle) throws ObjetInconnuException{
		List<Salle> listeSalles = new ArrayList<Salle>();
		Salle salle = null;
		TypeSalle ts = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idsalle, prix1h, prix2h FROM salle WHERE idtypeSalle = ? order by idsalle";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			ts = FabTypeSalle.getInstance().rechercher(typeSalle);
			
			pst.setInt(1, ts.getIdTypeSalle());
			
			ResultSet rs = pst.executeQuery();
			
			
			while(rs.next()){
				salle = new Salle();
				salle.setTypeSalle(ts);
				salle.setIdSalle(rs.getInt("idsalle"));
				salle.setPrixPlage1h(rs.getInt("prix1h"));
				salle.setPrixPlage2h(rs.getInt("prix2h"));
				listeSalles.add(salle);
			}

		} catch (SQLException e) {
			System.out.println("echec de la recuperation de la salle pour le type "+ts.getIdTypeSalle()
					+" - "+e.getMessage());
		}
		return listeSalles;
	}
}
