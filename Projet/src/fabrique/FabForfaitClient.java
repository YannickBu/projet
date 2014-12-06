package fabrique;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import donnee.Client;
import donnee.Forfait;
import donnee.ForfaitClient;
import donnee.Salle;

public class FabForfaitClient {

	private static FabForfaitClient instance;
	
	public static FabForfaitClient getInstance(){
		if(instance==null) {
			instance = new FabForfaitClient();
		}
		return instance;
	}
	
	//TODO A SUPPRIMER si inutile
	public List<Client> rechercherClientsPourUnForfait(String typeForfait){
		List<Client> listeClient = new ArrayList<Client>();
		
		
		
		return listeClient;
	}
	
	//TODO A SUPPRIMER si inutile
	public List<Forfait> rechercherForfaitsPourUnClient(int idClient){
		List<Forfait> listeForfait = new ArrayList<Forfait>();
		
		
		return listeForfait;
	}

	/**
	 * Recherche le ForfaitClient via le type du forfait, l'id du client et l'id de la salle
	 * @param typeForfait
	 * @param idClient
	 * @param idSalle
	 * @return
	 */
	public List<ForfaitClient> rechercherForfaitClient(String typeForfait, int idClient, int idSalle){
		List<ForfaitClient> listeForfaitClient = new ArrayList<ForfaitClient>();
		Forfait forfait = null;
		Client client = null;
		Salle salle = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = FabConnexion.getConnexion().prepareStatement("select idforfaitclient, tempsrestant from forfait_client where typeForfait = ? and idclient = ?");
		
			st.clearParameters();
			
			st.setString(1, typeForfait);
			st.setInt(2, idClient);
			
			rs = st.executeQuery();
			
			forfait = FabForfait.getInstance().rechercherForfait(typeForfait);
			client = FabClient.getInstance().rechercher(idClient);
			salle = FabSalle.getInstance().rechercher(idSalle);
			
			while(rs.next()){
				listeForfaitClient.add(
						new ForfaitClient(rs.getInt("idforfaitclient"), client, forfait, salle, rs.getInt("tempsrestant"))
						);
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de la recuperation de forfaitclient");
		}
		
		return listeForfaitClient;
	}
}
