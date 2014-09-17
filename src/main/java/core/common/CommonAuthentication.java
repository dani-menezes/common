package core.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.common.exception.CommonException;
import core.internationalization.CommonTranslator;
import core.model.CommonUser;
import core.model.Role;
import core.service.CommonUserService;
import core.service.RoleService;

/**
 * Define os métodos e propriedades para autenticação.
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
@SessionScoped
@ManagedBean (name="commonAuthentication")
public class CommonAuthentication {

	/** URL da página de login . */
	public static final String CONTEXT_URL = "/common/common/managerContextTab.xhtml";
	
	/** Password default LDAP . */
	public static final String LDAP_DEFAULT_PASSWORD = "*&Ldap%#";
	
	/** Flag que representa um usuário autenticado pelo LDAP. */
	public static final String LDAP_FLAG = "L";
	
	/** Identifica se houve erro no login. */
	private static Boolean LOGIN_ERROR = false;
	
	/** Logger da instância. */
	private final Logger logger = LogManager.getLogManager().getLogger(CommonAuthentication.class.getName());
	
	/** Classe de serviços dos usuários. */
	@EJB
	private CommonUserService atlasUserService;
	
	/** Classe de serviços das roles. */
	@EJB
	private RoleService roleService;
	
	/** Usuário da sessão.*/
	public CommonUser user = null;
	/** Login do usuário. */
	private String login;
	/** Password do usuário. */
	private String password;
	
	/**
	 * Verifica o acesso e atribui o usuário.
	 * @return Página de redirecionamento
	 * @throws ServletException 
	 */
	public String doLogin() {
		try {
			HttpServletRequest request = this.getRequest();
			request.login(this.login, this.password);
			this.user = this.getUser();
			if (this.user != null && CommonTranslator.LOCALE != null) {
				// necessario pra criar a instancia da sessao
				CommonTranslator.setLocale(this.user.getLocale());
				this.logger.info(String.format("Logging user %s [%s] [%s] [%s] [%s]", this.user.getName(), this.user.getLogin(), this.user.getLocale(), this.user.getOrigin(), this.user.getRoles()));
				CommonAuthentication.LOGIN_ERROR = false;
				return "managerContextTab.xhtml";
			}
		} catch (ServletException e) {
			CommonAuthentication.LOGIN_ERROR = true;
		} catch (Exception e) {
			this.addErrorMessage(e);
		}
		return null;
	}

	/**
	 * Recupera o usuário da sessão.
	 * @return {@link CommonUser}
	 */
	public CommonUser getUser() {
		try {
			ExternalContext context = this.getContext();
			Object principal = context.getUserPrincipal();
			List<Role> roles = new ArrayList<Role>();
			this.user = atlasUserService.findByLogin(principal.toString());
			if (this.user != null) {
				roles = atlasUserService.fetchRolesAssociated(this.user.getId());
				this.user.setRoles(roles);
			} else {
				CommonUser ldapUser = new CommonUser();
				ldapUser.setName(principal.toString());
				ldapUser.setLogin(principal.toString());
				ldapUser.setPassword(CommonAuthentication.LDAP_DEFAULT_PASSWORD);
				ldapUser.setLocale(CommonTranslator.PT_LANGUANGE);
				roles.add(roleService.findByName(Role.USER_ROLE));
				ldapUser.setRoles(roles);
				ldapUser.setOrigin(CommonAuthentication.LDAP_FLAG);
				atlasUserService.saveOrUpdate(ldapUser);
				this.user = ldapUser;
			}
		} catch (CommonException e) {
			this.addErrorMessage(e);
		} catch (Exception e) {
			this.addErrorMessage(e);
		}
        return this.user;
    }
 
    /**
     * Termina a sessão do usuário.
     */
    public void logOut(){
    	this.user = null;
    	this.login = null;
    	this.password = null;
        this.getRequest().getSession().invalidate();
    }
 
    /**
     * Retorna o request
     * @return Request
     */
    public HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    /**
     * Retorna o response
     * @return response
     */
    public HttpServletResponse getResponse() {
    	return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }
    
    /**
     * Retorna o context
     * @return context
     */
    public ExternalContext getContext() {
    	return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    /**
     * Metodo para chamada da constante da URL de login pelas views.
     * @return Constante da url do login.
     */
    public String getLoginUrl() {
    	// Usuário é redirecionado para a tela de contexto, para que o login ocorra com sucesso.
    	return CommonAuthentication.CONTEXT_URL;
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
	 * Retorna se houve erro no login. 
	 * @return 	<tt>true </tt> em caso de erro e <br>
	 * 			<tt>false</tt> caso contrário.
	 */
	public boolean hasError () {
		return CommonAuthentication.LOGIN_ERROR;
	}
	
	/**
	 * Adiciona a mensagem de erro
	 * @param e Exceção causada.
	 */
	private void addErrorMessage(Throwable e) {
		this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, CommonTranslator.getTranslate("MESSAGE_INTERNAL_ERROR"), e.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
    
}