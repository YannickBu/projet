package metier;

import java.text.SimpleDateFormat;

import donnee.Client;
import donnee.ForfaitClient;
import donnee.Reservation;
import donnee.Salle;
import donnee.TypeSalle;
import fabrique.FabClient;
import fabrique.FabForfaitClient;
import fabrique.FabReservation;
import fabrique.FabSalle;
import fabrique.FabTypeSalle;


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
		
		//TODO a verifier par toi Yannick :p 
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
			FabTypeSalle typeSalle = FabTypeSalle.getInstance();
			TypeSalle ts = typeSalle.rechercher(fc.getTypeSalle().getIdTypeSalle());
			FabSalle salle = FabSalle.getInstance();
			Salle s = salle.rechercherParType(ts.toString());
			
			SimpleDateFormat formatter = new SimpleDateFormat("HH");
			int heure = Integer.parseInt(formatter.format(res.getDate()));
			
			// Le paiement de 20h et plus
			if (heure >= 20 && c!= null && res != null){
				int plage = res.getPlage();
				double prix=0;
				while (plage != 0){ // pour le calcul du prix
					if ((plage%2) == 0){
						prix = s.getPrixPlage2h()*(2/100) + s.getPrixPlage2h();
					}
					else {
						prix = s.getPrixPlage1h()*(2/100) + s.getPrixPlage1h();
					}
				}
				if (plage == 2) { // pour le paiment avec le forfait ou point de fidelite la difference qui reste en cash
					if (c.getPointsFidelite() >= 150) {
						int pointFidelite = c.getPointsFidelite() - 150;
						c.setPointsFidelite(pointFidelite);
						prix = prix - s.getPrixPlage2h();
					} else {
						if (fc.getTempsRestant() >= 2){
							int temps = fc.getTempsRestant();
							temps = temps - 2;
							fc.setTempsRestant(temps);
							prix = prix - s.getPrixPlage2h();
						}
					}
				}
				if (plage > 2) {
					while (c.getPointsFidelite() >= 150 && plage != 0) {
						int pointFidelite = c.getPointsFidelite() - 150;
						c.setPointsFidelite(pointFidelite);
						plage = plage - 2;
						prix = prix - s.getPrixPlage2h();
					}
					if (plage >= 2) {
						while (fc.getTempsRestant() >= 1 && plage != 0){
							int temps = fc.getTempsRestant();
							temps = temps - 1;
							fc.setTempsRestant(temps);
							plage = plage -1;
							prix = prix - s.getPrixPlage1h();
						}
					}
					if (plage >= 1 || plage == 0 && prix >=0 ){ // Le client paye cash la difference
						res.setEstPaye(true);
						c.ajoutPointFidelite(res.getPlage());
					}
				}		
				if (plage == 1) {
					if (fc.getTempsRestant() >= 1){
						int temps = fc.getTempsRestant();
						temps = temps - 1;
						fc.setTempsRestant(temps);
						prix = prix - s.getPrixPlage1h();
					}
					else {
						res.setEstPaye(true);// le client paye par cash la reservation
						c.ajoutPointFidelite(res.getPlage());
					}
					res.setEstPaye(true); // Le client paye cash la difference qui reste
					c.ajoutPointFidelite(res.getPlage());
				}
			}
				
			
			
			
			// Le paiement avant 20h 
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
					int plage = res.getPlage();
					while (c.getPointsFidelite() >= 150 && plage != 0) {
						int pointFidelite = c.getPointsFidelite() - 150;
						c.setPointsFidelite(pointFidelite);
						plage = plage - 2;
						}
					if (plage >= 2) {
						while (fc.getTempsRestant() >= 1 && plage != 0){
							int temps = fc.getTempsRestant();
							temps = temps - 1;
							fc.setTempsRestant(temps);
							plage = plage -1;
						}
					}
					if (plage >= 1 || plage == 0 ){
						res.setEstPaye(true);
						c.ajoutPointFidelite(res.getPlage());
					}
				}		
				if (res.getPlage() == 1) {
					if (fc.getTempsRestant() >= 1){
						int temps = fc.getTempsRestant();
						temps = temps - 1;
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
