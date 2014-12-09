package metier;


import donnee.Client;
import donnee.Reservation;
import fabrique.FabClient;
import fabrique.FabReservation;

public class SupprimerReservation {
	
	/**
	 * Methode qui permet de supprimer une reservation
	 * @param idres
	 * @param idClient
	 */
	public void supprimerResrevation (Integer idres, Integer idClient) {
		FabClient fabClient = FabClient.getInstance();
		FabReservation reservation = FabReservation.getInstance();
		Reservation res = reservation.rechercher(idres);
		Client client = fabClient.rechercher(idClient);
		
		if (client != null && res != null){
			reservation.supprimer(idres);	
		}
	}

}
