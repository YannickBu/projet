package donnee;

public class Forfait {

	protected int validite; 
	protected String typeForfait;
	protected int prixPetitesSalles;
	protected int prixGrandesSalles;
	
	public Forfait() {}
	
	public String getTypeForfait() {
		return typeForfait;
	}
	
	public void setTypeForfait(String typeForfait) {
		this.typeForfait = typeForfait;
	}
	
	public int getValidite() {
		return validite;
	}
	
	public void setValidite(int validite) {
		this.validite = validite;
	}
	
	public int getPrixPetitesSalles() {
		return prixPetitesSalles;
	}
	
	public void setPrixPetitesSalles(int prixPetitesSalles) {
		this.prixPetitesSalles = prixPetitesSalles;
	}
	
	public int getPrixGrandesSalles() {
		return prixGrandesSalles;
	}
	
	public void setPrixGrandesSalles(int prixGrandesSalles) {
		this.prixGrandesSalles = prixGrandesSalles;
	}
	
	
}
