/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.github.cg.component.StringUtils;
import com.github.cg.gui.tablemodel.AttributeTableModel;
import com.github.cg.gui.util.EntityComboBoxModel;
import com.github.cg.gui.util.JFrameUtils;
import com.github.cg.gui.util.SuggestComboBox;
import com.github.cg.model.Attribute;
import com.github.cg.model.AttributeId;
import com.github.cg.model.AttributeManyToOne;
import com.github.cg.model.AttributeOneToMany;
import com.github.cg.model.Entity;
import com.github.cg.model.Gender;
import com.github.cg.model.TargetContext;
import com.github.cg.model.TargetTask;

/**
 * 
 * @author 205327
 */
public class DlgFrmEntity extends javax.swing.JDialog {
	
	private static final long serialVersionUID = 1L;
	
	public static final String CONFIG_RENDER_COLUMN = "renderColumn";
	public static final String CONFIG_RENDER_FORM = "renderForm";
	public static final String CONFIG_RENDER_FILTER = "renderFilter";
	public static final String CONFIG_SHOW_ATTRIBUTES_ONE_TO_MANY = "showAttributesOneToMany";
	public static final String CONFIG_RENDER_FORM_TYPE = "renderFormType";
	public static final String CONFIG_RENDER_ATTRIBUTE_DESCRIPTION = "renderAttributeDescription";
	
	/**
	 * Define se tabela de atributos sera renderizada no formulario	
	 */
	public static final String CONFIG_RENDER_ATTRIBUTES = "renderAttributes";
	
	private EntityComboBoxModel<Gender> cmGender;
	private Entity entity;
	private int status;
	private final List<String> patterns;
	private Class<?> entityClass = DlgFrmEntity.class; // Seta esta classe somente para nao dar erros;
	private SuggestComboBox cmAttributeDescription;

	/**
	 * Creates new form WinFrmEntity
	 */
	public DlgFrmEntity(java.awt.Frame parent, boolean modal, List<String> patterns) {

		super(parent, modal);

		this.patterns = patterns;
		
		initComponents();

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		txtLabel.requestFocus();

		cmGender = new EntityComboBoxModel<Gender>(Gender.values()) {

			private static final long serialVersionUID = 1L;

			@Override
			public String getLabel(Gender item) {
				return item.getDescription();
			}
		};
		
		cbbGender.setModel(cmGender);
		
		this.cmAttributeDescription = new SuggestComboBox(this.cbbAttributeDescription) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public List<String> getItems(String suggest) {
				return AttributeTableModel.getOptions(getEntityClass(), suggest);
			}
		};
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblEntity = new javax.swing.JLabel();
        lblTarget = new javax.swing.JLabel();
        lblLabel = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblPlural = new javax.swing.JLabel();
        lblAttributeDescription = new javax.swing.JLabel();
        txtEntity = new javax.swing.JTextField();
        txtTarget = new javax.swing.JTextField();
        txtLabel = new javax.swing.JTextField();
        cbbGender = new javax.swing.JComboBox();
        txtPlural = new javax.swing.JTextField();
        cbbAttributeDescription = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnAttributes = new com.github.cg.gui.PnAttributes(this.patterns);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Formulário de Entidade");
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        lblEntity.setText("Entidade:");

        lblTarget.setText("Target:");

        lblLabel.setText("Rótulo:");

        lblGender.setText("Gênero:");

        lblPlural.setText("Plural:");

        lblAttributeDescription.setText("Descrição:");

        txtTarget.setEditable(false);

        txtLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLabelKeyReleased(evt);
            }
        });

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
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
                    .addComponent(pnAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEntity)
                            .addComponent(lblLabel)
                            .addComponent(lblPlural))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPlural, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEntity, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(lblAttributeDescription))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblGender, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblTarget, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbGender, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbbAttributeDescription, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTarget, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTarget)
                    .addComponent(lblEntity)
                    .addComponent(txtEntity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGender)
                    .addComponent(txtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPlural)
                    .addComponent(txtPlural, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbAttributeDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAttributeDescription))
                .addGap(16, 16, 16)
                .addComponent(pnAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtLabelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLabelKeyReleased
        
		String label = txtLabel.getText();
		String plural = txtPlural.getText();
		
		if (label.contains(plural)) {
			txtPlural.setText(label);
		}
    }//GEN-LAST:event_txtLabelKeyReleased

	private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOkActionPerformed
		save();
	}// GEN-LAST:event_btnOkActionPerformed

	private void save() {

		// Verifica se foi informado os dados
		if (cmGender.getSelectedEntity() == null) {
			JFrameUtils.showErro("Erro de validação","O Gênero não foi informado!");
			return;
		}
		
		if (this.cmAttributeDescription.getSelectedItem() == null) {
			JFrameUtils.showErro("Erro de validação","O atributo que descreve a entidade não foi informado!");
			return;
		}

		if (!getPnAttributes().validateAttributes()) {
			return;
		}

		if (StringUtils.getInstance().isNullOrEmpty(txtLabel.getText())) {
			getEntity().setLabelDefault();
		}
		else {
			getEntity().setLabel(txtLabel.getText());
		}
		
		if (StringUtils.getInstance().isNullOrEmpty(txtPlural.getText())) {
			getEntity().setPluralDefault();
		}
		else {
			getEntity().setPlural(txtPlural.getText());
		}		

		getEntity().setGender(this.cmGender.getSelectedEntity());
		getEntity().setAttributeDescription(this.cmAttributeDescription.getSelectedItem().toString());

		getPnAttributes().save();

		status = JOptionPane.OK_OPTION;

		setVisible(false);
	}

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelActionPerformed
		status = JOptionPane.CANCEL_OPTION;
		setVisible(false);
	}// GEN-LAST:event_btnCancelActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(DlgFrmEntity.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(DlgFrmEntity.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(DlgFrmEntity.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(DlgFrmEntity.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>
		// </editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				DlgFrmEntity dialog = new DlgFrmEntity(
						new javax.swing.JFrame(), true, null);
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

	public Entity getEntity() {
		return entity;
	}

	public void start(TargetContext targetContext, TargetTask targetTask) {

		this.entity = targetContext.getEntity();
		this.entityClass = this.entity.getEntityClass();
		
		this.txtTarget.setText(targetContext.getTarget().getDescription());
		this.txtEntity.setText(targetContext.getEntity().getName());

		if (this.entity != null) {

			setTitle(getTitle() + " - " + entity.getName());

			this.txtLabel.setText(getEntity().getLabel());
			this.cmGender.setSelectedEntity(getEntity().getGender());
			this.txtPlural.setText(getEntity().getPlural());
			this.cmAttributeDescription.load();
			this.cbbAttributeDescription.setSelectedItem(getEntity().getAttributeDescription());
						
			List<Attribute> attributes = new ArrayList<Attribute>();

			for (Attribute attribute : getEntity().getAttributes()) {

				boolean add = false;

				if (attribute instanceof AttributeId) {
					add = true;
				} else if (attribute instanceof AttributeManyToOne) {
					add = true;
				} else if (attribute instanceof AttributeOneToMany) {
					if (targetTask.getConfigValueAsBoolean(CONFIG_SHOW_ATTRIBUTES_ONE_TO_MANY)) {
						add = true;
					}
				} else {
					add = true;
				}

				if (add) {
					attributes.add(attribute);
				}
			}

			getPnAttributes().initialize(attributes);
		}
		// Para testes
		else {
			getPnAttributes().initialize(null);
		}
		
		getPnAttributes().getTmAttributes().resetColumns();

		boolean showMaximized = true;
		
		if (targetTask != null) {			
			
			if (!targetTask.getConfigValueAsBoolean(CONFIG_RENDER_ATTRIBUTES)) {
				getPnAttributes().setVisible(false);
				setPreferredSize(new Dimension(800, 300));
				showMaximized = false;
			}
			else {
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
		}
		
		pack();

		JFrameUtils.setCenterLocation(this);
		if (showMaximized) {
			JFrameUtils.showMazimized(this);
		}
		status = JOptionPane.CANCEL_OPTION;
		setVisible(true);
	}

	public PnAttributes getPnAttributes() {
		return (PnAttributes) pnAttributes;
	}

	public boolean isStatusOK() {
		return status == JOptionPane.OK_OPTION;
	}
	
	public Class<?> getEntityClass() {
		return this.entityClass;
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox cbbAttributeDescription;
    private javax.swing.JComboBox cbbGender;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblAttributeDescription;
    private javax.swing.JLabel lblEntity;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblLabel;
    private javax.swing.JLabel lblPlural;
    private javax.swing.JLabel lblTarget;
    private com.github.cg.gui.PnAttributes pnAttributes;
    private javax.swing.JTextField txtEntity;
    private javax.swing.JTextField txtLabel;
    private javax.swing.JTextField txtPlural;
    private javax.swing.JTextField txtTarget;
    // End of variables declaration//GEN-END:variables
}
