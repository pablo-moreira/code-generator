/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.util;

import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author pablo-moreira
 */
public abstract class AbstractEntityListModel<E> extends AbstractListModel { 
    
    private List<E> items;
    
    public AbstractEntityListModel(E[] items) {
        this.items = Arrays.asList(items);
    }
    
    public AbstractEntityListModel(List<E> items) {
        this.items = items;
    }
            
    @Override
    public Object getElementAt(int index) {
        return getLabel(items.get(index));
    }
    
    abstract public String getLabel(E item);

    @Override
    public int getSize() {
        return items.size();
    }

    public List<E> getItens() {
        return items;
    }
}
