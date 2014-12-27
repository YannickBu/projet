package fabrique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import donnee.Client;
import donnee.Forfait;
import donnee.ForfaitClient;
import donnee.Salle;
import exception.ObjetExistantException;
import exception.ObjetInconnuException;

public class FabForfaitClient {

	private static FabForfaitClient instance;
	
	public static FabForfaitClient getInstance(){
		if(instance==null) {
			instance = new FabForfaitClient();
		}
		return instance;
	}
	
	/**
	 * Creation d'un forfait client
	 * @param idClient
	 * @param idSalle
	 * @param typeForfait
	 * @return ForfaitClient
	 * @throws ObjetExistantException
	 */
	public ForfaitClient creer( int idClient, int idSalle , String typeForfait) throws ObjetExistantException {
		ForfaitClient fc = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "INSERT INTO forfait_client (idclient,idsalle,typeforfait,tempsrestant) VALUES(?,?,?,?)";
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.clearParameters();
			
			pst.setInt(1, idClient);
			pst.setInt(2, idSalle);
			pst.setString(3,typeForfait);
			if (typeForfait.equals("12h")){
				pst.setInt(4, 5400); // 30 jours * 60h * 3mois
			} 
			else 
				pst.setInt(4,10800 ); // 30jours * 60h *6 mois
		
			
			pst.execute();
			
			fc = new ForfaitClient();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				fc.setIdForfaitClient(rs.getInt(1));
				fc.setClient(FabClient.getInstance().rechercher(rs.getInt(2)));
				fc.setSalle(FabSalle.getInstance().rechercher(rs.getInt(3)));
				fc.setForfait(FabForfait.getInstance().rechercherForfait(rs.getString(4)));
				fc.setTempsRestant(rs.getInt(5));
			}
			
		} catch (SQLException se) {
			System.out.println("Echec de la creation du ForfaitClient "+se.getMessage());
		}

		return fc;
	}
	
	/**
	 * Recherche d'un forfait client par son identifiant
	 * @param id
	 * @return ForfaitClient
	 * @throws ObjetInconnuException
	 */
	public ForfaitClient rechercher(int id) throws ObjetInconnuException{
		ForfaitClient fc = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idclient, idsalle, typeforfait, tempsrestant FROM forfait_client WHERE idforfaitclient = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
			
			if(!rs.next()) {
				throw new ObjetInconnuException(Client.class.toString(), "Aucun client a ete trouve pour l'identifiant "+id);
			}

			fc = new ForfaitClient();
			fc.setIdForfaitClient(id);
			fc.setClient(FabClient.getInstance().rechercher(rs.getInt("idclient")));
			fc.setSalle(FabSalle.getInstance().rechercher(rs.getInt("idsalle")));
			fc.setForfait(FabForfait.getInstance().rechercherForfait(rs.getString("typeforfait")));
			fc.setTempsRestant(rs.getInt("tempsrestant"));

		} catch (SQLException e) {
			System.out.println("Echec de la recuperation du forfaitClient pour l'id "+id
					+" - "+e.getMessage());
		}
		return fc;
	}

	/**
	 * Recherche le ForfaitClient via le type du forfait, lid du client et lid de la salle
	 * @param typeForfait
	 * @param idClient
	 * @param idSalle
	 * @return liste des forfait Client
	 */
	public List<ForfaitClient> rechercherForfaitClient(String typeForfait, int idClient, int idSalle){
		List<ForfaitClient> listeForfaitClient = new ArrayList<ForfaitClient>();
		Forfait forfait = null;
		Client client = null;
		Salle salle = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = FabConnexion.getConnexion()
					.prepareStatement("select idforfaitclient, tempsrestant "
							+ "from forfait_client where typeForfait = ? and idclient = ?");
		
			st.clearParameters();
			
			st.setString(1, typeForfait);
			st.setInt(2, idClient);
			
			rs = st.executeQuery();
			
			forfait = FabForfait.getInstance().rechercherForfait(typeForfait);
			client = FabClient.getInstance().rechercher(idClient);
			salle = FabSalle.getInstance().rechercher(idSalle);
			
			while(rs.next()){
				listeForfaitClient.add(
						new ForfaitClient(
								rs.getInt("idforfaitclient"), client, forfait, salle, rs.getInt("tempsrestant")
								)
						);
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de la recuperation de forfaitclient");
			System.out.println("Type du forfait : "+typeForfait);
			System.out.println("IdClient : " + idClient);
			System.out.println("IdSalle : " + idSalle);
			System.out.println(e.getMessage());
		}
		
		return listeForfaitClient;
	}
}
