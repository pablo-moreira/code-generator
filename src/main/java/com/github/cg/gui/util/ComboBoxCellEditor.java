package com.github.cg.gui.util;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class ComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor, Serializable, KeyListener {
    
	private static final long serialVersionUID = 1L;
	
	private JComboBox comboBox;
	private JTextField comboBoxEditor;
    
    public ComboBoxCellEditor(JComboBox comboBox) {
        this.comboBox = comboBox;
        this.comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        // hitting enter in the combo box should stop cellediting (see below)
//        this.comboBox.addActionListener(this);
        // remove the editor's border - the cell itself already has one
        this.comboBoxEditor = (JTextField) comboBox.getEditor().getEditorComponent();
        this.comboBoxEditor.setBorder(null);
        this.comboBoxEditor.addKeyListener(this);        
    }
    
    private void setValue(Object value) {
        comboBox.setSelectedItem(value);
    }
    
    // Implementing CellEditor
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }
    
    public boolean stopCellEditing() {
        if (comboBox.isEditable()) {
            // Notify the combo box that editing has stopped (e.g. User pressed F2)
            comboBox.actionPerformed(new ActionEvent(this, 0, ""));
        }
        fireEditingStopped();
        return true;
    }
    
    // Implementing TableCellEditor
    public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
        setValue(value);
        return comboBox;
    }

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			stopCellEditing();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}	
}