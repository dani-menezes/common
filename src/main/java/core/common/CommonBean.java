package core.common;

/**
 * Interface com o contrato de um bean.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
public interface CommonBean {
	
	/**
	 * Recupera o identificador da entidade.
	 * @return Identificador
	 */
	public Integer getId();
	
	/**
	 * Atribui o identificador da entidade.
	 * @param id Identificador
	 */
	public void setId(Integer id);
}