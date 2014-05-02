/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.gui;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.utils.swing.JFrameUtils;
import javax.swing.JOptionPane;

/**
 *
 * @author 205327
 */
public class WinFrmCodeGeneration extends javax.swing.JDialog {
	private GeradorCodigo cg;

    /**
     * Creates new form WinFrmCodeGeneration
     */
    public WinFrmCodeGeneration(java.awt.Frame parent, boolean modal) {
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

        jPanel1 = new javax.swing.JPanel();
        btnPageManage = new javax.swing.JButton();
        btnPageView = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnWinFrm = new javax.swing.JButton();
        btnGrid = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnDaoAndManager = new javax.swing.JButton();
        btnAll = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Telas"));
        jPanel1.setPreferredSize(new java.awt.Dimension(380, 60));

        btnPageManage.setText("Tela de Administração");
        btnPageManage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPageManageActionPerformed(evt);
            }
        });

        btnPageView.setText("Tela de Visualização");
        btnPageView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPageViewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPageView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPageManage)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPageManage)
                    .addComponent(btnPageView))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Componentes"));
        jPanel2.setPreferredSize(new java.awt.Dimension(138, 60));

        btnWinFrm.setText("WinFrm");
        btnWinFrm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWinFrmActionPerformed(evt);
            }
        });

        btnGrid.setText("Grid");
        btnGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGridActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnWinFrm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGrid)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnWinFrm)
                .addComponent(btnGrid))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Infraestrutura"));
        jPanel3.setPreferredSize(new java.awt.Dimension(171, 60));

        btnDaoAndManager.setText("Gerar DAO e Manager");
        btnDaoAndManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaoAndManagerActionPerformed(evt);
            }
        });

        btnAll.setText("Gerar Tudo");
        btnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDaoAndManager)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAll)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDaoAndManager)
                    .addComponent(btnAll))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPageViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPageViewActionPerformed
		try {
			getCg().makePageView();
		}
		catch (Exception e) {
			JFrameUtils.showErro("Erro", "Erro ao executar a geração de código, mensagem interna: " + e.getMessage());
		}
    }//GEN-LAST:event_btnPageViewActionPerformed

    private void btnDaoAndManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaoAndManagerActionPerformed
        try {
			getCg().makeDaoAndManager();
		}
		catch (Exception e) {
			JFrameUtils.showErro("Erro", "Erro ao executar a geração de código, mensagem interna: " + e.getMessage());
		}
    }//GEN-LAST:event_btnDaoAndManagerActionPerformed

    private void btnPageManageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPageManageActionPerformed
		try {
			getCg().makePageManager();
		}
		catch (Exception e) {
			JFrameUtils.showErro("Erro", "Erro ao executar a geração de código, mensagem interna: " + e.getMessage());
		}
    }//GEN-LAST:event_btnPageManageActionPerformed

    private void btnWinFrmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWinFrmActionPerformed
		try {
			getCg().makeWinFrm();
		}
		catch (Exception e) {
			JFrameUtils.showErro("Erro", "Erro ao executar a geração de código, mensagem interna: " + e.getMessage());
		}
    }//GEN-LAST:event_btnWinFrmActionPerformed

    private void btnGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGridActionPerformed
		try {
			getCg().makeGrid();
		}
		catch (Exception e) {
			JFrameUtils.showErro("Erro", "Erro ao executar a geração de código, mensagem interna: " + e.getMessage());
		}
    }//GEN-LAST:event_btnGridActionPerformed

    private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllActionPerformed
		try {
			getCg().makeAll();
		}
		catch (Exception e) {
			JFrameUtils.showErro("Erro", "Erro ao executar a geração de código, mensagem interna: " + e.getMessage());
		}
    }//GEN-LAST:event_btnAllActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
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
	    java.util.logging.Logger.getLogger(WinFrmCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(WinFrmCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(WinFrmCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(WinFrmCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	/* Create and display the dialog */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		WinFrmCodeGeneration dialog = new WinFrmCodeGeneration(new javax.swing.JFrame(), true);
		dialog.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
			System.exit(0);
		    }
		});
		dialog.setVisible(true);
	    }
	});
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnDaoAndManager;
    private javax.swing.JButton btnGrid;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnPageManage;
    private javax.swing.JButton btnPageView;
    private javax.swing.JButton btnWinFrm;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
	
    public void start(GeradorCodigo cg) {
		
		this.cg = cg;
		
		JFrameUtils.setCenterLocation(this);
        setVisible(true);
	}

	public GeradorCodigo getCg() {
		return cg;
	}	
}
