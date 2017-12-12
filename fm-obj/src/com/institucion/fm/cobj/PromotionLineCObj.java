package com.institucion.fm.cobj;

import java.io.Serializable;

public class PromotionLineCObj implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String promotionLineDescrition;
	private Long id;
	
	
	
	public PromotionLineCObj(String promotionLineDescrition, Long id) {
		super();
		this.promotionLineDescrition = promotionLineDescrition;
		this.id = id;
	}



	public String getPromotionLineDescrition() {
		return promotionLineDescrition;
	}



	public void setPromotionLineDescrition(String promotionLineDescrition) {
		this.promotionLineDescrition = promotionLineDescrition;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.promotionLineDescrition;
	}
	
	


}

