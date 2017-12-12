package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Label;
public class GridCrud extends Grid{
    
	private static final long serialVersionUID = 1L;
	public static final String ID = "gridCrud";
	private Panel parentPanel = null;
	
	private Row currentRow;
	private int index;
	

	public GridCrud(){
		setFixedLayout(true);
		setWidth("auto");
		setId(ID);
		Rows rows = new Rows();
		this.appendChild(rows);
		index = 2;
		

	}

	public Panel getParentPanel() {
		return parentPanel;
	}

	public void setParentPanel(Panel parentPanel) {
		this.parentPanel = parentPanel;
	}

	public void addField(Label label, Component cmp){
		if (index > 1){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
			index = 0;
		}

		currentRow.appendChild(label);
		currentRow.appendChild(cmp);
		index++;
	}
	
	public void addField1ComponentePorRow(Label label, Component cmp){
		currentRow = new Row();
		this.getRows().appendChild(currentRow);
		currentRow.appendChild(label);
		currentRow.appendChild(cmp);
	}
	
	public void addFieldClases(Label label, Component cmp, int num){
		if (num > 6){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
		}

		if(	currentRow == null || (num== 0 && cmp != null )){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
		}

		currentRow.appendChild(label);
		if(cmp != null)
			currentRow.appendChild(cmp);
	}
	
	public void addFieldClases(Label label, Label label2, int num){
		if (num > 6){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
		}

		if(	currentRow == null || (num== 0 && label2 != null )){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
		}

		currentRow.appendChild(label);
		if(label2 != null)
			currentRow.appendChild(label2);
	}
	
	public void addFieldClases(Component label, Component label2, int num){
		if (num > 6){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
		}

		if(	currentRow == null || (num== 0 && label2 != null )){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
		}

		currentRow.appendChild(label);
		if(label2 != null)
			currentRow.appendChild(label2);
	}
		
	public void addFieldClases(Textbox label, Component cmp, int num){
		if (num > 7){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
		}

		if(	currentRow == null || (num== 0 && cmp != null )){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
		}

		currentRow.appendChild(label);
		if(cmp != null)
			currentRow.appendChild(cmp);
	}
	public void addField(Component cmp, Label label, int num){
		if (num > 6){
            currentRow = new Row();
            this.getRows().appendChild(currentRow);
        }

		if(	currentRow == null || (num== 0 && cmp != null )){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
		}
		currentRow.appendChild(label);
        currentRow.appendChild(cmp);
    }
	
	
	public void addField(Component cmp){
           if (index > 1){
               currentRow = new Row();
               this.getRows().appendChild(currentRow);
               index = 0;
           }

           currentRow.appendChild(cmp);
           index++;
       }

	public void addField(Label label, Label value) {
		if (index > 1){
			currentRow = new Row();
			this.getRows().appendChild(currentRow);
			index = 0;
		}

		currentRow.appendChild(label);
		currentRow.appendChild(value);
		index++;
	}
	
	/**
	 * Un campo por linea 
	 */
	public void addFieldUnique(Label label, Component cmp){
		Row row = new Row();
		row.appendChild(label);
		row.appendChild(cmp);
		this.getRows().appendChild(row);
	}

	public void addFieldUnique(Component cmp){
		Row row = new Row();
//		row.appendChild(label);
		row.appendChild(cmp);
		this.getRows().appendChild(row);
	}
	
   public void addFieldUnique(Label label, Component cmp, Component cmp2){
       index=3;
        Row row = new Row();
        row.appendChild(label);
        row.appendChild(cmp);
        row.appendChild(cmp2);
        this.getRows().appendChild(row);
    }

   public void addFieldUnique(Label label, Component cmp, Label label2, Component cmp2){
       index=4;
        Row row = new Row();
        row.appendChild(label);
        row.appendChild(cmp);
        row.appendChild(label2);
        row.appendChild(cmp2);
        this.getRows().appendChild(row);
    }

   public void addFieldUnique(Label label, Component cmp, Label label2, Component cmp2, Label label3, Component cmp3){
       index=6;
        Row row = new Row();
        row.appendChild(label);
        row.appendChild(cmp);
        row.appendChild(label2);
        row.appendChild(cmp2);
        row.appendChild(label3);
        row.appendChild(cmp3);

        this.getRows().appendChild(row);
    }

   
   public void addFieldUnique(Label label7, Label label47, Label label, Component cmp, Label label2, Component cmp2, Label label3, Component cmp3){
       index=8;
        Row row = new Row();
        row.appendChild(label7);
        row.appendChild(label47);

        row.appendChild(label);
        row.appendChild(cmp);
        row.appendChild(label2);
        row.appendChild(cmp2);
        row.appendChild(label3);
        row.appendChild(cmp3);

        this.getRows().appendChild(row);
    }

   public void addFieldUnique(Label label99, Component cmp99,Label label78, Component cmp8, Label label, Component cmp, Label label2, Component cmp2, Label label3, Component cmp3){
       index=10;
        Row row = new Row();
        row.appendChild(label99);
        row.appendChild(cmp99);

        row.appendChild(label78);
        row.appendChild(cmp8);

        row.appendChild(label);
        row.appendChild(cmp);
        row.appendChild(label2);
        row.appendChild(cmp2);
        row.appendChild(label3);
        row.appendChild(cmp3);

        this.getRows().appendChild(row);
    }
   public void addFieldUnique(Label label, Label cmp, Label label2, Label cmp2, Label label3, Label cmp3){
       index=4;
        Row row = new Row();
        row.appendChild(label);
        row.appendChild(cmp);
        row.appendChild(label2);
        row.appendChild(cmp2);
        row.appendChild(label3);
        row.appendChild(cmp3);
      
        this.getRows().appendChild(row);
    }
}