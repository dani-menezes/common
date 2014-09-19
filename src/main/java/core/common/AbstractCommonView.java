package core.common;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.context.RequestContext;

import core.common.exception.CommonException;
import core.internationalization.CommonTranslator;

/**
 * Implements the commons methods by view layer.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 * @param <E> Entity related
 */
public abstract class AbstractCommonView<E extends CommonBean> implements CommonView<E>, Converter {
	
	/** Instance Logger. */
	protected final Logger logger = LogManager.getLogManager().getLogger(AbstractCommonView.class.getName());

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public void saveOrUpdateEntity() {
		try {
			this.getEntityService().saveOrUpdate(this.getEntity());
			this.clearEntity();
			this.addMessage(CommonTranslator.getTranslate("MESSAGE_SUCCESS_SAVE"));
			RequestContext rc = RequestContext.getCurrentInstance();
		    rc.execute("PF('entityCreateWindow').hide()");
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			this.addErrorMessage(CommonTranslator.getTranslate("MESSAGE_ERROR_SAVE"), e);
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public void deleteEntity() {
		try {
			if (this.getEntitySelected() != null) {
				this.getEntityService().delete(this.getEntitySelected());
				this.addMessage(CommonTranslator.getTranslate("MESSAGE_SUCCESS_DELETE"));
			} else {
				this.addMessage(CommonTranslator.getTranslate("MESSAGE_INFO_EMPTY_ENTITY"));
			}
		} catch (CommonException e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			this.addErrorMessage(CommonTranslator.getTranslate("MESSAGE_ERROR_ENTITY_HAS_RELATIONSHIPS"), e);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			this.addErrorMessage(CommonTranslator.getTranslate("MESSAGE_ERROR_DELETE"), e);
		}
	}
	
	/**
	 * Atualiza a lista de entidades buscando todos os registros do banco de dados ordenando pelo campo passado como parâmetro.
	 * @param orderBy Campo a ser ordenado
	 */
	public void updateEntityList(String orderBy) {
		try {
			this.setEntityList(this.getEntityService().findAll(orderBy));
		} catch (CommonException e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			this.addErrorMessage(CommonTranslator.getTranslate("MESSAGE_ERROR_LOAD_ENTITIES"), e);
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			this.addErrorMessage(CommonTranslator.getTranslate("MESSAGE_INTERNAL_ERROR"), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void editEntity() {
		if (this.getEntity() == null) {
			this.addErrorMessage(CommonTranslator.getTranslate("MESSAGE_ERROR_LOAD_ENTITY"), null);
		}
	}
	
	/**
	 * Adiciona uma mensagem para no contexto do faces para feedback ao usuário.
	 * @param message Mensagem
	 */
	protected void addMessage(String message) {
		this.addMessage(null, message);
	}
	
	/**
	 * Adiciona uma mensagem de erro para no contexto do faces para feedback ao usuário.
	 * @param message Mensagem
	 */
	protected void addErrorMessage(String summary, Throwable exception) {
		this.addErrorMessage(null, summary, exception);
	}
	
	/**
	 * Adiciona uma mensagem para no contexto do faces para feedback ao usuário.
	 * @param clientId Identificador do componente a ser exibida a mensagem
	 * @param message Mensagem
	 */
	protected void addMessage(String clientId, String message) {
		FacesMessage msg = new FacesMessage(message);
		FacesContext.getCurrentInstance().addMessage(clientId, msg);
	}
	
	/**
	 * Adiciona uma mensagem de erro para no contexto do faces para feedback ao usuário.
	 * @param clientId Identificador do compomente a ser exibida a mensagem
	 * @param summary Assunto da mensagem
	 * @param exception Exceção lançada
	 */
	protected void addErrorMessage(String clientId, String summary, Throwable exception) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, exception != null ? exception.getMessage() : null);
		FacesContext.getCurrentInstance().addMessage(clientId, msg);
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public Integer getEntityListSize() {
		return this.getEntityList().size();
	}
	
	/**
	 * {@inheritDoc}.
	 * @see javax.faces.convert.Converter
	 */
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		final String nullable = "null";
		try {
	        if(value != null && value.trim().length() > 0 && !value.equals(nullable)) {
	            return this.getEntityService().findById(Integer.valueOf(value));
	        }
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			this.addErrorMessage(CommonTranslator.getTranslate("MESSAGE_ERROR_ON_CONVERT_ENTITY"), e);
		}
		return null;
    }
 
	/**
	 * {@inheritDoc}.
	 * @see javax.faces.convert.Converter
	 */
	@Override
    @SuppressWarnings("unchecked")
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
    	try {
	        if(object != null) {
	            return String.valueOf(((E) object).getId());
	        }
    	} catch (Exception e) {
    		this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
    		this.addErrorMessage(CommonTranslator.getTranslate("MESSAGE_ERROR_ON_CONVERT_ENTITY"), e);
    	}
    	return null;
    }
	
}
