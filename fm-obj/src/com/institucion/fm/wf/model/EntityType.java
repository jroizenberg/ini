package com.institucion.fm.wf.model;

import java.util.Locale;

import com.institucion.fm.resource.I18NServer;

public enum EntityType
{
	PHARMACY(0), ADDRESS(15), HEALTH_PROFESSIONAL(1), INSTITUTION(2), PORTFOLIO(3), HEALTH_PROFESSIONAL_CONTACT(4),
	PHARMACY_CONTACT(5), INSTITUTION_CONTACT(6), PACIENT_CONTACT(7),PROMOTIONALACTION(8),FAILEDACTION(9),
	ADDITIONALACTION(10),SERVICEACTION(11),DELIVERYBONUSACTION(12), PRODUCTDELIVERYSALEACTION(13),OCCASIONALACTION(14),
	NOPROMOTIONALACTION(16),PERSON(17);

	private int id;
	
	private EntityType(int id) { this.id = id; }
	public int toInt() { return id; }
 
	public static EntityType fromInt(int value) {    
        switch(value) {
            case 0: return PHARMACY;
            case 1: return HEALTH_PROFESSIONAL;
            case 2: return INSTITUTION;
            case 3: return PORTFOLIO;
            case 4: return HEALTH_PROFESSIONAL_CONTACT;
    		case 5: return PHARMACY_CONTACT;
    		case 6: return INSTITUTION_CONTACT;
    		case 7: return PACIENT_CONTACT;            
    		case 8: return PROMOTIONALACTION;
    		case 9: return FAILEDACTION;
    		case 10: return ADDITIONALACTION;
    		case 11: return SERVICEACTION;
    		case 12: return DELIVERYBONUSACTION;
    		case 13: return PRODUCTDELIVERYSALEACTION;
    		case 14: return OCCASIONALACTION;
    		case 15: return ADDRESS;
    		case 16: return NOPROMOTIONALACTION;
    		case 17: return PERSON;
            default:
                    return PHARMACY;
        }
   }

	/** Pensado para el metodo FlexibleEntityEJB.getFEClass(CallContext ctx, String className) */
	public String getDictionaryString()
	{    
      switch(this)
      {
      	case PHARMACY: return "Pharmacy";
      	case HEALTH_PROFESSIONAL: return "HealthProfessional";
      	case INSTITUTION: return "Institution";
      	case ADDRESS: return "Address";
      	case PHARMACY_CONTACT: return "PharmacyContact";
      	case HEALTH_PROFESSIONAL_CONTACT: return "HealthProfessionalContact";
      	case INSTITUTION_CONTACT: return "InstitutionContact";
      	case PROMOTIONALACTION: return "PromotionalAction";
      	case FAILEDACTION: return "FailedAction";
      	case ADDITIONALACTION: return "AdditionalAction";
      	case SERVICEACTION: return "ServiceAction";
      	case DELIVERYBONUSACTION: return "DeliveryBonusAction";
      	case PRODUCTDELIVERYSALEACTION: return "ProductDeliverySaleAction";
      	case OCCASIONALACTION: return "OccasionalAction";
      	case NOPROMOTIONALACTION: return "NoPromotionalAction";
      	case PERSON: return "Person";
      	default: return "";
      }
 }

	/**
	 * Este metodo solo debe ejecutarse del lado servidor.
	 */
	public String getI18N(Locale locale)
	{
		switch (this)
		{
			case PHARMACY: return I18NServer.getLabel(locale, "entitytype.pharmacy");
			case HEALTH_PROFESSIONAL: return I18NServer.getLabel(locale, "entitytype.healthpro");
			case INSTITUTION: return I18NServer.getLabel(locale, "entitytype.institution");
			case PORTFOLIO: return I18NServer.getLabel(locale, "entitytype.portfolio");
			case ADDRESS: return I18NServer.getLabel(locale, "entitytype.address");
			case PHARMACY_CONTACT: return I18NServer.getLabel(locale, "entitytype.pharmacycontact");
			case HEALTH_PROFESSIONAL_CONTACT: return I18NServer.getLabel(locale, "entitytype.healthprocontact");
			case INSTITUTION_CONTACT: return I18NServer.getLabel(locale, "entitytype.institutioncontact");
			case PROMOTIONALACTION: return I18NServer.getLabel(locale, "entitytype.promotionalaction");
			case FAILEDACTION: return I18NServer.getLabel(locale, "entitytype.failedaction");
			case ADDITIONALACTION: return I18NServer.getLabel(locale, "entitytype.additionalaction");
			case SERVICEACTION: return I18NServer.getLabel(locale, "entitytype.serviceaction");
			case DELIVERYBONUSACTION: return I18NServer.getLabel(locale, "entitytype.deliverybonusaction");
			case PRODUCTDELIVERYSALEACTION: return I18NServer.getLabel(locale, "entitytype.productDeliverySale");
			case OCCASIONALACTION: return I18NServer.getLabel(locale, "entitytype.occasionalaction");
			case NOPROMOTIONALACTION: return I18NServer.getLabel(locale, "entitytype.nopromotionalaction");
			case PERSON: return I18NServer.getLabel(locale, "entitytype.person");
			
			default: throw new java.lang.IllegalArgumentException(this+" no contemplado");
		}
	}
}