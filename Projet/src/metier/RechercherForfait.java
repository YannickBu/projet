package metier;

import java.util.List;

import donnee.Forfait;
import fabrique.FabForfait;

public class RechercherForfait {

	public RechercherForfait() {}

	/**
	 * Recupere les forfaits existant
	 * @return
	 */
	public List<Forfait> lister(){
		return FabForfait.getInstance().lister();
	}
}
