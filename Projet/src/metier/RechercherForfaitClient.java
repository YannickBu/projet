package metier;

import java.util.List;

import donnee.ForfaitClient;
import fabrique.FabForfaitClient;

public class RechercherForfaitClient {

	public RechercherForfaitClient() {}

	public List<ForfaitClient> lister(){
		return FabForfaitClient.getInstance().lister();
	}
}
