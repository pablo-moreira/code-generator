/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.gui;

import java.util.List;

import javax.swing.JOptionPane;

import com.github.cg.gui.util.JFrameUtils;
import com.github.cg.model.AttributeOneToMany;
import com.github.cg.model.Target;
import com.github.cg.model.TargetContext;
import com.github.cg.model.TargetTask;

/**
 *
 * @author pablo-moreira
 */
public class DlgAttributeOneToMany extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;

	public static final String CONFIG_RENDER_COLUMN = "renderColumn";
	public static final String CONFIG_RENDER_FORM = "renderForm";
	public static final String CONFIG_RENDER_FILTER = "renderFilter";
	public static final String CONFIG_RENDER_FORM_TYPE = "renderFormType";
	public static final String CONFIG_RENDER_ATTRIBUTE_DESCRIPTION = "renderAttributeDescription";

	private int status;
	private AttributeOneToMany attributeOneToMany;
	private final List<String> patterns;
	private final List<String> formTypes;

	/**
	 * Creates new form WinFrmAttributeOneToMany
	 */
	public DlgAttributeOneToMany(java.awt.Frame parent, boolean modal, List<String> patterns, List<String> formTypes) {
		super(parent, modal);
		this.patterns = patterns;
		this.formTypes = formTypes;
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

        jSeparator1 = new javax.swing.JSeparator();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblTarget = new javax.swing.JLabel();
        txtTarget = new javax.swing.JTextField();
        pnAttributes = new com.github.cg.gui.PnAttributes(this.patterns, this.formTypes);
        lblAssociation = new javax.swing.JLabel();
        txtAssociation = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\"Formulário - \"");

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblTarget.setText("Target:");

        txtTarget.setEditable(false);

        lblAssociation.setText("Associação");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTarget)
                            .addComponent(lblAssociation))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTarget)
                            .addComponent(txtAssociation)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAssociation)
                    .addComponent(txtAssociation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTarget))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnAttributes, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		status = JOptionPane.CANCEL_OPTION;
		setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
    	save();
    }//GEN-LAST:event_btnOKActionPerformed
	
    private void save() {
        
        if (!getPnAttributes().validateAttributes()) {
        	return;
        }
		
		status = JOptionPane.OK_OPTION;
		
		setVisible(false);
    }
    
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(DlgAttributeOneToMany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(DlgAttributeOneToMany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(DlgAttributeOneToMany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(DlgAttributeOneToMany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				DlgAttributeOneToMany dialog = new DlgAttributeOneToMany(new javax.swing.JFrame(), true, null, null);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.start(null, null, null);
			}
		});
	}
		
	public AttributeOneToMany getAttributeOneToMany() {
		return attributeOneToMany;
	}

	
	public void start(AttributeOneToMany attributeOneToMany, TargetContext targetContext, TargetTask targetTask) {

		Target target = targetContext.getTarget();
		
		txtAssociation.setText(attributeOneToMany.getEntity().getName() + "." + attributeOneToMany.getName());
		txtTarget.setText(target.getDescription());
		
		setTitle("Formulário - " + attributeOneToMany.getEntity().getName() + "." + attributeOneToMany.getName() + " - " + attributeOneToMany.getAssociationClassName());
		
		this.attributeOneToMany = attributeOneToMany;

        getPnAttributes().initialize(attributeOneToMany.getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany());
		
		if (targetTask != null) {			
			if (!targetTask.getConfigValueAsBoolean(CONFIG_RENDER_COLUMN)) {
				getPnAttributes().getTmAttributes().hideColumnRenderColumn();
			}
			if (!targetTask.getConfigValueAsBoolean(CONFIG_RENDER_FILTER)) {
				getPnAttributes().getTmAttributes().hideColumnRenderFilter();
			}
			if (!targetTask.getConfigValueAsBoolean(CONFIG_RENDER_FORM)) {
				getPnAttributes().getTmAttributes().hideColumnRenderForm();
			}			
			if (!targetTask.getConfigValueAsBoolean(CONFIG_RENDER_ATTRIBUTE_DESCRIPTION)) {
				getPnAttributes().getTmAttributes().hideColumnAttributeDescription();
			}
			if (!targetTask.getConfigValueAsBoolean(CONFIG_RENDER_FORM_TYPE)) {
				getPnAttributes().getTmAttributes().hideColumnFormType();
			}
		}
		
		JFrameUtils.setCenterLocation(this);
        status = JOptionPane.CANCEL_OPTION;
        setVisible(true);
    }
	
	public PnAttributes getPnAttributes() {
        return (PnAttributes) pnAttributes;
    }
    
    public boolean isStatusOK() {
    	return status == JOptionPane.OK_OPTION;
    }
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblAssociation;
    private javax.swing.JLabel lblTarget;
    private com.github.cg.gui.PnAttributes pnAttributes;
    private javax.swing.JTextField txtAssociation;
    private javax.swing.JTextField txtTarget;
    // End of variables declaration//GEN-END:variables
}
