package metier;

import donnee.Client;
import fabrique.FabClient;
import fabrique.FabForfaitClient;

public class VendreForfait {

	/**
	 * Vendre un forfait a un client pour un type de salle
	 * @param idClient
	 * @param idTypeSalle
	 * @param typeForfait
	 */
	public void vendreForfait(Integer idClient, Integer idTypeSalle, String typeForfait) {
		FabClient fabClient = FabClient.getInstance();
		FabForfaitClient fabForfaitClient = FabForfaitClient.getInstance();
		Client client = fabClient.rechercher(idClient);
		
		if (client != null){
			fabForfaitClient.creer(idClient, idTypeSalle, typeForfait);
		}
		
	}
}
// si le client n'existe pas on le creer on fera appel a la classe CreerClient