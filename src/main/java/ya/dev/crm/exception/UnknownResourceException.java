package ya.dev.crm.exception;

public class UnknownResourceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 780405970648543635L;

	public UnknownResourceException() {
		super("UnknownResourceException");
	}

	public UnknownResourceException(String message) {
		super(message);
	}

	public UnknownResourceException(Throwable cause) {
		super(cause);
	}
}
