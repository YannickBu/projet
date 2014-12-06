package metier;

import java.util.Date;
import java.util.List;

import donnee.Reservation;
import donnee.Salle;
import fabrique.FabReservation;

public class PlanningReservation {

	public List<Reservation> planningReservation (Salle s, Date date) {
		FabReservation reservation = FabReservation.getInstance();
		
		return null;
	}
}
