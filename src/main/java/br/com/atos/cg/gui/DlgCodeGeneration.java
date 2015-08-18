/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.cg.gui;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.utils.swing.JFrameUtils;
import java.awt.Font;

/**
 *
 * @author 205327
 */
public class DlgCodeGeneration extends javax.swing.JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private CodeGenerator cg;

    /**
     * Creates new form WinFrmCodeGeneration
     */
    public DlgCodeGeneration(java.awt.Frame parent, boolean modal) {
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

        btnFinish = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        pnStageInfo = new javax.swing.JPanel();
        lblStages = new javax.swing.JLabel();
        lblStage1 = new javax.swing.JLabel();
        lblStage2 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnStage = new javax.swing.JPanel();
        pnSelectEntity = new br.com.atos.cg.gui.PnSelectEntity();
        pnSelectTarget = new br.com.atos.cg.gui.PnSelectTarget();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setPreferredSize(new java.awt.Dimension(640, 400));

        btnFinish.setText("Finalizar");
        btnFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinishActionPerformed(evt);
            }
        });

        pnStageInfo.setBackground(new java.awt.Color(255, 255, 255));

        lblStages.setFont(new java.awt.Font("Liberation", 1, 11)); // NOI18N
        lblStages.setText("Etapas");

        lblStage1.setText("1. Selecione as Entidades");

        lblStage2.setText("2. Selecione os Targets");

        javax.swing.GroupLayout pnStageInfoLayout = new javax.swing.GroupLayout(pnStageInfo);
        pnStageInfo.setLayout(pnStageInfoLayout);
        pnStageInfoLayout.setHorizontalGroup(
            pnStageInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnStageInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnStageInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStages)
                    .addGroup(pnStageInfoLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pnStageInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStage2)
                            .addComponent(lblStage1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnStageInfoLayout.setVerticalGroup(
            pnStageInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnStageInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStages)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStage1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStage2)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        btnBack.setText("< Retonar");

        btnNext.setText("Avançar >");

        btnCancel.setText("Cancelar");

        pnSelectEntity.setEnabled(false);

        javax.swing.GroupLayout pnSelectEntityLayout = new javax.swing.GroupLayout(pnSelectEntity);
        pnSelectEntity.setLayout(pnSelectEntityLayout);
        pnSelectEntityLayout.setHorizontalGroup(
            pnSelectEntityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnSelectEntityLayout.setVerticalGroup(
            pnSelectEntityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 562, Short.MAX_VALUE)
        );

        pnSelectTarget.setEnabled(false);

        javax.swing.GroupLayout pnSelectTargetLayout = new javax.swing.GroupLayout(pnSelectTarget);
        pnSelectTarget.setLayout(pnSelectTargetLayout);
        pnSelectTargetLayout.setHorizontalGroup(
            pnSelectTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        pnSelectTargetLayout.setVerticalGroup(
            pnSelectTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnStageLayout = new javax.swing.GroupLayout(pnStage);
        pnStage.setLayout(pnStageLayout);
        pnStageLayout.setHorizontalGroup(
            pnStageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnSelectEntity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnStageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnStageLayout.createSequentialGroup()
                    .addGap(573, 573, 573)
                    .addComponent(pnSelectTarget, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnStageLayout.setVerticalGroup(
            pnStageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnSelectEntity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnStageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnStageLayout.createSequentialGroup()
                    .addGap(95, 95, 95)
                    .addComponent(pnSelectTarget, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(367, 367, 367)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(412, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFinish)
                .addContainerGap())
            .addComponent(pnStageInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnStage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnStageInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnStage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFinish)
                    .addComponent(btnBack)
                    .addComponent(btnNext)
                    .addComponent(btnCancel))
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinishActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btnFinishActionPerformed

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
	    java.util.logging.Logger.getLogger(DlgCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(DlgCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(DlgCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(DlgCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	//</editor-fold>

	/* Create and display the dialog */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		DlgCodeGeneration dialog = new DlgCodeGeneration(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnFinish;
    private javax.swing.JButton btnNext;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblStage1;
    private javax.swing.JLabel lblStage2;
    private javax.swing.JLabel lblStages;
    private br.com.atos.cg.gui.PnSelectEntity pnSelectEntity;
    private br.com.atos.cg.gui.PnSelectTarget pnSelectTarget;
    private javax.swing.JPanel pnStage;
    private javax.swing.JPanel pnStageInfo;
    // End of variables declaration//GEN-END:variables
	
    public void start(CodeGenerator cg) {		
		this.cg = cg;		
		JFrameUtils.setCenterLocation(this);
        setVisible(true);
	}

	public CodeGenerator getCg() {
		return cg;
	}	
	
	
	public void goToStage(int stage) {
		
		this.lblStage1.setFont(new java.awt.Font("Liberation", Font.PLAIN, 11));
		this.lblStage2.setFont(new java.awt.Font("Liberation", Font.PLAIN, 11));
		
		switch (stage) {
			case 1 : 
				this.setTitle(this.lblStage1.getText());
				this.lblStage1.setFont(new java.awt.Font("Liberation", Font.BOLD, 11));
				break;
				
			case 2 :
				this.setTitle(lblStage2.getText());
				this.lblStage2.setFont(new java.awt.Font("Liberation", Font.BOLD, 11));
				break;
		}
	}
}