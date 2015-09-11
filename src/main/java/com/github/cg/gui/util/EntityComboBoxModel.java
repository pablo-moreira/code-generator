/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.gui.util;

import java.util.List;

import javax.swing.ComboBoxModel;

/**
 *
 * @author pablo-moreira
 */
abstract public class EntityComboBoxModel<E> extends AbstractEntityListModel<E> implements ComboBoxModel {

	private static final long serialVersionUID = 1L;

	public EntityComboBoxModel(E[] items) {
        super(items);
    }

    public EntityComboBoxModel(List<E> items) {
        super(items);
    }
    
    private String selectedItem;
           
    @Override
    public void setSelectedItem(Object selectedItem) {
        this.selectedItem = (String) selectedItem;
    }

    @Override
    public Object getSelectedItem() {
        return this.selectedItem;
    }
    
    public E getSelectedEntity() {
        for (E item : getItens()) {
            if (getLabel(item).equals(getSelectedItem())) {
                return item;
            }
        }
        return null;
    }
        
    public void setSelectedEntity(E selected) {        
        if (selected != null) {
            setSelectedItem(getLabel(selected));
        }
    }
}