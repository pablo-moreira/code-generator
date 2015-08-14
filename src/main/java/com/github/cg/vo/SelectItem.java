/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.vo;

/**
 *
 * @author pablo-moreira
 * @param <E> o tipo do item
 */
public class SelectItem<E> {
	
	private E item;
	private boolean selected;

	public SelectItem() {
	}
	
	public SelectItem(E item, boolean selected) {
		this.item = item;
		this.selected = selected;
	}
	
	public E getItem() {
		return item;
	}

	public void setItem(E item) {
		this.item = item;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
