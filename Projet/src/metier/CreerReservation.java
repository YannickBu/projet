package metier;

import java.util.Date;
import java.util.List;

import donnee.Client;
import donnee.Reservation;
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
	
	public void creerReservation(List<Reservation> listeReservations, String nomClt, String telClt){
		CreerClient metierCreerClt = new CreerClient();
		Client clt = metierCreerClt.creerClientSiInexistant(nomClt, telClt);
		
		for(Reservation res : listeReservations){
			FabReservation.getInstance().creer(clt.getId(), res.getSalle().getIdSalle(), res.getDate(), res.getPlage(), res.getDateCreation(), res.getEstPaye());
		}
	}
}
