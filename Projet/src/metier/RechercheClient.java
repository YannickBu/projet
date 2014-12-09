package metier;


import donnee.Client;
import fabrique.FabClient;


public class RechercheClient {
	
	/**
	 * Methode qui permet de rechercher un Client
	 * @param nom
	 * @param tel
	 * @return client si le client trouve, null sinon
	 */
	public Client rechercheClient(String nom , String tel ) {
		return FabClient.getInstance().rechercher(nom,tel);
	}

}
