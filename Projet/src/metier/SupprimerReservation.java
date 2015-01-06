package metier;


import java.util.Date;

import fabrique.FabReservation;

public class SupprimerReservation {
	
	/**
	 * Methode qui permet de supprimer une reservation par date et salle
	 * @param date
	 * @param idSalle
	 */
	public void supprimerReservationParDateCreation (Date dateCreation, Integer idClient){
		FabReservation.getInstance().supprimerParDateCreationEtClient(dateCreation, idClient);
	}
}
