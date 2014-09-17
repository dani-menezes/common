package core.common.exception;

/**
 * Exceção generica do CoffeyAtlas.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
public class CommonException extends Exception {

	/** Serial UID version. */
	private static final long serialVersionUID = 2126695576475764800L;
	
	private String translateKey;
	
	private Throwable throwable;

	public CommonException(){}
	
	public CommonException (String message, String translateKey) {
		this.translateKey = translateKey;
	}
	
	public CommonException (Throwable throwable) {
		this.throwable = throwable;
	}
	
	public CommonException (String translateKey, Throwable throwable) {
		this.translateKey = translateKey;
		this.throwable = throwable;
	}

	/**
	 * Getter da chave de tradução.
	 * @return the translateKey
	 */
	public String getTranslateKey() {
		return translateKey;
	}

	/**
	 * Setter da chave de tradução.
	 * @param translateKey the translateKey to set
	 */
	public void setTranslateKey(String translateKey) {
		this.translateKey = translateKey;
	}

	/**
	 * Getter do lançador da exceção.
	 * @return the throwable
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * Setter do lançador da exceção.
	 * @param throwable the throwable to set
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
}
