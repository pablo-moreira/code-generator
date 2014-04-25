/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.util;

import java.util.List;
import javax.swing.JList;

/**
 *
 * @author pablo-moreira
 */
public abstract class EntityListModel<E> extends AbstractEntityListModel<E> {

    private JList list;
    
    public EntityListModel(JList list, E[] itens) {
        super(itens);
        init(list);
    }

    public EntityListModel(JList list, List<E> itens) {
        super(itens);
        init(list);
    }

    private void init(JList list) {
        this.list = list;
        this.list.setModel(this);
    }
        
    public E getEntidadeSelecionada() {        
        return getItens().get(list.getSelectedIndex());
    }
        
    public void setEntidadeSelecionada(E selected) {        
        list.setSelectedIndex(getItens().indexOf(selected));
    }
}