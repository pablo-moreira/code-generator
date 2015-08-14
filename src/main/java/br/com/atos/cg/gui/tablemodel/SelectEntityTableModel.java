/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.cg.gui.tablemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import br.com.atos.cg.util.ColumnMetadata;
import br.com.atos.cg.util.EntityColumnWidthTableModel;

import com.github.cg.vo.SelectItem;

/**
 *
 * @author Pablo Filetti Moreira pablo.filetti@gmail.com
 */
public class SelectEntityTableModel extends EntityColumnWidthTableModel<SelectItem<Class<?>>> {

	private static final long serialVersionUID = 1L;
	
	public static final ColumnMetadata COL_SELECT = new ColumnMetadata(0, "", 60, "Selecionar a entidade");
	public static final ColumnMetadata COL_NAME = new ColumnMetadata(1, "Nome", 300);
	public static final ColumnMetadata COL_PACKAGE = new ColumnMetadata(2, "Pacote", 500);
  
    private List<SelectItem<Class<?>>> items = new ArrayList<SelectItem<Class<?>>>();

    public SelectEntityTableModel(JTable table) {
        super(table);
    }
    
    @Override
    public Object getValueAt(SelectItem<Class<?>> selectItem, int columnIndex) {
        switch (columnIndex) {
            case 0 :         
                return selectItem.isSelected();
            case 1 :
				return selectItem.getItem().getSimpleName();
            case 2 :
				return selectItem.getItem().getPackage().getName();
            default :
                return null;
        }
    }
        
	public void initItems(List<Class<?>> entitiesClass) {
	
		for (Class<?> entityClass : entitiesClass) {
			this.items.add(new SelectItem<Class<?>>(entityClass, false));
		}		
	}    

    @Override
    public List<ColumnMetadata> getColumnsMetadata() {
        return Arrays.asList(new ColumnMetadata[] { 
            COL_SELECT,
			COL_NAME,
			COL_PACKAGE
        });
    }

    @Override
    public List<SelectItem<Class<?>>> getEntities() {
        return items;
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {         
		return COL_SELECT.getIndex() == col;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        
        SelectItem<Class<?>> item = getEntityByRow(row);
        
        if (COL_SELECT.getIndex() == col) {
            item.setSelected((Boolean) value);
        }
        
        fireTableCellUpdated(row, col);
    }

    @Override
    public Class<?> getColumnClass(int col) {
        
        if (COL_SELECT.getIndex() == col) {
            return Boolean.class;
        }
        else {
            return super.getColumnClass(col);
        }
    }

    public List<SelectItem<Class<?>>> getItems() {
        return items;
    }
    
    public TableCellEditor getCellEditor(int row, int col) {        
        return null;
    }
}