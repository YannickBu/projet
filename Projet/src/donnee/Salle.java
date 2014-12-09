package donnee;

import java.util.ArrayList;

public class Salle {
	
	private ArrayList<Reservation> listeReservation;
	private int idSalle;
	private String typeSalle;
	private int prixPlage1h;
	private int prixPlage2h;
	
	public Salle() {}
	
	public String getTypeSalle() {
		return typeSalle;
	}

	public void setTypeSalle(String typeSalle) {
		this.typeSalle = typeSalle;
	}

	public ArrayList<Reservation> getListeReservation() {
		return listeReservation;
	}

	public void setListeReservation(ArrayList<Reservation> listeReservation) {
		this.listeReservation = listeReservation;
	}
	
	public int getIdSalle() {
		return idSalle;
	}

	public void setIdSalle(int idSalle) {
		this.idSalle = idSalle;
	}

	public boolean estLibre() {
		if (!estConfirme())
			return true;
		else 
			return false;
	}
	
	public boolean estConfirme() {
		if (!estLibre())
			return true;
		else 
			return false;
	}

	public int getPrixPlage1h() {
		return prixPlage1h;
	}

	public void setPrixPlage1h(int prixPlage1h) {
		this.prixPlage1h = prixPlage1h;
	}

	public int getPrixPlage2h() {
		return prixPlage2h;
	}

	public void setPrixPlage2h(int prixPlage2h) {
		this.prixPlage2h = prixPlage2h;
	}

}
