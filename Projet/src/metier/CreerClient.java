package metier;

import donnee.Client;
import exception.ObjetInconnuException;
import fabrique.FabClient;

public class CreerClient {
	
	public Client creerClient (String nom, String prenom, String num) {
		return FabClient.getInstance().creer(nom, prenom, num);
	}

	public Client creerClientSiInexistant (String nom, String num) {
		RechercheClient rechercheMetier = new RechercheClient();
		Client c;
		try{
			c = rechercheMetier.rechercheClient(nom, num);
		} catch (ObjetInconnuException oie){
			c = FabClient.getInstance().creer(nom, "", num);
		}
		return c;
	}
}
