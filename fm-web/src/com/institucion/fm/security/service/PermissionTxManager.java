package com.institucion.fm.security.service;

public interface PermissionTxManager {
	//**** methods
	boolean havePermission(String token);
	void refreshUserPermission();

	//**** tokens 
	//* postFix
	final String TX_POSTFIX_CREATE = "create";	
	final String TX_POSTFIX_DELETE = "delete";	
	final String TX_POSTFIX_MODIFY = "modify";	
	final String TX_POSTFIX_ACTIVATE = "activate";	
	final String TX_POSTFIX_DEACTIVATE = "deactivate";	
	final String TX_POSTFIX_APPROVE = "approve";
	final String TX_POSTFIX_REJECT = "reject";
	final String TX_POSTFIX_COPY = "copy";
	final String TX_POSTFIX_TRANSFER = "transfer";
	final String TX_POSTFIX_SAVE = "save";
	final String TX_POSTFIX_CLONE = "clone";
	final String TX_POSTFIX_CLOSE = "close";
	final String TX_POSTFIX_OPEN = "open";
	final String TX_POSTFIX_SEE_CONTACTS = "see.contacts";
	final String TX_POSTFIX_VIEW_ACTIONS = "view.actions";
	final String TX_POSTFIX_VIEW_AUDIT = "view.audit";
	final String TX_POSTFIX_VIEW_SEGMENTATION = "view.segmentation";

	
	final String TX_POSTFIX_EXPORT_EXCEL = "export.excel";
	final String TX_POSTFIX_EXPORT_PDF = "export.pdf";
	final String TX_POSTFIX_MASSIVE_ACTION = "massive.action";
	final String TX_POSTFIX_VIEW_WF_USERS = "view.wf.users";
//	final String TX_POSTFIX_CATEGORIZATION = "categorization";
	final String TX_POSTFIX_CATEGORIZATION_CONFIG = "categorization.config";
	final String TX_POSTFIX_CATEGORIZATION_RUN = "categorization.run";
	final String TX_POSTFIX_NEW_CONTACT = "new.contact";
	final String TX_POSTFIX_CREATE_PROMOTIONAL_ACTIONS= "create.promotionalaction";
	final String TX_POSTFIX_CREATE_OCCASIONAL_ACTIONS= "create.occasionalaction";
	final String TX_POSTFIX_CREATE_NOPROMOTIONAL_ACTIONS= "create.nopromotionalaction";
	
	final String TX_POSTFIX_CREATE_EVENT_ACTIONS = "create.eventaction";
	final String TX_POSTFIX_VIEW_CHART = "view.chart";

	final String TX_POSTFIX_VIEW_PROMOTIONAL_PLAN= "view.promotionalPlan";
	final String TX_POSTFIX_VIEWDETAIL = "viewDetails";	
	final String TX_POSTFIX_VIEW_SUMMARY= "view.summary";
	final String TX_POSTFIX_UNIFY_SEARCH= "unify.search";
	final String TX_POSTFIX_UNIFY_ADDRESS_SEARCH= "unify.address.search";
	final String TX_POSTFIX_UNIFY= "unify";
	final String TX_POSTFIX_ROUTING= "routing";
	final String TX_POSTFIX_ROUTING_VIEW_OTHER_CONTACT= "routing.viewothercontacts";
	final String TX_POSTFIX_ROUTING_HP_WITHOUT_CONTACT= "routing.viewhpwithoutcontact";
	final String TX_POSTFIX_VIEW_DISTRIBUTIONPLAN_LIST="view.list";
	final String TX_POSTFIX_VIEW_DISTRIBUTIONPLAN_SUMMARY="view.summary";
	final String TX_POSTFIX_COPY_CALENDAR= "tx.calendar.copycalendar";
	final String TX_POSTFIX_SEND_DISTRIBUTION= "send";
	
	//**** tokens 
	//* Pharmacy
	final String TX_PHARMACY_MODIFY = "tx.pharmacy.modify";	
	final String TX_PHARMACY_SAVE = "tx.pharmacy.save";
	final String TX_PHARMACY_UNIFY="tx.pharmacy.unify";
	//* health professional
	final String TX_HEALTHPROFESSIONAL_MODIFY = "tx.healthprofessional.modify";	
	final String TX_HEALTHPROFESSIONAL_SAVE = "tx.healthprofessional.save";
	final String TX_HEALTHPROFESSIONAL_UNIFY = "tx.healthprofessional.unify";
	//* institution
	final String TX_INSTITUTION_MODIFY = "tx.institution.modify";	
	final String TX_INSTITUTION_SAVE = "tx.institution.save";
	final String TX_INSTITUTION_UNIFY = "tx.institution.unify";
	//* portfolio
	final String TX_PORTFOLIO_MODIFY = "tx.portfolio.modify";	
	final String TX_PORTFOLIO_SAVE = "tx.portfolio.save";
	//* Contacts
	final String TX_CONTACTS_MODIFY = "tx.contact.modify";	
	final String TX_CONTACTS_SAVE = "tx.contact.save";
	//* Actions
	final String TX_ACTIONS_MODIFY = "tx.actions.modify";	
	final String TX_ACTIONS_SAVE = "tx.actions.save";
	//* Promotion Line
	final String TX_PROMOTIONLINE_MODIFY = "tx.promotionLine.modify";	
	final String TX_PROMOTIONLINE_SAVE = "tx.promotionLine.save";
	
	//* product Line
	final String TX_PRODUCT_MODIFY = "tx.productLine.modify";	
	final String TX_PRODUCT_SAVE = "tx.productLine.save";
	//* cycles
	final String TX_CYCLES_MODIFY = "tx.cycle.modify";	
	final String TX_CYCLES_SAVE = "tx.cycle.save";
	
	
	//* promotionplan
	final String TX_PLAN_MODIFY = "tx.plan.modify";	
	final String TX_PLAN_SAVE = "tx.plan.save";
	
	//* wf entity config
	final String TX_ENTITYCONFIG_MODIFY = "tx.entityConfig.modify";	
	final String TX_ENTITYCONFIG_SAVE = "tx.entityConfig.save";

	//* wf entity config
	final String TX_FLEXIBLEENTITY_MODIFY = "tx.flexibleEntity.modify";
	final String TX_FLEXIBLEENTITY_SAVE = "tx.flexibleEntity.save";

	//* structure
	final String TX_STRUCTURES_MODIFY = "tx.structures.modify";	
	final String TX_STRUCTURES_SAVE = "tx.structures.save";
	
	//* sales force
	final String TX_SF_MODIFY = "tx.salesForce.modify";	
	final String TX_SF_SAVE = "tx.salesForce.save";
	//* user
	final String TX_USER_MODIFY = "tx.user.modify";	
	final String TX_USER_SAVE = "tx.user.save";
	//* group
	final String TX_GROUP_MODIFY = "tx.group.modify";	
	final String TX_GROUP_SAVE = "tx.group.save";
	//*password
	final String TX_PASSWORD_SAVE = "tx.password.save";
	//*Promotional Material
	final String TX_PROMOTIONALMATERIAL_SAVE = "tx.prmotionalMaterial.save";
	final String TX_PROMOTIONALMATERIAL_MODIFY= "tx.promotionalMaterial.modify";
	//*Susdivision
	final String TX_SUBDIVISION_MODIFY = "tx.subdivision.modify";	
	//*Unify
	final String TX_UNIFY_SAVE = "tx.unify.save";
	
	final String TX_POSTFIX_VIEW_HPAS = "tx.contact.view.hpas";

	final String TX_POSTFIX_VIEW_SEQUENCE = "tx.contact.view.sequence";
	final String TX_POSTFIX_VIEW_SUMMARY_AUDIT="tx.contact.view.audit";
	final String TX_POSTFIX_VIEW_SUMMARY_SEGMENTATION="tx.contact.view.segmentation";
	
	final String TX_POSTFIX_VIEW_SUMMARY_PROMOTIONAL_PLAN= "tx.contact.view.promotionalPlan";
	final String TX_POSTFIX_VIEW_OBS = "tx.contact.view.obs";
	final String TX_POSTFIX_VIEW_SUMMARY_ROUTING = "tx.contact.view.routing";	
	final String TX_VIEWDETAIL = "tx.task.viewDetails";		
	//WhitePage
	final String TX_WHITEPAGE_MODIFY = "tx.whitepage.modify";	
	//PromotionZone
	//* portfolio
	final String TX_PROMOTIONZONE_MODIFY = "tx.promotionzone.modify";	
	final String TX_PROMOTIONZONE_SAVE = "tx.promotionzone.save";
	final String TX_ACTIONPLAN_MODIFY = "tx.actionplan.modify";
	public static final String PERMISSION_MAP_KEY="permission.tx.user.session.key";


	/*
	 * Components
	 */
	
	// Codiguera DT Speciality
	final String TX_COMPONENT_DT_SPECIALITY_MODIFY =  "tx.component.dt.speciality.modify"; 
	final String TX_COMPONENT_DT_SPECIALITY_MODIFY_DETAIL =  "tx.component.dt.speciality.modify.detail"; 

	final String TX_COMPONENT_DT_SPECIALITY_SAVE =  "tx.component.dt.speciality.save";
	final String TX_COMPONENT_DT_SPECIALITY_SAVE_DETAIL =  "tx.component.dt.speciality.save.detail";
	
	// Eliminar este no hace falta final String TX_COMPONENT_DT_SPECIALITY_CREATE =  "tx.component.dt.speciality.create";
	final String TX_COMPONENT_DT_SPECIALITY_CREATE_DETAIL =  "tx.component.dt.speciality.create.detail";
	
	final String TX_COMPONENT_DT_SPECIALITY_DELETE =  "tx.component.dt.speciality.delete";
	final String TX_COMPONENT_DT_SPECIALITY_DELETE_DETAIL =  "tx.component.dt.speciality.delete.detail";

	final String TX_COMPONENT_DT_SPECIALITY_ACTIVATE =  "tx.component.dt.speciality.activate";
	final String TX_COMPONENT_DT_SPECIALITY_ACTIVATE_DETAIL =  "tx.component.dt.speciality.activate.detail";

	final String TX_COMPONENT_DT_SPECIALITY_DEACTIVATE =  "tx.component.dt.speciality.deactivate";
	final String TX_COMPONENT_DT_SPECIALITY_DEACTIVATE_DETAIL =  "tx.component.dt.speciality.deactivate.detail";

	// Codiguera DT Segment
	final String TX_COMPONENT_DT_SEGMENT_MODIFY =  "tx.component.dt.segment.modify"; 
	final String TX_COMPONENT_DT_SEGMENT_MODIFY_DETAIL =  "tx.component.dt.segment.modify.detail"; 

	final String TX_COMPONENT_DT_SEGMENT_SAVE =  "tx.component.dt.speciality.save";
	final String TX_COMPONENT_DT_SEGMENT_SAVE_DETAIL =  "tx.component.dt.segment.save.detail";
	
	// Eliminar este no hace falta final String TX_COMPONENT_DT_SPECIALITY_CREATE =  "tx.component.dt.speciality.create";
	final String TX_COMPONENT_DT_SEGMENT_CREATE_DETAIL =  "tx.component.dt.segment.create.detail";
	
	final String TX_COMPONENT_DT_SEGMENT_DELETE =  "tx.component.dt.segment.delete";
	final String TX_COMPONENT_DT_SEGMENT_DELETE_DETAIL =  "tx.component.dt.segment.delete.detail";

	final String TX_COMPONENT_DT_SEGMENT_ACTIVATE =  "tx.component.dt.segment.activate";
	final String TX_COMPONENT_DT_SEGMENT_ACTIVATE_DETAIL =  "tx.component.dt.segment.activate.detail";

	final String TX_COMPONENT_DT_SEGMENT_DEACTIVATE =  "tx.component.dt.segment.deactivate";
	final String TX_COMPONENT_DT_SEGMENT_DEACTIVATE_DETAIL =  "tx.component.dt.segment.deactivate.detail";
	
}