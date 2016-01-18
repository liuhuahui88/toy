package com.neo.formfiller.ui.graphic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.neo.formfiller.ui.AbstractUI.ClassLoginInfo;

@SuppressWarnings("serial")
public class ClassLoginPanel extends JPanel {

	private static final String CLASSNAME_TEXT = "Classname:";
	private static final String PASSWORD_TEXT = "Password:";
	
	private static final String OK_TEXT = "OK";
	private static final String CANCEL_TEXT = "Cancel";

	private static final int X1 = 50;
	private static final int X2 = 150;

	private static final int HEIGHT = 20;
	private static final int SEGMENT_HEIGHT = HEIGHT * 2;
	private static final int Y1 = 30;
	private static final int Y2 = Y1 + SEGMENT_HEIGHT;
	
	private static final int SMALL_WIDTH = 100;
	private static final int BIG_WIDTH = 600;

	private static final int LABEL_X = X1;
	private static final int LABEL_WIDTH = SMALL_WIDTH;
	private static final int LABEL_HEIGHT = HEIGHT;

	private static final int INPUT_X = X2;
	private static final int INPUT_WIDTH = BIG_WIDTH;
	private static final int INPUT_HEIGHT = HEIGHT;
	
	private static final int BUTTON_WIDTH = 90;
	private static final int BUTTON_HEIGHT = HEIGHT;
	
	private static final int OK_BUTTON_X = 290;
	private static final int OK_BUTTON_Y = 150;
	
	private static final int CANCEL_BUTTON_X = 415;
	private static final int CANCEL_BUTTON_Y = 150;
	
	private JLabel classnameLabel;
	private JLabel passwordLabel;
	
	private JComboBox<String> comboBox;
	private JTextField passwordField;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private ClassLoginInfo info;
	
	public ClassLoginPanel(List<String> classnameList) {
		this.setLayout(null);

		classnameLabel = new JLabel(CLASSNAME_TEXT);
		setBoundsAndAdd(classnameLabel, LABEL_X, Y1,
				LABEL_WIDTH, LABEL_HEIGHT);

		passwordLabel = new JLabel(PASSWORD_TEXT);
		setBoundsAndAdd(passwordLabel, LABEL_X, Y2,
				LABEL_WIDTH, LABEL_HEIGHT);

		comboBox = new JComboBox<String>(classnameList.toArray(new String[0]));
		setBoundsAndAdd(comboBox, INPUT_X, Y1,
				INPUT_WIDTH, INPUT_HEIGHT);

		passwordField = new JTextField();
		setBoundsAndAdd(passwordField, INPUT_X, Y2,
				INPUT_WIDTH, INPUT_HEIGHT);
		
		okButton = new JButton(OK_TEXT);
		setBoundsAndAdd(okButton, OK_BUTTON_X, OK_BUTTON_Y,
				BUTTON_WIDTH, BUTTON_HEIGHT);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (ClassLoginPanel.this) {
					info = new ClassLoginInfo();
					int index = comboBox.getSelectedIndex();
					info.classname = comboBox.getItemAt(index);
					info.password = passwordField.getText();
					ClassLoginPanel.this.notifyAll();
				}
			}
		});
		
		cancelButton = new JButton(CANCEL_TEXT);
		setBoundsAndAdd(cancelButton, CANCEL_BUTTON_X, CANCEL_BUTTON_Y,
				BUTTON_WIDTH, BUTTON_HEIGHT);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (ClassLoginPanel.this) {
					info = null;
					ClassLoginPanel.this.notifyAll();
				}
			}
		});
	}
	
	public ClassLoginInfo getClassLoginInfo() {
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		return info;
	}
	
	private void setBoundsAndAdd(JComponent component, int x, int y,
			int width, int height) {
		component.setBounds(x, y, width, height);
		add(component);
	}
}
