package core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import core.common.AbstractCommonService;
import core.common.exception.CommonException;
import core.model.CommonUser;
import core.model.Role;

/**
 * Classe de controle da entidade {@link CommonUser}.<br>
 * Expõe os serviços para a camada de visão.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
@Stateless
public class CommonUserService extends AbstractCommonService<CommonUser> {
	
	/** Default serial UID version. */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected Class<CommonUser> getEntityClass() {
		return CommonUser.class;
	}
	
	/**
	 * Retorna o usuário filtrado pelo nome passado como parâmetro.
	 * @param name Nome do usuário Procurado
	 * @return AtlasUser
	 */
	public CommonUser findByName(String name) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append(" SELECT atlasUser FROM " + CommonUser.class.getName() + " atlasUser ");
			jpaQl.append("	WHERE atlasUser.name = :atlasUserName ");
			
			Query query = this.getEntityManager().createQuery(jpaQl.toString());
			query.setParameter("atlasUserName", name);
			
			CommonUser result = (CommonUser) query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}
	
	/**
	 * Retorna o usuário filtrado pelo login passado como parâmetro.
	 * @param login Login do usuário
	 * @return AtlasUser
	 */
	public CommonUser findByLogin(String login) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append(" SELECT atlasUser FROM " + CommonUser.class.getName() + " atlasUser ");
			jpaQl.append("	WHERE atlasUser.login = :atlasUserLogin ");
			
			Query query = this.getEntityManager().createQuery(jpaQl.toString());
			query.setParameter("atlasUserLogin", login);
			
			CommonUser result = (CommonUser) query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}
	
	/**
	 * Recupera as roles associadas ao usuario.
	 * @param id Identificador do usuario
	 * @return Lista de regras associadas ao usuario
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Role> fetchRolesAssociated(Integer id) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append(" SELECT user FROM ");
			jpaQl.append(getEntityClass().getName());
			jpaQl.append(" user  JOIN FETCH user.roles roles ");
			jpaQl.append(" WHERE user.id = :id ");
			Query query = getEntityManager().createQuery(jpaQl.toString());
			query.setParameter("id", id);
			jpaQl.append(" ORDER BY roles.");
			jpaQl.append(Role.ORDER_FIELD);
			CommonUser user = (CommonUser) query.getSingleResult();
			if (user.getRoles() != null) {
				return user.getRoles();
			}
		} catch (NoResultException e) {
			// Não é necessário nenhuma ação
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
		return new ArrayList<Role>();
	}
	
}
