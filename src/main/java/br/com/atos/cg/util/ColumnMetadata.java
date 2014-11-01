/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.cg.util;

/**
 *
 * @author 205327
 */
public class ColumnMetadata {

    public int index;
    private String label;
    private Integer width;
    private String tooltip;
    
    public ColumnMetadata(int index, String label, Integer width) {
        this.label = label;
        this.width = width;
        this.index = index;
        
    }
    
    public ColumnMetadata(int index, String label, Integer width, String tooltip) {
        this(index, label, width);
        this.tooltip = tooltip;
    }

    public int getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    public Integer getWidth() {
        return width;
    }
    
    public String getTooltip() {
        return tooltip;
    }
}
