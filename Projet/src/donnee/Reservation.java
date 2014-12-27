package donnee;

import java.util.Date;

public class Reservation {
	
	public final static int ETAT_HORS_DELAIS = 1;
	public final static int ETAT_CONFIRME = 2;
	public final static int ETAT_NON_CONFIRME = 3;
	
	private int idReserv;
	private Client client;
	private Salle salle;
	private Date date;
	private Date dateCreation;
	private int plage;
	private boolean estPaye;
	
	/**
	 * Constructeur de la classe Reservation
	 */
	public Reservation() {}

	/**
	 * Methode qui retourne l'identifiant de la reservation
	 * @return id
	 */
	public int getIdReserv() {
		return idReserv;
	}

	/** 
	 * Methode qui modifie l'identifiant de la reservation
	 * @param idReserv
	 */
	public void setIdReserv(int idReserv) {
		this.idReserv = idReserv;
	}

	/**
	 * Methode qui retourne si la reservation est payee
	 * @return true si la reservation est payee, false sinon
	 */
	public boolean getEstPaye() {
		return estPaye;
	}

	/** 
	 * Methode qui modifie l'etat de la reservation true si elle est payee, false sinon
	 * @param estPaye
	 */
	public void setEstPaye(boolean estPaye) {
		this.estPaye = estPaye;
	}

	/**
	 * Methode qui retourne le client de la reservation
	 * @return client
	 */
	public Client getClient() {
		return client;
	}

	/** 
	 * Methode qui modifie le client de la reservation
	 * @param client
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Methode qui retourne la salle de la reservation
	 * @return salle
	 */
	public Salle getSalle() {
		return salle;
	}

	/** 
	 * Methode qui modifie la salle de la reservation
	 * @param salle
	 */
	public void setSalle(Salle salle) {
		this.salle = salle;
	}

	/**
	 * Methode qui retourne la date de la reservation
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/** 
	 * Methode qui modifie la date de la reservation
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Methode qui retourne la date de creation de la reservation
	 * @return date creation
	 */
	public Date getDateCreation() {
		return dateCreation;
	}

	/** 
	 * Methode qui modifie la date de creation de la reservation
	 * @param dateCreation
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * Methode qui retourne la plage de la reservation
	 * @return plage
	 */
	public int getPlage() {
		return plage;
	}

	/** 
	 * Methode qui modifie la plage de la reservation
	 * @param plage
	 */
	public void setPlage(int plage) {
		this.plage = plage;
	}
	

}
