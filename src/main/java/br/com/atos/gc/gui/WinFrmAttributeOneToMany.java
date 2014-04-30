/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.gui;

import javax.swing.JOptionPane;

import br.com.atos.gc.model.AttributeOneToMany;
import br.com.atos.gc.model.Target;
import br.com.atos.gc.model.TargetConfig;
import br.com.atos.utils.swing.JFrameUtils;

/**
 *
 * @author pablo-moreira
 */
public class WinFrmAttributeOneToMany extends javax.swing.JDialog {

	private int status;
	private AttributeOneToMany attributeOneToMany;
	private Target target;

	/**
	 * Creates new form WinFrmAttributeOneToMany
	 */
	public WinFrmAttributeOneToMany(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
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

        frmAttributes = new br.com.atos.gc.gui.FrmAttributes();
        jSeparator1 = new javax.swing.JSeparator();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(frmAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOK)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(frmAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
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
        
		getFrmAttributes().validateAttributes();
		
		status = JOptionPane.OK_OPTION;
		
		setVisible(false);
    }//GEN-LAST:event_btnOKActionPerformed
	
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
			java.util.logging.Logger.getLogger(WinFrmAttributeOneToMany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(WinFrmAttributeOneToMany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(WinFrmAttributeOneToMany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(WinFrmAttributeOneToMany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				WinFrmAttributeOneToMany dialog = new WinFrmAttributeOneToMany(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.start(null, null);
			}
		});
	}
		
	public AttributeOneToMany getAttributeOneToMany() {
		return attributeOneToMany;
	}

	public void start(AttributeOneToMany attributeOneToMany, Target target) {
        		
		setTitle("Formulário - " + attributeOneToMany.getEntity().getClazzSimpleName() + "." + attributeOneToMany.getField().getName() + " - " + attributeOneToMany.getAssociationClassSimpleName());
		
		this.attributeOneToMany = attributeOneToMany;
		this.target = target;

        getFrmAttributes().initialize(attributeOneToMany.getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany());

		TargetConfig colRender = target.getWinFrmAttributeOneToMany();
		
		if (colRender != null) {			
			if (!colRender.isRenderRenderColumn()) {
				getFrmAttributes().getTmAttributes().hideColumnRenderColumn();
			}
			if (!colRender.isRenderRenderFilter()) {
				getFrmAttributes().getTmAttributes().hideColumnRenderFilter();
			}
			if (!colRender.isRenderRenderForm()) {
				getFrmAttributes().getTmAttributes().hideColumnRenderForm();
			}			
			if (!colRender.isRenderAttributeDescription()) {
				getFrmAttributes().getTmAttributes().hideColumnAttributeDescription();
			}
			if (!colRender.isRenderFormType()) {
				getFrmAttributes().getTmAttributes().hideColumnFormType();
			}
		}
		
		JFrameUtils.setCenterLocation(this);
        status = JOptionPane.CANCEL_OPTION;
        setVisible(true);
    }
	
	public FrmAttributes getFrmAttributes() {
        return (FrmAttributes) frmAttributes;
    }
    
    public boolean isStatusOK() {
    	return status == JOptionPane.OK_OPTION;
    }
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private br.com.atos.gc.gui.FrmAttributes frmAttributes;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
