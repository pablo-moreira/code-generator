/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author pablo-moreira
 */
public abstract class EntityTableModel<E> extends AbstractTableModel {
    
    private JTable table;
    private TableRowSorter<EntityTableModel<E>> rowSorter;
    abstract public List<ColumnMetadata> getColumnsMetadata();
    abstract public List<E> getEntities();

    protected void initialize() {}
    
    public EntityTableModel(JTable table) {
        
        this.table = table;
        this.table.setModel(this);
        
        rowSorter = new TableRowSorter<EntityTableModel<E>>(this);
        this.table.setRowSorter(rowSorter);
        
        initialize();
    }

    public TableRowSorter<EntityTableModel<E>> getRowSorter() {
        return rowSorter;
    }
    
    public ColumnMetadata findColumnMetadataByIndex(int index) {
        
        for (ColumnMetadata column : getColumnsMetadata()) {
            if (index == column.getIndex()) {
                return column;
            }
        }
        
        return null;
    }
    
    @Override
    public String getColumnName(int index) {
        return findColumnMetadataByIndex(index).getLabel();
    }
    
    @Override
    public int getRowCount() {
        return (getEntities() != null) ?  getEntities().size() : 0;
    }

    @Override
    public int getColumnCount() {
        return getColumnsMetadata().size();
    }    
    
    public E getEntitySelected() {        
        if (table.getSelectedRow() != -1) {
            int selectedRow = table.getSelectedRow();
            int selectedRowModel = table.convertRowIndexToModel(selectedRow);
            return getEntities().get(selectedRowModel);
        }
        else {
            return null;
        }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getValueAt(getEntities().get(rowIndex), columnIndex);
    }
    
    abstract public Object getValueAt(E entidade, int columnIndex);

    public void removeRow(int selectedRow) {
        getEntities().remove(selectedRow);
        fireTableDataChanged();
    }
    
    public JTable getTable() {
        return table;
    }

    public List<E> getEntitiesSelecteds() {
         
        if (table.getSelectedRowCount() > 0) {
            
            List<E> entities = new ArrayList<E>();
            
            int[] selectedRows = table.getSelectedRows();
            
            for (int selectedRow : selectedRows) {
                int selectedRowModel = table.convertRowIndexToModel(selectedRow);                
                entities.add(getEntities().get(selectedRowModel));
            }
            
            return entities;
        }        
        else {
            return new ArrayList<E>();
        }
    }
    
    protected void hideColumn(TableColumn column) {
        column.setPreferredWidth(0);
        column.setMinWidth(0);
        column.setWidth(0);
        column.setMaxWidth(0);
    }
}