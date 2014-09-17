package core.model;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import core.common.CommonBean;

/**
 * The persistent class for the COMMON_USER database table.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
@Entity
@Table(name="COMMON_USER")
public class CommonUser implements CommonBean, Serializable {
	
	/** Generated UID version. */
	private static final long serialVersionUID = -8963782919668644769L;

	/** Campo para ordenação. */
	public static final String ORDER_FIELD = "login";
	
	private Integer id;
	
	private String login;
	
	private String password;
	
	private String name;
	
	private String locale;
	
	@ElementCollection (fetch=FetchType.EAGER)
	private List<Role> roles;
	
	private String origin;
	
	/**
	 * @return identification
	 */
	@Id
	@SequenceGenerator(name="COMMON_USER_ID_GENERATOR", sequenceName="COMMON_USER_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMON_USER_ID_GENERATOR")
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
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/**
	 * @return the roles
	 */
	@ManyToMany (fetch=FetchType.EAGER)
	@JoinTable(
		name = "COMMON_USER_ROLE", 
		joinColumns = {@JoinColumn(name = "COMMON_USER_ID", referencedColumnName = "ID")}, 
		inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")}
	)
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	/**
	 * Verifica se o usuário possui a Regra de Administrador.
	 * @return 	<tt>TRUE </tt> em caso afirmativo e <br>
	 * 			<tt>FALSE </tt> caso contrário.
	 */
	@Transient
	public boolean isUserAdmin() {
		if (this.getRoles() != null) {
			for (Role role : this.getRoles()) {
				if (Role.ADMIN_ROLE.equals(role.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
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
		CommonUser other = (CommonUser) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (locale == null) {
			if (other.locale != null) {
				return false;
			}
		} else if (!locale.equals(other.locale)) {
			return false;
		}
		if (login == null) {
			if (other.login != null) {
				return false;
			}
		} else if (!login.equals(other.login)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		return true;
	}
	
}