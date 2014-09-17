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
 * The persistent class for the ROLE database table.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
@Entity
@Table(name="ROLE")
public class Role implements CommonBean, Serializable {
	
	/** Generated UID version. */
	private static final long serialVersionUID = 7827667538020171161L;
	
	/** Role que representa o administrador. */
	public static final String ADMIN_ROLE = "ADMIN";
	
	/** Role que representa o usuário. */
	public static final String USER_ROLE = "USER";

	/** Campo para ordenação. */
	public static final String ORDER_FIELD = "name";
	
	private Integer id;
	
	private String name;
	
	/**
	 * @return identification
	 */
	@Id
	@SequenceGenerator(name="ROLE_ID_GENERATOR", sequenceName="ROLE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_ID_GENERATOR")
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Impressão padrão de uma Role.
	 * @return String com as informações da Role
	 */
	@Override
	public String toString() {
		return String.format("Role: [%s][%s]", this.getId(), this.getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
}