package metier;


import java.util.Date;

import fabrique.FabReservation;

public class SupprimerReservation {
	
	/**
	 * Methode qui permet de supprimer une reservation
	 * @param idres
	 */
	public void supprimerReservation (Integer idres) {
		FabReservation.getInstance().supprimer(idres);	
	}
	
	/**
	 * Methode qui permet de supprimer une reservation par date et salle
	 * @param date
	 * @param idSalle
	 */
	public void supprimerReservation (Date date, Integer idSalle){
		FabReservation.getInstance().supprimer(date, idSalle);
	}

}
