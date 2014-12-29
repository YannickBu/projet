package metier;

import java.util.List;

import donnee.ForfaitClient;
import fabrique.FabForfaitClient;

public class RechercherForfaitClient {

	public RechercherForfaitClient() {}

	//TODO supprimer?
	public List<ForfaitClient> lister(){
		return FabForfaitClient.getInstance().lister();
	}
	
	public List<ForfaitClient> listerPourUnClient(int idClt){
		return FabForfaitClient.getInstance().listerPourUnClient(idClt);
	}
}
