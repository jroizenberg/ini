package com.institucion.fm.desktop.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import com.institucion.fm.conf.InstanceConf;

public class PanelReport extends Panel implements AfterCompose
{
	static Log log = LogFactory.getLog(PanelReport.class);
	private static final long serialVersionUID = 1L;
	public static final String ID = "panelReport";

	private String usegrid;
	private GridReport gridReport;

	public PanelReport()
	{
		setId(ID);
		setWidth("auto");
		setHeight("auto");
		setBorder("normal");
		setCollapsible(false);
	}

	public String getUsegrid() { return usegrid; }
	public void setUsegrid(String usegrid) { this.usegrid = usegrid; }

	@Override
	public void afterCompose()
	{
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);

		try
		{
			Class<?> gridClass = Class.forName(usegrid);
			gridReport = (GridReport) gridClass.newInstance();
			if (!getId().equals(ID))
				gridReport.setId(getId()+"Grid");
		}
		catch (Exception e)
		{
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			//log.debug(e);
			throw new RuntimeException("'"+usegrid+"' no se pudo crear.", e);
		}

		panelChild.appendChild(gridReport);
	}

	public GridReport getGridReport()
	{
		return gridReport;
	}
	protected String getDBproperties(){
		return "&jndi_name=java:"+ InstanceConf.getInstanceConfigurationProperty(InstanceConf.JNDI_NAME);
	}
	
	protected void buldFrame(String TypeReport){
		Component comp =this.getFellow("iframe");
	}

	protected void buildFrame (String name,String parameters,String reportType){
		String type =new String();
		if (reportType.equals("pdf")){
			type =  "&__format=pdf";
		}
		else if (reportType.equals("xls")){
			type =  "&__format=xls";
		}
		else {
			type = "";
		}  
		Component comp =this.getFellow("iframe");
		String url = "run?__report="+name;
		((Iframe)comp).setSrc(url + getDBproperties()+ parameters+ type);
	}
	public void cleanCache(){
	}
}