package core.common;

import java.util.List;

/**
 * Interface com o contrato das classes de controle.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
public interface CommonView<E extends CommonBean> {
	
	/**
	 * Retorna o service <tt>(Data Access Object)</tt> da entidade.
	 * @return DAO da entidade
	 */
	public CommonService<E> getEntityService();
	
	/**
	 * Realiza as configurações iniciais do ManagedBean, como instâncias e valores de lista.
	 */
	public void init();
	
	/**
	 * Retorna a lista da entidade para listagem.
	 * @return Lista da entidade
	 */
	public List<E> getEntityList();
	
	/**
	 * Atribui a lista de entidades.
	 * @param Lista da entidades
	 */
	public void setEntityList(List<E> entityList);
	
	/**
	 * Retorna a quantidade de entidades cadastradas.
	 * @return Quantidade de entidade cadastradas
	 */
	public Integer getEntityListSize();
	
	/**
	 * Getter da entidade.
	 * @return Entidade
	 */
	public E getEntity();
	
	/**
	 * Setter da entidade.
	 * @param entity Entidade
	 */
	public void setEntity(E entity);
	
	/**
	 * Getter da entidade selecionada (edição/remoção).
	 * @return Entidade
	 */
	public E getEntitySelected();
	
	/**
	 * Setter da entidade selecionada (edição/remoção)
	 * @param entity Entidade
	 */
	public void setEntitySelected(E entity);
	
	/**
	 * Salva ou atualiza o estado da entidade no banco de dados.
	 */
	public void saveOrUpdateEntity();
	
	/**
	 * Remove a entidade do banco de dados.
	 */
	public void deleteEntity();
	
	/**
	 * Atualiza a lista de entidades.
	 * @oderBy campo para ordenação da query
	 */
	public void updateEntityList(String orderBy);
	
	/**
	 * Atualiza a entidade com os valores da entidade selecionada para edição dos dados.
	 */
	public void editEntity();
	
	/**
	 * Limpa o valor da entidade.
	 */
	public void clearEntity();

	
}