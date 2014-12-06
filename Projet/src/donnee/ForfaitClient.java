package donnee;

public class ForfaitClient {

	private int idForfaitClient;
	private Client client;
	private Forfait forfait;
	private Salle salle;
	private int tempsRestant;
	
	public ForfaitClient() {}
	public ForfaitClient(int id, Client c, Forfait f, Salle s, int tempsRestant){
		idForfaitClient = id;
		client = c;
		forfait = f;
		salle = s;
		this.tempsRestant = tempsRestant;
	}
	
	
	public int getIdForfaitClient() {
		return idForfaitClient;
	}
	public void setIdForfaitClient(int idForfaitClient) {
		this.idForfaitClient = idForfaitClient;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Forfait getForfait() {
		return forfait;
	}
	public void setForfait(Forfait forfait) {
		this.forfait = forfait;
	}
	public Salle getSalle() {
		return salle;
	}
	public void setSalle(Salle salle) {
		this.salle = salle;
	}
	public int getTempsRestant() {
		return tempsRestant;
	}
	public void setTempsRestant(int tempsRestant) {
		this.tempsRestant = tempsRestant;
	}

	
}
