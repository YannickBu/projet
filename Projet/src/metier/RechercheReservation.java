package metier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import donnee.Reservation;
import donnee.Salle;
import fabrique.FabReservation;
import fabrique.FabSalle;

public class RechercheReservation {
	
	private int DELAIS_DE_PAIEMENT_EN_JOURS = 7;

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
	
	public List<Salle> rechercherSallesParType(String typeSalle){
		return FabSalle.getInstance().rechercher(typeSalle);
	}
	
	/**
	 * Recherche un creneau libre pour une reservation
	 * @param date
	 * @param duree
	 * @param tranche
	 * @param typeSalle
	 * @return reservation
	 */
	public Reservation rechercheCreneauLibre(String date, int duree, String tranche, String typeSalle){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		Reservation reservationCourante = null;
		List<Reservation> leCreneauAvecPlage1h = new ArrayList<Reservation>();
		List<Salle> listeSalle = FabSalle.getInstance().rechercher(typeSalle);
		List<List<Reservation>> reservationsPossibles = new ArrayList<List<Reservation>> ();
		
		String[] etatsSalle  = null;
		
		//Pour chaque salle du type typeSalle, on recupere letat de cette salle
		//et on essaie de 'caser' la reservation dans la bonne tranche
		//on stoppe la boucle des que lon a une liste des reservartions (par plage d1 et 2h) de creee
		for(Salle salleCourante : listeSalle){

			int creneauCourant = tranche.equals("matin")?9:(tranche.equals("soir")?20:13);
			int creaneauFin = tranche.equals("matin")?13:(tranche.equals("soir")?24:20);
			etatsSalle = etatsSalle(date, salleCourante.getIdSalle());
			
			//On parcourt les creneaux
			//On stoppe lorsque lon a genere une liste de creneaux libres
			//ou que tous les creneaux sont parcourus
			while(leCreneauAvecPlage1h.isEmpty() 
					&& creneauCourant < creaneauFin 
					&& creneauCourant + duree < 25){
				
				//On essaie de generer la liste des reservations
				for(int i=creneauCourant; i<creneauCourant + duree; i++){
					if("Libre".equals(etatsSalle[i-9])){
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
						//on vide des quun creneau nest PAS libre
						leCreneauAvecPlage1h.clear();
						break;
					}
				}
				
				creneauCourant++;
			}
			
			//Si la liste des creneaux nest pas vide
			//On a trouve une reservation possible
			if(leCreneauAvecPlage1h != null && !leCreneauAvecPlage1h.isEmpty()){
				reservationsPossibles.add(leCreneauAvecPlage1h);
				leCreneauAvecPlage1h = new ArrayList<Reservation>();
			}
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
			else if(new Date().getTime() - calendarCreation.getTimeInMillis() > (DELAIS_DE_PAIEMENT_EN_JOURS*24*60*60*1000)){
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
	 * @param idClt
	 * @param etat etat de la reservation - constantes possibles dans lobjet reservation<br/>
	 * Peut prendre la valeur null pour recuperation de tous les etats
	 * @return
	 */
	public List<Reservation> listerReservationsPourUnClient(int idClt, Integer etat){
		List<Reservation> listeRes = FabReservation.getInstance().listerParClient(idClt);
		List<Reservation> listeResModif = new ArrayList<Reservation>();
		
		if(etat != null){
			switch(etat){
			case Reservation.ETAT_HORS_DELAIS :
				for(Reservation res : listeRes){
					if(new Date().getTime() - res.getDateCreation().getTime() >= 7*24*60*60*1000){
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
}
