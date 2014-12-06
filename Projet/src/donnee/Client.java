package donnee;

import java.util.ArrayList;

public class Client {

	private int id;
	private String nom;
	private String prenom;
	private String numTel;
	private int pointsFidelite;
	private ArrayList<Forfait> listForfait;
	private ArrayList<Reservation> listReservation;

	public Client(){}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public ArrayList<Forfait> getListForfait() {
		return listForfait;
	}

	public void setListForfait(ArrayList<Forfait> listForfait) {
		this.listForfait = listForfait;
	}

	public ArrayList<Reservation> getListReservation() {
		return listReservation;
	}

	public void setListReservation(ArrayList<Reservation> listReservation) {
		this.listReservation = listReservation;
	}

	public String getNumTel() {
		return numTel;
	}

	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}

	public int getPointsFidelite() {
		return pointsFidelite;
	}

	public void setPointsFidelite(int pointsFidelite) {
		this.pointsFidelite = pointsFidelite;
	}

	public void ajoutPointFidelite (int h) {
		this.pointsFidelite = this.pointsFidelite + (5*h);
	}
	
	public void ajoutForfait (Forfait f) {
		this.listForfait.add(f);
	}
	
	public void supprimerForfait (Forfait f){
		this.listForfait.remove(f);
	}
	
	public void ajoutReservation (Reservation r) {
		this.listReservation.add(r);
	}
	
	public void supprimerReservation (Reservation r){
		this.listReservation.remove(r);
	}
	
	
}
