package metier;

import java.util.List;

import donnee.ForfaitClient;
import fabrique.FabForfaitClient;

public class RechercherForfaitClient {

	public RechercherForfaitClient() {}
	
	/**
	 * Methode qui permet de lister les forfaitClient pour un client
	 * @param idClt
	 * @return liste ForfaitClient
	 */
	public List<ForfaitClient> listerPourUnClient(int idClt){
		return FabForfaitClient.getInstance().listerPourUnClient(idClt);
	}
}
