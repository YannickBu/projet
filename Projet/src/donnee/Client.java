package donnee;

import java.util.ArrayList;

public class Client {

	public static final int POINTS_FIDELITE_PAR_HEURE = 5;
	public static final int POINTS_FIDELITE_POUR_UN_MOIS = 30;
	
	private int id;
	private String nom;
	private String prenom;
	private String numTel;
	private int pointsFidelite;
	private ArrayList<Forfait> listForfait;
	private ArrayList<Reservation> listReservation;
	
	/**
	 * Le constructeur de la classe Client
	 */
	public Client(){}
	
	/**
	 * Methode qui retourne l'identifiant du client 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Methode qui modifie l'identifiant du client
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Methode qui retourne le nom du client
	 * @return nom 
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Methode qui modifie le nom du client
	 * @param nom
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Methode qui retourne le prenom du client
	 * @return prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * Methode qui modifie le prenom du client
	 * @param prenom
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * Methode qui retourne la liste des forfaits du client 
	 * @return listForfait
	 */
	public ArrayList<Forfait> getListForfait() {
		return listForfait;
	}

	/**
	 * Methode qui modifie la liste des Forfaits du client
	 * @param listForfait
	 */
	public void setListForfait(ArrayList<Forfait> listForfait) {
		this.listForfait = listForfait;
	}

	/**
	 * Methode qui retourne la liste des reservations du client
	 * @return listReservations
	 */
	public ArrayList<Reservation> getListReservation() {
		return listReservation;
	}

	/**
	 * Methode qui modifie la liste des reservations
	 * @param listReservation
	 */
	public void setListReservation(ArrayList<Reservation> listReservation) {
		this.listReservation = listReservation;
	}

	/**
	 * Methode qui retourne le numero de telephone du client
	 * @return numTel
	 */
	public String getNumTel() {
		return numTel;
	}
	
	/**
	 * Methode qui modifie le numero de telephone
	 * @param numTel
	 */
	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}

	/**
	 * Methode qui retourne les points de fidelite
	 * @return pointsFidelite
	 */
	public int getPointsFidelite() {
		return pointsFidelite;
	}

	/**
	 * Methode qui modifie les points de fidelite
	 * @param pointsFidelite
	 */
	public void setPointsFidelite(int pointsFidelite) {
		this.pointsFidelite = pointsFidelite;
	}

	/**
	 * Methode qui permet d'ajouter les points de fidelite 
	 * @param h
	 */
	public void ajoutPointsFidelite (int h) {
		this.pointsFidelite += (POINTS_FIDELITE_PAR_HEURE*h);
	}
	
	/**
	 * Methode qui permet le rajout des 30 points de fidelite bonus pour ceux qui reservent 4 seances consecutives et plus
	 */
	public void ajoutPointsFideliteBonus() {
		this.pointsFidelite += POINTS_FIDELITE_POUR_UN_MOIS;
	}
	
	/**
	 * Methode qui permet d'ajouter un forfait
	 * @param f
	 */
	public void ajoutForfait (Forfait f) {
		this.listForfait.add(f);
	}
	
	/**
	 * Methode qui permet de supprimer un forfait
	 * @param f
	 */
	public void supprimerForfait (Forfait f){
		this.listForfait.remove(f);
	}
	
	/**
	 * Methode qui permet d'ajouter une reservation
	 * @param r
	 */
	public void ajoutReservation (Reservation r) {
		this.listReservation.add(r);
	}
	
	/**
	 * Methode qui permet de supprimer une reservation
	 * @param r
	 */
	public void supprimerReservation (Reservation r){
		this.listReservation.remove(r);
	}
	
	
}
