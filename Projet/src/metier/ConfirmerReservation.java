package metier;

import donnee.Client;
import donnee.ForfaitClient;
import donnee.Reservation;
import fabrique.FabClient;
import fabrique.FabForfaitClient;
import fabrique.FabReservation;


public class ConfirmerReservation {
	
	// Cette methode a utiliser aussi si le client veut cumuler ses points de fidelitŽ ou ses heures de forfait 
		/**
		 * Methode qui sert a confimer un payement (sans forfait ou points de fidelite) d'une reservation
		 * en ajouter les points de fidelite
		 * @param idClient
		 * @param idReservation
		 */
		public void payerReservation(Integer idClient , Integer idReservation) {
			FabReservation reservation = FabReservation.getInstance();
			FabClient client = FabClient.getInstance();
			Client c = client.rechercher(idClient);
			Reservation res = reservation.rechercher(idReservation);
		
			if(c != null && res != null ) {
				res.setEstPaye(true);
				c.ajoutPointFidelite(res.getPlage());
			}
		}
		
		// a revoir 
		// Cette methode a utiliser si le client veut utiliser ses points de fidelite et/ou son forfait
		/**
		 * payer une reservation avec les points de fidelite et/ou forfait
		 * @param idClient
		 * @param idReservation
		 * @param idForfaitClient
		 */
		public void payerReservationPointFideliteForfait(Integer idClient, Integer idReservation, Integer idForfaitClient) {
			FabReservation reservation = FabReservation.getInstance();
			FabClient client = FabClient.getInstance();
			FabForfaitClient forfaitClient = FabForfaitClient.getInstance();
			Client c = client.rechercher(idClient);
			Reservation res = reservation.rechercher(idReservation);
			ForfaitClient fc = forfaitClient.rechercher(idForfaitClient);
			
			if (c != null && res != null) {
				if (res.getPlage() == 2) {
					if (c.getPointsFidelite() >= 150) {
						int pointFidelite = c.getPointsFidelite() - 150;
						c.setPointsFidelite(pointFidelite);
						res.setEstPaye(true);
						c.ajoutPointFidelite(res.getPlage());
					} else {
						if (fc.getTempsRestant() >= 2){
							int temps = fc.getTempsRestant();
							temps = temps - 2;
							fc.setTempsRestant(temps);
							res.setEstPaye(true);
							c.ajoutPointFidelite(res.getPlage());
						}
					}
				}
				if (res.getPlage() > 2) {
					if (c.getPointsFidelite() >= 150) {
						int pointFidelite = c.getPointsFidelite() - 150;
						c.setPointsFidelite(pointFidelite);
						int plage = res.getPlage() - 2;
						if (fc.getTempsRestant() >= 2){
							int temps = fc.getTempsRestant();
							temps = temps - 2;
							fc.setTempsRestant(temps);
							plage = res.getPlage() -2;
							if (plage == 0 ){
								res.setEstPaye(true);
								c.ajoutPointFidelite(res.getPlage());
							}
						}
						
					}
				}
				if (res.getPlage() == 1) {
					if (fc.getTempsRestant() >= 2){
						int temps = fc.getTempsRestant();
						temps = temps - 2;
						fc.setTempsRestant(temps);
						res.setEstPaye(true);
						c.ajoutPointFidelite(res.getPlage());
					}
					else {
						res.setEstPaye(true);// le client paye par cash la reservation
						c.ajoutPointFidelite(res.getPlage());
					}
				}
			}
		}	

}
