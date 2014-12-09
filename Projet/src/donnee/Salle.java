package donnee;

import java.util.ArrayList;

public class Salle {
	
	private ArrayList<Reservation> listeReservation;
	private int idSalle;
	private String typeSalle;
	private int prixPlage1h;
	private int prixPlage2h;
	
	/**
	 * Constructeur de la classe Salle
	 */
	public Salle() {}
	
	/**
	 * Methode qui retourne le type de la salle
	 * @return typeSalle
	 */
	public String getTypeSalle() {
		return typeSalle;
	}

	/**
	 * Mathode qui modifie le type de la salle
	 * @param typeSalle
	 */
	public void setTypeSalle(String typeSalle) {
		this.typeSalle = typeSalle;
	}

	/**
	 * Methode qui retourne la liste de reservation de la salle
	 * @return listeReservation
	 */
	public ArrayList<Reservation> getListeReservation() {
		return listeReservation;
	}

	/**
	 * Mathode qui modifie la liste des reservations de la salle
	 * @param listeReservation
	 */
	public void setListeReservation(ArrayList<Reservation> listeReservation) {
		this.listeReservation = listeReservation;
	}
	
	/**
	 * Methode qui retourne l'identifiant de la salle
	 * @return idSalle
	 */
	public int getIdSalle() {
		return idSalle;
	}

	/**
	 * Mathode qui modifie l'identifiant de la salle
	 * @param idSalle
	 */
	public void setIdSalle(int idSalle) {
		this.idSalle = idSalle;
	}

	/**
	 * Methode qui verifie si la salle est libre
	 * @return true si la salle est libre, false sinon
	 */
	public boolean estLibre() {
		if (!estConfirme())
			return true;
		else 
			return false;
	}
	
	/**
	 * Methode qui verifie si la salle est confirme
	 * @return true si la salle est confirme, false sinon
	 */
	public boolean estConfirme() {
		if (!estLibre())
			return true;
		else 
			return false;
	}

	/**
	 * Methode qui retourne le prix d'une plage d'une heure 
	 * @return prixPlage1h
	 */
	public int getPrixPlage1h() {
		return prixPlage1h;
	}

	/**
	 * Mathode qui modifie le prix d'une plage d'une heure 
	 * @param prixPlage1h
	 */
	public void setPrixPlage1h(int prixPlage1h) {
		this.prixPlage1h = prixPlage1h;
	}

	/**
	 * Methode qui retourne le prix d'une plage de deux heures 
	 * @return prixPlage2h
	 */
	public int getPrixPlage2h() {
		return prixPlage2h;
	}

	/**
	 * Mathode qui modifie le prix d'une plage de deux heures 
	 * @param prixPlage2h
	 */
	public void setPrixPlage2h(int prixPlage2h) {
		this.prixPlage2h = prixPlage2h;
	}

}
