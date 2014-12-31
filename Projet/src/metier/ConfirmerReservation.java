package metier;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
		
		// Cette methode a utiliser aussi si le client reserve au moins  4 seances consecutives
		/**
		 * Methode qui permet de confirmer un payement (sans forfait ou points de fidelite) des reservations
		 * en ajoutant les points de fidelite et le bonus
		 * @param idClient
		 * @param idReservation
		 */
		public void payerReservationPlusBonus(Integer idClient , Integer idReservation) {
			FabReservation reservation = FabReservation.getInstance();
			FabClient client = FabClient.getInstance();
			Client c = client.rechercher(idClient);
			Reservation res = reservation.rechercher(idReservation);
		
			if(c != null && res != null ) {
				res.setEstPaye(true);
				c.ajoutPointFidelite(res.getPlage());
				c.ajoutPointFideliteBonus();
			}
		}
		
		/**
		 * Recupere dans lordre : le prix, les pts fid. restant(null si utiliserFidelite false) 
		 * et le tps restant sur le forfait(null si idForfaitClient null)
		 * @param idClient
		 * @param res
		 * @param idForfaitClient lid du ForfaitClient, null si pas dutilisation de forfait
		 * @param utiliserFidelite 
		 * @return le prix, les pts fid. restant(null si utiliserFidelite false) 
		 * et le tps restant sur le forfait(null si idForfaitClient null)
		 */
		public Double[] getInfosApresPaiement(Reservation res, Integer idClient, Integer idForfaitClient, boolean utiliserFidelite){
			Client client = idClient!=null?FabClient.getInstance().rechercher(idClient):null;
			ForfaitClient fc = idForfaitClient!=null?FabForfaitClient.getInstance().rechercher(idForfaitClient):null;

			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(res.getDate());
			Double ptsFid = null;
			Double tpsRestantForfait = null;
			int plage = res.getPlage();
			int heure = calendar .get(Calendar.HOUR_OF_DAY);
			Double prix = new Double((plage/2)*res.getSalle().getPrixPlage2h() + (plage % 2)*res.getSalle().getPrixPlage1h());
			
			
			if(utiliserFidelite && idClient!=null){
				ptsFid = new Double(client.getPointsFidelite());
				while(ptsFid >= 150 && plage >= 2){
					prix -= res.getSalle().getPrixPlage2h();
					plage -= 2;
					ptsFid -= 150;
				}
			}
			
			
			if(idForfaitClient != null){
				tpsRestantForfait = new Double(fc.getTempsRestant());
				while(plage >= 2 && tpsRestantForfait >= 2){
					prix -= res.getSalle().getPrixPlage2h();
					tpsRestantForfait-=2;
					plage-=2;
				}
				if(plage >= 1 && tpsRestantForfait >= 1){
					prix -= res.getSalle().getPrixPlage1h();
					tpsRestantForfait--;
					plage--;
				}
			}

			
			if(heure >= 20){
				prix = prix * 1.2;
			}
			
			return new Double[]{prix,ptsFid,tpsRestantForfait};
			
			
			}
		
		/**
		 * Methode qui permet de payer une reservation
		 * @param idReservation
		 * @param idForfaitClient
		 * @param ptsFid
		 * @param tpsRestantForfait
		 */
		public void payerReservation(Integer idReservation, Integer idForfaitClient, Double ptsFid, Double tpsRestantForfait){
			Reservation res = FabReservation.getInstance().rechercher(idReservation);
			Client client = FabClient.getInstance().rechercher(res.getClient().getId());
			ForfaitClient fc = idForfaitClient!=null?FabForfaitClient.getInstance().rechercher(idForfaitClient):null;
			
			res.setEstPaye(true);
			FabReservation.getInstance().modifierReservation(res);
			
			if(ptsFid != null){
				client.setPointsFidelite(ptsFid.intValue());
				FabClient.getInstance().modifierClient(client);
			}
			
			if(tpsRestantForfait != null){
				fc.setTempsRestant(tpsRestantForfait.intValue());
				FabForfaitClient.getInstance().modifierForfaitClient(fc);
			}
		}

		//TODO c'est a garder ou supprimer ??? n'oublie pas que y'a pas daffichage dans le metier 
		public static void main(String[] args) {
			ConfirmerReservation m = new ConfirmerReservation();
			m.payerReservation(19, 2, 10.0, 5.0);
		}
}
