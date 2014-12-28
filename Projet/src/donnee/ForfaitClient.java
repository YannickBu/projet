package donnee;

import java.util.Date;

public class ForfaitClient {

	private int idForfaitClient;
	private Client client;
	private Forfait forfait;
	private TypeSalle typeSalle;
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
	public ForfaitClient(int id, Client c, Forfait f, TypeSalle s, int tempsRestant, Date dateCreation){
		idForfaitClient = id;
		client = c;
		forfait = f;
		typeSalle = s;
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
	 * Methode qui retourne le type de salle du forfait client
	 * @return typeSalle
	 */
	public TypeSalle getTypeSalle() {
		return typeSalle;
	}
	
	/**
	 * Methode qui modifie le type de salle du forfait client
	 * @param typeSalle
	 */
	public void setTypeSalle(TypeSalle typeSalle) {
		this.typeSalle = typeSalle;
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

	/**
	 * Methode qui retourne la date de creation du forfait client
	 * @return tempsRestant
	 */
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * Methode qui modifie la date de creation du forfait client
	 * @param tempsRestant
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	
}
