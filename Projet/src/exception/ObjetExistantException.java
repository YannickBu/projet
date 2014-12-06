package exception;

public class ObjetExistantException extends RuntimeException{


	private static final long serialVersionUID = 1L;

	public ObjetExistantException(String objet, String nom){
		super("l'"+objet+" avec le nom "+nom+" est existant");
	}
}
