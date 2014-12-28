package fabrique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import donnee.Client;
import donnee.Forfait;
import donnee.ForfaitClient;
import donnee.Salle;
import donnee.TypeSalle;
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
	 * @param idTypeSalle
	 * @param typeForfait
	 * @return ForfaitClient
	 * @throws ObjetExistantException
	 */
	public ForfaitClient creer( int idClient, int idTypeSalle , String typeForfait) throws ObjetExistantException {
		ForfaitClient fc = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		int tpsRestant;
		Date dateCourante = new Date();
		String query = "INSERT INTO forfait_client (idclient,idtypesalle,typeforfait,tempsrestant,datecreation) VALUES(?,?,?,?,?)";
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.clearParameters();
			
			pst.setInt(1, idClient);
			pst.setInt(2, idTypeSalle);
			pst.setString(3,typeForfait);
			pst.setTimestamp(4, new Timestamp(dateCourante.getTime()));
			if (typeForfait.equals("12h")){
				tpsRestant = 2160;// 30 jours * 24h * 3mois
			} 
			else {
				tpsRestant = 4320; // 30jours * 24h *6 mois
			}
			pst.setInt(4, tpsRestant); 
			
			pst.execute();
			
			fc = new ForfaitClient();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				fc.setIdForfaitClient(rs.getInt(1));
				fc.setClient(FabClient.getInstance().rechercher(rs.getInt(2)));
				fc.setTypeSalle(FabTypeSalle.getInstance().rechercher(rs.getInt(3)));
				fc.setForfait(FabForfait.getInstance().rechercherForfait(rs.getString(4)));
				fc.setTempsRestant(tpsRestant);
				fc.setDateCreation(dateCourante);
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
		String query = "SELECT idclient, idtypesalle, typeforfait, tempsrestant, datecreation FROM forfait_client WHERE idforfaitclient = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
			
			if(!rs.next()) {
				throw new ObjetInconnuException(Client.class.toString(), "Aucun forfaitClient a ete trouve pour l'identifiant "+id);
			}

			fc = new ForfaitClient();
			fc.setIdForfaitClient(id);
			fc.setClient(FabClient.getInstance().rechercher(rs.getInt("idclient")));
			fc.setTypeSalle(FabTypeSalle.getInstance().rechercher(rs.getInt("idtypesalle")));
			fc.setForfait(FabForfait.getInstance().rechercherForfait(rs.getString("typeforfait")));
			fc.setTempsRestant(rs.getInt("tempsrestant"));
			fc.setDateCreation(rs.getTimestamp("datecreation"));

		} catch (SQLException e) {
			System.out.println("Echec de la recuperation du forfaitClient pour l'id "+id
					+" - "+e.getMessage());
		}
		return fc;
	}

	/**
	 * Recherche le ForfaitClient via le type du forfait, lid du client et lid du typesalle
	 * @param typeForfait
	 * @param idClient
	 * @param idTypeSalle
	 * @return liste des forfait Client
	 */
	public List<ForfaitClient> rechercherForfaitClient(String typeForfait, int idClient, int idTypeSalle){
		List<ForfaitClient> listeForfaitClient = new ArrayList<ForfaitClient>();
		Forfait forfait = null;
		Client client = null;
		TypeSalle typeSalle = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = FabConnexion.getConnexion()
					.prepareStatement("select idforfaitclient, tempsrestant, datecreation "
							+ "from forfait_client where typeForfait = ? and idclient = ? and idsalle = ?");
		
			st.clearParameters();
			
			st.setString(1, typeForfait);
			st.setInt(2, idClient);
			st.setInt(3, idTypeSalle);
			
			rs = st.executeQuery();
			
			forfait = FabForfait.getInstance().rechercherForfait(typeForfait);
			client = FabClient.getInstance().rechercher(idClient);
			typeSalle = FabTypeSalle.getInstance().rechercher(idTypeSalle);
			
			while(rs.next()){
				listeForfaitClient.add(
						new ForfaitClient(
								rs.getInt("idforfaitclient"), client, forfait, typeSalle, rs.getInt("tempsrestant"), rs.getTimestamp("datecreation")
								)
						);
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de la recuperation de forfaitclient");
			System.out.println("Type du forfait : "+typeForfait);
			System.out.println("IdClient : " + idClient);
			System.out.println("IdTypeSalle : " + idTypeSalle);
			System.out.println(e.getMessage());
		}
		
		return listeForfaitClient;
	}
}
