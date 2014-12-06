package metier;

import java.util.Date;
import java.util.List;

import donnee.Reservation;
import donnee.Salle;
import fabrique.FabReservation;

public class RechercheReservation {
	
	public Reservation rechercheReservation(Integer idres, Date date , Salle s) {
		FabReservation reservation = FabReservation.getInstance();
		
		if(reservation.rechercher(idres) != null) {
			Reservation res = reservation.rechercher(idres);
			if (res.getDate().equals(date) && res.getSalle().equals(s)) {
				return res;
			}
		}
		
		return null;	
		
	}
	
	public List<Reservation> rechercheReservationParDateEtTypeSalle(String date, String typeSalle){
		return FabReservation.getInstance().rechercherParDateEtTypeSalle(date, typeSalle);
	}

}
