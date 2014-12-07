package metier;

import donnee.Client;
import fabrique.FabClient;

public class ConversionPointFidelite {

	public void conversionPointFidelite (Integer idClient){
		FabClient client = FabClient.getInstance();
		
		if(client.rechercher(idClient) != null) {
			Client c = client.rechercher(idClient);
			if (c.getPointsFidelite() >= 150) {
				int pointFidelite = c.getPointsFidelite() - 150;
				c.setPointsFidelite(pointFidelite);
				
			// reste a ajouter 2h de plage dans une reservation sauf que le client peut choisir dans quel type de salle 
			}
		}
		
	}
}
