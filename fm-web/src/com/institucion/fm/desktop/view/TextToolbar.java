package com.institucion.fm.desktop.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbar;

import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.security.service.PermissionTxManager;
import com.institucion.model.SucursalEnum;

public abstract class TextToolbar extends Panel implements AfterCompose {
    private static final long serialVersionUID = 4809537482864742957L;
    static Log log = LogFactory.getLog(TextToolbar.class);
    public static final String ID_BUTTON_FINDADV = "button_findadv";
    public static final String ID_BUTTON_FINDDUP = "button_finddup";
    public static final String ID_BUTTON_UNIFY_MODE = "button_unify_mode";

	private String tokenPrefix;
	private Toolbar toolbar;
	private TextToolbarButton insertButton;
	private TextToolbarButton insertIngresosEgresosButton;
	private TextToolbarButton insertCerrarCajaButton;
	private TextToolbarButton venderProdButton;
	private TextToolbarButton gastoMaipuButton;
	
	private TextToolbarButton insertClassButton;
	private TextToolbarButton insertCursoButton;

	private TextToolbarButton assignClassButton;

	private TextToolbarButton insertSubsButton;
	private TextToolbarButton assignSubsButton;
	private TextToolbarButton insertSubsVerButton;
	private TextToolbarButton insertSubsAnularButton;
	private TextToolbarButton vencerElCursoButton;

	private TextToolbarButton saldarPorErrorElCursoButton;
	private TextToolbarButton insertAnularVentaButton;
	
	private TextToolbarButton gastoCentroButton;
	private TextToolbarButton insertentregarProdPorFallaButton;

	private TextToolbarButton insertSubsSaldarDeudaButton;
	private TextToolbarButton insertQuitarDeClaseButton;
	private TextToolbarButton insertMatriculaGratisButton;
	private TextToolbarButton insertReimprimirComprobanteButton;

	private TextToolbarButton insertSubsPosponerButton;

	private TextToolbarButton deleteButton;
	private TextToolbarButton abonarButton;
	private TextToolbarButton venderButton;
	private TextToolbarButton updateButton;
	private TextToolbarButton saveButton;
	private TextToolbarButton generateButton;

	private TextToolbarButton activateButton;
	private TextToolbarButton deactivateButton;
	private TextToolbarButton findButton;
	private TextToolbarButton findAdvButton;
	private TextToolbarButton onBackButton;
	private TextToolbarButton clearFilterButton;
	private TextToolbarButton cloneButton;
	private TextToolbarButton closeButton;
	private TextToolbarButton openButton;
	private TextToolbarButton transferButton;
	private TextToolbarButton copyButton;
	private TextToolbarButton viewContactButton;
	private TextToolbarButton viewActionButton;
	private TextToolbarButton viewAudiButton;
	
	private TextToolbarButton viewListaPresentesButton;
	private TextToolbarButton tomarListaPresentesButton;

	private TextToolbarButton viewSegmentationButton;
	private TextToolbarButton exportExcelButton;
	private TextToolbarButton verCumplesContratadosButton;
	private TextToolbarButton exportPdfButton;
	private TextToolbarButton massiveActionButton;
	private TextToolbarButton viewWFUsersButton;
	private TextToolbarButton approveButton;
	private TextToolbarButton rejectButton;
	private TextToolbarButton CategorizationButton;
	private TextToolbarButton HPAButton;
	private TextToolbarButton promotionalActionButton;
	private TextToolbarButton occasionalActionButton;
	private TextToolbarButton viewPromotionPlan;
	private TextToolbarButton summaryButton;
	private TextToolbarButton unifySearchButton;
	private TextToolbarButton unifyButton;
	private TextToolbarButton colapseButton;
	private TextToolbarButton expandButton;
	private TextToolbarButton selUnselALL;
	private TextToolbarButton unifySearchAddressButton;
	private TextToolbarButton viewDetailWf;
	private TextToolbarButton noPromotionalActionButton;
	private TextToolbarButton routingButton;
	private TextToolbarButton viewDetail;
	private TextToolbarButton viewOtherContactsButton;
	private TextToolbarButton viewHPWithOutContactsButton;
	private TextToolbarButton copyCalendarButton;
	private TextToolbarButton copyActionAppointmentButton;
	private TextToolbarButton updateActionAppointmentButton;
	private TextToolbarButton deleteActionAppointmentButton;
	private TextToolbarButton saveActionAppointmentButton;
	private TextToolbarButton viewListDistributionPlan;
	private TextToolbarButton viewDistributionPlanSummary;
	private TextToolbarButton eventActionButton;
	private TextToolbarButton chartButton;
	private TextToolbarButton sendDistributionButton;
	private TextToolbarButton moveBackButton;
	private TextToolbarButton moveNextButton;
	private TextToolbarButton viewPromotionalMaterialButton;
	private TextToolbarButton vacioButton;

	public TextToolbar() {
		this("texttoolbar");
	}

	public TextToolbar(String ccsClass) {
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);
		this.toolbar = new Toolbar();
		panelChild.appendChild(this.toolbar);
		
		this.setWidth("auto");
		this.setStyle("padding: 3px 0px 5px 0px");
		this.toolbar.setClass(ccsClass);
		this.toolbar.setHeight("15px");
	}

	public void setTokenPrefix(String uc) {
		this.tokenPrefix = uc;
	}
	
	public String getTokenPrefix() {
		return this.tokenPrefix;
	}

	public abstract void addButtons();

	public void afterCompose() {
		this.addButtons();
	}
		
	public void setAlign(String align) {
		this.toolbar.setAlign(align);
	}
	
	public void addToolbarButton(TextToolbarButton button){
		if (this.getTokenPrefix() != null) {
			// solo toma los que se les ha agregado seguridad en la toolbar, sino estan todos los botones disponibles
			button.processTxToken(this.getTokenPrefix());
		}
		if (this.toolbar.getChildren().size() > 0) {
			// workaround para que el separador no quede pegado a la izquierda.
			if (Executions.getCurrent().isBrowser("ie6-") || Executions.getCurrent().isBrowser("ie7-")) {
				this.addInternalSeparator();
			}
			this.addBar();
		} else {
			this.addInternalSeparator();
		}
		
		boolean esMaipu=false;
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				if( ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals(SucursalEnum.MAIPU)){
					esMaipu= true;
				}
			}
		}
		if(button.getId() != null && button.getId().equalsIgnoreCase("idaddExportExcelCajaButton")){
			this.addInternalSeparator444(740, esMaipu);

		}else if(button.getId() != null && button.getId().equalsIgnoreCase("idaddExportExcelClientButton")){
			this.addInternalSeparator444(740, esMaipu);
		}else if(button.getId() != null && button.getId().equalsIgnoreCase("idaddExportExcelButtonButton")){  // es para los cursos
			this.addInternalSeparator444(740, false);
		}
		
		this.toolbar.appendChild(button);
	}

	public void addSeparator() {
		// Esto es por compatibilidad con la otra toolbar. Para no andar comentando llamados.
	}

	protected void addInternalSeparator()
	{
		Space separator = new Space();
		separator.setSpacing("5px");
		this.toolbar.appendChild(separator);
	}

	
	protected void addInternalSeparator444(int val, boolean esMaipu)
	{
		if(esMaipu){
			Space separator = new Space();
			separator.setSpacing(val + 80+"px");
			this.toolbar.appendChild(separator);
			
		}else{
			Space separator = new Space();
			separator.setSpacing(val+"px");
			this.toolbar.appendChild(separator);
			
		}
	}

	protected void addInternalSeparator2()
	{
		Space separator = new Space();
		separator.setSpacing("20px");
		this.toolbar.appendChild(separator);
	}
	
	protected void addBar()
	{
		Space separator = new Space();
		separator.setBar(true);
		this.toolbar.appendChild(separator);
	}
	
	protected void addEspacioBlancoButton()
	{
		TextToolbarButton insertButton = new TextToolbarButton(null);
		insertButton.setLabelAndTooltip("_______");
		insertButton.setWidth("130%");
		insertButton. setOnClickMethod("onAlgo");
		insertButton.setDisabled(true);
		addToolbarButton(insertButton);
	}
	
	protected void addInsertButton()
	{
		insertButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_CREATE);
		insertButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.insert"));
		insertButton. setOnClickMethod("onInsert");
		addToolbarButton(insertButton);
	}

	protected void addIngresosEgresosButton()
	{
		insertIngresosEgresosButton = new TextToolbarButton(null);
		insertIngresosEgresosButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.ingresosEgresos"));
		insertIngresosEgresosButton. setOnClickMethod("onIngresosEgresos");
		addToolbarButton(insertIngresosEgresosButton);
	}
	
	
	protected void addNuevoGastoCentroButton(){
		gastoCentroButton = new TextToolbarButton(null);
		gastoCentroButton.setLabelAndTooltip("Nuevo Gastos");
		gastoCentroButton. setOnClickMethod("onGastosCentro");
		addToolbarButton(gastoCentroButton);
	}

	
	protected void addVenderProductosButton(){
		venderProdButton = new TextToolbarButton(null);
		venderProdButton.setLabelAndTooltip("Vender Productos");
		venderProdButton. setOnClickMethod("onVenderProd");
		addToolbarButton(venderProdButton);
	}
	
	protected void addNuevoGastoMaipuButton(){
		gastoMaipuButton = new TextToolbarButton(null);
		gastoMaipuButton.setLabelAndTooltip("Gastos Maipu");
		gastoMaipuButton. setOnClickMethod("onGastosMaipu");
		addToolbarButton(gastoMaipuButton);
	}
	
	protected void addCerrarCajaButton()
	{
		insertCerrarCajaButton = new TextToolbarButton(null);
		insertCerrarCajaButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.cerrarCaja"));
		insertCerrarCajaButton. setOnClickMethod("onCerrarCaja");
		addToolbarButton(insertCerrarCajaButton);
	}
	
	protected void addInsertButton(String title)
	{
		insertButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_CREATE);
		insertButton.setLabelAndTooltip(title);
		insertButton. setOnClickMethod("onInsert");
		addToolbarButton(insertButton);
	}
	
	protected void addInsertSubsButton()
	{
		insertSubsButton = new TextToolbarButton(null);
		insertSubsButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.insert.subs"));
		insertSubsButton. setOnClickMethod("onInsertSubs");
		addToolbarButton(insertSubsButton);
	}
	
	protected void addVerSubsButton()
	{
		insertSubsVerButton = new TextToolbarButton(null);
		insertSubsVerButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.insert.subs.ver"));
		insertSubsVerButton. setOnClickMethod("onVerSubs");
		addToolbarButton(insertSubsVerButton);
	}
	
	protected void addSaldarDeudaSubsButton()
	{
		insertSubsSaldarDeudaButton = new TextToolbarButton(null);
		insertSubsSaldarDeudaButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.insert.subs.saldar"));
		insertSubsSaldarDeudaButton. setOnClickMethod("onSaldarDeudaSubs");
		addToolbarButton(insertSubsSaldarDeudaButton);
	}
	
	protected void addinsertReimprimirComprobanteButton()
	{
		insertReimprimirComprobanteButton = new TextToolbarButton(null);
		insertReimprimirComprobanteButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.insert.reimprimir.comprobante"));
		insertReimprimirComprobanteButton. setOnClickMethod("onReimprimirSubs");
		addToolbarButton(insertReimprimirComprobanteButton);
	}
	
	protected void addMatriculaGratisButton()
	{
		insertMatriculaGratisButton = new TextToolbarButton(null);
		insertMatriculaGratisButton.setLabelAndTooltip("Matrícula Gratuita");
		insertMatriculaGratisButton. setOnClickMethod("onMatriculaGratis");
		addToolbarButton(insertMatriculaGratisButton);
	}
	
	protected void addQuitarDeClaseButton()
	{
		insertQuitarDeClaseButton = new TextToolbarButton(null);
		insertQuitarDeClaseButton.setLabelAndTooltip("Anular ingreso a clase/Sesion");
		insertQuitarDeClaseButton. setOnClickMethod("onQuitarDeClase");
		addToolbarButton(insertQuitarDeClaseButton);
	}
	
	protected void addAnularSubsButton()
	{
		insertSubsAnularButton = new TextToolbarButton(null);
		insertSubsAnularButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.insert.subs.anular"));
		insertSubsAnularButton. setOnClickMethod("onAnularSubs");
		addToolbarButton(insertSubsAnularButton);
	}
	
	protected void addVencerElCursoButton()
	{
		vencerElCursoButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_CREATE_OCCASIONAL_ACTIONS);
		vencerElCursoButton.setLabelAndTooltip("Vencer el curso");
		vencerElCursoButton. setOnClickMethod("onVencerElCurso");
		addToolbarButton(vencerElCursoButton);
	}

	protected void addPasarASaldadoButton()
	{
	saldarPorErrorElCursoButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_CHART);
	saldarPorErrorElCursoButton.setLabelAndTooltip("SALDAR x ERROR");
	saldarPorErrorElCursoButton. setOnClickMethod("onSaldarElCurso");
		addToolbarButton(saldarPorErrorElCursoButton);
	}


	public void setVisibleSaldaCursoPorErrorElCursoButton(boolean enabled)
	{
		saldarPorErrorElCursoButton.setVisible(enabled);
	}
	
	
	public void setVisibleVencerElCursoButton(boolean enabled)
	{
		vencerElCursoButton.setVisible(enabled);
	}
	
	protected void addAnularVentaButton(String nombre)
	{
		insertAnularVentaButton = new TextToolbarButton(null);
		insertAnularVentaButton.setLabelAndTooltip(nombre);
		insertAnularVentaButton. setOnClickMethod("onAnularSubs");
		addToolbarButton(insertAnularVentaButton);
	}
	
	protected void addCambiarProductoPorFallaButton(String nombre)
	{
		insertentregarProdPorFallaButton = new TextToolbarButton(null);
		insertentregarProdPorFallaButton.setLabelAndTooltip(nombre);
		insertentregarProdPorFallaButton. setOnClickMethod("onCambiarProductoSubs");
		addToolbarButton(insertentregarProdPorFallaButton);
	}
	
	
	protected void addPospoenerSubsButton()
	{
		insertSubsPosponerButton = new TextToolbarButton(null);
		insertSubsPosponerButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.insert.subs.posponer"));
		insertSubsPosponerButton. setOnClickMethod("onPosponerSubs");
		addToolbarButton(insertSubsPosponerButton);
	}
	
	protected void addAsignSubs()
	{
		assignSubsButton = new TextToolbarButton(null);
		assignSubsButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.assingSubs"));
		assignSubsButton. setOnClickMethod("onAsignSubs");
		addToolbarButton(assignSubsButton);
	}
	
	protected void addInsertClassButton()
	{
		insertClassButton = new TextToolbarButton(null);
		insertClassButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.insert.class"));
		insertClassButton. setOnClickMethod("onInsertClass");
		addToolbarButton(insertClassButton);
	}
	
	protected void addInsertCursoButton()
	{
		insertCursoButton = new TextToolbarButton(null);
		insertCursoButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.insert.curso"));
		insertCursoButton. setOnClickMethod("onInsertCurso");
		addToolbarButton(insertCursoButton);
	}
	
	
	protected void addAsignClass()
	{
		assignClassButton = new TextToolbarButton(null);
		assignClassButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.assingClass"));
		assignClassButton. setOnClickMethod("onAsignClass");
		addToolbarButton(assignClassButton);
	}
	
	protected void addApproveButton()
	{
		approveButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_APPROVE);
		approveButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.approve"));
		approveButton. setOnClickMethod("onApprove");
		addToolbarButton(approveButton);
	}
//
	protected void addOpenClass()
	{
		assignClassButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_CREATE);
		assignClassButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.openClass"));
		assignClassButton. setOnClickMethod("onOpenClass");
		addToolbarButton(assignClassButton);
	}
//	
//	protected void addCloseClassButton()
//	{
//		approveButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_APPROVE);
//		approveButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.closeClass"));
//		approveButton. setOnClickMethod("onCloseClass");
//		addToolbarButton(approveButton);
//	}
//	
	protected void addRejectButton()
	{
		rejectButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_REJECT);
		rejectButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.reject"));
		rejectButton. setOnClickMethod("onReject");
		addToolbarButton(rejectButton);
	}

	protected void addCopyButton()
	{
		copyButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_COPY);
		copyButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.copy"));
		copyButton. setOnClickMethod("onCopy");
		addToolbarButton(copyButton);
	}

	protected void addTransferButton()
	{
		transferButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_TRANSFER);
		transferButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.transfer"));
		transferButton. setOnClickMethod("onTransfer");
		addToolbarButton(transferButton);
	}
	
	protected void addClearFilterButton()
	{
		clearFilterButton = new TextToolbarButton(null);
		clearFilterButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.clear.short"), I18N.getLabel("toolbarselector.clear"));
//		clearFilterButton.setStyle("color: #CEF6F5 !important;");
		clearFilterButton.setOnClickMethod("onClearFilter");
		addToolbarButton(clearFilterButton);
	}

	protected void addOnBackButton()
	{
		onBackButton = new TextToolbarButton(null);
		onBackButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.back"));
//		onBackButton.setStyle("color: #CEF6CE !important;");
		onBackButton.setOnClickMethod("onBack");
		addToolbarButton(onBackButton);
	}
	
	protected void addUpdateButton() {
		updateButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_MODIFY);
		updateButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.update"));
		updateButton.setOnClickMethod("onUpdate");
		addToolbarButton(updateButton);
	}
	
	protected void addUpdateButton(String title) {
		updateButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_MODIFY);
		updateButton.setLabelAndTooltip(title);
		updateButton.setOnClickMethod("onUpdate");
		addToolbarButton(updateButton);
	}

	protected void addVenderButton() {
		venderButton = new TextToolbarButton(null);
		venderButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.vender"));
		venderButton.setOnClickMethod("onVender");
		addToolbarButton(venderButton);
	}

	protected void addDeleteButton() {
		deleteButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_DELETE);
		deleteButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.delete"));
		deleteButton.setOnClickMethod("onDelete");
		addToolbarButton(deleteButton);
	}
	
	protected void addDeleteButton(String title) {
		deleteButton = new TextToolbarButton(null);
		deleteButton.setLabelAndTooltip(title);
		deleteButton.setOnClickMethod("onDelete");
		addToolbarButton(deleteButton);
	}
	
	protected void addAbonarYSildarDeleteButton(String title) {
		abonarButton = new TextToolbarButton(null);
		abonarButton.setLabelAndTooltip(title);
		abonarButton.setOnClickMethod("onAbonar");
		addToolbarButton(abonarButton);
	}

	
	public void setVisiblePagar(boolean visible){
		abonarButton.setVisible(visible);
	}

	public TextToolbarButton getButtonAbonar(){
		return abonarButton;
	}
	
	protected void addClearFilterButton2(){
		clearFilterButton = new TextToolbarButton(null);
		clearFilterButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.clear.short"), I18N.getLabel("toolbarselector.clear"));
//		clearFilterButton.setStyle("color: #CEF6F5 !important;");
		clearFilterButton.setOnClickMethod("onClearFilter2");
		addToolbarButton(clearFilterButton);
	}
	
	protected void addGenerarCodigoBarrasButton() {
		generateButton = new TextToolbarButton(null);
		generateButton.setLabelAndTooltip("Generar codigos Barras", "Generar codigos Barras");
		generateButton.setOnClickMethod("onGenerateCodBarras");
		addToolbarButton(generateButton);
	}
	
	protected void addSaveButton() {
		saveButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_SAVE);
		saveButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.save.short"), I18N.getLabel("toolbarselector.save"));
		saveButton.setOnClickMethod("onSave");
		addToolbarButton(saveButton);
	}

	protected void addSave222Button() {
		saveButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_SAVE);
		saveButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.save.short"), I18N.getLabel("toolbarselector.save"));
		saveButton.setOnClickMethod("pagarCurso");
		addToolbarButton(saveButton);
	}

	protected void addSaveButtonSinPermisos() {
		saveButton = new TextToolbarButton(null);
		saveButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.save.short"), I18N.getLabel("toolbarselector.save"));
		saveButton.setOnClickMethod("onSave");
		addToolbarButton(saveButton);
	}
	
	protected void addSaveButton(String nombre) {
		saveButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_SAVE);
		saveButton.setLabelAndTooltip(nombre, nombre);
		saveButton.setOnClickMethod("onSave");
		addToolbarButton(saveButton);
	}
	
	protected TextToolbarButton addSAveButtonWithReturn() {
		addSaveButton();
		return saveButton;
	}

//	protected void addSeleAll()
//	{
//		selUnselALL = new TextToolbarButton(null);
//		selUnselALL.setLabelAndTooltip(I18N.getLabel("toolbarselector.selectall.short"), I18N.getLabel("toolbarselector.selectall"));
//		selUnselALL.setOnClickMethod("onSelectAll");
//		addToolbarButton(selUnselALL);
//	}
	
	protected void addFindButton()
	{
		findButton = new TextToolbarButton(null);
		findButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.find.short"), I18N.getLabel("toolbarselector.find"));
//		findButton.setStyle("color: #CEF6F5 !important;");
		findButton.setOnClickMethod("onFind");
		addToolbarButton(findButton);
	}
	
	protected void addFindButton2()
	{
		findButton = new TextToolbarButton(null);
		findButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.find.short"), I18N.getLabel("toolbarselector.find"));
//		findButton.setStyle("color: #CEF6F5 !important;");
		findButton.setOnClickMethod("onFind2");
		addToolbarButton(findButton);
	}

//	protected void addFindAdvButton()
//	{
//		findAdvButton = new TextToolbarButton(null);
//		findAdvButton.setId(ID_BUTTON_FINDADV);
//		findAdvButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.openfindadv"), I18N.getLabel("toolbarselector.openfindadv.tooltip"));
////		findAdvButton.setStyle("color: #CEF6F5 !important;");
//		findAdvButton.setOnClickMethod("onFindAdv");
//		addToolbarButton(findAdvButton);
//	}

//	protected void addActivateButton()
//	{
//		activateButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_ACTIVATE);
//		activateButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.activate"));
////		activateButton.setStyle("color: yellow !important;");
//		activateButton.setOnClickMethod("onActivate");
//		addToolbarButton(activateButton);
//	}

//	protected void addDesactivateButton()
//	{
//		deactivateButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_DEACTIVATE);
//		deactivateButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.deactivate"));
////		deactivateButton.setStyle("color: yellow !important;");
//		deactivateButton.setOnClickMethod("onDeactivate");
//		addToolbarButton(deactivateButton);
//	}

//	protected void addCloneButton()
//	{
//		cloneButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_CLONE);
//		cloneButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.clone"));
////		cloneButton.setStyle("color: #CEF6CE !important;");
//		cloneButton.setOnClickMethod("onClone");
//		addToolbarButton(cloneButton);
//	}
//	protected void addCloseButton()
//	{
//		closeButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_CLOSE);
//		closeButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.close"));
////		closeButton.setStyle("color: #CEF6CE !important;");
//		closeButton.setOnClickMethod("onClosed");
//		addToolbarButton(closeButton);
//	}
//	protected void addOpenButton()
//	{
//		openButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_OPEN);
//		openButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.open"));
////		onBackButton.setStyle("color: #CEF6CE !important;");
//		openButton.setOnClickMethod("onOpen");
//		addToolbarButton(openButton);
//	}
	
//	protected void addViewContactButton()
//	{
//		viewContactButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_SEE_CONTACTS);
//		viewContactButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.viewContacts"));
////		onBackButton.setStyle("color: #CEF6CE !important;");
//		viewContactButton.setOnClickMethod("onViewContacts");
//		addToolbarButton(viewContactButton);
//	}
//	
//	protected void addViewActionsButton()
//	{
//		viewActionButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_ACTIONS);
//		viewActionButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.viewActions"));
////		onBackButton.setStyle("color: #CEF6CE !important;");
//		viewActionButton.setOnClickMethod("onViewActions");
//		addToolbarButton(viewActionButton);
//	}
//
//	protected void addViewAuditButton()
//	{
//		viewAudiButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_AUDIT);
//		viewAudiButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.viewAudit"));
////		onBackButton.setStyle("color: #CEF6CE !important;");
//		viewAudiButton.setOnClickMethod("onViewAudit");
//		addToolbarButton(viewAudiButton);
//	}


	protected void addViewListaPresentesButton()
	{
		viewListaPresentesButton = new TextToolbarButton(null);
		viewListaPresentesButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.viewListaPresentes"));
//		onBackButton.setStyle("color: #CEF6CE !important;");
		viewListaPresentesButton.setOnClickMethod("onVerListaPresentesClass");
		addToolbarButton(viewListaPresentesButton);
	}

	
	protected void addTomarListaPresentesButton()
	{
		tomarListaPresentesButton = new TextToolbarButton(null);
		tomarListaPresentesButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.tomarLista"));
//		onBackButton.setStyle("color: #CEF6CE !important;");
		tomarListaPresentesButton.setOnClickMethod("onTomarListaPresentesClass");
		addToolbarButton(tomarListaPresentesButton);
	}
	
//	protected void addViewSegmentationButton()
//	{
//		viewSegmentationButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_SEGMENTATION);
//		viewSegmentationButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.viewSegmentation"));
////		onBackButton.setStyle("color: #CEF6CE !important;");
//		viewSegmentationButton.setOnClickMethod("onViewSegmentation");
//		addToolbarButton(viewSegmentationButton);
//	}

	protected void addExportExcelButton()
	{
		exportExcelButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_EXPORT_EXCEL);
		exportExcelButton.setLabelAndTooltip("Exportar lista de Precios");		
		exportExcelButton.setId("idaddExportExcelButtonButton");

//		exportExcelButton.setStyle("float:right !important;");
		exportExcelButton.setOnClickMethod("onExportExcel");
		addToolbarButton(exportExcelButton);
	}

	protected void addExportExcelClientButton()
	{
		exportExcelButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_EXPORT_EXCEL);
		exportExcelButton.setId("idaddExportExcelClientButton");
		exportExcelButton.setLabelAndTooltip("Exportar Clientes");
//		exportExcelButton.setStyle("float:right  !important; border-style: none;");
		exportExcelButton.setOnClickMethod("onExportClientExcel");
		addToolbarButton(exportExcelButton);
	}

	
	protected void addExportExcelClientButton(String aaa)
	{
		exportExcelButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_EXPORT_EXCEL);
		exportExcelButton.setId("idaddExportExcelClientButton");
		exportExcelButton.setLabelAndTooltip(aaa);
//		exportExcelButton.setStyle("float:right  !important; border-style: none;");
		exportExcelButton.setOnClickMethod("onExportClientExcel");
		addToolbarButton(exportExcelButton);
	}
	protected void addExportExcelCajaButton()
	{
		exportExcelButton = new TextToolbarButton(null);
		exportExcelButton.setId("idaddExportExcelCajaButton");
		exportExcelButton.setLabelAndTooltip("Exportar Caja");
//		exportExcelButton.setStyle("float:right  !important; border-style: none;");
		exportExcelButton.setOnClickMethod("onExportCajaExcel");
		addToolbarButton(exportExcelButton);
	}

	protected void onExportClientTODOExcel()
	{
		exportExcelButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_EXPORT_EXCEL);
		exportExcelButton.setId("idaddExportExcelClientTODOButton");
		exportExcelButton.setLabelAndTooltip("Exportar Historico");
//		exportExcelButton.setStyle("float:right  !important; border-style: none;");
		exportExcelButton.setOnClickMethod("onExportClientTODOExcel");
		addToolbarButton(exportExcelButton);
	}

		
	
	
	
	protected void addVerCumplesContratadosButton()
	{
		verCumplesContratadosButton = new TextToolbarButton(null);
		verCumplesContratadosButton.setLabelAndTooltip("Ver Cumpleaños contratados");
//		onBackButton.setStyle("color: #CEF6CE !important;");
		verCumplesContratadosButton.setOnClickMethod("onVerCumplesContratados");
		addToolbarButton(verCumplesContratadosButton);
	}

	
	protected void addBotonVacioButton()
	{
		vacioButton = new TextToolbarButton(null);
		vacioButton.setLabelAndTooltip("                          ");
		vacioButton.setImage("/img/icon/excell_24.png");
//		style="float:left; border-style: none;">
//		vacioButton.setDisabled(true);
		addToolbarButton(vacioButton);
	}
	protected void addExportPdfButton()
	{
		exportPdfButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_EXPORT_PDF);
		exportPdfButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.exportpdf"));
//		onBackButton.setStyle("color: #CEF6CE !important;");
		exportPdfButton.setOnClickMethod("onExportPdf");
		addToolbarButton(exportPdfButton);
	}

//	protected void addMassiveActionButton()
//	{
//		massiveActionButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_MASSIVE_ACTION);
//		massiveActionButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.massiveAction"));
////		onBackButton.setStyle("color: #CEF6CE !important;");
//		massiveActionButton.setOnClickMethod("onMassiveActions");
//		addToolbarButton(massiveActionButton);
//	}
	
//	protected void addViewWFUsersButton()
//	{
//		viewWFUsersButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_WF_USERS);
//		viewWFUsersButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.viewWFUsers"));
////		onBackButton.setStyle("color: #CEF6CE !important;");
//		viewWFUsersButton.setOnClickMethod("onViewWFUsers");
//		addToolbarButton(viewWFUsersButton);
//	}

//	protected void addCategoryConfigFields()
//	{
//		TextToolbarButton button = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_CATEGORIZATION_CONFIG);
//		button.setLabelAndTooltip(I18N.getLabel("toolbar.categoryconfigfields"));
//		button.setOnClickMethod("onCategoryConfigFields");
//		addToolbarButton(button);
//	}

//	protected void addRunCategorization()
//	{
//		TextToolbarButton button = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_CATEGORIZATION_RUN);
//		button.setLabelAndTooltip(I18N.getLabel("toolbar.runcategorization"));
//		button.setOnClickMethod("onRunCategorization");
//		addToolbarButton(button);
//	}

//	protected void addVacancyWFUsersButton() {
//		TextToolbarButton button = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_WF_USERS);
//		button.setLabelAndTooltip(I18N.getLabel("toolbarselector.vacancyWFUsers"));
//		button.setOnClickMethod("onVacancyWFUsers");
//		addToolbarButton(button);
//	}

//	protected void addHistory() {
//		TextToolbarButton button = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_WF_USERS);
//		button.setLabelAndTooltip("Historial");
//		button.setOnClickMethod("onHistory");
//		addToolbarButton(button);
//	}

//	protected void addSendMessageable() {
//		TextToolbarButton button = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_DELETE);
//		button.setLabelAndTooltip("Messageable");
//		button.setOnClickMethod("onMessageable");
//		addToolbarButton(button);
//	}

	protected void addNewContactButton() {
		TextToolbarButton newContactButton;
		newContactButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_NEW_CONTACT);
		newContactButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.portfolio.newcontact"), I18N.getLabel("toolbarselector.portfolio.newcontact.tooltip"));
		newContactButton. setOnClickMethod("onNewContact");
		addToolbarButton(newContactButton);
	}
	
//	protected void addNewContactAPButton()
//	{
//		TextToolbarButton newContactButton;
//		newContactButton = new TextToolbarButton(null);
//		newContactButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.portfolio.newcontact"), I18N.getLabel("toolbarselector.portfolio.newcontact.tooltip"));
//		newContactButton. setOnClickMethod("onNewContact");
//		addToolbarButton(newContactButton);
//	}
	
//	protected void addHPAButton()
//	{
//		HPAButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_HPAS);
//		HPAButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.viewHPAs"));
//		HPAButton.setOnClickMethod("onViewHPA");
//		addToolbarButton(HPAButton);
//	}
	
//	protected void addViewDetailWF()
//	{
//		
//		viewDetailWf= new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEWDETAIL);
//		viewDetailWf.setLabelAndTooltip(I18N.getLabel("toolbarselector.view.viewDetailWf"));
//		viewDetailWf.setOnClickMethod("onViewDetailWf");
//		addToolbarButton(viewDetailWf);
//	}
	
//	protected void addMoveBackButton(){
//		moveBackButton = new TextToolbarButton(null);
//		moveBackButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.moveBack"));
//		moveBackButton.setOnClickMethod("onMoveBack");
//		moveBackButton.setStyle("left:10px");
//		addToolbarButton(moveBackButton);
//	}
//	
//	
//	protected void addColapseButton(){
//		colapseButton = new TextToolbarButton(null);
//		colapseButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.colapse.whitepage"));
//		colapseButton.setOnClickMethod("onColapse");
//		addToolbarButton(colapseButton);
//	}
//	
//	protected void addExpandButton(){
//		expandButton = new TextToolbarButton(null);
//		expandButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.expand.whitepage"));
//		expandButton.setOnClickMethod("onExpand");
//		addToolbarButton(expandButton);
//	}
//	protected void addNoPromotionalActionButton()
//	{
//		noPromotionalActionButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_CREATE_NOPROMOTIONAL_ACTIONS);
//		noPromotionalActionButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.create.nopromotionalaction"));
//		noPromotionalActionButton.setOnClickMethod("onCreatNoPromotionalAction");
//		addToolbarButton(noPromotionalActionButton);
//	}
//	protected void addViewDetailButton()
//	{
//		viewDetail = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEWDETAIL);
//		viewDetail.setLabelAndTooltip(I18N.getLabel("toolbarselector.viewDetail"));
//		viewDetail.setOnClickMethod("onViewDetail");
//		addToolbarButton(viewDetail);
//	}
//	protected void addViewOtherContactsButton()
//	{
//		viewOtherContactsButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_ROUTING_VIEW_OTHER_CONTACT);
//		viewOtherContactsButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.routing.view.other.contacts"));
//		viewOtherContactsButton.setOnClickMethod("onViewOtherContacts");
//		
//		addToolbarButton(viewOtherContactsButton);
//	}
//	protected void addViewHPWithOutContacts()
//	{
//		viewHPWithOutContactsButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_ROUTING_HP_WITHOUT_CONTACT);
//		viewHPWithOutContactsButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.routing.view.hp.with.out.contact"));
//		viewHPWithOutContactsButton.setOnClickMethod("onViewHPWithOutContact");
//		addToolbarButton(viewHPWithOutContactsButton);
//	}
//
//	protected void addCopyCalendarButton(){
//		copyCalendarButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_COPY_CALENDAR);
//		copyCalendarButton.setLabelAndTooltip(I18N.getLabel("toolbar.copy.calendar"));
//		copyCalendarButton.setOnClickMethod("onCopyCalendar");
//		addToolbarButton(copyCalendarButton);
//	}
//	
//	protected void addCopyActionAppointmentButton(){
//		copyActionAppointmentButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_COPY_CALENDAR);
//		copyActionAppointmentButton.setLabelAndTooltip(I18N.getLabel("toolbar.copy.actionappointment"));
//		copyActionAppointmentButton.setOnClickMethod("onCopyActionAppointment");
//		addToolbarButton(copyActionAppointmentButton);
//	}
//	
//	protected void addUpdateActionAppointmentButton()
//	{
//		updateActionAppointmentButton = new TextToolbarButton(null);
//		updateActionAppointmentButton.setLabelAndTooltip(I18N.getLabel("toolbar.selector.updateactionappointment"));
//		updateActionAppointmentButton.setOnClickMethod("onUpdateActionAppointment");
//		addToolbarButton(updateActionAppointmentButton);
//	}
//
//
//	protected void addDeleteActionAppointmentButton()
//	{
//		deleteActionAppointmentButton = new TextToolbarButton(null);
//		deleteActionAppointmentButton.setLabelAndTooltip(I18N.getLabel("toolbar.selector.deleteactionappointment"));
//		deleteActionAppointmentButton.setOnClickMethod("onDeleteActionAppointment");
//		addToolbarButton(deleteActionAppointmentButton);
//	}
//	
//	protected void addSaveActionAppointmentButton()
//	{
//		saveActionAppointmentButton = new TextToolbarButton(null);
//		saveActionAppointmentButton.setLabelAndTooltip(I18N.getLabel("toolbar.save.actionappointment"));
//		saveActionAppointmentButton.setOnClickMethod("onSaveActionAppointment");
//		addToolbarButton(saveActionAppointmentButton);
//	}
//	protected void addViewListDistributionPlantButton()
//	{
//		viewListDistributionPlan = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_DISTRIBUTIONPLAN_LIST);
//		viewListDistributionPlan.setLabelAndTooltip(I18N.getLabel("toolbar.distributionPlan.view.list"));
//		viewListDistributionPlan.setOnClickMethod("onViewList");
//		addToolbarButton(viewListDistributionPlan);
//	}
//	protected void addDistributionPlanSummaryButton()
//	{
//		viewDistributionPlanSummary = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_DISTRIBUTIONPLAN_SUMMARY);
//		viewDistributionPlanSummary.setLabelAndTooltip(I18N.getLabel("toolbar.distributionPlan.view.summary"));
//		viewDistributionPlanSummary.setOnClickMethod("onViewSummary");
//		addToolbarButton(viewDistributionPlanSummary);
//	}
//	
//	protected void addChartButton() {
//		chartButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_VIEW_CHART);
//		chartButton.setLabelAndTooltip(I18N.getLabel("toolbar.reportsSeq.view.chart"));
//		chartButton.setOnClickMethod("onViewChart");
//		addToolbarButton(chartButton);
//	}
//	
//	protected void addSendDistributionButton() {
//		sendDistributionButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_SEND_DISTRIBUTION);
//		sendDistributionButton.setLabelAndTooltip(I18N.getLabel("toolbar.distributionPlan.send"));
//		sendDistributionButton.setOnClickMethod("onSendDistribution");
//		addToolbarButton(sendDistributionButton);
//	}
//	
//	protected void addViewPromotionalMaterialButton(){
//		viewPromotionalMaterialButton = new TextToolbarButton(null);
//		viewPromotionalMaterialButton.setLabelAndTooltip(I18N.getLabel("toolbar.promotionalMaterial.view"));
//		viewPromotionalMaterialButton.setOnClickMethod("onView");
//		addToolbarButton(viewPromotionalMaterialButton);
//	}
//
//	protected void addNewEventButton() {
//		TextToolbarButton newEventButton;
//		newEventButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_MODIFY);
//		newEventButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.event.newevent"), I18N.getLabel("toolbarselector.event.newevent.tooltip"));
//		newEventButton. setOnClickMethod("onNewEvent");
//		addToolbarButton(newEventButton);
//	}
//
//	protected void addUpdateEventButton() {
//		TextToolbarButton updateEventButton = new TextToolbarButton(PermissionTxManager.TX_POSTFIX_MODIFY);
//		updateEventButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.event.updateevent"));
//		updateEventButton.setOnClickMethod("onUpdateEvent");
//		addToolbarButton(updateEventButton);
//	}
//	
	
	public void setEnabledViewDetailWF(boolean enabled)
	{
		viewDetailWf.setEnabled(enabled);
	}
	public void setEnabledExportPdf(boolean enabled)
	{
		exportPdfButton.setEnabled(enabled);
	}
	public void setEnabledExportExcel(boolean enabled)
	{
		exportExcelButton.setEnabled(enabled);
	}
	
	public void setEnabledClone(boolean enabled)
	{
		cloneButton.setEnabled(enabled);
	}
	
	public void setEnabledOpen(boolean enabled)
	{
		openButton.setEnabled(enabled);
	}
	
	public void setEnabledClose(boolean enabled)
	{
		closeButton.setEnabled(enabled);
	}
	
	
	public void setEnabledInsert(boolean enabled)
	{
		insertButton.setEnabled(enabled);
	}
	
	public void setEnabledAssignClass(boolean enabled)
	{
		assignClassButton.setEnabled(enabled);
	}
	
	public void setEnabledInsertClass(boolean enabled)
	{
		insertClassButton.setEnabled(enabled);
	}
	
	public void setEnabledColapse(boolean enabled)
	{
		colapseButton.setEnabled(enabled);
	}
	
	public void setEnabledExpand(boolean enabled)
	{
		expandButton.setEnabled(enabled);
	}

	
	
	public void setEnabledDelete(boolean enabled)
	{
		deleteButton.setEnabled(enabled);
	}

	public void setEnabledUpdate(boolean enabled)
	{
		updateButton.setEnabled(enabled);
	}

	public void setEnabledSave(boolean enabled)
	{
		saveButton.setEnabled(enabled);
	}

	public void setEnabledFind(boolean enabled)
	{
		findButton.setEnabled(enabled);
	}

	public void setEnabledFindAdv(boolean enabled)
	{
		findAdvButton.setEnabled(enabled);
	}

	public void setEnabledActivate(boolean enabled)
	{
		activateButton.setEnabled(enabled);
	}

	
	public void setEnabledInsertCursoButton(boolean enabled)
	{
		insertCursoButton.setEnabled(enabled);
	}
	
	public void setEnabledDeactivate(boolean enabled)
	{
		deactivateButton.setEnabled(enabled);
	}

	public void setEnabledOnBack(boolean enabled)
	{
		onBackButton.setEnabled(enabled);
	}
	public void setEnabledCopy(boolean enabled)
	{
		copyButton.setEnabled(enabled);
	}
	public void setEnabledTransfer(boolean enabled)
	{
		transferButton.setEnabled(enabled);
	}

	public void setEnabledOnCleanFilter(boolean enabled)
	{
		clearFilterButton.setEnabled(enabled);
	}

	public void setEnabledViewContact(boolean enabled)
	{
		viewContactButton.setEnabled(enabled);
	}

	public void setEnabledViewActions(boolean enabled)
	{
		viewActionButton.setEnabled(enabled);
	}
	
	
			
	public void setEnabledViewListaPresentesButton(boolean enabled)
	{
		viewListaPresentesButton.setEnabled(enabled);
	}
	
	public void setEnabledTomarPresentes(boolean enabled)
	{
		tomarListaPresentesButton.setEnabled(enabled);
	}
	
	public void setEnabledViewAudit(boolean enabled)
	{
		viewAudiButton.setEnabled(enabled);
	}
	public void setEnabledMassiveActions(boolean enabled)
	{
		massiveActionButton.setEnabled(enabled);
	}
	public void setEnabledViewWFUsers(boolean enabled)
	{
		viewWFUsersButton.setEnabled(enabled);
	}
	public void setEnabledViewCategorization(boolean enabled)
	{
		CategorizationButton.setEnabled(enabled);
	}
	public void setEnabledViewHPA(boolean enabled)
	{
		HPAButton.setEnabled(enabled);
	}
	public void setEnabledCreatePromotionalAction(boolean enabled)
	{
		promotionalActionButton.setEnabled(enabled);
	}

	public void setEnabledCreateOccasionalAction(boolean enabled)
	{
		occasionalActionButton.setEnabled(enabled);
	}
	public void setEnabledSummary(boolean enabled)
	{
		summaryButton.setEnabled(enabled);
	}
	public void setEnabledUnify(boolean enabled)
	{
		unifyButton.setEnabled(enabled);
	}
	public void setEnabledUnifySearch(boolean enabled)
	{
		unifySearchButton.setEnabled(enabled);
	}
	public void setEnabledUnifySearchAddress(boolean enabled)
	{
		unifySearchAddressButton.setEnabled(enabled);
	}
	public void setEnabledNoPromotionalAction(boolean enabled)
	{
		noPromotionalActionButton.setEnabled(enabled);
	}
	public void setSelUnselAll(boolean enabled)
	{
		selUnselALL.setEnabled(enabled);
	}
	public void setEnabledRouting(boolean enabled)
	{
		routingButton.setEnabled(enabled);
	}
	public void setEnabledViewDetail(boolean enabled)
	{
		viewDetail.setEnabled(enabled);
	}
	public void setEnabledViewOtherContacts(boolean enabled)
	{
		viewOtherContactsButton.setEnabled(enabled);
	}
	public void setEnabledViewListDistributionPlan(boolean enabled)
	{
		viewListDistributionPlan.setEnabled(enabled);
	}
	public void setEnabledCopyCalendarButton(boolean enabled)
	{
		copyCalendarButton.setEnabled(enabled);
	}
	public void setEnabledEventActionButton(boolean enabled) {
		eventActionButton.setEnabled(enabled);
	}
	
	public void setEnabledChartButton(boolean enabled) {
		chartButton.setEnabled(enabled);
	}
	public void setEnabledDistributionPlanSummaryButton(boolean enabled){
		viewDistributionPlanSummary.setEnabled(enabled);
	}
	public void setEnabledSendDistributionButton(boolean enabled){
		sendDistributionButton.setEnabled(enabled);
	}
	public void setEnabledOnMoveBackButton(boolean enabled){
		moveBackButton.setEnabled(enabled);
	}
	public void setEnabledOnMoveNextButton(boolean enabled){
		moveNextButton.setEnabled(enabled);
	}
	public void setEnableViewPromotionalMaterialButton(boolean enabled){
		viewPromotionalMaterialButton.setEnabled(enabled);
	}
	public Toolbar getToolBar(){
		return this.toolbar;
	}
	
}
