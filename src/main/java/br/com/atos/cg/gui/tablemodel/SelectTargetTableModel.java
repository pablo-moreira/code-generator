/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.cg.gui.tablemodel;

import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import br.com.atos.cg.util.ColumnMetadata;

import com.github.cg.model.Target;
import com.github.cg.vo.SelectItem;

/**
 *
 * @author Pablo Filetti Moreira pablo.filetti@gmail.com
 */
abstract public class SelectTargetTableModel extends SelectItemTableModel<Target> {

	private static final long serialVersionUID = 1L;
	
	public static final ColumnMetadata COL_SELECT = new ColumnMetadata(0, "", 60, "Selecionar o target");
	public static final ColumnMetadata COL_NAME = new ColumnMetadata(1, "Nome", 300);
	public static final ColumnMetadata COL_DESCRIPTION = new ColumnMetadata(2, "Descrição", 800);
  
    public SelectTargetTableModel(JTable table) {
        super(table);
    }
    
    @Override
    public Object getValueAt(SelectItem<Target> selectItem, int columnIndex) {
        switch (columnIndex) {
            case 0 :         
                return selectItem.isSelected();
            case 1 :
				return selectItem.getItem().getName();
            case 2 :
				return selectItem.getItem().getDescription();
            default :
                return null;
        }
    }
        
    @Override
    public List<ColumnMetadata> getColumnsMetadata() {
        return Arrays.asList(new ColumnMetadata[] { 
            COL_SELECT,
			COL_NAME,
			COL_DESCRIPTION
        });
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {         
		return COL_SELECT.getIndex() == col;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        
        SelectItem<Target> item = getEntityByRow(row);
        
        if (COL_SELECT.getIndex() == col) {
            item.setSelected((Boolean) value);
			onChangeSelection(item);
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
    
    public TableCellEditor getCellEditor(int row, int col) {        
        return null;
    }
	
	abstract public void onChangeSelection(SelectItem<Target> item);
}