/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.gui.tablemodel;

import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.util.ColumnMetadata;
import br.com.atos.gc.util.EntityColumnWidthTableModel;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author 205327
 */
public class AttributeTableModel extends EntityColumnWidthTableModel<Attribute> {
    
    public static final ColumnMetadata COL_ATTRIBUTE = new ColumnMetadata(1, "Atributo", 150);
    public static final ColumnMetadata COL_LABEL = new ColumnMetadata(2, "Rótulo", 200);
    public static final ColumnMetadata COL_RENDER_COLUMN = new ColumnMetadata(3, "Coluna", 50);
    public static final ColumnMetadata COL_RENDER_FILTER = new ColumnMetadata(4, "Filtro", 50);
    public static final ColumnMetadata COL_RENDER_FORM = new ColumnMetadata(5, "Form.", 50);
    public static final ColumnMetadata COL_ATTRIBUTE_DESCRIPTION = new ColumnMetadata(6, "Atrib. Descrição da Associação", 200);
    public static final ColumnMetadata COL_FORM_TYPE = new ColumnMetadata(7, "Tipo de Formulário", 150);
  
    private List<Attribute> attributes;

    public AttributeTableModel(JTable table, List<Attribute> attributes) {
        super(table);
        this.attributes = attributes;
    }
    
    @Override
    public Object getValueAt(Attribute entidade, int columnIndex) {
        switch (columnIndex) {
            case 0 :
                return "";
            case 1 :
                return "";
            case 2 :
                return "";
            case 3 :
                return "";
            default :
                return null;
        }
    }

    @Override
    protected void initialize() {
        
        super.initialize();
        
        //getTable().getColumnModel().getColumn(2).setCellRenderer(TableCellRendererUtils.getDataHorarioTableCellRenderer());
        
//        TableColumn sportColumn = table.getColumnModel().getColumn(2);
//       
//        JComboBox comboBox = new JComboBox();
//        comboBox.addItem("Snowboarding");
//        comboBox.addItem("Rowing");
//        comboBox.addItem("Chasing toddlers");
//        comboBox.addItem("Speed reading");
//        comboBox.addItem("Teaching high school");
//        comboBox.addItem("None");
//        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }
    
    public TableColumn getColumnRenderFilter() {
        return getTable().getColumnModel().getColumn(COL_RENDER_FILTER.getIndex());
    }
    
    public TableColumn getColumnRenderForm() {
        return getTable().getColumnModel().getColumn(COL_RENDER_FORM.getIndex());
    }
    
    public TableColumn getColumnRenderColumn() {
        return getTable().getColumnModel().getColumn(COL_RENDER_COLUMN.getIndex());
    }

    public void hideColumnRenderColumn() {
        hideColumn(getColumnRenderColumn());
    }

    public void hideColumnRenderFilter() {
        hideColumn(getTable().getColumnModel().getColumn(4));
    }

    public void hideColumnRenderForm() {
        hideColumn(getTable().getColumnModel().getColumn(5));   
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public List<ColumnMetadata> getColumnsMetadata() {
        return Arrays.asList(new ColumnMetadata[] { 
            COL_ATTRIBUTE, 
            COL_LABEL, 
            COL_RENDER_COLUMN, 
            COL_RENDER_FILTER, 
            COL_RENDER_FORM, 
            COL_ATTRIBUTE_DESCRIPTION, 
            COL_FORM_TYPE 
        });
    }

    @Override
    public List<Attribute> getEntities() {
        return attributes;
    }
}