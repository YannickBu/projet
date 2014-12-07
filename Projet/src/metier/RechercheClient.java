package metier;


import donnee.Client;
import fabrique.FabClient;


public class RechercheClient {
	
	
	public Client rechercheClient(Integer idClient, String nom , String prenom) {
		FabClient client = FabClient.getInstance();
		
		if(client.rechercher(idClient) != null) {
			Client c = client.rechercher(idClient);
			if (c.getNom().equals(nom) && c.getPrenom().equals(prenom)) {
				return c;
			}
		}
		
		return null;	
		
	}

}
