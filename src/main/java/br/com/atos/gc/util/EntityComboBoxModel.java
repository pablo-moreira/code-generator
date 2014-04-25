/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.util;

import java.util.List;
import javax.swing.ComboBoxModel;

/**
 *
 * @author pablo-moreira
 */
abstract public class EntityComboBoxModel<E> extends AbstractEntityListModel<E> implements ComboBoxModel {

    public EntityComboBoxModel(E[] itens) {
        super(itens);
    }

    public EntityComboBoxModel(List<E> itens) {
        super(itens);
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
    
    public E getEntidadeSelecionada() {
        for (E item : getItens()) {
            if (getLabel(item).equals(getSelectedItem())) {
                return item;
            }
        }
        return null;
    }
        
    public void setEntidadeSelecionada(E selected) {        
        if (selected != null) {
            setSelectedItem(getLabel(selected));
        }
    }
}