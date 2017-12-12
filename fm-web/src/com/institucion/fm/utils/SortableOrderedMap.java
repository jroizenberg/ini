package com.institucion.fm.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.map.ListOrderedMap;

@SuppressWarnings("serial")
public class SortableOrderedMap extends ListOrderedMap{
	/**
	   * Inserts a given Key/Value-Pair on a certain Position
	   * alle following Pairs are moved up accordingly 
	   * 
	   * @param newKey
	   * @param newValue
	   * @param pos
	   * @return
	   */
	  public Object putOnPosition(Object newKey, Object newValue, int pos) {
	    Object res = put(newKey, newValue);
	    while (indexOf(newKey) > pos) {
	      moveUp(newKey);
	    }
	    return res;
	  }

	  /**
	   * Inserts a given Key/Value Pair before position of a given Key
	   * 
	   * @param newKey
	   * @param newValue
	   * @param oldKey
	   * @return
	   */
	  public Object putBefore(Object newKey, Object newValue, Object oldKey) {
	    if (!containsKey(oldKey)) {
	      return put(newKey, newValue);
	    }
	    return putOnPosition(newKey, newValue, indexOf(oldKey));
	  }

	  /**
	   * 
	   * inserts a given key/value pair after the position of a given key
	   * 
	   * @param newKey
	   * @param newValue
	   * @param oldKey
	   * @return
	   */
	  public Object putAfter(Object newKey, Object newValue, Object oldKey) {
	    if (!containsKey(oldKey)) {
	      return put(newKey, newValue);
	    }
	    return putOnPosition(newKey, newValue, indexOf(oldKey)+1);
	  }
		
	  /**
	   * moves item with given key up by one step. Does nothing
	   * if the item is already on top or not existend.
	   * 
	   * @param the key of the Item to be moved
	   */
	  public synchronized void moveUp(Object key) {

	    // return if already on top or key not in map	
	    if (key.equals(firstKey()) || !containsKey(key)) {
	      return;
	    }
	    int pcur = indexOf(key);

	    insertOrder.set(pcur, previousKey(key));
	    insertOrder.set(pcur - 1, key);
	  }

	  /**
	   * moves item with given key down by one step. Does nothing
	   * if the item is already on bottom or not existend.
	   * 
	   * @param the key of the Item to be moved
	   */
	  public synchronized void moveDown(Object key) {
	    if (key.equals(lastKey()) || !containsKey(key)) {
	      return;
	    }
	    int pcur = indexOf(key);
	    insertOrder.set(pcur, nextKey(key));
	    insertOrder.set(pcur + 1, key);
	  }

	  /* (non-Javadoc)
	   * @see org.apache.commons.collections.map.ListOrderedMap#asList()
	   */
	  public List asList() {
	    ArrayList v = new ArrayList();
	    for (Iterator it = values().iterator(); it.hasNext();) {
	      v.add(it.next());
	    }
	    return v;
	  }

		
	  /* (non-Javadoc)
	   * @see java.lang.Object#toString()
	   */
	  public String toString() {
	    StringBuffer b = new StringBuffer();
	    for (Iterator it = values().iterator(); it.hasNext();) {
	      b.append(it.next().toString() + "\n");
	    }  
	    return b.toString();
	  }
}
