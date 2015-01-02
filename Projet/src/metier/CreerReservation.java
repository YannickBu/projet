package metier;

import java.util.ArrayList;
import java.util.List;

import donnee.Client;
import donnee.ForfaitClient;
import donnee.Reservation;
import exception.ObjetInconnuException;
import fabrique.FabClient;
import fabrique.FabForfaitClient;
import fabrique.FabReservation;

public class CreerReservation {
	
	//TODO inutile?
	/**
	 * Methode qui permet de creer une nouvelle reservation
	 * @param idClient
	 * @param idSalle
	 * @param dateDebut
	 * @param plage
	 * @param dateCreation
	 * @param estPayee
	 * @return reservation
	 */
	/*public Reservation creerReservation (Integer idClient, Integer idSalle ,Date dateDebut,int plage,Date dateCreation, Boolean estPayee) {
		FabClient fabClient = FabClient.getInstance();
		FabReservation fabReservation = FabReservation.getInstance();
		Reservation res = null;
		Client client = fabClient.rechercher(idClient);
		
		if (client != null){
			res = fabReservation.creer(idClient,idSalle,dateDebut,plage,dateCreation,estPayee);
			
		}
		
		return res;	
		
	}*/
	
	/**
	 * Cree une nouvelle reservation<br/>
	 * Met a jour le forfait client avec le temps restant<br/>
	 * Met a jour les pts fidelite du client
	 * @param res
	 * @param nomClt
	 * @param prenomClt
	 * @param telClt
	 * @param fc forfaitclient a mettre a jour (null si aucun forfait utilise)
	 * @param ptsFidRestant nouveau montant de pts fidelite (null si aucun point utilise)
	 * @throws ObjetInconnuException
	 */
	public void creerReservation(Reservation res, String nomClt, String prenomClt, String telClt, ForfaitClient fc, Integer ptsFidRestant) throws ObjetInconnuException{
		RechercheClient metierRechClt = new RechercheClient();
		Client clt = metierRechClt.rechercheClient(nomClt, prenomClt, telClt);
		
		if(fc!=null){
			if(fc.getTempsRestant()==0){
				FabForfaitClient.getInstance().supprimer(fc.getIdForfaitClient());
			} else{
				FabForfaitClient.getInstance().modifierForfaitClient(fc);
			}
		}
		
		if(ptsFidRestant!=null){
			clt.setPointsFidelite(ptsFidRestant);
		}
		clt.ajoutPointsFidelite(res.getPlage());
		FabClient.getInstance().modifierClient(clt);
		
		FabReservation.getInstance().creer(clt.getId(), res.getSalle().getIdSalle(), res.getDate(), res.getPlage(), res.getDateCreation(), res.getEstPaye());
	}
	

	/**
	 * Methode qui permet de creer une reservation un meme jour de semaine pendant une periode donnee 
	 * @param res
	 * @param nomClt
	 * @param prenomClt
	 * @param telClt
	 * @param fc
	 * @param ptsFidRestant
	 * @param nbSemaines
	 * @return
	 */
	public List<Reservation> creerReservation(List<Reservation> listeRes, String nomClt, String prenomClt, String telClt, ForfaitClient fc, Integer ptsFidRestant) {
		List<Reservation> listeReservationsCrees = new ArrayList<Reservation>();
		Reservation reservationCree = null;
		Client client = FabClient.getInstance().rechercher(nomClt, prenomClt, telClt);
		
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
			for(int i=0; i<listeRes.size(); i++){
				reservationCree = FabReservation.getInstance().creer(client.getId(), listeRes.get(i).getSalle().getIdSalle(), 
						listeRes.get(i).getDate(), listeRes.get(i).getPlage(), listeRes.get(i).getDateCreation(), listeRes.get(i).getEstPaye());
			}
			listeReservationsCrees.add(reservationCree);
		} 
		return listeReservationsCrees;
	} 
	
}
