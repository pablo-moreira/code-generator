/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.gui;

import br.com.atos.gc.gui.tablemodel.AttributeTableModel;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atos.gc.model.AttributeOneToMany;
import br.com.atos.gc.model.Entity;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

/**
 *
 * @author 205327
 */
public class FrmAttributes extends javax.swing.JPanel {
  
    private final AttributeTableModel tmAttributes;
    private Entity entity;

    /**
     * Creates new form FrmAttributes
     */
    public FrmAttributes() {
        
        initComponents();
        
        tmAttributes = new AttributeTableModel(tblAttributes);
    }
    
    public void initialize(Entity entity) {
        
        this.entity = entity;
        
        if (entity != null) {                       
            getTmAttributes().getAttributes().clear();
            getTmAttributes().getAttributes().addAll(getEntity().getAttributes());
        }
        // Para testes
        else {
            List<Attribute> attributes = new ArrayList<Attribute>();

            attributes.add(new Attribute());
            attributes.add(new AttributeOneToMany());
            attributes.add(new AttributeManyToOne());

            getTmAttributes().getAttributes().addAll(attributes);
            //getTmAttributes().hideColumnRenderColumn();
            //getTmAttributes().hideColumnRenderFilter();
            //getTmAttributes().hideColumnRenderForm();
            //getTmAttributes().hideColumnAttributeDescription();    
            getTmAttributes().fireTableDataChanged();
        }
        
        
    }

    public AttributeTableModel getTmAttributes() {
        return tmAttributes;
    }
    
    public Entity getEntity() {
        return entity;
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnAttributes = new javax.swing.JScrollPane();
        tblAttributes = new javax.swing.JTable() {

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

        setBorder(javax.swing.BorderFactory.createTitledBorder("Atributos"));
        setPreferredSize(new java.awt.Dimension(720, 113));

        pnAttributes.setAutoscrolls(true);

        tblAttributes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        pnAttributes.setViewportView(tblAttributes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane pnAttributes;
    private javax.swing.JTable tblAttributes;
    // End of variables declaration//GEN-END:variables
}
