/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import com.github.cg.gui.tablemodel.AttributeTableModel;
import com.github.cg.gui.tablemodel.SelectTargetGroupTableModel;
import com.github.cg.gui.tablemodel.SelectTargetTableModel;
import com.github.cg.gui.util.JFrameUtils;
import com.github.cg.model.Target;
import com.github.cg.model.TargetGroup;
import com.github.cg.vo.SelectItem;

/**
 *
 * @author pablo-moreira
 */
public class PnSelectTarget extends javax.swing.JPanel {

	
	private static final long serialVersionUID = 1L;
	private SelectTargetTableModel tmSelectTargets;
	private SelectTargetGroupTableModel tmSelectTargetsGroups;

	/**
	 * Creates new form PnSelectTarget
	 */
	public PnSelectTarget() {		
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        pnTargetGroup = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTargetsGroups = new javax.swing.JTable() {

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
        pnTarget = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTargets = new javax.swing.JTable() {

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

        jSplitPane1.setDividerLocation(190);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        pnTargetGroup.setBorder(javax.swing.BorderFactory.createTitledBorder("Grupos"));

        jScrollPane1.setAutoscrolls(true);

        tblTargetsGroups.setAutoCreateRowSorter(true);
        tblTargetsGroups.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblTargetsGroups.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(tblTargetsGroups);

        javax.swing.GroupLayout pnTargetGroupLayout = new javax.swing.GroupLayout(pnTargetGroup);
        pnTargetGroup.setLayout(pnTargetGroupLayout);
        pnTargetGroupLayout.setHorizontalGroup(
            pnTargetGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
        );
        pnTargetGroupLayout.setVerticalGroup(
            pnTargetGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTargetGroupLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jSplitPane1.setTopComponent(pnTargetGroup);

        pnTarget.setBorder(javax.swing.BorderFactory.createTitledBorder("Targets"));

        jScrollPane2.setAutoscrolls(true);

        tblTargets.setAutoCreateRowSorter(true);
        tblTargets.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblTargets.setRowSelectionAllowed(false);
        jScrollPane2.setViewportView(tblTargets);

        javax.swing.GroupLayout pnTargetLayout = new javax.swing.GroupLayout(pnTarget);
        pnTarget.setLayout(pnTargetLayout);
        pnTargetLayout.setHorizontalGroup(
            pnTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
        );
        pnTargetLayout.setVerticalGroup(
            pnTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(pnTarget);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pnTarget;
    private javax.swing.JPanel pnTargetGroup;
    private javax.swing.JTable tblTargets;
    private javax.swing.JTable tblTargetsGroups;
    // End of variables declaration//GEN-END:variables

	public void initialize(List<TargetGroup> targetsGroups, List<Target> targets) {		
		
		Collections.sort(targetsGroups, new Comparator<TargetGroup>(){
			@Override
			public int compare(TargetGroup o1, TargetGroup o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		Collections.sort(targets, new Comparator<Target>(){
			@Override
			public int compare(Target o1, Target o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		tmSelectTargetsGroups = new SelectTargetGroupTableModel(tblTargetsGroups) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onChangeSelection(SelectItem<TargetGroup> selectItem) {
				
				TargetGroup targetGroup = selectItem.getItem();
				
				if (selectItem.isSelected()) {						
					tmSelectTargets.selectItems(targetGroup.getTargets());
				}
				else {
					tmSelectTargets.unselectItemsIfAllSelected(targetGroup.getTargets());
				}
				
				tmSelectTargets.fireTableDataChanged();
			}
		};		
		tmSelectTargetsGroups.initItems(targetsGroups);
		
		tmSelectTargets = new SelectTargetTableModel(tblTargets) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onChangeSelection(SelectItem<Target> selectItem) {
						
				Target item = selectItem.getItem();
				
				List<TargetGroup> groupsMemberOf = item.getGroupsMemberOf();
				
				for (TargetGroup targetGroup : groupsMemberOf) {
					
					SelectItem<TargetGroup> selectItemTg = tmSelectTargetsGroups.findSelectItemByItem(targetGroup);
					
					if (!selectItem.isSelected() && selectItemTg.isSelected()) {
						selectItemTg.setSelected(false);
					}
					else if (selectItem.isSelected() && !selectItemTg.isSelected()) {
						
						List<Target> targets = selectItemTg.getItem().getTargets();
						
						boolean allSelected = true;
						
						for (Target target : targets) {
							
							SelectItem<Target> selectItemTarget = tmSelectTargets.findSelectItemByItem(target);
							
							if (selectItemTarget == null || !selectItemTarget.isSelected()) {
								allSelected = false;
								break;
							}
						}
						
						if (allSelected) {
							selectItemTg.setSelected(true);
						}
					}
				}
				
				tmSelectTargetsGroups.fireTableDataChanged();
			}
		};
		tmSelectTargets.initItems(targets);
		
		repaint();
	}
	
	public List<Target> getTargetsSelecteds() {
		
		List<Target> targetsSelecteds = new ArrayList<Target>();
		
		for (SelectItem<Target> selectItem : tmSelectTargets.getSelectItems()) {
			if (selectItem.isSelected()) {
				targetsSelecteds.add(selectItem.getItem());
			}
		}
		
		return targetsSelecteds;
	}
	
	public boolean validateFrm() {
		
		List<Target> targetsSelecteds = getTargetsSelecteds();
		
		if (targetsSelecteds.isEmpty()) {
			JFrameUtils.showErro("Erro de validação", "Não foi selecionado nenhum target!");
			return false;
		}
		else {
			return true;
		}
	}
}
