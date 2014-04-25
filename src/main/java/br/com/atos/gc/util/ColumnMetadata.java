/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.util;

/**
 *
 * @author 205327
 */
public class ColumnMetadata {

    private Integer index;
    private String label;
    private Integer width;
            
    public ColumnMetadata(Integer index, String label, Integer width) {
        this.label = label;
        this.width = width;
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    public Integer getWidth() {
        return width;
    }
}
