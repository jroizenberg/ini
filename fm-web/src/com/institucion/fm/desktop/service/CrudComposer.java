package com.institucion.fm.desktop.service;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericAutowireComposer;

import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.security.service.PermissionTxManager;

public abstract class CrudComposer extends GenericAutowireComposer
{
	private final String indexpos = "position";
	private final String maxium = "max";
	private final String list = "key";
	private final String page = "pagno";
	private final String filters = "filters";
	private final String totalactions = "totalactions";
	private final String col = "col";
	private final String order = "order";
		
	private PermissionTxManager permTxMgr = null;
	
	public PermissionTxManager getManager() {
		if (permTxMgr == null) {
			permTxMgr = BeanFactory.<PermissionTxManager>getObject("fm.mgr.tx.permission");
		}
		return permTxMgr;
	}
	
	public void gotoPage(String zulpage)
	{
		Session.getDesktopPanel().setSrc(null);
		Session.getDesktopPanel().setSrc(zulpage);
		Session.getDesktopPanel().setMessage("");
	}

	public void gotoPage(String zulpage, String message)
	{
		Session.getDesktopPanel().setSrc(null);
		Session.getDesktopPanel().setSrc(zulpage);
		Session.getDesktopPanel().setMessage(message);
	}
	

    protected boolean hasSelectedItem(GridList gridlist) {
        if (gridlist.getSelectedItem() != null)
            return true;
        MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"),
                        I18N.getLabel("selector.actionwithoutitem.title"));
        return false;
    }

    protected boolean hasSelectedOneItem(GridList gridlist) {
        if (gridlist.getSelectedItem() != null && gridlist.getSelectedItems().size() == 1) {
            Sessions.getCurrent().setAttribute(getSelectedEntitySessionId(),gridlist.getSelectedItem().getValue());
            return true;
        }
        MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"),
                        I18N.getLabel("selector.actionwithoutitem.title"));
        return false;
    }
    /**
     * Identificador para recuperar la entidad seleccionada cuando se vuelve de
     * algun reporte.
     */
    private String getSelectedEntitySessionId() {
        return "EntitySelected_"+this.getClass().getCanonicalName();
    }
	public void onMoveBack(){
		List<Object> l = (ArrayList<Object>) Session.getAttribute(list);
		int index = Integer.parseInt(Session.getAttribute(indexpos).toString());
		if (index > 0) {
			index--;
			Session.setAttribute(indexpos, index);
		}
		else {
		}
		
	}
	
	public void onMoveNext(){
		List<Object> l = (ArrayList<Object>) Session.getAttribute(list);
		int index = Integer.parseInt(Session.getAttribute(indexpos).toString());
		int max = Integer.parseInt(Session.getAttribute(maxium).toString());
		if (index+1 < max) {
			index++;
			Session.setAttribute(indexpos, index);
		}
		else {

		}
	}
}