package core.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import core.common.AbstractCommonView;
import core.common.CommonService;
import core.internationalization.CommonTranslator;
import core.model.Rent;
import core.service.RentService;

@RequestScoped
@ManagedBean (name="rentView")
public class RentView extends AbstractCommonView<Rent> {

	/** DAO de {@link Plugin}. */
	@EJB
	private RentService entityService;

	/** List of all entities. */
	private List<Rent> entityList;

	/** Entity selected for update or select. */
	private Rent entitySelected;
	
	/** Entity for persist data. */
	private Rent entity;

	/**
	 * Initialize the view state
	 */
	@PostConstruct
	public void init() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		this.entity = new Rent();
		this.entityList = new ArrayList<Rent>();
		this.updateEntityList(Rent.ORDER_FIELD);
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public CommonService<Rent> getEntityService() {
		return this.entityService;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public List<Rent> getEntityList() {
		return this.entityList;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public void setEntityList(List<Rent> entityList) {
		this.entityList = entityList;
	}
	

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public Rent getEntity() {
		return this.entity;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public void setEntity(Rent entity) {
		this.entity = entity;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public Rent getEntitySelected() {
		return this.entitySelected;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public void setEntitySelected(Rent entity) {
		this.entitySelected = entity;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	public void clearEntity() {
		this.entity = null;
		this.entitySelected = null;
	}
	
	/**
	 * Recalculate the value rental incressing 10% of original value
	 */
	public void renovateValue() {
		if (this.getEntity() == null) {
			this.addErrorMessage(CommonTranslator.getTranslate("MESSAGE_ERROR_LOAD_ENTITY"), null);
		}
		Rent rent = this.getEntity();
		rent.setValue(this.increaseTenPercentInValue(rent.getValue()));
		this.setEntity(rent);
		this.saveOrUpdateEntity();
		this.addMessage(CommonTranslator.getTranslate("MESSAGE_ERROR_LOAD_ENTITY"), null);
		
	}
	
	/**
	 * Increase ten percent in rental value
	 * @param value
	 * @return value + 10% of it value
	 */
	public Double increaseTenPercentInValue(Double value) {
		Double result = value * 1.1;
		long rounded = Math.round(result*100);
	    return rounded/100.0;
	}
	
	
	/* (non-Javadoc)
	 * @see core.common.AbstractCommonView#saveOrUpdateEntity()
	 */
	@Override
	public void saveOrUpdateEntity() {
		super.saveOrUpdateEntity();
		super.updateEntityList(Rent.ORDER_FIELD);
	}

	/* (non-Javadoc)
	 * @see core.common.AbstractCommonView#deleteEntity()
	 */
	@Override
	public void deleteEntity() {
		super.deleteEntity();
		super.updateEntityList(Rent.ORDER_FIELD);
	}
	
}
