package com.institucion.fm.desktop.model;


/**
 * Un componente de menú puede ser un SubMenú o Ín ítem de menú
 */
public abstract class MenuComponentModel
{
	public static final char MNEMONIC = '&';

	/** Texto del menú */
	private String text;
	private char mnemonic = '&';

	public MenuComponentModel() {}

	/** Creates a new instance of ItemComponent. */
	public MenuComponentModel(String text)
	{
		this.setText(text);
	}

	public String getText() { return text; }
	public void setText(String text)
	{
		if (text.indexOf(mnemonic) < 0)
		{
			this.text = text;
		}
		else
		{
			int index = text.indexOf(mnemonic);
			this.text = text.substring(0, index)+text.substring(index+1);
			this.mnemonic = text.charAt(index+1);
		}
	}

	public char getMnemonic() { return mnemonic; }
	public void setMnemonic(char mnemonic) { this.mnemonic = mnemonic; }

	public abstract MenuComponentModel clone();
}