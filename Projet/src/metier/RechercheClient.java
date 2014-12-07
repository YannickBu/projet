package metier;


import donnee.Client;
import fabrique.FabClient;


public class RechercheClient {
	
	
	public Client rechercheClient(Integer idClient, String nom , String prenom ,String tel ) {
		FabClient client = FabClient.getInstance();
		
		if(client.rechercher(idClient) != null) {
			Client c = client.rechercher(idClient);
			if (c.getNom().equals(nom) && c.getPrenom().equals(prenom) && c.getNumTel().equals(tel)) {
				return c;
			}
		}
		
		return null;	
		
	}

}
