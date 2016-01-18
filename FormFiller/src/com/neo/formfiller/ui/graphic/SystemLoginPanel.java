package com.neo.formfiller.ui.graphic;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.neo.formfiller.ui.AbstractUI.SystemLoginInfo;

@SuppressWarnings("serial")
public class SystemLoginPanel extends JPanel {
	
	private static final String USERNAME_TEXT = "Username:";
	private static final String PASSWORD_TEXT = "Password:";
	private static final String CODE_TEXT = "Code:";
	
	private static final String OK_TEXT = "OK";
	private static final String CANCEL_TEXT = "Cancel";

	private static final int X1 = 50;
	private static final int X2 = 150;

	private static final int HEIGHT = 20;
	private static final int SEGMENT_HEIGHT = HEIGHT * 2;
	private static final int Y1 = 30;
	private static final int Y2 = Y1 + SEGMENT_HEIGHT;
	private static final int Y3 = Y2 + SEGMENT_HEIGHT;
	
	private static final int SMALL_WIDTH = 100;
	private static final int BIG_WIDTH = 600;
	
	private static final int LABEL_X = X1;
	private static final int LABEL_WIDTH = SMALL_WIDTH;
	private static final int LABEL_HEIGHT = HEIGHT;
	
	private static final int FIELD_X = X2;
	private static final int FIELD_WIDTH = BIG_WIDTH;
	private static final int FIELD_HEIGHT = HEIGHT;
	
	private static final int CODE_FIELD_X = X2;
	private static final int CODE_FIELD_WIDTH = BIG_WIDTH - SMALL_WIDTH;
	private static final int CODE_FIELD_HEIGHT = HEIGHT;
	
	private static final int IMAGE_LABEL_X = CODE_FIELD_X + CODE_FIELD_WIDTH;
	private static final int IMAGE_LABEL_WIDTH = SMALL_WIDTH;
	private static final int IMAGE_LABEL_HEIGHT = HEIGHT;
	
	private static final int BUTTON_WIDTH = 90;
	private static final int BUTTON_HEIGHT = HEIGHT;
	
	private static final int OK_BUTTON_X = 290;
	private static final int OK_BUTTON_Y = 150;
	
	private static final int CANCEL_BUTTON_X = 415;
	private static final int CANCEL_BUTTON_Y = 150;
	
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JLabel codeLabel;
	
	private JTextField usernameField;
	private JTextField passwordField;
	private JTextField codeField;

	private JLabel codeImageLabel;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private SystemLoginInfo info;

	public SystemLoginPanel(Image image) {
		this.setLayout(null);

		usernameLabel = new JLabel(USERNAME_TEXT);
		setBoundsAndAdd(usernameLabel, LABEL_X, Y1,
				LABEL_WIDTH, LABEL_HEIGHT);

		passwordLabel = new JLabel(PASSWORD_TEXT);
		setBoundsAndAdd(passwordLabel, LABEL_X, Y2,
				LABEL_WIDTH, LABEL_HEIGHT);

		codeLabel = new JLabel(CODE_TEXT);
		setBoundsAndAdd(codeLabel, LABEL_X, Y3,
				LABEL_WIDTH, LABEL_HEIGHT);

		usernameField = new JTextField();
		setBoundsAndAdd(usernameField, FIELD_X, Y1,
				FIELD_WIDTH, FIELD_HEIGHT);

		passwordField = new JTextField();
		setBoundsAndAdd(passwordField, FIELD_X, Y2,
				FIELD_WIDTH, FIELD_HEIGHT);

		codeField = new JTextField();
		setBoundsAndAdd(codeField, CODE_FIELD_X, Y3,
				CODE_FIELD_WIDTH, CODE_FIELD_HEIGHT);

		codeImageLabel = new JLabel(new ImageIcon(image));
		setBoundsAndAdd(codeImageLabel, IMAGE_LABEL_X, Y3,
				IMAGE_LABEL_WIDTH, IMAGE_LABEL_HEIGHT);
		
		okButton = new JButton(OK_TEXT);
		setBoundsAndAdd(okButton, OK_BUTTON_X, OK_BUTTON_Y,
				BUTTON_WIDTH, BUTTON_HEIGHT);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (SystemLoginPanel.this) {
					info = new SystemLoginInfo();
					info.username = usernameField.getText();
					info.password = passwordField.getText();
					info.code = codeField.getText();
					SystemLoginPanel.this.notifyAll();
				}
			}
		});
		
		cancelButton = new JButton(CANCEL_TEXT);
		setBoundsAndAdd(cancelButton, CANCEL_BUTTON_X, CANCEL_BUTTON_Y,
				BUTTON_WIDTH, BUTTON_HEIGHT);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (SystemLoginPanel.this) {
					info = null;
					SystemLoginPanel.this.notifyAll();
				}
			}
		});
	}
	
	public SystemLoginInfo getSystemLoginInfo() {
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
