/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.cg.util;

import javax.swing.JTable;

/**
 *
 * @author pablo-moreira
 */
public abstract class EntityColumnWidthTableModel<E> extends EntityTableModel<E> {

    public EntityColumnWidthTableModel(JTable table) {        
        super(table);        
        initializeWidth();
    }
   
    private void initializeWidth() {
        for (int i=0; i < getTable().getColumnModel().getColumnCount(); i++) {
            getTable().getColumnModel().getColumn(i).setPreferredWidth(findColumnMetadataByIndex(i).getWidth());
        }
    }
}