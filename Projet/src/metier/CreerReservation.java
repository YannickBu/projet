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
			
			//TODO A supprimer
			/*if (res != null) {
				client.getListReservation().add(res);
				fabReservation.rechercher(res.getIdReserv()).setClient(client);
			}*/
			
		}
		
		return res;	
		
	}
}
