package com.institucion.fm.desktop.validator;

import com.institucion.fm.desktop.service.I18N;

// See http://haacked.com/archive/2007/08/21/i-knew-how-to-validate-an-email-address-until-i.aspx

public class EmailConstraint extends TextConstraint {
	private static final long serialVersionUID = 1L;
	private static String spect;
	
	// inicializacion de spect
	static {
//		String qtext = "[^\\x0d\\x22\\x5c\\x80-\\xff]"; // <any CHAR excepting <">, "\" & CR, and including linear-white-space>
//		String dtext = "[^\\x0d\\x5b-\\x5d\\x80-\\xff]"; // <any CHAR excluding "[", "]", "\" & CR, & including linear-white-space>
//		String atom = "[^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+"; // *<any CHAR except specials, SPACE and CTLs>
//		String quoted_pair = "\\x5c[\\x00-\\x7f]"; // "\" CHAR
//		String quoted_string = MessageFormat.format("\\x22({0}|{1})*\\x22", qtext, quoted_pair); // <"> *(qtext/quoted-pair) <">
//		String word = MessageFormat.format("({0}|{1})", atom, quoted_string); //atom / quoted-string
//		String domain_literal = MessageFormat.format("\\x5b({0}|{1})*\\x5d", dtext, quoted_pair); // "[" *(dtext / quoted-pair) "]"
//	
//		String domain_ref = atom; // atom
//		String sub_domain = MessageFormat.format("({0}|{1})", domain_ref, domain_literal); // domain-ref / domain-literal
//		String domain = MessageFormat.format("{0}(\\x2e{0})*", sub_domain); // sub-domain *("." sub-domain)
//		String local_part = MessageFormat.format("{0}(\\x2e{0})*", word); // word *("." word)
//		String addr_spec = MessageFormat.format("{0}\\x40{1}", local_part, domain); //local-part "@" domain
//	
//		spect = MessageFormat.format("^{0}$", addr_spec);
		
		// See http://www.regular-expressions.info/email.html
		spect = "(?:[a-zñÑ0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zñÑ0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
				"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\" +
				"[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zñÑ0-9](?:[a-zñÑ0-9-]*[a-zñÑ0-9])?\\.)+" +
				"[a-zñÑ0-9](?:[a-zñÑ0-9-]*[a-zñÑ0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)" +
				"{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zñÑ0-9-]*[a-zñÑ0-9]:(?:" +
				"[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	}
	
	public EmailConstraint(int flags) {
		super(flags, spect);
	}

	public EmailConstraint() {
		super(spect);
	}

	protected String getInvalidRegExText() {
		return I18N.getLabel("error.invalid.email.format");
	}
}
