/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.gui.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import com.github.cg.component.StringUtils;

/**
 *
 * @author pablo-moreira
 */
abstract public class SuggestBoxModel extends EntityComboBoxModel<String> implements KeyListener {

	private static final long serialVersionUID = 1L;
	
    private JComboBox<String> comboBox;
    private ComboBoxEditor comboBoxEditor;
    private JTextField editorTextField;
	private boolean hideFlag;
	private String lastSelectedItem;
	private ListCellRenderer<? super String> renderer;

    public SuggestBoxModel(JComboBox<String> comboBox) {
    	super();
        init(comboBox);
    }
    
    @Override
    public void setSelectedItem(Object selectedItem) {
        if ((this.lastSelectedItem != null && !this.lastSelectedItem.equals( selectedItem )) || this.lastSelectedItem == null && selectedItem != null) {
        	this.lastSelectedItem = (String) selectedItem;
        	super.setSelectedItem(selectedItem);
        	fireContentsChanged(this, -1, -1);
        }
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

            	if (!hideFlag) {
            		
            		String text = editorTextField.getText();
            		
            		System.out.println("text=" + text);
            		System.out.println("event key=" + e.getKeyChar() + "-" + e.getKeyCode());
            		System.out.println("selectindex=" + comboBox.getSelectedIndex());
            		            		
            		loadSuggestions(text);
	                                		
                    comboBox.setModel(comboBox.getModel()); 
    	            comboBox.setSelectedIndex(-1);
    	            
    	            if (getItems().size() > 0) {
	                    EventQueue.invokeLater(new Runnable() {
	                    	@Override
	                        public void run() {
	                    		comboBox.showPopup();
	                    	}
	                    });
    	            }
    	            else {
    	            	EventQueue.invokeLater(new Runnable() {
	                    	@Override
	                        public void run() {
	                    		comboBox.hidePopup();
	                    	}
	                    });
    	            }
            	}
            	else {
            		comboBox.hidePopup();
            		hideFlag = false;
            	}
            }
        });
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
                        
        int code = e.getKeyCode();

        if (code==KeyEvent.VK_ENTER) {
            hideFlag = true; 
        }
        else if(code==KeyEvent.VK_ESCAPE) {
            hideFlag = true; 
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public void loadSuggestions(String suggest) {
    	
    	getItems().clear();
    	
    	if (!StringUtils.getInstance().isNullOrEmpty(suggest)) {
            
            for(String option : getOptions(suggest)) {
                if(option.startsWith(suggest)) {
                    addItem(option);
                }
            }
        }
        else {
        	setItems(getOptions(suggest));
        }
    }
    
    private void init(JComboBox<String> comboBox) {
        
        this.comboBox = comboBox;
    	this.comboBox.setModel(this);
        this.comboBox.setEditable(true);
        
        this.comboBoxEditor = comboBox.getEditor();
        this.renderer = comboBox.getRenderer();
        
        //here we add the key listener to the text field that the combobox is wrapped around                
        this.editorTextField = (JTextField) comboBoxEditor.getEditorComponent();
        this.editorTextField.addKeyListener(this);
        
        loadSuggestions("");
    }
	
    abstract public List<String> getOptions(String suggest);
    
    @Override
    public String getLabel(String item) {
    	return item;
    }
}
