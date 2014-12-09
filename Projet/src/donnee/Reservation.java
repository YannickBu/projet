package donnee;

import java.util.Date;

public class Reservation {
	
	private int idReserv;
	private Client client;
	private Salle salle;
	private Date date;
	private Date dateCreation;
	private int plage;
	private boolean estPaye;
	
	public Reservation() {}

	public int getIdReserv() {
		return idReserv;
	}

	public void setIdReserv(int idReserv) {
		this.idReserv = idReserv;
	}

	public boolean getEstPaye() {
		return estPaye;
	}

	public void setEstPaye(boolean estPaye) {
		this.estPaye = estPaye;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Salle getSalle() {
		return salle;
	}

	public void setSalle(Salle salle) {
		this.salle = salle;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public int getPlage() {
		return plage;
	}

	public void setPlage(int plage) {
		this.plage = plage;
	}
	

}
