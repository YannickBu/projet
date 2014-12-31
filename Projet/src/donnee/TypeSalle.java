package donnee;

public class TypeSalle {

	private int idTypeSalle;
	private String typeSalle;
	
	/**
	 * Constructeur de la classe
	 * @param id
	 * @param typeSalle
	 */
	public TypeSalle(int id, String typeSalle) {
		this.idTypeSalle = id;
		this.typeSalle = typeSalle;
	}

	/**
	 * Methode qui permet de retourner l'identifiant du type de la salle
	 * @return idTypeSalle
	 */
	public int getIdTypeSalle() {
		return idTypeSalle;
	}

	/**
	 * Methode qui permet de modifier l'identifiant du type de la salle
	 * @param idTypeSalle
	 */
	public void setIdTypeSalle(int idTypeSalle) {
		this.idTypeSalle = idTypeSalle;
	}

	/**
	 * Methode qui permet de retourner le type de la salle
	 * @return typeSalle
	 */
	public String getTypeSalle() {
		return typeSalle;
	}

	/**
	 * Methode qui permet de modifier le type de la salle
	 * @param typeSalle
	 */
	public void setTypeSalle(String typeSalle) {
		this.typeSalle = typeSalle;
	}

	
}
