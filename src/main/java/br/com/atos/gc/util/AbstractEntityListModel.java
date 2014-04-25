/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.util;

import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author pablo-moreira
 */
public abstract class AbstractEntityListModel<E> extends AbstractListModel { 
    
    private List<E> itens;
    
    public AbstractEntityListModel(E[] itens) {
        this.itens = Arrays.asList(itens);
    }
    
    public AbstractEntityListModel(List<E> itens) {
        this.itens = itens;
    }
            
    @Override
    public Object getElementAt(int index) {
        return getLabel(itens.get(index));
    }
    
    abstract public String getLabel(E item);

    @Override
    public int getSize() {
        return itens.size();
    }

    public List<E> getItens() {
        return itens;
    }
}
