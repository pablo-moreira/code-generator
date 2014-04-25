/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.gui;

import br.com.atos.gc.gui.tablemodel.AttributeTableModel;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.util.EntityColumnWidthTableModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.TableColumn;

/**
 *
 * @author 205327
 */
public class FrmEntity extends javax.swing.JPanel {
    
    private final AttributeTableModel tmAttributes;

    /**
     * Creates new form FrmEntity
     */
    public FrmEntity() {
        
        initComponents();
        
        final List<Attribute> attributes = new ArrayList<Attribute>();
        
        tmAttributes = new AttributeTableModel(tblAttributes, attributes);
        
        tmAttributes.hideColumnRenderColumn();
        tmAttributes.hideColumnRenderFilter();
        tmAttributes.hideColumnRenderForm();
    }
    
    public static void main(String[] args) {
        
        JFrame win = new JFrame("Formulário");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(800,600);
        
        FrmEntity frm = new FrmEntity();
        
        win.add(frm);
        
        win.setVisible(true);        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbbGender = new javax.swing.JComboBox();
        txtLabel = new javax.swing.JTextField();
        pnAttributesRoot = new javax.swing.JPanel();
        pnAttributes = new javax.swing.JScrollPane();
        tblAttributes = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Entidade"));
        setPreferredSize(new java.awt.Dimension(800, 300));

        jLabel1.setText("Rótulo:");

        jLabel2.setText("Gênero: ");

        cbbGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbGenderActionPerformed(evt);
            }
        });

        pnAttributesRoot.setBorder(javax.swing.BorderFactory.createTitledBorder("Atributos"));
        pnAttributesRoot.setPreferredSize(new java.awt.Dimension(720, 100));

        pnAttributes.setAutoscrolls(true);

        tblAttributes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        pnAttributes.setViewportView(tblAttributes);

        javax.swing.GroupLayout pnAttributesRootLayout = new javax.swing.GroupLayout(pnAttributesRoot);
        pnAttributesRoot.setLayout(pnAttributesRootLayout);
        pnAttributesRootLayout.setHorizontalGroup(
            pnAttributesRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAttributesRootLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnAttributesRootLayout.setVerticalGroup(
            pnAttributesRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnAttributesRootLayout.createSequentialGroup()
                .addComponent(pnAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnAttributesRoot, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 526, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnAttributesRoot, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbbGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbGenderActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbbGender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane pnAttributes;
    private javax.swing.JPanel pnAttributesRoot;
    private javax.swing.JTable tblAttributes;
    private javax.swing.JTextField txtLabel;
    // End of variables declaration//GEN-END:variables
}
