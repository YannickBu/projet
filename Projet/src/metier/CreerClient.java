package metier;

import donnee.Client;
import fabrique.FabClient;

public class CreerClient {
	
	public Client creerClient (String nom, String prenom, String num) {
		return FabClient.getInstance().creer(nom, prenom, num);
	}


}
