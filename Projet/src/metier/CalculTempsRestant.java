package metier;

import donnee.Client;
import donnee.Reservation;
import donnee.ForfaitClient;
import fabrique.FabClient;
import fabrique.FabForfaitClient;
import fabrique.FabReservation;

public class CalculTempsRestant {

	public void calculTempsRestant (Integer idforfaitClient , Integer idClient , Integer idReservation){
		FabClient client = FabClient.getInstance();
		FabReservation reservation = FabReservation.getInstance();
		FabForfaitClient forfaitClient = FabForfaitClient.getInstance();
		Client c = client.rechercher(idClient);
		Reservation res = reservation.rechercher(idReservation);
		ForfaitClient fc = forfaitClient.rechercher(idforfaitClient);
		
		if(c != null && res != null && fc != null) {
			int temps = fc.getTempsRestant();
			temps = temps - res.getPlage();
			fc.setTempsRestant(temps);	
		}
	}
}
