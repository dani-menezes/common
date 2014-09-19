package core.service;

import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import core.common.AbstractCommonService;
import core.common.exception.CommonException;
import core.model.Rent;

/**
 * Business class for entity {@link Rent}.<br>
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
@Stateless
public class RentService extends AbstractCommonService<Rent> {
	
	/** Default serial UID version. */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected Class<Rent> getEntityClass() {
		return Rent.class;
	}
	
	/**
	 * Return the Rent by address param.
	 * @param address Adrres of the rent
	 * @return {@link Rent}
	 */
	public Rent findByAddress(String address) throws CommonException {
		try {
			StringBuilder jpaQl = new StringBuilder();
			jpaQl.append(" SELECT rent FROM " + Rent.class.getName() + " rent ");
			jpaQl.append("	WHERE rent.adress= :Address ");
			
			Query query = this.getEntityManager().createQuery(jpaQl.toString());
			query.setParameter("address", address);
			
			Rent result = (Rent) query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new CommonException("ERRO.INTEGRIDADE.RELACIONAL.BANCO.DE.DADOS", e);
		}
	}
	
}
