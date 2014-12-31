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
			if (typeForfait.equals("12h")){
				tpsRestant = 12;
			} 
			else {
				tpsRestant = 24;
			}
			pst.setTimestamp(5, new Timestamp(dateCourante.getTime()));
			pst.setInt(4, tpsRestant); 
			
			pst.execute();
			
			fc = new ForfaitClient();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				fc.setIdForfaitClient(rs.getInt(1));
				fc.setClient(FabClient.getInstance().rechercher(idClient));
				fc.setTypeSalle(FabTypeSalle.getInstance().rechercher(idTypeSalle));
				fc.setForfait(FabForfait.getInstance().rechercherForfait(typeForfait));
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
	 * Recherche le ForfaitClient via lid du client et lid du typesalle
	 * @param idClient
	 * @param idTypeSalle
	 * @return liste des forfait Client
	 */
	public List<ForfaitClient> rechercherForfaitClient(int idClient, int idTypeSalle){
		List<ForfaitClient> listeForfaitClient = new ArrayList<ForfaitClient>();
		Forfait forfait = null;
		Client client = null;
		TypeSalle typeSalle = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = FabConnexion.getConnexion()
					.prepareStatement("select idforfaitclient, typeforfait, tempsrestant, datecreation "
							+ "from forfait_client where idclient = ? and idtypesalle = ?");
		
			st.clearParameters();
			
			st.setInt(1, idClient);
			st.setInt(2, idTypeSalle);
			
			rs = st.executeQuery();
			
			client = FabClient.getInstance().rechercher(idClient);
			typeSalle = FabTypeSalle.getInstance().rechercher(idTypeSalle);
			
			while(rs.next()){
				forfait = FabForfait.getInstance().rechercherForfait(rs.getString("typeforfait"));
				listeForfaitClient.add(
						new ForfaitClient(
								rs.getInt("idforfaitclient"), client, forfait, typeSalle, rs.getInt("tempsrestant"), rs.getTimestamp("datecreation")
								)
						);
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de la recuperation de forfaitclient");
			System.out.println("IdClient : " + idClient);
			System.out.println("IdTypeSalle : " + idTypeSalle);
			System.out.println(e.getMessage());
		}
		
		return listeForfaitClient;
	}
	

	/**
	 * Modifie le forfaitClient ayant comme id celui du forfaitClient fc
	 * et la modifie avec les donness contenus dans fc
	 * @param clt
	 */
	public void modifierForfaitClient(ForfaitClient fc){
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		
		String query = "update forfait_client set idclient = ?, idtypesalle = ?, typeforfait = ?, tempsrestant = ?, datecreation = ? "
				+ " where idforfaitclient = ?";
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.clearParameters();

			pst.setInt(1, fc.getClient().getId());
			pst.setInt(2, fc.getTypeSalle().getIdTypeSalle());
			pst.setString(3, fc.getForfait().getTypeForfait());
			pst.setInt(4, fc.getTempsRestant());
			pst.setTimestamp(5, new Timestamp(fc.getDateCreation().getTime()));
			pst.setInt(6, fc.getIdForfaitClient());
			pst.execute();
		} catch (SQLException se) {
			System.out.println("Echec de la modification du forfaitClient "+fc.getIdForfaitClient()+" - "+se.getMessage());
		}
	}
	
	/**
	 * Liste tous les forfaitClients
	 * @return
	 */
	public List<ForfaitClient> lister(){
		List<ForfaitClient> listeFC = new ArrayList<ForfaitClient>();
		ForfaitClient fc = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = FabConnexion.getConnexion().prepareStatement("SELECT idforfaitclient, idclient, idtypesalle, typeforfait, tempsrestant, datecreation FROM forfait_client");
			rs=st.executeQuery();
			
			while(rs.next()){
				fc = new ForfaitClient();
				fc.setIdForfaitClient(rs.getInt("idforfaitclient"));
				fc.setClient(FabClient.getInstance().rechercher(rs.getInt("idclient")));
				fc.setForfait(FabForfait.getInstance().rechercherForfait(rs.getString("typeforfait")));
				fc.setTypeSalle(FabTypeSalle.getInstance().rechercher(rs.getInt("idtypesalle")));
				fc.setDateCreation(new Date(rs.getTimestamp("datecreation").getTime()));
				fc.setTempsRestant(rs.getInt("tempsrestant"));
				listeFC.add(fc);
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de la recuperation des forfaitclient" + e.getMessage());
		}
		
		return listeFC;
	}
	
	/**
	 * Liste les forfaitClient pour le client en parametre
	 * @param idClt
	 * @return
	 */
	public List<ForfaitClient> listerPourUnClient(int idClt){
		List<ForfaitClient> listeFC = new ArrayList<ForfaitClient>();
		ForfaitClient fc = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = FabConnexion.getConnexion().prepareStatement("SELECT idforfaitclient, idtypesalle, typeforfait, tempsrestant, datecreation FROM forfait_client where idclient = ?");
			st.setInt(1, idClt);
			rs=st.executeQuery();
			
			while(rs.next()){
				fc = new ForfaitClient();
				fc.setIdForfaitClient(rs.getInt("idforfaitclient"));
				fc.setClient(FabClient.getInstance().rechercher(idClt));
				fc.setForfait(FabForfait.getInstance().rechercherForfait(rs.getString("typeforfait")));
				fc.setTypeSalle(FabTypeSalle.getInstance().rechercher(rs.getInt("idtypesalle")));
				fc.setDateCreation(new Date(rs.getTimestamp("datecreation").getTime()));
				fc.setTempsRestant(rs.getInt("tempsrestant"));
				listeFC.add(fc);
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de la recuperation des forfaitclient" + e.getMessage());
		}
		
		return listeFC;
	}
	
	/**
	 * Supprime un forfaitclient par son id
	 * @param id
	 */
	public void supprimer(int id){
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "DELETE FROM forfait_client WHERE idforfaitclient = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setInt(1, id);
			
			pst.execute();
			
		} catch (SQLException e) {
			System.out.println("Echec de la suppression du forfaitclient pour l'id "+id
					+" - "+e.getMessage());
		}
	}
}
