package com.institucion.fm.filteradv.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.fm.filteradv.dao.OperatorDAO;

/** A non persistible CC */
public class CriteriaClauseFlow extends CriteriaClause {
	private static final long serialVersionUID = 1L;
	//lala    
	static Log log = LogFactory.getLog(CriteriaClauseFlow.class);   
	

	public CriteriaClauseFlow() {
		super();
	}

	public CriteriaClauseFlow(CriteriaClause cc) {
		this.setPredicates(cc.getPredicates());
	}

	public String getCriteria() {
		StringBuffer criteria = new StringBuffer();
		String bitwiseAux = null;
		//mapa donde guardo los predicados que ya agregue, para despues buscar si ya estan agregados
		Map<String,Object>predicatesAdded= new HashMap<String, Object>();
		Iterator<Predicate> itprd = super.getPredicates().iterator();
		//log.debug("ccf.predicates: " + super.getPredicates());
		int count=1;
		while (itprd.hasNext()) {
			Predicate predicate = itprd.next();
			if (predicate instanceof PredicateFlow) {
				PredicateFlow pf = (PredicateFlow)predicate;
				if (pf.getType() == PredicateFlow.FlowType.OPEN) {
					if (bitwiseAux != null) {
						criteria.append(bitwiseAux);
						bitwiseAux = null;
					}
					criteria.append("(");
				} else if (pf.getType() == PredicateFlow.FlowType.CLOSE) {
					criteria.append(")");
				}
			} else {
				if (bitwiseAux != null) {
					criteria.append(bitwiseAux);
					bitwiseAux = null;
				}
				String predicateStr ="";
				if(predicatesAdded.get(predicate.getFieldName())!=null){
					predicateStr= predicate.getCriteria(null,addExtension(count));
				}else{
					predicateStr= predicate.getCriteria(null,null);
				}

				//log.debug("ccf.predicateStr: " + predicateStr);
				criteria.append(predicateStr);
				if (itprd.hasNext()) {
					bitwiseAux = " "+predicate.getBitwise()+" ";
				}
			}

			//agrego el predicate al map
			predicatesAdded.put(predicate.getFieldName(), predicate);

		}



		if (criteria.length() == 0) {
			return "";
		}
		return "where "+criteria.toString();
	}
	
	public Map<String, Object> getCriteriaParameters() {
		Map<String, Object> params = new HashMap<String, Object>();
		int count=1;
		for (Predicate predicate : super.getPredicates()) {
			if (predicate instanceof PredicateFlow) {
				continue;
			}

			if(params.get(predicate.getFieldNameAsParameter(true))!=null){
				params.put(addExtension(count)+predicate.getFieldNameAsParameter(true), predicate.getExpression1AsParameter());
				if (predicate.getExpression2() != null) {
					params.put(addExtension(count)+predicate.getFieldNameAsParameter(false), predicate.getExpression2AsParameter());
				}
				count++;
			}else{
				params.put(predicate.getFieldNameAsParameter(true), predicate.getExpression1AsParameter());
				if (predicate.getExpression2() != null) {
					params.put(predicate.getFieldNameAsParameter(false), predicate.getExpression2AsParameter());
				}
			}


		}
		return params;
	}

	public Predicate setPredicate(Collection<?> values, String fieldName, Operator op) {
		Predicate predicate = new Predicate();
		StringBuffer buf = new StringBuffer();
		
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.STRING);
		predicate.setFieldName(fieldName);
		for (Iterator<?> iterator = values.iterator(); iterator.hasNext();) {
			Object object = (Object)iterator.next();
			buf.append(object.toString());
			if (iterator.hasNext()) {
				buf.append(",");
			}
		}
		predicate.setExpression1(buf.toString());

		predicate.setOperator(op);
		this.addPredicate(predicate);

		return predicate;
	}

	public Predicate setPredicate(String value, String fieldName, Operator op) {
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.STRING);
		predicate.setFieldName(fieldName);
		predicate.setExpression1(value);

		predicate.setOperator(op);
		this.addPredicate(predicate);
		
		return predicate;
	}
	
	public Predicate setPredicate(long value, String fieldName, Operator op) {
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.LONG);
		predicate.setFieldName(fieldName);
		predicate.setExpression1(Long.valueOf(value).toString());

		predicate.setOperator(op);
		this.addPredicate(predicate);
		
		return predicate;
	}
	
	public Predicate setPredicate( Enum<?> value, String fieldName, Operator op) {
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.ENUM);
		predicate.setFieldName(fieldName);
		predicate.setExpression1(value);

		predicate.setOperator(op);
		this.addPredicate(predicate);
		
		return predicate;
	}

	public Predicate setPredicate( Enum<?> value1, Enum<?> value2, String fieldName, Operator op) {
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.ENUM);
		predicate.setFieldName(fieldName);
		predicate.setExpression1(value1);
		predicate.setExpression2(value2);

		predicate.setOperator(op);
		this.addPredicate(predicate);
		
		return predicate;
	}

	public Predicate setPredicate(Integer value, String fieldName, Operator op) {
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.INTEGER);
		predicate.setFieldName(fieldName);
		predicate.setExpression1(value);

		predicate.setOperator(op);
		this.addPredicate(predicate);
		
		return predicate;
	}
	
	

	public Predicate setPredicate(boolean value, String fieldName) {
		Predicate predicate = new Predicate();
		predicate.setBitwise(Predicate.AND_BITWISE);
		predicate.setFieldType(Predicate.Type.BOOLEAN);
		predicate.setFieldName(fieldName);
		predicate.setExpression1(value);

		predicate.setOperator(OperatorDAO.instance().findByName("equal"));
		this.addPredicate(predicate);
		
		return predicate;
	}
	
	public void openAgrupation() {
		Predicate predicate = new PredicateFlow(PredicateFlow.FlowType.OPEN);
		this.addPredicate(predicate);
	}
	
	public void closeAgrupation() {
		Predicate predicate = new PredicateFlow(PredicateFlow.FlowType.CLOSE);
		this.addPredicate(predicate);
	}
	
}
