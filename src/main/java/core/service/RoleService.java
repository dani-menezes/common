package core.service;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import core.common.AbstractCommonService;
import core.common.exception.CommonException;
import core.model.Role;

/**
 * Classe de controle da entidade {@link Role}.<br>
 * Expõe os serviços para a camada de visão.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
@Stateless
public class RoleService extends AbstractCommonService<Role> {
	
	/** Default serial UID version. */
	private static final long serialVersionUID = 887436724051386412L;

	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected Class<Role> getEntityClass() {
		return Role.class;
	}
	
	/**
	 * Retorna a {@link Role} que possui o nome passado como parâmetro.
	 * @param name Nome da role.
	 * @return Role
	 */
	public Role findByName(String name) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append(" SELECT role FROM " + Role.class.getName() + " role ");
			jpaQl.append("	WHERE role.name = :name ");
			
			Query query = this.getEntityManager().createQuery(jpaQl.toString());
			query.setParameter("name", name);
			
			Role result = (Role) query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
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
	public List<Role> findAllByUsrRole(Boolean isUsrAdmin, String orderBy) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append("SELECT entity FROM ");
			jpaQl.append(getEntityClass().getName());
			jpaQl.append(" entity ");
			if (!isUsrAdmin) {
				jpaQl.append(" WHERE entity.name <> :name ");
			}
			if(orderBy != null){
				jpaQl.append(" ORDER BY entity.");
				jpaQl.append(orderBy);
			}
			Query query = this.getEntityManager().createQuery(jpaQl.toString());
			if (!isUsrAdmin) {
				query.setParameter("name", Role.ADMIN_ROLE);
			}
			List<Role> list = query.getResultList();
			return list;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}
	
	
}
