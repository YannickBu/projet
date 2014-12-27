package metier;

import donnee.Client;
import fabrique.FabClient;
import fabrique.FabForfaitClient;

public class VendreForfait {

	/**
	 * Vendre un forfait un client 
	 * @param idClient
	 * @param idSalle
	 * @param typeForfait
	 */
	public void vendreForfait(Integer idClient, Integer idSalle, String typeForfait) {
		FabClient fabClient = FabClient.getInstance();
		FabForfaitClient fabForfaitClient = FabForfaitClient.getInstance();
		Client client = fabClient.rechercher(idClient);
		
		if (client != null){
			fabForfaitClient.creer(idClient, idSalle, typeForfait);
		}
		
	}
}
// si le client n'existe pas on le creer on fera appel a la classe CreerClient