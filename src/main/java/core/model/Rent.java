package core.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import core.common.CommonBean;

/**
 * The persistent class for the RENT database table.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
@Entity
@Table(name="RENT")
public class Rent implements CommonBean, Serializable {
	
	/** Generated UID version. */
	private static final long serialVersionUID = -8963782919668644769L;

	/** Campo para ordenação. */
	public static final String ORDER_FIELD = "description";
	
	private Integer id;
	
	private String description;
	
	private String address;
	
	private Double value;
	
	/**
	 * @return identification
	 */
	@Id
	@SequenceGenerator(name="RENT_ID_GENERATOR", sequenceName="RENT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RENT_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getId() {
		return this.id;
	}
	
	/**
	 * @param The identification
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Getter da propriedade description
	 * @return description
	 */
	@Column(name="DESCRIPTION", nullable=true)
	public String getDescription() {
		return description;
	}

	/**
	 * Setter da propriedade description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter da propriedade address
	 * @return address
	 */
	@Column(name="ADDRESS", nullable=false)
	public String getAddress() {
		return address;
	}

	/**
	 * Setter da propriedade address
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Getter da propriedade value
	 * @return value
	 */
	@Column(name="VALUE", nullable=false)
	public Double getValue() {
		return value;
	}

	/**
	 * Setter da propriedade value
	 * @param value
	 */
	public void setValue(Double value) {
		this.value = value;
	}

}