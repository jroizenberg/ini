package com.institucion.fm.filteradv.model;

public class PredicateFlow extends Predicate {
	private static final long serialVersionUID = 1L;
	private FlowType type;
	
	public enum FlowType {
		OPEN,
		CLOSE
	}

	public PredicateFlow(FlowType type) {
		this.type = type;
	}
	
	public FlowType getType() {
		return this.type;
	}
	
	public String toString() {
		return "PredicateFlow type [" + this.type + "]";
	}
	
	@Override
	public String getFieldName() {
		if (this.type == FlowType.OPEN) {
			return "predicateFlow.open";
		}
		return "predicateFlow.close";
	}

}
