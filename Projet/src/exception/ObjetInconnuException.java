package exception;

public class ObjetInconnuException extends RuntimeException{
	

	private static final long serialVersionUID = 1L;

	public ObjetInconnuException(String objet, String nom){
		super("l'"+objet+" avec le nom "+nom+" est inconnu");
	}

}
