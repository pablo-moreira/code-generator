/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.gui.tablemodel;

import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.github.cg.model.TargetGroup;
import com.github.cg.vo.ColumnMetadata;
import com.github.cg.vo.SelectItem;

/**
 *
 * @author Pablo Filetti Moreira pablo.filetti@gmail.com
 */
abstract public class SelectTargetGroupTableModel extends SelectItemTableModel<TargetGroup> {

	private static final long serialVersionUID = 1L;
	
	public static final ColumnMetadata COL_SELECT = new ColumnMetadata(0, "", 60, "Selecionar o target group");
	public static final ColumnMetadata COL_NAME = new ColumnMetadata(1, "Nome", 300);
	public static final ColumnMetadata COL_TARGETS = new ColumnMetadata(2, "Targets", 800);
  
    public SelectTargetGroupTableModel(JTable table) {
        super(table);
    }
    
    @Override
    public Object getValueAt(SelectItem<TargetGroup> selectItem, int columnIndex) {
        switch (columnIndex) {
            case 0 :         
                return selectItem.isSelected();
            case 1 :
				return selectItem.getItem().getName();
            case 2 :
				return selectItem.getItem().getTargetsNames();
            default :
                return null;
        }
    }
        
    @Override
    public List<ColumnMetadata> getColumnsMetadata() {
        return Arrays.asList(new ColumnMetadata[] { 
            COL_SELECT,
			COL_NAME,
			COL_TARGETS
        });
    }

    @Override
    public boolean isCellEditable(int row, int col) {         
		return COL_SELECT.getIndex() == col;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        
        SelectItem<TargetGroup> item = getEntityByRow(row);
        
        if (COL_SELECT.getIndex() == col) {
            item.setSelected((Boolean) value);
			onChangeSelection(item);
        }
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
	
	abstract public void onChangeSelection(SelectItem<TargetGroup> item);
}