// File: StateList.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1998, 1999, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.Hashtable;

import msgtool.common.BGColorManager;

@SuppressWarnings("serial")
public final class StateList extends Panel {

	public StateList(int rows, boolean multipleMode) {
        super();
        fList = new List(rows, multipleMode);
		BGColorManager.getInstance().add(fList);
        fStateTable = new Hashtable<String,State>();
        
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setLayout(gridBag);
        
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        
        gridBag.setConstraints(fList, constraints);
        add(fList);
        }
    /**
     * Adds the specified item to the end of scrolling list.
     */
    public void add(String item) {
        fStateTable.put(item, new State(item));
        fList.add("  " + item);
        }
    /**
     * Adds the specified item to the end of the scrolling list. 
     */
    public void add(String item, int index) {
        fStateTable.put(item, new State(item));
        fList.add("  " + item, index);
        }
    /**
     * Adds the specified mouse listener to receive mouse events from this component.
     */
    public void addMouseListener(MouseListener l) {
        fList.addMouseListener(l);
        }
   	/**
	 * Addes the specified mouse motion listener to receive mose motion events from this component 
	 */
 	public void addMouseMotionListener(MouseMotionListener l) {
		fList.addMouseMotionListener(l);	
	}

    /**
     * Deselects the item at the specified index.
     */
    public void deselect(int index) {
        fList.deselect(index);
        }      
    /**
     *  Gets the items in the list.
     */
    public String getItem(int index) {
        String  item = fList.getItem(index);
        
        if (item == null)
            return(null);
        else
            return(item.substring(2));
        }
    /**
     *  Gets the number of items in the list.
     */
    public int getItemCount() {
        return(fList.getItemCount());
        }
    /**
     * get a position of an item from the list
     */
    public int getItemPosition(String item) {
        int count = fList.getItemCount();
        
        if (count == 0)
            return(-1);
        
        for (int i = 0; i < count; i++) {
            if (item.equals(fList.getItem(i).substring(2)))
                return(i);
            }
        return(-1);
        }    
    /**
     * Gets the items in the list.
     */
    public String[] getItems() {
        String[] items = fList.getItems();
        
        if (items == null || items.length == 0)
            return(null);
        
        for (int i = 0; i < items.length; i++)
            items[i] = items[i].substring(2);
      
        return(items);
        }
    /**
     * Gets the index of the selected item on the list
     */
    public int getSelectedIndex() {
        return(fList.getSelectedIndex());
        }
    /**
     * Gets the selected indexes on the list.
     */
    public int[] getSelectedIndexes() {
        return(fList.getSelectedIndexes());
        }
    /**
     *  Get the selected item on this scrolling list.
     */
    public String getSelectedItem() {
        String  selectedItem = fList.getSelectedItem();
        
        if (selectedItem == null)
            return(null);
        else
            return(selectedItem.substring(2));
        }          
    /**
     * Remove the item at the specified position from this scrolling list. 
     */
    public void remove(int position) {
        String item = fList.getItem(position).substring(2);
        
        fList.remove(position);
        fStateTable.remove(item);
        } 
    /**
     * Removes the first occurrence of an item from the list.
     * @exception IllegalArgumentException if the item doesn't exist in the list.
     */
    public void remove(String item) {
        int position = getItemPosition(item);
        
        if (position == -1) {
            throw new IllegalArgumentException();
            }
        fList.remove(position);
        fStateTable.remove(item);
        }
    /**
     * Removes all items from this list.
     */
    public void removeAll() {
        fList.removeAll();
        fStateTable.clear();
        }
    /**
     * Selects the item at the specified index in the scrolling list.
     */
    public void select(int index) {
        fList.select(index);
        } 
    /**
     * Set a beNotThere status to the specified item.
     */
    public void setNotBeThere(
        String  item,
        boolean notBeThere) {
        State state = fStateTable.get(item);
        
        if (state != null &&
            state.notBeThere != notBeThere) {
            state.notBeThere = notBeThere;
            updateItemState(state);
            }
        }
    /**
     * Set a messageWaiting status to the specified item.
     */
    public void setMessageWaiting(
        String  item,
        boolean messageWaiting) {
        State   state = fStateTable.get(item);
        
        if (state != null && 
            state.messageWaiting != messageWaiting) {
            state.messageWaiting = messageWaiting;
            updateItemState(state);
            }
        }
    /**
     * clear All message waitings
     */
    public void clearAllMessageWaitings() {
        Collection<State> states = fStateTable.values();
        
        for (State state: states) {
            if (state.messageWaiting) {
                state.messageWaiting = false;
                updateItemState(state);
                }
            }
        }    
    //
    // Private methods
    //    

    /**
     * Updates the state of an item
     */
    private void updateItemState(State state) {
        int     position = getItemPosition(state.item);

        if (position == -1)
            throw new IllegalArgumentException("updateItemState");
       
        if (state.messageWaiting)
            fList.replaceItem("! " + state.item, position);
        else if (state.notBeThere)
            fList.replaceItem("x " + state.item, position);
        else
            fList.replaceItem("  " + state.item, position);
        }
    /**
     * Inner class to represent state
     */
    private class State {
        public boolean      messageWaiting;
        public boolean      notBeThere;
        public final String item;
        
        public State(String item) {
            messageWaiting  = false;
            notBeThere      = false;
            this.item       = item;
            }
        }
        
    /** hash table to hold states */    
    private Hashtable<String, State> fStateTable   = null;
    /** list to show */
    private List    fList   = null;
    }

// LOG
// 1.90 : 21-Feb-98 Y.Shibata   created
// 2.19 : 27-Mar-99	Y.Shibata	used BGColorManager
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics


