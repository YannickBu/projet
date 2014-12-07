package metier;

import java.util.Date;

import donnee.Client;
import donnee.Reservation;
import fabrique.FabClient;
import fabrique.FabReservation;

public class CreerReservation {

	public Reservation creerReservation (Integer idClient, Integer idSalle ,Date dateDebut,int plage,Date dateCreation, Boolean estPayee) {
		FabClient fabClient = FabClient.getInstance();
		FabReservation fabReservation = FabReservation.getInstance();
		Reservation res = null;
		Client client = fabClient.rechercher(idClient);
		
		if (client != null){
			res = fabReservation.creer(idClient,idSalle,dateDebut,plage,dateCreation,estPayee);
			
		}
		
		return res;	
		
	}
}
