package metier;

import java.util.Date;

import donnee.Client;
import donnee.Reservation;
import donnee.Salle;
import fabrique.FabClient;
import fabrique.FabReservation;

public class SupprimerReservation {
	
	public void supprimerResrevation (Integer idres,Client c,Salle s,Date d,int plage, Integer idClient) {
		FabClient client = FabClient.getInstance();
		FabReservation reservation = FabReservation.getInstance();
		if (client.rechercher(idClient) != null && idres != null){
			Reservation res = reservation.rechercher(idres);
			if ( res != null) {
				Client cl = client.rechercher(idClient);
				cl.getListReservation().remove(res);
				reservation.rechercher(idres).setClient(null);	
			}
		}
			
	}

}
