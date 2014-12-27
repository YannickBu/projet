package donnee;

import java.util.Date;

public class ForfaitClient {

	private int idForfaitClient;
	private Client client;
	private Forfait forfait;
	private Salle salle;
	private int tempsRestant;
	private Date dateCreation;
	
	/**
	 * 1er constructeur de la classe ForfaitClient
	 */
	public ForfaitClient() {}
	
	/**
	 * 2eme constructeur de la classe ForfaitClient
	 * @param id 
	 * @param c
	 * @param f
	 * @param s
	 * @param tempsRestant
	 * @param dateCreation
	 */
	public ForfaitClient(int id, Client c, Forfait f, Salle s, int tempsRestant, Date dateCreation){
		idForfaitClient = id;
		client = c;
		forfait = f;
		salle = s;
		this.tempsRestant = tempsRestant;
		this.dateCreation = dateCreation;
	}
	
	/**
	 * Methode qui retourne l'identifiant du forfait client
	 * @return idForfaitClient
	 */
	public int getIdForfaitClient() {
		return idForfaitClient;
	}
	
	/**
	 * Methode qui modifie l'identifiant du forfait client
	 * @param idForfaitClient
	 */
	public void setIdForfaitClient(int idForfaitClient) {
		this.idForfaitClient = idForfaitClient;
	}
	
	/**
	 * Methode qui retourne le client du forfait client
	 * @return client
	 */
	public Client getClient() {
		return client;
	}
	
	/**
	 * Methode qui modifie le client du forfait client
	 * @param client
	 */
	public void setClient(Client client) {
		this.client = client;
	}
	
	/**
	 * Methode qui retourne le forfait du forfait client
	 * @return forfait
	 */
	public Forfait getForfait() {
		return forfait;
	}
	
	/**
	 * Methode qui modifie le forfait du forfait client
	 * @param forfait
	 */
	public void setForfait(Forfait forfait) {
		this.forfait = forfait;
	}
	
	/**
	 * Methode qui retourne la salle du forfait client
	 * @return salle
	 */
	public Salle getSalle() {
		return salle;
	}
	
	/**
	 * Methode qui modifie la salle du forfait client
	 * @param salle
	 */
	public void setSalle(Salle salle) {
		this.salle = salle;
	}
	
	/**
	 * Methode qui retourne le temps restant du forfait client
	 * @return tempsRestant
	 */
	public int getTempsRestant() {
		return tempsRestant;
	}
	
	/**
	 * Methode qui modifie le temps restant du forfait client
	 * @param tempsRestant
	 */
	public void setTempsRestant(int tempsRestant) {
		this.tempsRestant = tempsRestant;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	
}
