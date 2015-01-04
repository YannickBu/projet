package metier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import donnee.Client;
import donnee.Reservation;
import donnee.Salle;
import fabrique.FabClient;
import fabrique.FabReservation;
import fabrique.FabSalle;

public class RechercheReservation {
	
	/**
	 * Methode qui permet de rechercher une reservation
	 * @param idres
	 * @param date
	 * @param s
	 * @return reservation si trouvee , null sinon
	 */
	public Reservation rechercheReservation(Integer idres, Date date , Salle s) {
		FabReservation reservation = FabReservation.getInstance();
		
		if(reservation.rechercher(idres) != null) {
			Reservation res = reservation.rechercher(idres);
			if (res.getDate().equals(date) && res.getSalle().equals(s)) {
				return res;
			}
		}
		
		return null;	
		
	}
	
	/**
	 * Methode qui permet de rechercher une reservation par date et type de salle
	 * @param date
	 * @param idSalle
	 * @return reservation
	 */
	public List<Reservation> rechercheReservationParDateEtTypeSalle(String date, int idSalle){
		return FabReservation.getInstance().rechercherParDateEtTypeSalle(date, idSalle);
	}
	
	/**
	 * Methode qui permet de rechercher Salle par son type
	 * @param typeSalle
	 * @return liste Salle
	 */
	public List<Salle> rechercherSallesParType(String typeSalle){
		return FabSalle.getInstance().rechercher(typeSalle);
	}
	
	/**
	 * Recherche un creneau libre pour une reservation
	 * @param date Date au format dd-MM-yyyy
	 * @param duree
	 * @param tranche
	 * @param typeSalle
	 * @param autoriseEcrasementHorsDelais
	 * @return reservation
	 */
	public Reservation rechercheCreneauLibre(String date, int duree, String tranche, String typeSalle, boolean autoriseEcrasementHorsDelais){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		Reservation reservationCourante = null;
		List<Reservation> leCreneauAvecPlage1h = new ArrayList<Reservation>();
		List<Salle> listeSalle = FabSalle.getInstance().rechercher(typeSalle);
		List<List<Reservation>> reservationsPossibles = new ArrayList<List<Reservation>> ();
		
		String[] etatsSalle  = null;
		
		//Pour chaque salle du type typeSalle, on recupere l'etat de cette salle
		//et on essaie de 'caser' la reservation dans la bonne tranche
		//on stoppe la boucle des que lon a une liste des reservartions (par plage d1 et 2h) de creee
		for(Salle salleCourante : listeSalle){

			int creneauCourant = tranche.equals("matin")?9:(tranche.equals("soir")?20:13);
			int creaneauFin = tranche.equals("matin")?13:(tranche.equals("soir")?24:20);
			etatsSalle = etatsSalle(date, salleCourante.getIdSalle());
			
			//On parcourt les creneaux
			//On stoppe lorsque l'on a genere une liste de creneaux libres
			//ou que tous les creneaux sont parcourus
			while(leCreneauAvecPlage1h.isEmpty() 
					&& creneauCourant < creaneauFin 
					&& creneauCourant + duree < 25){
				
				//On essaie de generer la liste des reservations
				for(int i=creneauCourant; i<creneauCourant + duree; i++){
					if("Libre".equals(etatsSalle[i-9]) 
							|| (autoriseEcrasementHorsDelais && "Hors delais".equals(etatsSalle[i-9]))){
						reservationCourante = new Reservation();
						reservationCourante.setClient(null);
						reservationCourante.setSalle(salleCourante);
						try {
							reservationCourante.setDate(formatter.parse(date + " " + i + "-00-00"));
						} catch (ParseException e) {
							System.out.println("Erreur de parsing de la date : '" + date + " " + i + "-00-00' - " + e.getMessage());
						}
						reservationCourante.setDateCreation(new Date());
						reservationCourante.setEstPaye(false);
						reservationCourante.setPlage(1);
						leCreneauAvecPlage1h.add(reservationCourante);
					} else {
						//on vide des qu'un creneau n'est PAS libre
						leCreneauAvecPlage1h.clear();
						break;
					}
				}
				
				creneauCourant++;
			}
			
			//Si la liste des creneaux n'est pas vide
			//On a trouve une reservation possible
			if(leCreneauAvecPlage1h != null && !leCreneauAvecPlage1h.isEmpty()){
				reservationsPossibles.add(leCreneauAvecPlage1h);
				leCreneauAvecPlage1h = new ArrayList<Reservation>();
			}
		}
		
		if(reservationsPossibles==null || reservationsPossibles.isEmpty()){
			return null;
		}
		
		leCreneauAvecPlage1h = new ArrayList<Reservation>();
		leCreneauAvecPlage1h.add(new Reservation());
		try {
			leCreneauAvecPlage1h.get(0).setDate(formatter.parse(date + " 24-00-00"));
		} catch (ParseException e) {
			System.out.println("Erreur de parsing de la date : '" + date + " 24-00-00'" + e.getMessage());
		}
		//On choisit la reservation qui commence le plus tot
		for(List<Reservation> reservation : reservationsPossibles){
			if(leCreneauAvecPlage1h.get(0).getDate().after(reservation.get(0).getDate())){
				leCreneauAvecPlage1h = reservation;
			}
		}
		
		return plage1hEnReservation(leCreneauAvecPlage1h);
	}
	
	/**
	 * Recherche un creneau libre sur plusieurs semaines pour une reservation
	 * @param date Date au format <i>"dd-MM-yyyy"</i>
	 * @param duree
	 * @param tranche
	 * @param typeSalle
	 * @param nbSemaines
	 * @return reservation
	 */
	public List<Reservation> rechercheCreneauLibre(String date, int duree, String tranche, String typeSalle, int nbSemaines){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dateEncours = null;
		List<Reservation> listeRes = new ArrayList<Reservation>();
		Reservation reservationEnCours = null;
		Calendar cal = Calendar.getInstance(); 
		Date dateCreation = new Date();//Pour faire en sorte que ttes les reservations aient une meme date de creation
		  
		for(int i=0; i<nbSemaines; i++){
			try {
				dateEncours = formatter.parse(date);
			} catch (ParseException e) {
				System.out.println(e.getMessage());
				return null;
			}
			cal.setTime(dateEncours);
			cal.add(Calendar.DAY_OF_MONTH, 7*i);
			reservationEnCours = rechercheCreneauLibre(formatter.format(cal.getTime()), duree, tranche, typeSalle, true);
			if(reservationEnCours==null){
				return null;
			}
			reservationEnCours.setDateCreation(dateCreation);
			listeRes.add(reservationEnCours);
			
		}
		
		return listeRes;
	}
	
	/**
	 * Methode qui permet retourner une liste de reservation en plage de plusieurs heures
	 * @param liste1h
	 * @return reservation
	 */
	private Reservation plage1hEnReservation(List<Reservation> liste1h){
		Reservation res = null;
		
		if(liste1h!=null && !liste1h.isEmpty()){
			res = new Reservation();
			res.setClient(liste1h.get(0).getClient());
			res.setDate(liste1h.get(0).getDate());
			res.setDateCreation(liste1h.get(0).getDateCreation());
			res.setEstPaye(liste1h.get(0).getEstPaye());
			res.setPlage(liste1h.size());
			res.setSalle(liste1h.get(0).getSalle());
		}
		
		return res;
	}
	
	/**
	 * Retourne un tableau de String représentant l'état de la salle
	 * pour la date donnee
	 * @param date
	 * @param idSalle
	 * @return etat de la salle
	 */
	public String[] etatsSalle(String date, int idSalle){
		GregorianCalendar calendarDebut = new GregorianCalendar();
		GregorianCalendar calendarCreation = new GregorianCalendar();
		List<Reservation> listeReservationsExistantes = FabReservation.getInstance().rechercherParDateEtTypeSalle(date, idSalle);
		String[] etatsSalle = new String[15];
		for(int i=0; i<etatsSalle.length;i++){
			etatsSalle[i] = "Libre";
		}
		
		for(Reservation reservationEnCours : listeReservationsExistantes){
			calendarDebut.setTime(reservationEnCours.getDate());
			calendarCreation.setTime(reservationEnCours.getDateCreation());
			
			if(reservationEnCours.getEstPaye()){
				for(int i=0; i<reservationEnCours.getPlage();i++)
					etatsSalle[calendarDebut.get(Calendar.HOUR_OF_DAY)-9+i] = "Confirmee";
			}
			else if(new Date().getTime() - calendarCreation.getTimeInMillis() > (Reservation.DELAIS_DE_PAIEMENT_EN_JOURS*24*60*60*1000)){
				for(int i=0; i<reservationEnCours.getPlage();i++)
					etatsSalle[calendarDebut.get(Calendar.HOUR_OF_DAY)-9+i] = "Hors delais";
			} else {
				for(int i=0; i<reservationEnCours.getPlage();i++)
					etatsSalle[calendarDebut.get(Calendar.HOUR_OF_DAY)-9+i] = "Non confirmee";
			}
		}
		
		return etatsSalle;
	}

	/**
	 * Pour une date et une salle données, on retourne un tableau representant 
	 * les 15 creneaux dune journee, avec pour chaque creneau la reservation,
	 * null si non reserve
	 * @param dateDuJour
	 * @param idSalle
	 * @return
	 */
	public Reservation[] reservationParHeure(String dateDuJour, int idSalle){
		GregorianCalendar calendar = new GregorianCalendar();
		Reservation[] tabRes = new Reservation[15];
		List<Reservation> listeReservationsExistantes = FabReservation.getInstance().rechercherParDateEtTypeSalle(dateDuJour, idSalle);
		
		for(Reservation res : listeReservationsExistantes){
			calendar.setTime(res.getDateCreation());
			calendar.setTime(res.getDate());
			for(int i=0; i<res.getPlage(); i++){
				tabRes[calendar.get(Calendar.HOUR_OF_DAY)+i-9] = res;
			}
		}
		
		
		return tabRes;
	}
	
	/**
	 * Methode permet de lister les reservations pour un client
	 * @param idClt
	 * @param etat etat de la reservation - constantes possibles dans lobjet reservation<br/>
	 * Peut prendre la valeur null pour recuperation de tous les etats
	 * @return liste reservation
	 */
	public List<Reservation> listerReservationsPourUnClient(int idClt, Integer etat){
		List<Reservation> listeRes = FabReservation.getInstance().listerParClient(idClt);
		List<Reservation> listeResModif = new ArrayList<Reservation>();
		
		if(etat != null){
			switch(etat){
			case Reservation.ETAT_HORS_DELAIS :
				for(Reservation res : listeRes){
					if(new Date().getTime() - res.getDateCreation().getTime() >= 7*24*60*60*1000 && !res.getEstPaye()){
						listeResModif.add(res);
					}
				}
				break;
			case Reservation.ETAT_CONFIRME :
				for(Reservation res : listeRes){
					if(res.getEstPaye()){
						listeResModif.add(res);
					}
				}
				break;
			case Reservation.ETAT_NON_CONFIRME :
				for(Reservation res : listeRes){
					if(!res.getEstPaye() 
							&& new Date().getTime() - res.getDateCreation().getTime() < 7*24*60*60*1000
							){
						listeResModif.add(res);
					}
				}
				break;
			}
			return listeResModif;
		}
		
		return listeRes;
	}
	
	/**
	 * Genere un objet Reservation sans id
	 * @param idClient
	 * @param idSalle
	 * @param date
	 * @param plage
	 * @param estPayee
	 * @return
	 */
	public Reservation genererReservation(Integer idClient, Integer idSalle, Date date, Integer plage, boolean estPayee){
		Reservation laReservation = new Reservation();
		Client c = null;
		Salle s = null;
		
		if(idClient!=null){
			c = FabClient.getInstance().rechercher(idClient);
		}
		if(idSalle!=null){
			s = FabSalle.getInstance().rechercherParId(idSalle);
		}
		
		laReservation.setClient(c);
		laReservation.setSalle(s);
		laReservation.setDate(date);
		laReservation.setDateCreation(new Date());
		laReservation.setEstPaye(estPayee);
		laReservation.setPlage(plage);
		
		return laReservation;
	}
	
	public boolean estLibre(Reservation reservation){
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String[] etatSalles = etatsSalle(formatter.format(reservation.getDate()), reservation.getSalle().getIdSalle());
		
		calendar.setTime(reservation.getDate());
		for(int i=0; i<reservation.getPlage(); i++){
			if(!"Libre".equals(etatSalles[calendar.get(Calendar.HOUR_OF_DAY)+i-9])
					&& !"Hors delais".equals(etatSalles[calendar.get(Calendar.HOUR_OF_DAY)+i-9])){
				return false;
			}
		}
		return true;
	}
	
	//TODO suppr?
	/**
	 * Methode permet de lister les reservations pour une salle
	 * @param idSalle
	 * @param etat etat de la reservation - constantes possibles dans lobjet reservation<br/>
	 * Peut prendre la valeur null pour recuperation de tous les etats
	 * @return liste reservation
	 */
	/*public List<Reservation> listerReservationsPourUneSalle(int idSalle, Integer etat){
		List<Reservation> listeRes = FabReservation.getInstance().listerParSalle(idSalle);
		List<Reservation> listeResModif = new ArrayList<Reservation>();
		
		if(etat != null){
			switch(etat){
			case Reservation.ETAT_HORS_DELAIS :
				for(Reservation res : listeRes){
					if(new Date().getTime() - res.getDateCreation().getTime() >= 7*24*60*60*1000 && !res.getEstPaye()){
						listeResModif.add(res);
					}
				}
				break;
			case Reservation.ETAT_CONFIRME :
				for(Reservation res : listeRes){
					if(res.getEstPaye()){
						listeResModif.add(res);
					}
				}
				break;
			case Reservation.ETAT_NON_CONFIRME :
				for(Reservation res : listeRes){
					if(!res.getEstPaye() 
							&& new Date().getTime() - res.getDateCreation().getTime() < 7*24*60*60*1000
							){
						listeResModif.add(res);
					}
				}
				break;
			}
			return listeResModif;
		}
		
		return listeRes;
	}*/
	
	public static void main(String[] args) {
		List<Reservation> l = new RechercheReservation().rechercheCreneauLibre("01-01-2015", 3, "matin", "petite", 5);
		System.out.println(l.size());
	}
}
