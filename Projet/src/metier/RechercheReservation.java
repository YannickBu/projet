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
	
	public List<Reservation> rechercheReservationParDateEtTypeSalle(String date, String typeSalle){
		return FabReservation.getInstance().rechercherParDateEtTypeSalle(date, typeSalle);
	}
	
	/**
	 * Recherche un creneau libre pour une reservation
	 * @param date
	 * @param duree
	 * @param tranche
	 * @param typeSalle
	 * @return
	 */
	public List<Reservation> rechercheCreneauLibre(String date, int duree, String tranche, String typeSalle){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		Reservation reservationCourante = null;
		List<Reservation> leCreneauAvecPlage1h = new ArrayList<Reservation>();
		String[] etatSalles  = etatsSalle(date, typeSalle);
		int creneauCourant = tranche.equals("matin")?9:(tranche.equals("soir")?20:13);
		int creaneauFin = tranche.equals("matin")?13:(tranche.equals("soir")?24:20);
		
		while(leCreneauAvecPlage1h.isEmpty() 
				&& creneauCourant < creaneauFin 
				&& creneauCourant + duree < 25){
			
			for(int i=creneauCourant; i<creneauCourant + duree; i++){
				if("Libre".equals(etatSalles[i-9])){
					reservationCourante = new Reservation();
					reservationCourante.setClient(null);
					reservationCourante.setSalle(FabSalle.getInstance().rechercher(typeSalle));
					try {
						reservationCourante.setDate(formatter.parse(date + " " + i + "-00-00"));
					} catch (ParseException e) {
						System.out.println("Erreur de parsing de la date : '" + date + " " + i + "-00-00' - " + e.getMessage());
					}
					reservationCourante.setDateCreation(new Date());
					reservationCourante.setEstPaye(false);
					reservationCourante.setPlage(1);//TODO gere plage 2h
					leCreneauAvecPlage1h.add(reservationCourante);
				} else {
					leCreneauAvecPlage1h.clear();
					break;
				}
			}
			
			creneauCourant++;
		}
		
		return plage1hEnPlage2hEt1h(leCreneauAvecPlage1h);
	}
	
	private List<Reservation> plage1hEnPlage2hEt1h(List<Reservation> liste1h){
		List<Reservation> liste2h = new ArrayList<Reservation>();
		Reservation reservationCourante = null;
		int nbPlage2h;
		
		if(!liste1h.isEmpty()){
			nbPlage2h = liste1h.size() / 2;
			if(nbPlage2h > 0){
				for(int i=0; i<nbPlage2h; i++){
					reservationCourante = liste1h.get(2*i);
					reservationCourante.setPlage(2);
					liste2h.add(reservationCourante);
				}
			}
			if(liste1h.size() % 2 != 0){
				liste2h.add(liste1h.get(liste1h.size()-1));
			}
		}
		
		return liste2h;
	}
	
	/**
	 * Retourne un tableau de String représentant l'état de la salle
	 * pour la date donnee
	 * @param date
	 * @param typeSalle
	 * @return
	 */
	public String[] etatsSalle(String date, String typeSalle){
		GregorianCalendar calendarDebut = new GregorianCalendar();
		GregorianCalendar calendarCreation = new GregorianCalendar();
		List<Reservation> listeReservationsExistantes = FabReservation.getInstance().rechercherParDateEtTypeSalle(date, typeSalle);
		String[] etatsSalle = new String[15];
		for(int i=0; i<etatsSalle.length;i++){
			etatsSalle[i] = "Libre";
		}
		
		for(Reservation reservationEnCours : listeReservationsExistantes){
			calendarDebut.setTime(reservationEnCours.getDate());
			calendarCreation.setTime(reservationEnCours.getDateCreation());
			
			if(reservationEnCours.getEstPaye()){
				for(int i=0; i<reservationEnCours.getPlage();i++)
					etatsSalle[calendarDebut.get(Calendar.HOUR_OF_DAY)-9+i] = "Confirmée";
			}
			else if(calendarDebut.getTimeInMillis() - calendarCreation.getTimeInMillis() > (DELAIS_DE_PAIEMENT_EN_JOURS*24*60*60*1000)){
				for(int i=0; i<reservationEnCours.getPlage();i++)
					etatsSalle[calendarDebut.get(Calendar.HOUR_OF_DAY)-9+i] = "Hors délais";
			} else {
				for(int i=0; i<reservationEnCours.getPlage();i++)
					etatsSalle[calendarDebut.get(Calendar.HOUR_OF_DAY)-9+i] = "Non confirmée";
			}
		}
		
		return etatsSalle;
	}

}
