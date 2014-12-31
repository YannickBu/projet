package metier;


import java.util.List;

import donnee.Client;
import fabrique.FabClient;


public class RechercheClient {
	
	/**
	 * Methode qui permet de rechercher un Client
	 * @param nom
	 * @param tel
	 * @return client si le client trouve, null sinon
	 */
	public Client rechercheClient(String nom , String prenom, String tel ) {
		return FabClient.getInstance().rechercher(nom,prenom,tel);
	}
	
	/**
	 * Methode qui permet de lister les clients 
	 * @return Liste Client
	 */
	public List<Client> listerClients(){
		return FabClient.getInstance().lister();
	}

}
