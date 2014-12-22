package metier;


import java.util.Date;

import fabrique.FabReservation;

public class SupprimerReservation {
	
	/**
	 * Methode qui permet de supprimer une reservation
	 * @param idres
	 * @param idClient
	 */
	public void supprimerReservation (Integer idres) {
		FabReservation.getInstance().supprimer(idres);	
	}
	
	
	public void supprimerReservation (Date date, Integer idSalle){
		FabReservation.getInstance().supprimer(date, idSalle);
	}

}
