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
			
			//TODO suppr
			/*int nbHeureHorsSoiree = heure>=20?0:(heure+plage<=20?plage:20-heure);
			int nbHeureSoiree = heure>=20?plage:(heure+plage<=20?0:heure+plage-20);
			Double prix = new Double((nbHeureHorsSoiree/2)*res.getSalle().getPrixPlage2h() + (nbHeureHorsSoiree % 2)*res.getSalle().getPrixPlage1h()
					+ (nbHeureSoiree/2)*res.getSalle().getPrixPlage2h()*1.2 + (nbHeureSoiree % 2)*res.getSalle().getPrixPlage1h()*1.2);*/
			
			
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
			
			
			//TODO suppr?
			/*
			 
			 
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
			 
			 
			 
			 * */
		}
		
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

		public static void main(String[] args) {
			ConfirmerReservation m = new ConfirmerReservation();
			m.payerReservation(19, 2, 10.0, 5.0);
		}
}
