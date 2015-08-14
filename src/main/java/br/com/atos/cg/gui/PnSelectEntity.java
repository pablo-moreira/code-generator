/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.cg.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import br.com.atos.cg.gui.tablemodel.AttributeTableModel;
import br.com.atos.cg.gui.tablemodel.SelectEntityTableModel;
import br.com.atos.utils.swing.JFrameUtils;

import com.github.cg.vo.SelectItem;

/**
 *
 * @author pablo-moreira
 */
public class PnSelectEntity extends javax.swing.JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final SelectEntityTableModel tmSelectEntities;

	/**
	 * Creates new form PnSelectEntities
	 */
	public PnSelectEntity() {
	
		initComponents();
		
		tmSelectEntities = new SelectEntityTableModel(tblEntities);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblEntities = new javax.swing.JTable() {

            public TableCellEditor getCellEditor(int row, int column) {

                TableModel tm = getModel();

                TableCellEditor cellEditor = null;

                if (tm instanceof AttributeTableModel) {
                    AttributeTableModel atm = (AttributeTableModel) tm;
                    cellEditor = atm.getCellEditor(row, column);
                }

                if (cellEditor != null) {
                    return cellEditor;
                }   
                else {
                    return super.getCellEditor(row, column);
                }
            }
        };

        setAutoscrolls(true);

        jScrollPane1.setAutoscrolls(true);

        tblEntities.setAutoCreateRowSorter(true);
        tblEntities.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblEntities.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(tblEntities);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEntities;
    // End of variables declaration//GEN-END:variables

	public void initialize(List<Class<?>> entitiesClass) {
		
		Collections.sort(entitiesClass, new Comparator<Class<?>>(){
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getName().compareTo(o2.getName());
			}			
		});
		
		tmSelectEntities.initItems(entitiesClass);
		tmSelectEntities.fireTableDataChanged();
	}
	
	public List<Class<?>> getEntitiesClassSelecteds() {
		
		List<SelectItem<Class<?>>> entities = tmSelectEntities.getEntities();
		List<Class<?>> entitiesSelecteds = new ArrayList<Class<?>>();
		
		for (SelectItem<Class<?>> selectItem : entities) {
			if (selectItem.isSelected()) {
				entitiesSelecteds.add(selectItem.getItem());
			}
		}
		
		return entitiesSelecteds;
	}
	
	public boolean validateFrm() {
		
		List<Class<?>> entitiesClassSelecteds = getEntitiesClassSelecteds();
		
		if (entitiesClassSelecteds.isEmpty()) {
			JFrameUtils.showErro("Erro de validação", "Não foi selecionado nenhuma entidade!");
			return false;
		}
		else {
			return true;
		}
	}
}
