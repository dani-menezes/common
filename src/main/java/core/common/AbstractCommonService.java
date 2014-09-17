package core.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import core.common.exception.CommonException;

/**
 * Responsável por provir os métodos comuns a todos os Services(DAOs) <tt>(Data access objects)</tt>.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 * @param <E> Entidade relacionada
 */
public abstract class AbstractCommonService<E extends CommonBean> implements CommonService<E>, Serializable  {
	
	/** Logger da instância. */
	protected final Logger logger = LogManager.getLogManager().getLogger(AbstractCommonService.class.getName());

	/** Default UID serial version. */
	private static final long serialVersionUID = 1L;
	
	/** {@link EntityManager} para acesso ao banco de dados. */
	@PersistenceContext(unitName="common", type=PersistenceContextType.TRANSACTION )
	private EntityManager entityManager;

	/**
	 * Recupera a classe da entidade.
	 * @return Class
	 */
	protected abstract Class<E> getEntityClass();

	/**
	 * Recupera a entityManager.
	 * @return {@link EntityManager}
	 */
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Remove a entidade do banco de dados.
	 * @param entity Entidade a ser removida
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delete(E entity) throws CommonException {
		try {
			getEntityManager().remove(this.getEntityManager().getReference(entity.getClass(), entity.getId()));
			getEntityManager().flush();
		} catch (PersistenceException ex) {
			this.logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw new CommonException(ex);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteAll() throws CommonException{
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append(" DELETE ");
			jpaQl.append(getEntityClass().getName());
			jpaQl.append(" entity ");

			this.getEntityManager().createQuery(jpaQl.toString()).executeUpdate();
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}

	}

	/**
	 * {@inheritDoc}.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteById(Number id) throws CommonException {
		E obj = getEntityManager().find(getEntityClass(), id);
		if (obj!= null){
			delete(obj);
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteByIds(List<Integer> listId) throws CommonException {
		if (listId!= null && listId.size()>0){
			for (Integer id : listId) {
				deleteById(id);
			}
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<E> findByIds(List<Integer> listId) throws CommonException {
		if (listId!= null && listId.size()>0){
			try {
				StringBuilder jpaQl = new StringBuilder();
				jpaQl.append(" SELECT entity FROM ");
				jpaQl.append(getEntityClass().getName());
				jpaQl.append(" entity WHERE entity.id in (:listId) ");
				Query query = this.getEntityManager().createQuery(jpaQl.toString());
				query.setParameter("listId", listId);
				List<E> list = query.getResultList();
				return list;
			} catch (Exception e) {
				this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
			}
		}
		return new ArrayList<E>();
	}

	/**
	 * {@inheritDoc}.
	 */
	public List<E> findAll() throws CommonException {
		return findAll(null);
	}

	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<E> findAll(String orderBy) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append("SELECT entity FROM ");
			jpaQl.append(getEntityClass().getName());
			jpaQl.append(" entity ");
			if(orderBy != null){
				jpaQl.append(" ORDER BY entity.");
				jpaQl.append(orderBy);
			}
			Query query = this.getEntityManager().createQuery(jpaQl.toString());
			List<E> list = query.getResultList();
			return list;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public E findById(Integer id) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append("SELECT entity ");
			jpaQl.append("FROM ");
			jpaQl.append(getEntityClass().getName());
			jpaQl.append(" entity WHERE entity.id = :id");
			Query query = getEntityManager().createQuery(jpaQl.toString());
			query.setParameter("id", id);	
			return (E) query.getSingleResult();
		} catch (NoResultException e) {
			// Não é necessário nenhuma ação
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<E> findAllOrderBy(String campo, String tipoOrdenacao) throws CommonException {
		try {
			StringBuilder hql = new StringBuilder("from ");
			hql.append(getEntityClass().getName());
			hql.append(" ORDER BY ");
			hql.append(campo);
			hql.append(" ");
			hql.append(tipoOrdenacao);
	
			return getEntityManager().createQuery(hql.toString()).getResultList();
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer countBy(String property , Object value) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append("FROM ");
			jpaQl.append(getEntityClass().getName());
			jpaQl.append(" entity WHERE entity.");
			jpaQl.append(property);
			jpaQl.append(" = ?");
			Integer count = 0;
			List resultado = getEntityManager().createQuery(jpaQl.toString()).setParameter(1, property).getResultList();
			if (resultado!= null && resultado.size()>0) {
				count = Integer.parseInt(resultado.get(0).toString());
			}
			return count;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer countByList(Map<String,Object> properties) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append("FROM ");
			jpaQl.append(getEntityClass().getName());
			jpaQl.append(" WHERE 1=1 ");
			Iterator it = properties.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry map = (Map.Entry)it.next();
		        jpaQl.append(" AND ");
		        jpaQl.append(map.getKey() + " = " + map.getValue());
		    }
			Integer count = 0;
			List resultado = getEntityManager().createQuery(jpaQl.toString()).getResultList();
			if ( resultado!= null && resultado.size() > 0 ) {
				count = Integer.parseInt(resultado.get(0).toString());
			}
			return count;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveOrUpdateAll(List<E> objList) throws CommonException {
		try {
			EntityManager em = getEntityManager();
			for (E entity : objList) {
				em.merge(entity);
			}
			this.getEntityManager().flush();
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			this.getEntityManager().getTransaction().rollback();
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public E saveOrUpdate(E entity) throws CommonException {
		E newObj = null;
		try {
			newObj = this.getEntityManager().merge(entity);
			this.getEntityManager().flush();
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
		return newObj;
	}
	
}

