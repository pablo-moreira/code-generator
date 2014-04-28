/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.com.atos.utils.StringUtils;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author pablo-moreira
 */
abstract public class SuggestBoxModel<E> extends AbstractListModel implements ComboBoxModel, KeyListener {

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

                if(text.length()==0) {
                    comboBox.hidePopup();
                    loadSuggestions("");
                    
                    // Hack
                    comboBox.setModel(comboBox.getModel());
                    comboBox.setSelectedIndex(-1);
                    editorTextField.setText("");
                }
                else {
                    loadSuggestions(text);
                    
                    if(suggestions.isEmpty() || hideFlag) {
                        comboBox.hidePopup();
                        hideFlag = false;
                    }
                    else{                        
                        // Hack
                        comboBox.setModel(comboBox.getModel()); 
                        comboBox.setSelectedIndex(-1);
                        editorTextField.setText(text);
                        comboBox.showPopup();
                    }
                }
            }
        });
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        
        String text = editorTextField.getText();
                
        int code = e.getKeyCode();
                
        if (code==KeyEvent.VK_ENTER) {
//            if(!v.contains(text)) {
//                v.addElement(text);
//                Collections.sort(v);
//                setModel(getSuggestedModel(v, text), text);
//            }
            hideFlag = true; 
        }
        else if(code==KeyEvent.VK_ESCAPE) {
            hideFlag = true; 
        }
        else if(code==KeyEvent.VK_RIGHT) {
            for(int i=0; i < suggestions.size();i++) {
                String str = suggestions.get(i);
                if(str.startsWith(text)) {
                    comboBox.setSelectedIndex(-1);
                    editorTextField.setText(str);
                    return;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private void loadSuggestions(String suggest) {

        suggestions.clear();

        if (!StringUtils.isNullOrEmpty(suggest)) {
            
            suggest = StringUtils.substituiCaracteresAcentuadosPorNaoAcentuados(suggest.toLowerCase());    
            
            for(E entity : getEntities()) {
                
                String label = getEntityLabel(entity);
                
                if (!StringUtils.isNullOrEmpty(suggest)) {
                    label = StringUtils.substituiCaracteresAcentuadosPorNaoAcentuados(label.toLowerCase());
                }
                
                if(label.startsWith(suggest)) {
                    suggestions.add(getEntityLabel(entity));
                }
            }
        }
        else {        
            for(E entity : getEntities()) {
                suggestions.add(getEntityLabel(entity)); 
            }
        }
    }
    
    private void init() {
        
        //set the model on the combobox
        comboBox.setModel(this);
        comboBox.setEditable(true);
        
        comboBoxEditor = comboBox.getEditor();
        
        //here we add the key listener to the text field that the combobox is wrapped around                
        editorTextField = (JTextField) comboBoxEditor.getEditorComponent();
        editorTextField.addKeyListener(this);
        
        loadSuggestions("");
    }
    
    abstract public List<E> getEntities();
    abstract public String getEntityLabel(E entity);
}
