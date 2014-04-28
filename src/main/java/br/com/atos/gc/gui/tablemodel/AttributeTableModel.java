/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.gui.tablemodel;

import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeFormType;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atos.gc.model.AttributeOneToMany;
import br.com.atos.gc.util.ColumnMetadata;
import br.com.atos.gc.util.EntityColumnWidthTableModel;
import br.com.atos.gc.util.EntityComboBoxModel;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author 205327
 */
public class AttributeTableModel extends EntityColumnWidthTableModel<Attribute> {
    
    public static final ColumnMetadata COL_ATTRIBUTE = new ColumnMetadata(0, "Atributo", 150);
    public static final ColumnMetadata COL_LABEL = new ColumnMetadata(1, "Rótulo", 200);
    public static final ColumnMetadata COL_RENDER_COLUMN = new ColumnMetadata(2, "Coluna", 50, "Renderizar uma coluna para atributo");
    public static final ColumnMetadata COL_RENDER_FILTER = new ColumnMetadata(3, "Filtro", 50, "Renderizar um filtro para atributo");
    public static final ColumnMetadata COL_RENDER_FORM = new ColumnMetadata(4, "Form.", 50, "Renderizar um campo no formulário para atributo");
    public static final ColumnMetadata COL_ATTRIBUTE_DESCRIPTION = new ColumnMetadata(5, "Atrib. Descrição da Associação", 200);
    public static final ColumnMetadata COL_FORM_TYPE = new ColumnMetadata(6, "Tipo de Formulário", 150);
  
    private List<Attribute> attributes;

    public AttributeTableModel(JTable table, List<Attribute> attributes) {
        super(table);
        this.attributes = attributes;
    }
    
    @Override
    public Object getValueAt(Attribute attribute, int columnIndex) {
        switch (columnIndex) {
            case 0 :         
                return "";//entidade.getField().getName();
            case 1 :
                return attribute.getLabel();
            case 2 :
                return attribute.isRenderColumn();
            case 3 :
                return attribute.isRenderFilter();
            case 4 :
                return attribute.isRenderForm();
            case 5 :
                //return entidade;
            case 6 :
                if (attribute instanceof AttributeOneToMany) {
                    return ((AttributeOneToMany) attribute).getFormType().getDescription();
                }
                else {
                    return null;
                }
            default :
                return null;
        }
    }

    @Override
    protected void initialize() {
        
        super.initialize();
        
        //getTable().getColumnModel().getColumn(2).setCellRenderer(TableCellRendererUtils.getDataHorarioTableCellRenderer());
        
        TableColumn colFormType = getColumnFormType();
        
        JComboBox comboBox = new JComboBox(new EntityComboBoxModel<AttributeFormType>(AttributeFormType.values()) {
            @Override
            public String getLabel(AttributeFormType item) {
                return item.getDescription();
            }
        });
                
        colFormType.setCellEditor(new DefaultCellEditor(comboBox));
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
    
    public TableColumn getColumnAttributeDescription() {
        return getTable().getColumnModel().getColumn(COL_ATTRIBUTE_DESCRIPTION.getIndex());
    }
    
    public void hideColumnRenderColumn() {
        hideColumn(getColumnRenderColumn());
    }

    public void hideColumnRenderFilter() {
        hideColumn(getColumnRenderFilter());
    }

    public void hideColumnRenderForm() {
        hideColumn(getColumnRenderForm());   
    }

    public void hideColumnAttributeDescription() {
        hideColumn(getColumnAttributeDescription());   
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
    
    private TableColumn getColumnFormType() {
        return getColumnByIndex(COL_FORM_TYPE.getIndex());
    }
    
    @Override
    public boolean isCellEditable(int row, int col) { 
        
        if (col == 0) {
            return false; 
        }
        else if (col == 1 || col == 2 || col == 3 || col == 4 ) {
            return true;
        }
        else {
            
            Attribute attribute = getEntityByRow(row);
            
            boolean instanceofOneToMany = (attribute instanceof AttributeOneToMany);
            boolean instanceofManyToOne = (attribute instanceof AttributeManyToOne);
            
            if (col == 5 && instanceofManyToOne) {
                return true;
            }            
            else if (col == 6 && instanceofOneToMany) {
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        
        Attribute entity = getEntityByRow(row);
        
        if (COL_LABEL.getIndex() == col) {
            entity.setLabel((String) value);
        }
        else if (COL_RENDER_COLUMN.getIndex() == col) {
            entity.setRenderColumn((Boolean) value);
        }
        else if (COL_RENDER_FILTER.getIndex() == col) {
            entity.setRenderFilter((Boolean)value);
        }
        else if (COL_RENDER_FORM.getIndex() == col) {
            entity.setRenderForm((Boolean)value);
        }
        else if (COL_FORM_TYPE.getIndex() == col) {
            System.out.println("form type");    
        }
        else {
            System.out.println("Teste!!!");
                
        }
        
        System.out.println("Value: " + value + " - Row: " + row + " - Col: " + col);
        
        //fireTableRowsUpdated(row,row);
        fireTableCellUpdated(row, col);
    }

    @Override
    public Class<?> getColumnClass(int col) {
        
        if (COL_RENDER_COLUMN.getIndex() == col 
                || COL_RENDER_FILTER.getIndex() == col
                || COL_RENDER_FORM.getIndex() == col) {
            return Boolean.class;
        }
        else {
            return super.getColumnClass(col);
        }
    }
}