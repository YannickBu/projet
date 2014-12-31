package metier;

import donnee.Reservation;
import fabrique.FabReservation;

public class ModifierReservation {

	public ModifierReservation() {}

	/**
	 * Passe la reservation dont l'id est celui de l'objet res
	 * a l'etat payee
	 * @param res
	 */
	public void payer(Reservation res){
		FabReservation.getInstance().modifierReservation(res);
	}
}
