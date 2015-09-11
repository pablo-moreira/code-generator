/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.gui.tablemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import com.github.cg.gui.util.EntityColumnWidthTableModel;
import com.github.cg.vo.SelectItem;

/**
 *
 * @author Pablo Filetti Moreira pablo.filetti@gmail.com
 */
abstract public class SelectItemTableModel<E> extends EntityColumnWidthTableModel<SelectItem<E>> {

	private static final long serialVersionUID = 1L;
	  
    private List<SelectItem<E>> selectItems = new ArrayList<SelectItem<E>>();

    public SelectItemTableModel(JTable table) {
        super(table);
    }
            
	public void initItems(List<E> entitiesClass) {	
		for (E entityClass : entitiesClass) {
			this.selectItems.add(new SelectItem<E>(entityClass, false));
		}		
	}    

    @Override
    public List<SelectItem<E>> getEntities() {
        return getSelectItems();
    }
        
    public List<SelectItem<E>> getSelectItems() {
        return selectItems;
    }

	public SelectItem<E> findSelectItemByItem(E target) {
		
		for (SelectItem<E> selectItem : getSelectItems()) {		
			if (selectItem.getItem().equals(target)) {
				return selectItem;
			}
		}
		
		return null;
	}
	
	public void selectItems(List<E> items) {
		
		for (E item : items) {
			selectItem(item);
		}
	}

	public void selectItem(E item) {
		
		SelectItem<E> selectItem = findSelectItemByItem(item);
			
		if (selectItem != null) {
			selectItem.setSelected(true);
		}
	}
	
	public void unselectItemsIfAllSelected(List<E> items) {
		
		boolean allSelected = true;
		
		for (E item : items) {
			
			SelectItem<E> selectItem = findSelectItemByItem(item);
			
			if (selectItem == null || !selectItem.isSelected()) {
				allSelected = false;
				break;
			}
		}
		
		if (allSelected) {
			for (E item : items) {				
				SelectItem<E> selectItem = findSelectItemByItem(item);					
				selectItem.setSelected(false);
			}
		}
	}
}