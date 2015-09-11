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
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import br.com.atos.utils.StringUtils;

/**
 *
 * @author pablo-moreira
 */
abstract public class SuggestBoxModel extends AbstractListModel implements ComboBoxModel, KeyListener {

	private static final long serialVersionUID = 1L;

	private List<String> suggestions = new ArrayList<String>();
    private String selectedItem;
    private JComboBox comboBox;
    private ComboBoxEditor comboBoxEditor;
    private JTextField editorTextField;
    private boolean hideFlag = false;

    public SuggestBoxModel(JComboBox comboBox) {
        this.comboBox = comboBox;
        init();
    }

    @Override
    public int getSize() {
        return suggestions.size();
    }
    
    @Override
    public Object getElementAt(int index) {
        return suggestions.get(index);
    }
    
    @Override
    public void setSelectedItem(Object anObject) {        
        if ((selectedItem != null && !selectedItem.equals( anObject )) ||
	    selectedItem == null && anObject != null) {
	    selectedItem = (String) anObject;
	    fireContentsChanged(this, -1, -1);
        }
    }
    
    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }
    
    @Override
    public void keyTyped(KeyEvent e) { 
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                String text = editorTextField.getText();
                

//                if(text.length()==0) {
//                    comboBox.hidePopup();
//                    loadSuggestions("");
                    
                    // Hack
                    //comboBox.setModel(comboBox.getModel());
                    //comboBox.setSelectedIndex(-1);
                    //editorTextField.setText("");
//                }
//                else {
                    loadSuggestions(text);
                    
//                    if(suggestions.isEmpty() || hideFlag) {
//                        //comboBox.hidePopup();
//                        hideFlag = false;
//                    }
//                    else{                        
                        // Hack
                    comboBox.setModel(comboBox.getModel()); 
                    comboBox.setSelectedIndex(-1);
                    editorTextField.setText(text);
                        
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                        	comboBox.showPopup();
                        }
                    });
//                    }
//                }
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

    private void loadSuggestions(String suggest) {
		
        suggestions.clear();

        if (!StringUtils.isNullOrEmpty(suggest)) {
            
            for(String option : getOptions(suggest)) {
                if(option.startsWith(suggest)) {
                    suggestions.add(option);
                }
            }
        }
        else {
            suggestions.addAll(getOptions(suggest));
        }
        
        DefaultComboBoxModel model = (DefaultComboBoxModel) (comboBox).getModel();
        model.removeAllElements();
        
        for (String s : suggestions) {
        	model.addElement(s);
        }
    
    }
    
    private void init() {
        
        //set the model on the combobox
        //comboBox.setModel();
        comboBox.setEditable(true);
        
        comboBoxEditor = comboBox.getEditor();
        
        //here we add the key listener to the text field that the combobox is wrapped around                
        editorTextField = (JTextField) comboBoxEditor.getEditorComponent();
        editorTextField.addKeyListener(this);
        
        loadSuggestions("");
    }
	
	public abstract List<String> getOptions(String suggest);
}
