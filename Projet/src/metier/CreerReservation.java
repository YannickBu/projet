package metier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	 * Methode qui permet de creer une nouvelle reservation
	 * @param idClient
	 * @param idSalle
	 * @param dateDebut
	 * @param plage
	 * @param dateCreation
	 * @param estPayee
	 * @return reservation
	 */
	public Reservation creerReservation (Integer idClient, Integer idSalle ,Date dateDebut,int plage,Date dateCreation, Boolean estPayee) {
		FabClient fabClient = FabClient.getInstance();
		FabReservation fabReservation = FabReservation.getInstance();
		Reservation res = null;
		Client client = fabClient.rechercher(idClient);
		
		if (client != null){
			res = fabReservation.creer(idClient,idSalle,dateDebut,plage,dateCreation,estPayee);
			
		}
		
		return res;	
		
	}
	
	/**
	 * Cree une nouvelle reservation
	 * Met a jour le forfait client avec le temps restant
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
			FabClient.getInstance().modifierClient(clt);
		}
		
		FabReservation.getInstance().creer(clt.getId(), res.getSalle().getIdSalle(), res.getDate(), res.getPlage(), res.getDateCreation(), res.getEstPaye());
	}
	
	/**
	 * Methode qui permet de creer une reservation un meme jour de semaine pendant une periode donnee 
	 * @param idClient
	 * @param idSalle
	 * @param dateDebut
	 * @param plage
	 * @param dateCreation
	 * @param estPayee
	 * @param nbSemaine
	 * @return liste de reservation
	 */
	public List<Reservation> creerReservation(Integer idClient, Integer idSalle,Date dateDebut, int plage,Date dateCreation , boolean estPayee,int nbSemaine) {
		FabClient fabClient = FabClient.getInstance();
		FabReservation fabReservation = FabReservation.getInstance();
		List<Reservation> listRes = new ArrayList<Reservation>();
		Reservation res = null;
		Client client = fabClient.rechercher(idClient);
		
		if (client != null) {
			int cpt = 0;
			int i = 0;
			while ( cpt < nbSemaine) {
				res = fabReservation.creer(idClient,idSalle,ajouterJour(dateDebut,i),plage,dateCreation,estPayee);
				listRes.add(res);
				i = i + 7;
			}
		} 
		return listRes;
	} 
	
	/**
	 * Methode qui permet d'ajouter des jours a une date
	 * @param date
	 * @param nbJour
	 * @return date
	 */
	public Date ajouterJour(Date date, int nbJour) { 
		  Calendar cal = Calendar.getInstance(); 
		  cal.setTime(date);
		  cal.add(Calendar.DATE, nbJour);
		  return cal.getTime();
		}
}
