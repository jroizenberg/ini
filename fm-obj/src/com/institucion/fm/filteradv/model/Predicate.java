package com.institucion.fm.filteradv.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.fm.filteradv.dao.OperatorDAO;
import com.institucion.fm.lang.ExtDate;
import com.institucion.fm.util.ObjectSerializator;
import com.institucion.model.Actividad;
import com.institucion.model.TipoCursoEnum;

public class Predicate implements Serializable, Comparable<Predicate>{
	private static Log log = LogFactory.getLog(Predicate.class);
	
	private static final long serialVersionUID = 1L;
//	public static final String NULLSTR = "NULL";

	public static final String EXP1_TERMINTOR = "___1";
	public static final String EXP2_TERMINTOR = "___2";
	public enum Type {STRING, INTEGER, LONG, DECIMAL, DATE, STATE, DT, BOOLEAN, ENUM, HEALTHPROF_CONTACT_TYPE, FREQUENCY, ROUTING_TYPE, DAY_OF_WEEK, 
		WEEK_OF_MONTH, PHARMACY_CONTACT_TYPE, CLIENT_TYPE, ACTIVITY_TYPE, INSTITUTION_CONTACT_TYPE, ATTITUDINAL_SEGMENTATION_TYPE, ADDRESS_TYPE, CLIENT, CONTACT_TYPE,
		ADDRESS, PORTFOLIO, SPECIALLITY, PROMOTIONZONE, SEGMENT, CORPORATIVELINE, PROMOTIONLINE,DATEBOXREMEMBER, ACTIVIDAD, TIPO_CURSO}
	
	public static final String AND_BITWISE = "AND";
	public static final String OR_BITWISE  = "OR";
	private final String UNDERSCORE="_";
	private final String ESCAPE_UNDERSCORE="\\_";
	private Long id;
	private int order;
	private String fieldName;
	private Type fieldType;
	private Operator operator;
	private String expression1;
	private String expression2;
	private String bitwise;
	private boolean isSoft = false;
	private CriteriaClause criteriaClause;

	public Predicate() {}

	public Long getId() { return id; }
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public void setOrder(int order) { this.order = order; }
	public int getOrder() { return order; }

	public String getFieldName() { return fieldName; }
	public void setFieldName(String fieldName) { this.fieldName = fieldName; }

	public Type getFieldType() { return fieldType; }
	public void setFieldType(Type fieldType) { this.fieldType = fieldType; }

	public void setFieldTypeEnumStr(String fieldType) { this.fieldType = Type.valueOf(fieldType); }
	public String getFieldTypeEnumStr() { return fieldType.toString(); }

	public Operator getOperator() { return operator; }
	public void setOperator(Operator operator) { this.operator = operator; }

	public String getOperatorXML() { return operator.getName(); }
	public void setOperatorXML(String operator) { this.operator = OperatorDAO.instance().findByName(operator); }

	public void setSoft(boolean isSoft) { this.isSoft = isSoft; }
	public boolean isSoft() { return isSoft; }

	private String getStringExpression(Object expression){
		if (expression == null)
			return null;
		if (expression instanceof String)
			return (String) expression;
		else if (expression instanceof Integer)
			return ((Integer) expression).toString();
		else if (expression instanceof Long)
			return ((Long) expression).toString();
		else if (expression instanceof Double)
			return ((Double) expression).toString();
//		else if (expression instanceof DomainTableDetail)
//			return ObjectSerializator.serialization(expression);
		else if (expression instanceof ExtDate)
			return ((ExtDate) expression).toSQLString();
		else if (expression instanceof Date){
			ExtDate extDate = new ExtDate((Date)expression);
			return extDate.toSQLString();
		} 
//		else if (expression instanceof FEState)
//			return ((FEState) expression).toString();
		else if (expression instanceof Boolean)
			return ((Boolean) expression).toString();
//		else if (expression instanceof ActionType)
//			return ((ActionType) expression).toString();
//		else if (expression instanceof ProductType)
//			return ((ProductType) expression).toString();
		
		
	
//		else if (expression instanceof PromotionZone)
//			return ((PromotionZone) expression).getDescription().toString();
//
//
//		else if (expression instanceof CorporativeLine)
//			return new Integer(((CorporativeLine) expression).toInt()).toString();
//		else if (expression instanceof PromotionLine)
//			return ((PromotionLine) expression).getId().toString();
		
		else if (expression instanceof Actividad)
			return ((Actividad) expression).getId().toString();
		
		else if (expression instanceof TipoCursoEnum)
			return ((TipoCursoEnum) expression).toString();
		
		
		
		else
			throw new IllegalArgumentException("Predicate.class no pudo resolver la expresión '"+expression.toString()+"'");
	}
	
	public String getExpression1() { return expression1; }
	public void setExpression1(String expression1) { this.expression1 = expression1; }
	
	public void setExpression1(Enum<?> expression1){ 
		setExpression1(ObjectSerializator.serialization(expression1));
	}
	
	public void setExpression1(Object expression1){
		if (expression1 == null)
			this.expression1 = null;
		else
			setExpression1(getStringExpression(expression1));
	}

	public String getExpression2() { return expression2; }
	
	public void setExpression2(String expression2) { this.expression2 = expression2; }

	public void setExpression2(Object expression2){
		if (expression2 == null)
			this.expression2 = null;
		else
			setExpression2(getStringExpression(expression2));
	}
	
	public void setExpression2(Enum<?> expression2){ 
		setExpression2(ObjectSerializator.serialization(expression2));
	}

//	private DomainTableDetail deserializationDTD(String dtd){
//		try	{
//			byte[] decoded = Base64Coder.decode(dtd);
//			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decoded));
//			Object obj = in.readObject();
//			return (DomainTableDetail) obj;
//		}
//		catch (IOException e){
//			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
//			throw new IllegalArgumentException(e);
//		}
//		catch (ClassNotFoundException e){
//			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
//			throw new IllegalArgumentException(e);
//		}
//	}

	public String getBitwise() { return bitwise; }
	public void setBitwise(String bitwise) { this.bitwise = bitwise; }

	public CriteriaClause getCriteriaClause() { return criteriaClause; }
	public void setCriteriaClause(CriteriaClause criteriaClause) { this.criteriaClause = criteriaClause; }

	// ---------------------------------------------------------------------------
	// Sección de parámetros. Se solapa un poco con la clase getExpressionAsObject
	// ---------------------------------------------------------------------------
	
	private String replaceFieldNameChars(String fieldName) {
		String newString;
		
		newString = fieldName.replace(".", "");
		newString = newString.replace("(", "");
		newString = newString.replace(")", "");
		return newString;
	}

	public String getFieldNameAsParameter(boolean one) {
		return (one) ? replaceFieldNameChars(this.getFieldName())+EXP1_TERMINTOR : replaceFieldNameChars(this.getFieldName())+EXP2_TERMINTOR;
	}
	public Object getExpression1AsParameter() {
		return this.processExpression(this.getExpression1());
	}
	
	public Object getExpression2AsParameter() {
		return this.processExpression(this.getExpression2());
	}

	private Object processExpression(String expr){
		if (expr == null)
			return null;
		switch (getFieldType()){
			case STRING:
				String s = expr;
				if (s.contains(UNDERSCORE)){
					s = s.replace(UNDERSCORE,ESCAPE_UNDERSCORE);
				}
				String pattern = operator.getPattern();
				if (pattern.indexOf("%EXP1%") != -1 || pattern.indexOf("%EXP2%") != -1 || pattern.indexOf("%EXP%") != -1) {
					return "%" + s + "%";
				} else if (pattern.indexOf("%EXP1") != -1 || pattern.indexOf("%EXP2") != -1 || pattern.indexOf("%EXP") != -1) {
					return "%" + s;
				} else if (pattern.indexOf("EXP1%") != -1 || pattern.indexOf("EXP2%") != -1 || pattern.indexOf("EXP%") != -1) {
					return s + "%";
				}
				return s;
//			case STATE:
//				return FEState.valueOf(expr);
//			case DT:
//				if (isSoft())
//					return this.deserializationDTD(expr).getId();
//				return this.deserializationDTD(expr);
			case LONG:
				return Long.parseLong(expr);
			case INTEGER:
				return Integer.parseInt(expr);
			case DECIMAL:
				return Double.parseDouble(expr);
			case ENUM:
				return ObjectSerializator.deserialization(expr);
			case DATE:
				ExtDate date = new ExtDate();
				date.setSQLString(expr);
				return date;
			case BOOLEAN:
				return Boolean.parseBoolean(expr);
				
			case CLIENT:  
				s = expr;
				if (s.contains(UNDERSCORE)){
					s = s.replace(UNDERSCORE,ESCAPE_UNDERSCORE);
				}
				pattern = operator.getPattern();
				if (pattern.indexOf("%EXP1%") != -1 || pattern.indexOf("%EXP2%") != -1 || pattern.indexOf("%EXP%") != -1) {
					return "%" + s + "%";
				} else if (pattern.indexOf("%EXP1") != -1 || pattern.indexOf("%EXP2") != -1 || pattern.indexOf("%EXP") != -1) {
					return "%" + s;
				} else if (pattern.indexOf("EXP1%") != -1 || pattern.indexOf("EXP2%") != -1 || pattern.indexOf("EXP%") != -1) {
					return s + "%";
				}
				return s;
			case ADDRESS: s = expr;
				if (s.contains(UNDERSCORE)){
					s = s.replace(UNDERSCORE,ESCAPE_UNDERSCORE);
				}
				pattern = operator.getPattern();
				if (pattern.indexOf("%EXP1%") != -1 || pattern.indexOf("%EXP2%") != -1 || pattern.indexOf("%EXP%") != -1) {
					return "%" + s + "%";
				} else if (pattern.indexOf("%EXP1") != -1 || pattern.indexOf("%EXP2") != -1 || pattern.indexOf("%EXP") != -1) {
					return "%" + s;
				} else if (pattern.indexOf("EXP1%") != -1 || pattern.indexOf("EXP2%") != -1 || pattern.indexOf("EXP%") != -1) {
					return s + "%";
				}
				return s;
			case PORTFOLIO: 
				s = expr;
				if (s.contains(UNDERSCORE)){
					s = s.replace(UNDERSCORE,ESCAPE_UNDERSCORE);
				}
				pattern = operator.getPattern();
				if (pattern.indexOf("%EXP1%") != -1 || pattern.indexOf("%EXP2%") != -1 || pattern.indexOf("%EXP%") != -1) {
					return "%" + s + "%";
				} else if (pattern.indexOf("%EXP1") != -1 || pattern.indexOf("%EXP2") != -1 || pattern.indexOf("%EXP") != -1) {
					return "%" + s;
				} else if (pattern.indexOf("EXP1%") != -1 || pattern.indexOf("EXP2%") != -1 || pattern.indexOf("EXP%") != -1) {
					return s + "%";
				}
				return s;
			
//			case FREQUENCY: return Frequency.valueOf(expr); 
//			case HEALTHPROF_CONTACT_TYPE:  return HealthProfessionalContactType.valueOf(expr);// Type.HEALTHPROF_CONTACT_TYPE;
//			case ROUTING_TYPE: return RoutingType.valueOf(expr);
//			case DAY_OF_WEEK: return  DayOfWeek.valueOf(expr);
//			case WEEK_OF_MONTH: return WeekOfMonth.valueOf(expr);
//			case PHARMACY_CONTACT_TYPE: return PharmacyContactType.valueOf(expr);
//			case CLIENT_TYPE: return ClientType.valueOf(expr);
//			case CONTACT_TYPE: return  ClientType.valueOf(expr);
//			case ACTIVITY_TYPE: return ActivityType.valueOf(expr);
//			case INSTITUTION_CONTACT_TYPE: return InstitutionContactType.valueOf(expr);
//			case ATTITUDINAL_SEGMENTATION_TYPE: return AttitudinalSegmentationType.valueOf(expr);
//
//			case ADDRESS_TYPE: return HealthProfessionalContactType.valueOf(expr);
//			
//			case SPECIALLITY: return this.deserializationDTD(expr);
//			case SEGMENT: return this.deserializationDTD(expr);
			case PROMOTIONZONE:  
				s = expr;
				if (s.contains(UNDERSCORE)){
					s = s.replace(UNDERSCORE,ESCAPE_UNDERSCORE);
				}
				pattern = operator.getPattern();
				if (pattern.indexOf("%EXP1%") != -1 || pattern.indexOf("%EXP2%") != -1 || pattern.indexOf("%EXP%") != -1) {
					return "%" + s + "%";
				} else if (pattern.indexOf("%EXP1") != -1 || pattern.indexOf("%EXP2") != -1 || pattern.indexOf("%EXP") != -1) {
					return "%" + s;
				} else if (pattern.indexOf("EXP1%") != -1 || pattern.indexOf("EXP2%") != -1 || pattern.indexOf("EXP%") != -1) {
					return s + "%";
				}
				return s;
//			case CORPORATIVELINE:
//				return CorporativeLine.fromInt(new Integer (expr));//expr;
			case PROMOTIONLINE:  
				return Long.parseLong(expr);
			case DATEBOXREMEMBER:
				ExtDate datebox = new ExtDate();
				datebox.setSQLString(expr.substring(0, 8)+"000000");
				return datebox;
				
			case ACTIVIDAD:  
				return Long.parseLong(expr);

			case TIPO_CURSO:  
				return TipoCursoEnum.valueOf(expr);
				
			default:
				throw new IllegalArgumentException("El tipo '"+this.getFieldType()+"' no está programada en el case.");
		}
	}
	// -------------------------------------------------------------------------
	// Fin Sección de parámetros.
	// -------------------------------------------------------------------------

	public Object getExpression1AsObject(){
		return getExpressionAsObject(expression1);
	}

	public Object getExpression2AsObject(){
		return getExpressionAsObject(expression2);
	}

	private Object getExpressionAsObject(String expression){
		if (expression == null)
			return null;
//		if (expression.equals(NULLSTR))
//			return null;
		switch (getFieldType()){
			case STRING: return expression;
//			case STATE:  return FEState.valueOf(expression);
//			case DT:     return this.deserializationDTD(expression);
			case LONG:   return Long.parseLong(expression);
			case INTEGER:   return Integer.parseInt(expression);
			case ENUM:   return ObjectSerializator.deserialization(expression);
			case DECIMAL:return Double.parseDouble(expression);
			case DATE:
				ExtDate date = new ExtDate();
				date.setSQLString(expression);
				return date;
			case BOOLEAN: return Boolean.parseBoolean(expression);
			
			case CLIENT:  return expression;
			case ADDRESS: return expression;
			case PORTFOLIO: return expression;
			 
//			case FREQUENCY: return Frequency.valueOf(expression); 
//			case HEALTHPROF_CONTACT_TYPE:  return HealthProfessionalContactType.valueOf(expression);// Type.HEALTHPROF_CONTACT_TYPE;
//			case ROUTING_TYPE: return RoutingType.valueOf(expression);
//			case DAY_OF_WEEK: return  DayOfWeek.valueOf(expression);
//			case WEEK_OF_MONTH: return WeekOfMonth.valueOf(expression);
//			case PHARMACY_CONTACT_TYPE: return PharmacyContactType.valueOf(expression);
//			case CLIENT_TYPE: return ClientType.valueOf(expression);
//			case CONTACT_TYPE: return ClientType.valueOf(expression);
//			case ACTIVITY_TYPE: return ActivityType.valueOf(expression);
//			case INSTITUTION_CONTACT_TYPE: return InstitutionContactType.valueOf(expression);
//			case ATTITUDINAL_SEGMENTATION_TYPE: return AttitudinalSegmentationType.valueOf(expression);
//
//			case ADDRESS_TYPE: return HealthProfessionalContactType.valueOf(expression);
//			
//			case SPECIALLITY: return this.deserializationDTD(expression);
//			case SEGMENT: return this.deserializationDTD(expression);
			case PROMOTIONZONE:  return expression;
//			case CORPORATIVELINE: return CorporativeLine.valueOf(expression);
			case PROMOTIONLINE: return expression;
			case ACTIVIDAD: return expression;
			case TIPO_CURSO:return TipoCursoEnum.valueOf(expression);
			case DATEBOXREMEMBER: return expression;
			default:
				throw new IllegalArgumentException("El tipo '"+this.getFieldType()+"' no está programada en el case.");
		}
	}

	public String getCriteria(){
		return getCriteria(null,null);
	}

	public String getCriteria(String tableAlias,String useDuplicatePrefix){
		String pattern = operator.getPattern();

		if (isSoft()){
			String fieldsId = "";

			switch (fieldType){
				case STRING:  fieldsId = "fields.id"; break;
				case INTEGER: fieldsId = "cast(fields.id as integer)"; break;
				case LONG:    fieldsId = "cast(fields.id as long)"; break;
				case DECIMAL: fieldsId = "cast(fields.id as double)"; break;
				case BOOLEAN: fieldsId = "cast(fields.id as boolean)"; break;
	
				case DATE:    fieldsId = "to_timestamp(fields.id, 'YYYYMMDDHH24MISS')"; break;
				case DT:      fieldsId = "cast(fields.id as long)"; break;
				case ADDRESS: fieldsId = "adfields.id"; break;
				case DATEBOXREMEMBER:	fieldsId = "cast (to_timestamp(fields.id, 'DD/MM') as timestamp) "; break; 
				
				default:
					throw new IllegalArgumentException("Predicate.class no pudo resolver el campo 'flexible' de tipo '"+fieldType.toString()+"'");
			}

			String fieldName = replaceFieldNameChars(this.fieldName);
			pattern = pattern.replaceAll("FIELD", fieldsId);
			if(useDuplicatePrefix!=null){
				String fieldnameFixed=useDuplicatePrefix+fieldName;
				pattern = getExpressionPattern(pattern, fieldnameFixed);
			}else{
				pattern = getExpressionPattern(pattern, fieldName);
			}
			//where (index(fields) = 'phones' AND upper(fields.id) LIKE upper(:phones___1)) OR (index(fields) = 'VAL' AND upper(addresses.fields.VAL) LIKE upper(:VAL___1))
			if(fieldType == Type.ADDRESS)
				pattern = "("+pattern+")";  //"	(upper(adfields.id) LIKE upper(:VAL___1))" +
			else
				pattern = "(index(fields) = '"+fieldName+"' AND "+pattern+")";
			
		}else{
			if (tableAlias == null)
				pattern = pattern.replaceAll("FIELD", fieldName);
			else
				pattern = pattern.replaceAll("FIELD", tableAlias+"."+fieldName);

			String fieldName = replaceFieldNameChars(this.fieldName);
			if(useDuplicatePrefix!=null){
				String fieldnameFixed=useDuplicatePrefix+fieldName;
				pattern = getExpressionPattern(pattern, fieldnameFixed);
			}else{
				pattern = getExpressionPattern(pattern, fieldName);
			}

		}
		return pattern;
	}


	private String getExpressionPattern(String pattern, String fieldName){
		if (fieldType == Type.STRING || fieldType == Type.ADDRESS) {
			pattern = this.replaceString(pattern, "EXP2", 2, fieldName);
			pattern = this.replaceString(pattern, "EXP1", 1, fieldName);
			pattern = this.replaceString(pattern, "EXP", 1, fieldName);
		} else {
			pattern = this.replaceObject(pattern, "EXP2", 2, fieldName);
			pattern = this.replaceObject(pattern, "EXP1", 1, fieldName);
			pattern = this.replaceObject(pattern, "EXP", 1, fieldName);
		}
		return pattern;
	}

	private String replaceString(String original, String EXP, int idx, String fieldName) {
		String terminator = (idx == 2) ? EXP2_TERMINTOR : EXP1_TERMINTOR;
		String s = original.replaceAll("%"+EXP+"%", ":"+ fieldName+terminator);
		s = s.replaceAll(EXP+"%", ":"+ fieldName+terminator);
		s = s.replaceAll("%"+EXP, ":"+ fieldName+terminator);
		s = s.replaceAll(EXP, ":"+ fieldName+terminator);
		return s;
	}
	
	private String replaceObject(String original, String EXP, int idx, String fieldName) {
		String terminator = (idx == 2) ? EXP2_TERMINTOR : EXP1_TERMINTOR;
		String s = original.replaceAll(EXP, ":"+ fieldName+terminator);
		return s;
	}

	public int compareTo(Predicate predicate){
		if (order > predicate.getOrder())
			return 1;
		else if (order < predicate.getOrder())
			return -1;
		else
			return 0;
	}
}