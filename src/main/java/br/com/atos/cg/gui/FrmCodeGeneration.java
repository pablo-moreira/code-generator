/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.cg.gui;

import java.awt.CardLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.utils.swing.JFrameUtils;

import com.github.cg.model.Target;
import com.github.cg.model.TargetStatus;

/**
 *
 * @author pablo-moreira
 */
public class FrmCodeGeneration extends javax.swing.JFrame {
	
	private final CodeGenerator cg;
	private int currentStage;
	private List<Class<?>> entitiesClassSelecteds;
	private List<Target> targetsSelecteds;
	private ArrayList<TargetStatus> targetsStatus;

	/**
	 * Creates new form FrmCodeGeneration
	 * @param cg
	 */
	public FrmCodeGeneration(CodeGenerator cg) {
		
		initComponents();
				
		this.cg = cg;
		
		initialize();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnExecute = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        pnStageInfo = new javax.swing.JPanel();
        lblStages = new javax.swing.JLabel();
        lblStage1 = new javax.swing.JLabel();
        lblStage2 = new javax.swing.JLabel();
        lblStage3 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnStage = new javax.swing.JPanel();
        pnSelectTarget = new br.com.atos.cg.gui.PnSelectTarget();
        pnSelectEntity = new br.com.atos.cg.gui.PnSelectEntity();
        pnExecutionMonitor = new br.com.atos.cg.gui.PnExecutionMonitor();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnExecute.setText("Executar");
        btnExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecuteActionPerformed(evt);
            }
        });

        pnStageInfo.setBackground(new java.awt.Color(255, 255, 255));

        lblStages.setFont(new java.awt.Font("Liberation", 1, 11)); // NOI18N
        lblStages.setText("Etapas");

        lblStage1.setText("1. Selecione as Entidades");

        lblStage2.setText("2. Selecione os Targets");

        lblStage3.setText("3. Lista de targets e entidades para execução");

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
                            .addComponent(lblStage1)
                            .addComponent(lblStage3))))
                .addContainerGap(353, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblStage3)
                .addContainerGap())
        );

        btnBack.setText("< Retonar");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnNext.setText("Avançar >");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        pnStage.setLayout(new java.awt.CardLayout());
        pnStage.add(pnSelectTarget, "stage2");
        pnStage.add(pnSelectEntity, "stage1");
        pnStage.add(pnExecutionMonitor, "stage3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExecute)
                .addContainerGap())
            .addComponent(pnStageInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addComponent(pnStage, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnStageInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnStage, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExecute)
                    .addComponent(btnBack)
                    .addComponent(btnNext)
                    .addComponent(btnCancel))
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecuteActionPerformed
        execute();
    }//GEN-LAST:event_btnExecuteActionPerformed

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        nextStage();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        backStage();
    }//GEN-LAST:event_btnBackActionPerformed

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
			java.util.logging.Logger.getLogger(FrmCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(FrmCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(FrmCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(FrmCodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
        //</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new FrmCodeGeneration(null).setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExecute;
    private javax.swing.JButton btnNext;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblStage1;
    private javax.swing.JLabel lblStage2;
    private javax.swing.JLabel lblStage3;
    private javax.swing.JLabel lblStages;
    private br.com.atos.cg.gui.PnExecutionMonitor pnExecutionMonitor;
    private br.com.atos.cg.gui.PnSelectEntity pnSelectEntity;
    private br.com.atos.cg.gui.PnSelectTarget pnSelectTarget;
    private javax.swing.JPanel pnStage;
    private javax.swing.JPanel pnStageInfo;
    // End of variables declaration//GEN-END:variables
	
    public void start() {		
		JFrameUtils.setCenterLocation(this);
		JFrameUtils.showMazimized(this);				
		goToStage(1);		
        setVisible(true);
	}

	public CodeGenerator getCg() {
		return cg;
	}    	
    	
	public void goToStage(int stage) {
		
		this.currentStage = stage;
		
		this.lblStage1.setFont(new java.awt.Font("Liberation", Font.PLAIN, 11));
		this.lblStage2.setFont(new java.awt.Font("Liberation", Font.PLAIN, 11));
		this.lblStage3.setFont(new java.awt.Font("Liberation", Font.PLAIN, 11));
		this.btnBack.setEnabled(false);
		this.btnNext.setEnabled(false);
		this.btnExecute.setEnabled(false);
		
		if (stage > 1) {
			this.btnBack.setEnabled(true);
		}
		
		if (stage < 3) {
			this.btnNext.setEnabled(true);
		}
				
		switch (stage) {
			case 1 : 
				this.setTitle(this.lblStage1.getText());
				this.lblStage1.setFont(new java.awt.Font("Liberation", Font.BOLD, 11));
				break;
				
			case 2 :
				this.setTitle(lblStage2.getText());
				this.lblStage2.setFont(new java.awt.Font("Liberation", Font.BOLD, 11));
				break;
			
			case 3 :
				initStage3();				
				break;
		}
		
		getPnStageLayout().show(pnStage, "stage" + stage);
	}
	
	@Override
	public void setTitle(String title) {
		super.setTitle(CodeGenerator.APP_TITLE + " - " + title);
	}

	private void nextStage() {
		if (validateStage()) {
			goToStage(this.currentStage + 1);
		}
	}
	
	private void backStage() {
		goToStage(this.currentStage - 1);
	}
	
    private void execute() {    	
		
		btnExecute.setEnabled(false);
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				for (TargetStatus targetStatus : targetsStatus) {
					
					getCg().execute(targetStatus.getEntityClass(), targetStatus.getTarget());
					
					targetStatus.setExecuted(true);
					
					pnExecutionMonitor.refreshTable();
				}				
			}
		});
		thread.start();
	}
	
	private CardLayout getPnStageLayout() {
		return (CardLayout) pnStage.getLayout();
	}

	private void initialize() {		
		this.pnSelectEntity.initialize(getCg().getEntitiesClass());
		this.pnSelectTarget.initialize(getCg().getTargetsGroup(), getCg().getTargets());
	}

	private boolean validateStage() {
				
		switch (this.currentStage) {
			
			case 1 :
				if (pnSelectEntity.validateFrm()) {
					entitiesClassSelecteds = pnSelectEntity.getEntitiesClassSelecteds();
					return true;
				}
				break;
				
			case 2 :
				if (pnSelectTarget.validateFrm()) {
					targetsSelecteds = pnSelectTarget.getTargetsSelecteds();
					return true;
				}
				break;
		}
		
		return false;
	}

	private void initStage3() {
		
		this.setTitle(lblStage3.getText());
		this.lblStage3.setFont(new java.awt.Font("Liberation", Font.BOLD, 11));				
		this.btnExecute.setEnabled(true);
		
		this.targetsStatus = new ArrayList<TargetStatus>();
		
		for (Class<?> entityClass : entitiesClassSelecteds) {
			for (Target target : targetsSelecteds) {
				targetsStatus.add(new TargetStatus(entityClass, target));
			}
		}
				
		this.pnExecutionMonitor.initialize(targetsStatus);
	}
}
