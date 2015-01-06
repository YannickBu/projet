package metier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import donnee.Client;
import donnee.ForfaitClient;
import donnee.Reservation;
import exception.ObjetInconnuException;
import fabrique.FabClient;
import fabrique.FabForfaitClient;
import fabrique.FabReservation;

public class CreerReservation {

	/**
	 * Methode qui permet de creer une reservation un meme jour de semaine pendant une periode donnee 
	 * @param res
	 * @param nomClt
	 * @param prenomClt
	 * @param telClt
	 * @param fc
	 * @param ptsFidRestant
	 * @return
	 */
	public List<Reservation> creerReservation(List<Reservation> listeRes, String nomClt, String prenomClt, String telClt, ForfaitClient fc, Integer ptsFidRestant) {
		List<Reservation> listeReservationsCrees = new ArrayList<Reservation>();
		Reservation reservationCree = null;
		Client client = FabClient.getInstance().rechercher(nomClt, prenomClt, telClt);
		Calendar cal = Calendar.getInstance(); 
		
		if(fc!=null){
			if(fc.getTempsRestant()==0){
				FabForfaitClient.getInstance().supprimer(fc.getIdForfaitClient());
			} else{
				FabForfaitClient.getInstance().modifierForfaitClient(fc);
			}
		}
		
		if(ptsFidRestant!=null){
			client.setPointsFidelite(ptsFidRestant);
		}
		//on ajoute les 30 pts si reservation sur au moins 4 semaines
		if(listeRes.size()>3){
			client.ajoutPointsFideliteBonus();
		}
		if(ptsFidRestant!=null || listeRes.size()>3){
			FabClient.getInstance().modifierClient(client);
		}
		
		if (client != null) {
			//on supprime les reservations sur les creneaux des semaines a reserver
			for(int i=0; i<listeRes.size(); i++){
				cal.setTime(listeRes.get(i).getDate());
				cal.add(Calendar.HOUR_OF_DAY, listeRes.get(i).getPlage());
				FabReservation.getInstance().supprimer(listeRes.get(i).getDate(), cal.getTime());
			}
			
			//on cree une reservation par semaine
			for(int i=0; i<listeRes.size(); i++){
				reservationCree = FabReservation.getInstance().creer(client.getId(), listeRes.get(i).getSalle().getIdSalle(), 
						listeRes.get(i).getDate(), listeRes.get(i).getPlage(), listeRes.get(i).getDateCreation(), listeRes.get(i).getEstPaye());
			}
			listeReservationsCrees.add(reservationCree);
		} 
		return listeReservationsCrees;
	} 
	
}
