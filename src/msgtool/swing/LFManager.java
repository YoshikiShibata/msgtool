// File: LFManager.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1998, 2003 by Yoshiki Shibata, All rights reserved.

package msgtool.swing;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import msgtool.db.PropertiesDB;

public final class LFManager implements PropertyChangeListener {

    public static LFManager fInstance = new LFManager();
    public static LFManager getInstance() { return(fInstance); }

    private LFManager() {
        fLFName = fPropertiesDB.getLFName();
        
        if (fLFName == null) {
			// As default, the Java Look and Feel will be selected.
            String  javaLF = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.LookAndFeelInfo[] lfInfos = UIManager.getInstalledLookAndFeels();
            
            if (javaLF != null && lfInfos != null) {
                for (int i = 0; i < lfInfos.length; i++) {
                    if (javaLF.equals(lfInfos[i].getClassName())) {
                        fLFName = lfInfos[i].getName();
                        fPropertiesDB.setLFName(fLFName);
                        break;
                        }
                    }
                }
            }
        installLookAndFeel();
        fPropertiesDB.addPropertyChangeListener(this);
        fComponentList = new Vector<Component>();
        }
    //
    // PropertyChangeListener implementation
    //
    public void propertyChange(PropertyChangeEvent event) {
        String  propertyName = event.getPropertyName();
        
        if (propertyName.equals(PropertiesDB.kName)) {
            String newLFName = fPropertiesDB.getLFName();
            
            if (fLFName == null || !fLFName.equals(newLFName)) {
                fLFName = newLFName;
                installLookAndFeel();
                updateAllComponents();
                }
            }
        }
   
    public void add(Component  c) {
        fComponentList.addElement(c);
        }
        
    //
    // Private method
    //    
    private void installLookAndFeel() {
        if (fLFName != null) {
            UIManager.LookAndFeelInfo[] lfInfos = UIManager.getInstalledLookAndFeels();
            
            if (lfInfos != null) {
                for (int i = 0; i < lfInfos.length; i++) {
                    if (lfInfos[i].getName().equals(fLFName)) { 
                        try {
                            UIManager.setLookAndFeel(lfInfos[i].getClassName());
                            }
                        catch (ClassNotFoundException e) {}
                        catch (InstantiationException e) {}
                        catch (IllegalAccessException e) {}
                        catch (UnsupportedLookAndFeelException e) {}
                        break;
                        }
                    }
                }
            }
        }
        
    private void updateAllComponents() {
        int size = fComponentList.size();
    
        for (int i = 0; i < size; i++) 
            SwingUtilities.updateComponentTreeUI((Component) fComponentList.elementAt(i));
        }
    
    private String  fLFName         = null;
    private Vector<Component>  fComponentList  = null;
    private PropertiesDB    fPropertiesDB   = PropertiesDB.getInstance();
    
    }

// LOG
// 3.50 27-Dec-03   Y.Shibata   used Java Generics
     
