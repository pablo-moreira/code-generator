/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.gui.tablemodel;

import br.com.atos.gc.gui.WinFrmEntity;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeFormType;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atos.gc.model.AttributeOneToMany;
import br.com.atos.gc.util.ColumnMetadata;
import br.com.atos.gc.util.EntityColumnWidthTableModel;
import br.com.atos.gc.util.EntityComboBoxModel;
import br.com.atos.utils.ReflectionUtils;

/**
 *
 * @author 205327
 */
public class AttributeTableModel extends EntityColumnWidthTableModel<Attribute> {

	private static final long serialVersionUID = 1L;
	
	public static final ColumnMetadata COL_ATTRIBUTE = new ColumnMetadata(0, "Atributo", 150);
    public static final ColumnMetadata COL_LABEL = new ColumnMetadata(1, "Rótulo", 200);
    public static final ColumnMetadata COL_RENDER_COLUMN = new ColumnMetadata(2, "Coluna", 60, "Renderizar uma coluna para atributo");
    public static final ColumnMetadata COL_RENDER_FILTER = new ColumnMetadata(3, "Filtro", 60, "Renderizar um filtro para atributo");
    public static final ColumnMetadata COL_RENDER_FORM = new ColumnMetadata(4, "Form.", 60, "Renderizar um campo no formulário para atributo");
    public static final ColumnMetadata COL_ATTRIBUTE_DESCRIPTION = new ColumnMetadata(5, "Atrib. Descrição da Associação", 220);
    public static final ColumnMetadata COL_FORM_TYPE = new ColumnMetadata(6, "Tipo de Formulário", 150);
  
    private List<Attribute> attributes = new ArrayList<Attribute>();

    public AttributeTableModel(JTable table) {
        super(table);
    }
    
    @Override
    public Object getValueAt(Attribute attribute, int columnIndex) {
        switch (columnIndex) {
            case 0 :         
                return attribute.getField() != null ? attribute.getField().getName() : null;
            case 1 :
                return attribute.getLabel();
            case 2 :
                return attribute.isRenderColumn();
            case 3 :
                return attribute.isRenderFilter();
            case 4 :
                return attribute.isRenderForm();
            case 5 :
            	if (attribute instanceof AttributeManyToOne) {
		    return ((AttributeManyToOne) attribute).getDescriptionAttributeOfAssociation();
            	}
            	else {
		    return null;
            	}
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
		    
    private TableColumn getColumnFormType() {
        return getColumnByIndex(COL_FORM_TYPE.getIndex());
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
		
	public void hideColumnFormType() {
		hideColumn(getColumnFormType());
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
    
    @Override
    public boolean isCellEditable(int row, int col) { 
        
        if (col == COL_ATTRIBUTE.getIndex()) {
            return false; 
        }
        else if (col == COL_LABEL.getIndex() || col == COL_RENDER_COLUMN.getIndex() || col == COL_RENDER_FILTER.getIndex() || col == COL_RENDER_FORM.getIndex()) {
            return true;
        }
        else {
            
            Attribute attribute = getEntityByRow(row);
            
            boolean instanceofOneToMany = (attribute instanceof AttributeOneToMany);
            boolean instanceofManyToOne = (attribute instanceof AttributeManyToOne);
            
            if (col == COL_ATTRIBUTE_DESCRIPTION.getIndex() && instanceofManyToOne) {
                return true;
            }            
            else if (col == COL_FORM_TYPE.getIndex() && instanceofOneToMany) {
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        
        Attribute attribute = getEntityByRow(row);
        
        if (COL_LABEL.getIndex() == col) {
            attribute.setLabel((String) value);
        }
        else if (COL_RENDER_COLUMN.getIndex() == col) {
            attribute.setRenderColumn((Boolean) value);
        }
        else if (COL_RENDER_FILTER.getIndex() == col) {
            attribute.setRenderFilter((Boolean)value);
        }
        else if (COL_RENDER_FORM.getIndex() == col) {
            attribute.setRenderForm((Boolean)value);
        }
        else if (COL_ATTRIBUTE_DESCRIPTION.getIndex() == col) {
	    AttributeManyToOne attributeManyToOne = (AttributeManyToOne) attribute;
	    attributeManyToOne.setDescriptionAttributeOfAssociation((String) value);
        }
        else if (COL_FORM_TYPE.getIndex() == col) {
        	
        	AttributeOneToMany attributeOneToMany = (AttributeOneToMany) attribute;
        	
        	for (AttributeFormType item : AttributeFormType.values()) {
                if (item.getDescription().equals(value)) {
                	attributeOneToMany.setFormType(item);
                	break;
                }
            }
        }
        
        //System.out.println("Value: " + value + " - Row: " + row + " - Col: " + col);
        
        fireTableCellUpdated(row, col);
    }

    @Override
    public Class<?> getColumnClass(int col) {
        
        if (COL_RENDER_COLUMN.getIndex() == col 
                || COL_RENDER_FILTER.getIndex() == col
                || COL_RENDER_FORM.getIndex() == col) {
            return Boolean.class;
        }
        else if (COL_FORM_TYPE.getIndex() == col) {
        	return AttributeFormType.class;
        }
        else {
            return super.getColumnClass(col);
        }
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
    
    public TableCellEditor getCellEditor(int row, int col) {
        
        if (COL_ATTRIBUTE_DESCRIPTION.getIndex() == col) {
            
            final Attribute attribute = getEntityByRow(row);

            List<String> entities = new ArrayList<String>();
            
            Class<?> associationClass = null;
            
            if (attribute.getField() != null) {
                associationClass = attribute.getField().getType();
            }
            else {
            	associationClass = WinFrmEntity.class;
            }
            
            List<Field> associationAttributes = ReflectionUtils.getFieldsRecursive(associationClass);
    		
            for (Field field : associationAttributes) {    	
                if (!Modifier.isStatic(field.getModifiers())) {
                        entities.add(field.getName());
                }
            }
    		
            JComboBox cbbAttributeDescription = new JComboBox(new EntityComboBoxModel<String>(entities) {

				private static final long serialVersionUID = 1L;
		
				@Override
				public String getLabel(String item) {
					return item;
				}    			
            });			

            return new DefaultCellEditor(cbbAttributeDescription);
        }
        else if (COL_FORM_TYPE.getIndex() == col) {
        	
        	final Attribute attribute = getEntityByRow(row);
        	
        	if (attribute instanceof AttributeOneToMany) {
        		
        		AttributeOneToMany attrOneToMany = (AttributeOneToMany) attribute;
        	              
        		//AttributeFormType.values()
        		
        		AttributeFormType[] types;
        		
        		if (attrOneToMany.isAllowedFormTypeInternal()) {
        			types = new AttributeFormType[] { AttributeFormType.EXTERNAL };
        		}
        		else {
        			types = new AttributeFormType[] { AttributeFormType.EXTERNAL, AttributeFormType.INTERNAL };
        		}
        			
		        JComboBox cbbFormType = new JComboBox(new EntityComboBoxModel<AttributeFormType>(types) {
		
					private static final long serialVersionUID = 1L;
		
					@Override
		            public String getLabel(AttributeFormType item) {
		                return item.getDescription();
		            }
		        });
		                
		        return new DefaultCellEditor(cbbFormType);
        	}
        }
        
        return null;
    }
}