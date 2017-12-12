package com.institucion.fm.desktop.helper;

import com.institucion.fm.report.model.Color;



public class ColorViewHelper {
	
	public static String getImage(Color color){
		if(color!=null){
			if (color.equals(Color.BLUE))
				return "/img/icon/blue.png";
			else if (color.equals(Color.GREEN))
				return "/img/icon/green.png";
			else if (color.equals(Color.YELLOW)){
				return "/img/icon/yellow.png";
			}else if (color.equals(Color.RED)){
				return "/img/icon/red.png";
			}
		}
		return "";
	}
}
