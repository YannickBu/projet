package metier;


import donnee.Client;
import fabrique.FabClient;


public class RechercheClient {
	
	
	public Client rechercheClient(String nom , String tel ) {
		return FabClient.getInstance().rechercher(nom,tel);
	}

}
