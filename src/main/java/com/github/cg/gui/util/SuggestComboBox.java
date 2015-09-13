package com.github.cg.gui.util;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public abstract class SuggestComboBox extends KeyAdapter {
	
	private final JComboBox<String> comboBox;
	private boolean shouldHide;
	private JTextField comboBoxEditor;

	public SuggestComboBox(JComboBox<String> comboBox) {
	    this.comboBox = comboBox;
	    this.comboBox.setEditable(true);	    
	    this.comboBoxEditor = (JTextField) comboBox.getEditor().getEditorComponent();
	    this.comboBoxEditor.addKeyListener(this);
	}
	
	@Override
	public void keyTyped(final KeyEvent e) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				String text = comboBoxEditor.getText();
									
				ComboBoxModel<String> cbModel;
				
				List<String> items = getItems(text);
				
				if (text.isEmpty()) {
					
					String[] array = items.toArray(new String[items.size()]);
					
					cbModel = new DefaultComboBoxModel<String>(array);					
					setSuggestionModel(cbModel, "");
					comboBox.hidePopup();
				}
				else {
					cbModel = createSuggestedModel(items, text);
					if (cbModel.getSize() == 0 || shouldHide) {
						comboBox.hidePopup();
					} else {
						setSuggestionModel(cbModel, text);
						comboBox.showPopup();
					}
				}
			}
		});
	}

	abstract public List<String> getItems(String suggest);

	@Override
	public void keyPressed(KeyEvent e) {
		
		String text = this.comboBoxEditor.getText();
		
		shouldHide = false;
		
		List<String> list = getItems(text);
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				for (String s : list) {
					if (s.startsWith(text)) {
						this.comboBoxEditor.setText(s);
						return;
					}
				}
				break;
			case KeyEvent.VK_ENTER:
				shouldHide = true;
				break;
			case KeyEvent.VK_ESCAPE:
				shouldHide = true;
				break;
			default:
				break;
		}
	}
	
	public void load() {
		setSuggestionModel(createSuggestedModel(getItems(""), ""), "");
	}
	
	private void setSuggestionModel(ComboBoxModel<String> mdl, String str) {		
		this.comboBox.setModel(mdl);
		this.comboBox.setSelectedIndex(-1);
		this.comboBoxEditor.setText(str);
	}

	private ComboBoxModel<String> createSuggestedModel(List<String> items, String text) {
		
		DefaultComboBoxModel<String> cbModel = new DefaultComboBoxModel<String>();
		
		for (String item : items) {
			if (item.startsWith(text)) {
				cbModel.addElement(item);
			}
		}
		
	    return cbModel;
	}

	public String getSelectedItem() {
		return (String) comboBox.getSelectedItem();
	}
}