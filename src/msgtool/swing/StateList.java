// File: StateList.java - last edit:
// Yoshiki Shibata 21-Feb-98

// Copyright (c) 1998 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public final class StateList extends JPanel {

	public StateList(
        int     rows, 
        boolean multipleMode,
        Color   textBackgroundColor) {
 	    this(rows, multipleMode);
		setBackground(textBackgroundColor);
		}

    public StateList(
        int     rows, 
        boolean multipleMode) {
        super();
        fListModel = new StateListModel();
        fList      = new JList(fListModel);
        fList.setVisibleRowCount(rows);
		fStateRenderer = new StateRenderer();
        fList.setCellRenderer(fStateRenderer);
        if (multipleMode)
            fList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        else
            fList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setLayout(gridBag);
        
        JScrollPane pane = new JScrollPane();
        pane.getViewport().add(fList);
        
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        
        gridBag.setConstraints(pane, constraints);
        add(pane);
		fInitialized = true;
        }
    
   	public StateList(int rows) {
 	    this(rows, false);
		fSimpleList = true;
		}

    public StateList(int rows, Color textBackgroundColor) {
		this(rows);
		setBackground(textBackgroundColor);
        }

  	public void setBackground(Color color) {
		if (fInitialized) {
			fStateRenderer.setTextBackground(color);
			fList.setBackground(color);
			}
	  	else
			super.setBackground(color);
	 	}

	public Color getBackground() {
		if (fInitialized) {
			return(fStateRenderer.getTextBackground());
			}
	   	else
			return(super.getBackground());
		}
    /**
     * Adds the specified item to the end of scrolling list.
     */
    public void add(String item) {
        fListModel.addElement(new State(item));
        }
    /**
     * Adds the specified item to the end of the scrolling list. 
     */
    public void add(String item, int index) {
        fListModel.add(index, new State(item));
        }
    /**
     * Add a listener to the list that's notified each time a change to the selection
     * occurs. Listeners added directly to the JList will have their
     * ListSelectionEvent.getSource() == this JList (instead of the ListSelectionModel).
     */
    public void addListSelectionListener(ListSelectionListener listener) {
        fList.addListSelectionListener(listener);
        }
    /**
     * Adds the specified mouse listener to receive mouse events from this component.
     */
    public void addMouseListener(MouseListener l) {
        fList.addMouseListener(l);
        }
  	/**
	 * Adds the Mouse Motion Listener
	 */
  	public void addMouseMotionListener(MouseMotionListener l) {
		fList.addMouseMotionListener(l);
		}
    /**
     * Deselects the item at the specified index.
     */
    public void deselect(int index) {
        fList.removeSelectionInterval(index, index);
        }      
    /**
     *  Gets the items in the list.
     */
    public String getItem(int index) {
        State state = (State) fListModel.getElementAt(index);
        
        if (state == null)
            return(null);
        else
            return(state.item);
        }
    /**
     *  Gets the number of items in the list.
     */
    public int getItemCount() {
        return(fListModel.getSize());
        }
    /**
     * get a position of an item from the list
     */
    public int getItemPosition(String item) {
        int count = getItemCount();
        
        if (count == 0)
            return(-1);
        
        for (int i = 0; i < count; i++) {
            if (item.equals(getItem(i)))
                return(i);
            }
        return(-1);
        }    
    /**
     * Gets the items in the list.
     */
    public String[] getItems() {
        int count = getItemCount();
        
        if (count == 0)
            return(null);
            
        String[] items = new String[count];
        for (int i = 0; i < count; i++)
            items[i] = getItem(i);
      
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
        return(fList.getSelectedIndices());
        }
    /**
     *  Get the selected item on this scrolling list.
     */
    public String getSelectedItem() {
        State state = (State) fList.getSelectedValue();
        
        if (state == null)
            return(null);
        else
            return(state.item);
        }          
    /**
     * Remove the item at the specified position from this scrolling list. 
     */
    public void remove(int position) {
        fListModel.removeElementAt(position);
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
        remove(position);
        }
    /**
     * Removes all items from this list.
     */
    public void removeAll() {
        fListModel.removeAllElements();
        }
    /**
     * Selects the item at the specified index in the scrolling list.
     */
    public void select(int index) {
        fList.setSelectedIndex(index);
        }
    /**
     * Selects all items
     */
    public void selectAll() {
        fList.setSelectionInterval(0, getItemCount() - 1);
        }
    /**
     * Sets font
     */
    public void setFont(Font    font) {
        fFont = font;
        if (fListModel != null)
            fListModel.fireContentsChanged(fListModel, 0, getItemCount() - 1);
        super.setFont(font);
        } 
    /**
     * Set a beNotThere status to the specified item.
     */
    public void setNotBeThere(
        String  item,
        boolean notBeThere) {
        int position = getItemPosition(item);
         
        if (position == -1)
            throw new IllegalArgumentException();
        
        State state = (State) fListModel.getElementAt(position);
        
        if (state != null &&
            state.notBeThere != notBeThere) {
            state.notBeThere = notBeThere;
            fListModel.fireContentsChanged(fListModel, position, position);
            }
        }
    /**
     * Set a messageWaiting status to the specified item.
     */
    public void setMessageWaiting(
        String  item,
        boolean messageWaiting) {
        int position = getItemPosition(item);
         
        if (position == -1)
            throw new IllegalArgumentException();
        
        State state = (State) fListModel.getElementAt(position);
        
        if (state != null && 
            state.messageWaiting != messageWaiting) {
            state.messageWaiting = messageWaiting;
            fListModel.fireContentsChanged(fListModel, position, position);
            }
        }
    /**
     * clear All message waitings
     */
    public void clearAllMessageWaitings() {
        Enumeration<?> states = fListModel.elements();
        int     position = 0;
        
        if (states == null)
            return;
        while (states.hasMoreElements()) {
            State state = (State) states.nextElement();
            
            if (state.messageWaiting) {
                state.messageWaiting = false;
                fListModel.fireContentsChanged(fListModel, position, position);;
                }
            position ++;
            }
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
    /**
     * Inner class for List Model
     */
    class StateListModel extends DefaultListModel {
    
        public void fireContentsChanged(Object source,
                                    int index0,
                                    int index1) {
            super.fireContentsChanged(source, index0, index1);
            }
        }
    /**
     * Inner class to render a state
     */
    private class StateRenderer extends JLabel implements ListCellRenderer {
        private Color textBackground = null;
        
        public StateRenderer() {
            setOpaque(true);
            }
            
        public Component getListCellRendererComponent(
            JList   list,
            Object  value,
            int     index,
            boolean isSelected,
            boolean cellHasFocus) {
            State state = (State) value;
            
            this.setFont(fFont);
            setIcon(null);
            
            if (state.messageWaiting) {
                setText("! " + state.item);
                setForeground(Color.red);
                }
            else if (state.notBeThere) {
                setText("x " + state.item);
                setForeground(Color.blue);
                }
            else {
                if (fSimpleList)
                    setText(state.item);
                else
                    setText("  " + state.item);
                setForeground(isSelected ? Color.white: Color.black);
                }
            this.setBackground(isSelected ? textBackground.darker(): textBackground);
            return(this);
            }
  
		public void setTextBackground(Color color) {
			textBackground = color;
			}

	  	public Color getTextBackground() {
			return(textBackground);
			}
        }
           
    /** list to show */
    private StateListModel      fListModel  = null;
    private JList               fList       = null;
    private Font                fFont       = null;
    private boolean             fSimpleList = false;
	private StateRenderer		fStateRenderer = null;
	private boolean				fInitialized = false;
    }

// LOG
// 1.90 : 21-Feb-98 Y.Shibata   created


