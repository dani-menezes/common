package core.common;

import java.util.List;

import core.common.exception.CommonException;

/**
 * Interface responsavel por definir as operações de comunicação com o banco de dados.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 * @param <E> Entidade
 */
public interface CommonService<E extends CommonBean> {

	/**
	 * Remove a entidade da base de dados.
	 * @param E Entidade a ser removida
	 * @throws CommonException Em caso de exceção.
	 */
	public void delete(E entity) throws CommonException;

	/**
	 * Remove um conjunto de entidades pela lista de identificadores.
	 * @param listId Lista de identificadores
	 * @throws CommonException Em caso de exceção.
	 */
	public void deleteByIds(List<Integer> listId) throws CommonException;

	/**
	 * Recupera todas as entidades.
	 * @return Lista de entidades
	 * @throws CommonException Em caso de exceção.
	 */
	public List<E> findAll() throws CommonException;

	/**
	 * Recupera todas as entidades com ordenação.
	 * @param orderBy Cláusula order by para ser adicionada à query.
	 * @return Lista de entidades
	 * @throws CommonException Em caso de exceção.
	 */
	public List<E> findAll(String orderBy) throws CommonException;

	/**
	 * Recupera uma entidade pelo identificador
	 * @param id Identificador
	 * @return Entidade
	 * @throws CommonException Em caso de exceção.
	 */
	public E findById(Integer id) throws CommonException;

	/**
	 * Recupera uma lista de entidades pelos identificadores
	 * @param listId Lista de identificadores
	 * @return Lista de entidades
	 * @throws CommonException Em caso de exceção.
	 */
	public List<E> findByIds(List<Integer> listId) throws CommonException;

	/**
	 * Recupera todas as entidades pelos identificadores com ordenação.
	 * @param campo Campo a ser ordenado
	 * @param tipoOrdenacao <tt>ASC / DESC </tt>
	 * @return Lista de entidades
	 * @throws CommonException Em caso de exceção.
	 */
	public List<E> findAllOrderBy(String campo, String tipoOrdenacao) throws CommonException;

	/**
	 * Persiste uma lista entidades.
	 * @param objList Lista de entidades a ser persistida
	 * @throws CommonException Em caso de exceção.
	 */
	public void saveOrUpdateAll(List<E> objList) throws CommonException;
	
	/**
	 * Persiste uma entidade.
	 * @param entity Entidade a ser persistida
	 * @return Objeto persistido em banco
	 * @throws CommonException Em caso de exceção.
	 */
	public E saveOrUpdate (E entity) throws CommonException;

}