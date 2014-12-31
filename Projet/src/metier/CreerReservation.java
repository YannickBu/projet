package metier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import donnee.Client;
import donnee.Reservation;
import exception.ObjetInconnuException;
import fabrique.FabClient;
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
	 * Methode qui permet de creer une reservation
	 * @param res
	 * @param nomClt
	 * @param prenomClt
	 * @param telClt
	 * @throws ObjetInconnuException
	 */
	public void creerReservation(Reservation res, String nomClt, String prenomClt, String telClt) throws ObjetInconnuException{
		RechercheClient metierRechClt = new RechercheClient();
		Client clt = metierRechClt.rechercheClient(nomClt, prenomClt, telClt);
		
		FabReservation.getInstance().creer(clt.getId(), res.getSalle().getIdSalle(), res.getDate(), res.getPlage(), res.getDateCreation(), res.getEstPaye());
	}
	
	/*
	public List<Reservation> creerReservation(Integer idClient, Integer idSalle,Date dateDebut, int plage,Date dateCreation , boolean estPayee,int nbSemaine) {
		FabClient fabClient = FabClient.getInstance();
		FabReservation fabReservation = FabReservation.getInstance();
		List<Reservation> listRes = new ArrayList<Reservation>();
		Reservation res = null;
		Client client = fabClient.rechercher(idClient);
		
		if (client != null) {
			int cpt = 0;
			while ( cpt < nbSemaine) {
				
			}
			
		} 
		
		
		
	} */
}
