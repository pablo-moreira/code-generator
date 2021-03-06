/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.gui.tablemodel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.OneToMany;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import com.github.cg.gui.DlgFrmEntity;
import com.github.cg.gui.util.ComboBoxCellEditor;
import com.github.cg.gui.util.EntityColumnWidthTableModel;
import com.github.cg.gui.util.EntityComboBoxModel;
import com.github.cg.gui.util.SuggestComboBox;
import com.github.cg.model.Attribute;
import com.github.cg.model.AttributeManyToOne;
import com.github.cg.model.AttributeOneToMany;
import com.github.cg.util.ReflectionUtils;
import com.github.cg.vo.ColumnMetadata;

/**
 * @author pablo.filetti@gmail.com
 */
public class AttributeTableModel extends EntityColumnWidthTableModel<Attribute> {

	private static final long serialVersionUID = 1L;
	
	public static final ColumnMetadata COL_ATTRIBUTE = new ColumnMetadata(0, "Atributo", 150);
    public static final ColumnMetadata COL_LABEL = new ColumnMetadata(1, "Rótulo", 200);
	public static final ColumnMetadata COL_PATTERN = new ColumnMetadata(2, "Pattern", 150);
    public static final ColumnMetadata COL_RENDER_COLUMN = new ColumnMetadata(3, "Coluna", 60, "Renderizar uma coluna para atributo");
    public static final ColumnMetadata COL_RENDER_FILTER = new ColumnMetadata(4, "Filtro", 60, "Renderizar um filtro para atributo");
    public static final ColumnMetadata COL_RENDER_FORM = new ColumnMetadata(5, "Form.", 60, "Renderizar um campo no formulário para atributo");    
    public static final ColumnMetadata COL_FORM_TYPE = new ColumnMetadata(6, "Tipo de Formulário", 150);
    public static final ColumnMetadata COL_ATTRIBUTE_DESCRIPTION = new ColumnMetadata(7, "Atrib. Descrição da Associação", 350);
  
    private List<Attribute> attributes = new ArrayList<Attribute>();
	private final List<String> patterns;
	private final List<String> formTypes;

    public AttributeTableModel(JTable table, List<String> patterns, List<String> formTypes) {
        super(table);
		this.patterns = patterns;
		this.formTypes = formTypes;
    }
    
    @Override
    public Object getValueAt(Attribute attribute, int columnIndex) {
        switch (columnIndex) {
            case 0 :         
                return attribute.getName();
            case 1 :
                return attribute.getLabel();
			case 2 :
                return attribute.getClass() == Attribute.class ? attribute.getPattern() : null;
			case 3 :
                return attribute.isRenderColumn();
            case 4 :
                return attribute.isRenderFilter();
            case 5 :
                return attribute.isRenderForm();
            case 6 :
                if (attribute instanceof AttributeOneToMany) {
                    return ((AttributeOneToMany) attribute).getFormType();
                }
                else {
                    return null;
                }
            case 7 :
            	if (attribute instanceof AttributeManyToOne) {
            		return ((AttributeManyToOne) attribute).getDescriptionAttributeOfAssociation();
            	}
            	else {
					return null;
            	}
            default :
                return null;
        }
    }
        
	public TableColumn getColumnPattern() {
        return getTable().getColumnModel().getColumn(COL_PATTERN.getIndex());
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
			COL_PATTERN, 
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
		else if (col == COL_PATTERN.getIndex()) {
			
			Attribute attribute = getEntityByRow(row);
			
			return isEditablePattern(attribute);
		}
        else {
            
            Attribute attribute = getEntityByRow(row);
            
            boolean instanceofOneToMany = (attribute instanceof AttributeOneToMany);
            boolean instanceofManyToOne = (attribute instanceof AttributeManyToOne);
            
            if (col == COL_ATTRIBUTE_DESCRIPTION.getIndex() && instanceofManyToOne) {
                return true;
            }            
            else if (col != COL_FORM_TYPE.getIndex() || !instanceofOneToMany) {
				return false;
			}
            else {
				return true;
			}
        }
    }
    
    private boolean isEditablePattern(Attribute attribute) {

    	Class<?> type = attribute.getType();
    	
		return attribute.getClass() == Attribute.class 
    			&& !Date.class.isAssignableFrom(type)
    			&& !Calendar.class.isAssignableFrom(type)
    			&& !Enum.class.isAssignableFrom(type); 
	}

	@Override
    public void setValueAt(Object value, int row, int col) {
        
        Attribute attribute = getEntityByRow(row);
        
        if (COL_LABEL.getIndex() == col) {
            attribute.setLabel((String) value);
        }
		else if (COL_PATTERN.getIndex() == col) {
			attribute.setPattern((String) value);
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
        	attributeOneToMany.setFormType((String) value);
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

            final Class<?> associationClass;
            
            if (attribute.getType() != null) {
                associationClass = attribute.getType();
            }
            else {
            	associationClass = DlgFrmEntity.class;
            }
    		
            JComboBox<String> cbbAttributeDescription = new JComboBox<String>();

			SuggestComboBox suggestComboBox = new SuggestComboBox(cbbAttributeDescription) {

				@Override
				public List<String> getItems(String suggest) {
					return AttributeTableModel.getOptions(associationClass, suggest);
				}
			};
		    suggestComboBox.load();
		    
            return new ComboBoxCellEditor(cbbAttributeDescription);
        }
		else if (COL_PATTERN.getIndex() == col) {
			
			final Attribute attribute = getEntityByRow(row);
        				
        	if (isEditablePattern(attribute)) {

		        JComboBox<String> cbbFormType = new JComboBox<String>(new EntityComboBoxModel<String>(this.patterns) {
		
					private static final long serialVersionUID = 1L;
		
					@Override
		            public String getLabel(String item) {
		                return item;
		            }
		        });
		                
		        return new DefaultCellEditor(cbbFormType);
        	}
		}
        else if (COL_FORM_TYPE.getIndex() == col) {
        	
        	final Attribute attribute = getEntityByRow(row);
        	
        	if (attribute instanceof AttributeOneToMany) {
        		
		        JComboBox<String> cbbFormType = new JComboBox<String>(new EntityComboBoxModel<String>(this.formTypes) {
		
					private static final long serialVersionUID = 1L;
		
					@Override
		            public String getLabel(String item) {
		                return item;
		            }
		        });
		                
		        return new DefaultCellEditor(cbbFormType);
        	}
        }
        
        return null;
    }
	
	public static List<String> getOptions(Class<?> clazz, String suggestion) {

		if (suggestion == null) {
			suggestion = "";
		}
		
		String[] items = suggestion.split("\\.");
				
		return getOptions(clazz, items, 0);
	}

	public static List<String> getOptions(Class<?> clazz, String[] items, int level) {
				
		String levelField;

		if (level >= items.length) {
			levelField = "";
		}
		else {
			levelField = items[level];
		}
				
		String path = "";
		
		for (int i = 0; i < level; i++) {
			path += items[i] + ".";
		}
		
		List<Field> fields = ReflectionUtils.getFieldsRecursive(clazz);

		List<String> options = new ArrayList<String>();
						
		for (Field f : fields) {

			if (!(Modifier.isStatic(f.getModifiers())
					|| Collection.class.isAssignableFrom(f.getType())
					|| f.getAnnotation(OneToMany.class) != null)) {
								
				options.add(path + f.getName());	
						
				if (f.getName().equals(levelField)) {
					options.addAll(getOptions(f.getType(), items, level + 1));
				}
			}
		}		
		
		return options;
	}
}