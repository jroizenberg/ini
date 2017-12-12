package com.institucion.fm.filteradv.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.institucion.fm.security.model.User;

/**
 * Las consultas avanzadas se guardan en una clausula. Una clausula esta compuesta
 * por uno o mas predicados.
 */
public class CriteriaClause implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String selectorPage;
	private User user;

	private Set<Predicate> predicates;

	
	
	
	public CriteriaClause(String name) {
		super();
		this.name = name;
	}

	public CriteriaClause()
	{
		predicates = new LinkedHashSet<Predicate>();
	}

	public CriteriaClause(String name, String selectorPage)
	{
		this();
		setName(name);
		setSelectorPage(selectorPage);
	}

	public Long getId() { return id; }
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name.trim(); }

	public String getSelectorPage() { return selectorPage; }
	public void setSelectorPage(String selectorPage) { this.selectorPage = selectorPage; }

	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }

	public void addCriteria(CriteriaClause criteria)
	{
		Iterator<Predicate> itprd = criteria.getPredicates().iterator();
		while (itprd.hasNext())
		{
			Predicate prd = itprd.next();
			addPredicate(prd);
		}
	}

	public void addPredicate(Predicate predicate)
	{
		predicate.setOrder(predicates.size());
		predicates.add(predicate);
	}

	public Set<Predicate> getPredicates()
	{
		return predicates;
	}
	public void setPredicates(Set<Predicate> predicates)
	{
		this.predicates = predicates;
	}

	/**
	 * Devuelve un iterador de predicados ordendo por el campo 'order' de predicate.
	 */
	public Iterator<Predicate> getPredicatesIterator()
	{
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Iterator<Predicate> itPredicate = predicates.iterator();
		while (itPredicate.hasNext())
			predicateList.add(itPredicate.next());
		Collections.sort(predicateList);
		return predicateList.iterator();
	}

	/**
	 * Devolver solamente los predicados. Ejemplo: WHERE predicado1 AND predicad2 OR predicado3 
	 */
	public String getCriteria()
	{
		return getCriteria(null);
	}

	/**
	 * Devolver solamente los predicados. Ejemplo: WHERE predicado1 AND predicad2 OR predicado3 
	 */
	public String getCriteria(String tableAlias){
		StringBuffer criteria = new StringBuffer();

		//mapa donde guardo los predicados que ya agregue, para despues buscar si ya estan agregados
		Map<String,Object>predicatesAdded= new HashMap<String, Object>();
		
		Iterator<Predicate> itprd = predicates.iterator();
		int count=1;
		while (itprd.hasNext()){
			Predicate predicate = itprd.next();
			String predicateStr ="";
			if(predicatesAdded.get(predicate.getFieldName())!=null){
				predicateStr	= predicate.getCriteria(tableAlias,addExtension(count));
				count++;
			}else{
				predicateStr	= predicate.getCriteria(tableAlias,null);
			}
		
			criteria.append(predicateStr);
			if (itprd.hasNext())
				criteria.append(" "+predicate.getBitwise()+" ");
			
			//agrego el predicate al map
			predicatesAdded.put(predicate.getFieldName(), predicate);
		}

		if (criteria.length() == 0)
			return "";
		return "where "+criteria.toString();
	}

	public Map<String, Object> getCriteriaParameters() {
		Map<String, Object> params = new HashMap<String, Object>();
		int count=1;
		for (Predicate predicate : this.predicates) {
			
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
	
	
	protected String addExtension(int terminador){
		 NumberFormat formatter = new DecimalFormat("A00");
		 return formatter.format(terminador)+"_";
	}
	
}