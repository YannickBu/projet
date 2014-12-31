package fabrique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import donnee.Salle;
import donnee.TypeSalle;
import exception.ObjetExistantException;
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
	 * Cree une nouvelle salle
	 * @param id
	 * @param typeSalle
	 * @param prix1h
	 * @param prix2h
	 * @return Salle
	 * @throws ObjetExistantException
	 */
	public Salle creer(int idTypeSalle, int prix1h, int prix2h) throws ObjetExistantException {
		Salle salle = null;
		Connection connection = FabConnexion.getConnexion();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "INSERT INTO salle (idtypesalle,prix1h,prix2h) VALUES(?,?,?)";
		try{

			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.clearParameters();
		
			pst.setInt(1,idTypeSalle);
			pst.setInt(2,prix1h);
			pst.setInt(3, prix2h);
			
			pst.execute();
			
			salle = new Salle();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				salle.setIdSalle(rs.getInt(1));
				salle.setTypeSalle(FabTypeSalle.getInstance().rechercher(rs.getInt("idtypesalle")));
				salle.setPrixPlage1h(prix1h);
				salle.setPrixPlage2h(prix2h);
			}
		} catch (SQLException se) {
			System.out.println("Echec de la creation de la salle - "+se.getMessage());
		}
	
		return salle;
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
	 * recherche d'une salle par son type
	 * @param typeSalle
	 * @return une salle 
	 * @throws ObjetInconnuException
	 */
	public Salle rechercherParType(String typeSalle) throws ObjetInconnuException{
		Salle salle = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idtypesalle, prix1h, prix2h FROM salle WHERE typesalle = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setString(1, typeSalle);
			
			ResultSet rs = pst.executeQuery();
			
			if(!rs.next()) {
				throw new ObjetInconnuException(Salle.class.toString(), "Aucune Salle a etait trouve pour le type "+typeSalle);
			}

			salle = new Salle();
			salle.setIdSalle(rs.getInt("idsalle"));
			salle.setTypeSalle(FabTypeSalle.getInstance().rechercher(rs.getString("typeSalle")));
			salle.setPrixPlage1h(rs.getInt("prix1h"));
			salle.setPrixPlage2h(rs.getInt("prix2h"));
			

		} catch (SQLException e) {
			System.out.println("Echec de la recuperation de la salle pour le type "+typeSalle
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
	
	/**
	 * Supprime une salle par son id
	 * @param id
	 */
	public void supprimer(int id){
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "DELETE FROM salle WHERE idsalle = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setInt(1, id);
			
			pst.execute();
			
		} catch (SQLException e) {
			System.out.println("Echec de la suppression de la salle pour l'id "+id
					+" - "+e.getMessage());
		}
	}

	/**
	 * Recupere lensemble des salles
	 * @return liste de salle
	 */
	public List<Salle> lister() {
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idsalle,idtypesalle,prix1h,prix2h FROM salle";
		Statement st = null;
		ResultSet rs = null;
		List<Salle> listSalle = new ArrayList<Salle>();
		try{
			st = connection.createStatement();
			rs = st.executeQuery(query);

			while(rs.next()){
				Salle salle = new Salle();
				salle.setIdSalle(rs.getInt("idsalle"));
				salle.setTypeSalle(FabTypeSalle.getInstance().rechercher(rs.getInt("idtypesalle")));
				salle.setPrixPlage1h(rs.getInt("prix1h"));
				salle.setPrixPlage2h(rs.getInt("prix2h"));
				listSalle.add(salle);
			}
		}catch(SQLException e){
			System.out.println("Erreur d'acces aux listes des salles en base de donnees - "
					+e.getMessage());
		}
		return listSalle;
	}
}
