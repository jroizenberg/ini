package com.institucion.controller;

import java.awt.Color;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Stretching;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;

public class Styles {

	public static Style detailStyle()
	{
		Style detailStyle = new Style();
		detailStyle.setBorder(Border.THIN);
		detailStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		detailStyle.setFont(new Font(8, Font._FONT_VERDANA, false));
		return detailStyle;
	}
	
	
	public static Style detailCursosStyle(){
		Style detailStyle = new Style();
		detailStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		detailStyle.setFont(Font.ARIAL_MEDIUM_BOLD);

		detailStyle.setBorder(Border.THIN);

		detailStyle.setBackgroundColor(Color.lightGray);
		detailStyle.setTransparency(Transparency.OPAQUE);
		
		return detailStyle;
	}
	
	
	public static Style imageCellStyle()
	{
		Style imageCellStyle = new Style();
		imageCellStyle.setBorder(Border.THIN);
		imageCellStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		imageCellStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		imageCellStyle.setOverridesExistingStyle(true);
		imageCellStyle.setStreching(Stretching.RELATIVE_TO_BAND_HEIGHT);
		imageCellStyle.setStretchWithOverflow(true);
		return imageCellStyle;
	}
	
	public static Style firstColumnDetailStyle()
	{
		Style firstColumnDetailStyle = new Style();
		firstColumnDetailStyle.setBorder(Border.THIN);
		firstColumnDetailStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		firstColumnDetailStyle.setFont(new Font(8, Font._FONT_VERDANA, false));
		return firstColumnDetailStyle;
	}
	
	public static Style subtitleStyle()
	{
		Style subtitleStyle = new Style();
		subtitleStyle.setFont(new Font(8, Font._FONT_VERDANA, false));
		subtitleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		return subtitleStyle;
	}
	
	public static Style detailNameStyle()
	{
		Style detailNameStyle = new Style();
		detailNameStyle.setBorder(Border.THIN);
		detailNameStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		detailNameStyle.setFont(new Font(7, Font._FONT_VERDANA, false));
		return detailNameStyle;
	}
	
	public static Style headerStyle()
	{
		Style headerStyle = new Style();
		headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
		headerStyle.setBorder(Border.THIN);
		headerStyle.setBackgroundColor(Color.gray);
		headerStyle.setTextColor(Color.white);
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setTransparency(Transparency.OPAQUE);
		return headerStyle;
	}
	
	public static Style headerStyle2()
	{
		Style headerStyle = new Style();
		headerStyle.setFont(Font.ARIAL_SMALL_BOLD);
		headerStyle.setBorder(Border.THIN);
		headerStyle.setBackgroundColor(Color.gray);
		headerStyle.setTextColor(Color.white);
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setTransparency(Transparency.OPAQUE);
		return headerStyle;
	}
	
	public static Style headerVariables()
	{
		Style headerVariables = new Style();
		headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
		headerVariables.setBorder(Border.PEN_1_POINT); // Ver
		headerVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
		headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);
		headerVariables.setBackgroundColor(Color.green);
		return headerVariables;
	}
	
	public static Style grandTotalStyle()
	{
		Style grandTotalStyle = new Style();
		grandTotalStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
		grandTotalStyle.setBorder(Border.PEN_1_POINT);
		grandTotalStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		grandTotalStyle.setVerticalAlign(VerticalAlign.BOTTOM);
		grandTotalStyle.setBackgroundColor(Color.cyan);
		return grandTotalStyle;
	}
	
	public static Style titleStyle()
	{
		Style titleStyle = new Style();
		titleStyle.setFont(new Font(18, Font._FONT_VERDANA, true));
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		return titleStyle;
	}
	
	public static Style importeStyle()
	{
		Style importeStyle = new Style();
		importeStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		return importeStyle;
	}
	
	public static Style oddRowStyle()
	{
		Style oddRowStyle = new Style();
		oddRowStyle.setBorder(Border.THIN);
		oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
		oddRowStyle.setTransparency(Transparency.OPAQUE);
		return oddRowStyle;
	}

}
