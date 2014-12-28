package donnee;

public class TypeSalle {

	private int idTypeSalle;
	private String typeSalle;
	
	public TypeSalle(int id, String typeSalle) {
		this.idTypeSalle = id;
		this.typeSalle = typeSalle;
	}

	public int getIdTypeSalle() {
		return idTypeSalle;
	}

	public void setIdTypeSalle(int idTypeSalle) {
		this.idTypeSalle = idTypeSalle;
	}

	public String getTypeSalle() {
		return typeSalle;
	}

	public void setTypeSalle(String typeSalle) {
		this.typeSalle = typeSalle;
	}

	
}
