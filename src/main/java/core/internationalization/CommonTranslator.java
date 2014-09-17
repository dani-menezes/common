package core.internationalization;
import java.io.Serializable;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import core.model.CommonUser;
import core.service.CommonUserService;

/**
 * Define os métodos e propriedades para a tradução de labels e mensagens do Coffey Atlas. 
 * @author Daniel Menezes <tt>daniel.afmenezes@gmail.com</tt>
 */
@SessionScoped
@ManagedBean (name="commonTranslator")
public class CommonTranslator implements Serializable {
	
	/** Constante do idioma Português. */
	public static final String PT_LANGUANGE = "pt";
	
	/** Constante do idioma Português. */
	private static final String BR_COUNTRY = "BR";
	
	/** Logger da instância. */
	private final Logger logger = LogManager.getLogManager().getLogger(CommonTranslator.class.getName());
	
	@EJB
	private CommonUserService userService;

	/** Generated UID version. */
	private static final long serialVersionUID = 3516684217112661546L;
	
	/** Arquivos de mensagens. */
	private static ResourceBundle resouce;
	
	/** Identificação dos arquivos de tradução. */
	private static final String RESOURCE_PATH = "core.internationalization.translator";
	
	/** String com a identificação de local da lingua a ser usada. */
	public static String LOCALE;
	
	/** String com a o valor passado pela view. */
	private String localeParam;
	
	static {
		Locale localeDefault = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		if (CommonTranslator.BR_COUNTRY.equals(localeDefault.getCountry())) {
			localeDefault = new Locale(CommonTranslator.PT_LANGUANGE);
		}
		CommonTranslator.LOCALE = CommonTranslator.PT_LANGUANGE;
		CommonTranslator.resouce = ResourceBundle.getBundle(CommonTranslator.RESOURCE_PATH, localeDefault);
	}
	
	/**
	 * Recupera a string de identificação do idioma.
	 * @return String com a identificação do idioma
	 */
	public static String getLocale() {
		return LOCALE;
	}

	/**
	 * Atribui a string de identificação do idioma.
	 * @param localeParam
	 */
	public static void setLocale(String localeParam) {
		CommonTranslator.resouce = ResourceBundle.getBundle(CommonTranslator.RESOURCE_PATH, new Locale(localeParam));
		LOCALE = localeParam;
	}
	
	/**
	 * @return the localeParam
	 */
	public String getLocaleParam() {
		this.localeParam = CommonTranslator.LOCALE; 
		return this.localeParam;
	}

	/**
	 * @param localeParam the localeParam to set
	 */
	public void setLocaleParam(String localeParam) {
		CommonTranslator.LOCALE = localeParam;
		this.localeParam = localeParam;
		this.localeChanged();
	}

	/**
	 * Altera o locale.
	 */
	public void localeChanged() {
		try {
			if (this.getLocaleParam() != null) {
				Principal user = (Principal) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
				CommonUser atlasUser = userService.findByLogin(user.getName());
				if (CommonTranslator.LOCALE != null && !"".equals(CommonTranslator.LOCALE)) {
					FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(CommonTranslator.LOCALE));  
					CommonTranslator.resouce = ResourceBundle.getBundle(CommonTranslator.RESOURCE_PATH, new Locale(CommonTranslator.LOCALE));
					if (atlasUser != null) {
						atlasUser.setLocale(CommonTranslator.getLocale());
						userService.saveOrUpdate(atlasUser);
					}
				}
				this.localeParam = CommonTranslator.LOCALE;
			}
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, CommonTranslator.getTranslate("MESSAGE_INTERNAL_ERROR"), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public String getStaticLocale() {
		return CommonTranslator.LOCALE;
	}

	/**
	 * Recupera a String de tradução da chave enviada.
	 * @param key Chave da string procurada
	 * @return String de tradução
	 */
	public static String getTranslate (String key) {
		if (key != null && !"".equals(key)) {
			return CommonTranslator.resouce.getString(key);
		}
		return "";
	}
	
	/**
	 * Recupera a String de tradução da chave enviada com parâmetros.
	 * @param key Chave da string procurada
	 * @param parameters Parâmetros para a tradução
	 * @return String de tradução
	 */
	public static String getTranslate (String key, Object ... parameters) {
		if (key != null && !"".equals(key)) {
			return MessageFormat.format(CommonTranslator.resouce.getString(key), parameters);
		}
		return "";
	}
	
}

