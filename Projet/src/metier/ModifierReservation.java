package metier;

import donnee.Reservation;
import fabrique.FabReservation;

public class ModifierReservation {

	public ModifierReservation() {}

	/**
	 * Passe la reservation dont lid est celui de lobjet res
	 * a letat payee
	 * @param res
	 */
	public void payer(Reservation res){
		FabReservation.getInstance().modifierReservation(res);
	}
}
