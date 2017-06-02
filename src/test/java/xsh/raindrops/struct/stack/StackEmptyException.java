package xsh.raindrops.struct.stack;

@SuppressWarnings("serial")
public class StackEmptyException extends RuntimeException{

	public StackEmptyException() {
		super();
	}

	public StackEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StackEmptyException(String message, Throwable cause) {
		super(message, cause);
	}

	public StackEmptyException(String message) {
		super(message);
	}

	public StackEmptyException(Throwable cause) {
		super(cause);
	}
	
}
