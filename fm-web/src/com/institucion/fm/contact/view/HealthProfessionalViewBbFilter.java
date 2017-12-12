package com.institucion.fm.contact.view;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.bb.BandBoxFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

/**
 * The Class HealthProfessionalViewBbFilter.
 */
public class HealthProfessionalViewBbFilter extends BandBoxFilter {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new health professional view bb filter.
	 */
	public HealthProfessionalViewBbFilter() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.institucion.fm.desktop.view.bb.BandBoxFilter#getMasterFieldLabel()
	 */
	protected Label getMasterFieldLabel() {
		return new Label(I18N.getLabel("header.client.name"));
	}

	/* (non-Javadoc)
	 * @see com.institucion.fm.desktop.view.bb.BandBoxFilter#getOnFindEventName()
	 */
	@Override
	protected String getOnFindEventName() {
		return "onFindClient";
	}

	/* (non-Javadoc)
	 * @see com.institucion.fm.desktop.view.GridFilter#getCriteriaFilters()
	 */
	@Override
	public CriteriaClause getCriteriaFilters() {


		StringBuilder criteria = new StringBuilder();
		/** El string a buscar en mayusculas como estan estos datos en la DB */
		String filterText = this.getMaster().getText().toUpperCase();
		/** Armo la criteria con los matching por nombre */
		if (filterText != null && filterText.length() > 0) {
			criteria.append(" and ( ");			
			/** clausula para firstname */
			criteria.append("firstname like '%");
			criteria.append(filterText);
			criteria.append("%'");

			criteria.append(" or ");

			/** clausula para second_firstname */
			criteria.append("second_firstname like '%");
			criteria.append(filterText);
			criteria.append("%'");

			criteria.append(" or ");

			/** clausula para lastname */
			criteria.append("lastname like '%");
			criteria.append(filterText);
			criteria.append("%'");

			criteria.append(" or ");

			/** clausula para second_lastname */
			criteria.append("second_lastname like '%");
			criteria.append(filterText);
			criteria.append("%'");
			
			criteria.append(" )");
		}
		/** 
		 *  Como Panel esta contenido fuertemente en BandBox no es posible
		 *  acceder a su Panel inerno sino a tres observadores, uno de los cuales
		 *  es este metodo. Para poder devolver String se uso el field selectorPage 
		 *  de la clase CriteriaClause (o sea se la uso de wrapper). Esto resulto
		 *  mejor solucion que ampliar las clases intervinientes MUCHO MAS GENERICAS
		 *  para algo tan puntual. 
		 */
		CriteriaClause criteriaClause = new CriteriaClause();
		criteriaClause.setSelectorPage(criteria.toString());
		
		return criteriaClause;
	}

}