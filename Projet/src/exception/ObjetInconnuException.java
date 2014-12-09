package exception;

public class ObjetInconnuException extends RuntimeException{
	

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de l'exception
	 * @param objet
	 * @param nom
	 */
	public ObjetInconnuException(String objet, String nom){
		super("l'"+objet+" avec le nom "+nom+" est inconnu");
	}

}
