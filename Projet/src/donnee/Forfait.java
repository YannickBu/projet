package donnee;

public class Forfait {

	protected int validite; 
	protected String typeForfait;
	protected int prixPetitesSalles;
	protected int prixGrandesSalles;
	
	/**
	 * Constructeur de la classe Forfait
	 */
	public Forfait() {}
	
	/**
	 * Methode qui retourne le type de forfait
	 * @return typeForfait
	 */
	public String getTypeForfait() {
		return typeForfait;
	}
	
	/**
	 * Methode qui modifie le type de forfait
	 * @param typeForfait
	 */
	public void setTypeForfait(String typeForfait) {
		this.typeForfait = typeForfait;
	}
	
	/**
	 * Methode qui retourne la validite d'un forfait
	 * @return validite
	 */
	public int getValidite() {
		return validite;
	}
	
	/**
	 * Methode qui modifie la validite
	 * @param validite
	 */
	public void setValidite(int validite) {
		this.validite = validite;
	}
	
	/**
	 * Methode qui retourne le prix de petite salle
	 * @return prixPetitesSalles
	 */
	public int getPrixPetitesSalles() {
		return prixPetitesSalles;
	}
	
	/**
	 * Methode qui modifie le prix de petite salle
	 * @param prixPetitesSalles
	 */
	public void setPrixPetitesSalles(int prixPetitesSalles) {
		this.prixPetitesSalles = prixPetitesSalles;
	}
	
	/**
	 * Methode qui retourne le prix de grande salle
	 * @return prixGrandesSalles
	 */
	public int getPrixGrandesSalles() {
		return prixGrandesSalles;
	}
	
	/**
	 * Methode qui modifie le prix de grande salle
	 * @param prixGrandesSalles
	 */
	public void setPrixGrandesSalles(int prixGrandesSalles) {
		this.prixGrandesSalles = prixGrandesSalles;
	}
	
	
}
