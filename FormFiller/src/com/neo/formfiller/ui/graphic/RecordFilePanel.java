package com.neo.formfiller.ui.graphic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class RecordFilePanel extends JPanel {
	
	private static final String CLASSNAME_TEXT = "Classname:";
	private static final String PATH_TEXT = "Path:";
	
	private static final String BROWSE_TEXT = "Browse";
	
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
	
	private static final int FIELD_X = X2;
	private static final int FIELD_WIDTH = BIG_WIDTH;
	private static final int FIELD_HEIGHT = HEIGHT;
	
	private static final int BUTTON_WIDTH = 90;
	private static final int BUTTON_HEIGHT = HEIGHT;

	private static final int BROWSE_BUTTON_X = 250;
	private static final int BROWSE_BUTTON_Y = 150;
	
	private static final int OK_BUTTON_X = 355;
	private static final int OK_BUTTON_Y = 150;
	
	private static final int CANCEL_BUTTON_X = 460;
	private static final int CANCEL_BUTTON_Y = 150;
	
	private static final String EXCEL_SUFFIX = ".xls";
	private static final String EXCEL_DESCRIPTION = "Microsoft Excel(.xls)";
	
	private JLabel classnameLabel;
	private JLabel pathLabel;
	
	private JTextField classnameField;
	private JTextField pathField;
	
	private JFileChooser fileChooser;
	private JButton browseButton;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private File recordFile;

	public RecordFilePanel(String classname) {
		this.setLayout(null);
		
		classnameLabel = new JLabel(CLASSNAME_TEXT);
		setBoundsAndAdd(classnameLabel, LABEL_X, Y1,
				LABEL_WIDTH, LABEL_HEIGHT);
		
		pathLabel = new JLabel(PATH_TEXT);
		setBoundsAndAdd(pathLabel, LABEL_X, Y2,
				LABEL_WIDTH, LABEL_HEIGHT);
		
		classnameField = new JTextField(classname);
		classnameField.setEditable(false);
		setBoundsAndAdd(classnameField, FIELD_X, Y1,
				FIELD_WIDTH, FIELD_HEIGHT);
		
		pathField = new JTextField();
		setBoundsAndAdd(pathField, FIELD_X, Y2,
				FIELD_WIDTH, FIELD_HEIGHT);
		
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				} else {
					String name = file.getName();
					String suffix = name.substring(name.lastIndexOf("."));
					return suffix.toLowerCase().equals(EXCEL_SUFFIX);
				}
			}

			@Override
			public String getDescription() {
				return EXCEL_DESCRIPTION;
			}
		});
		
		browseButton = new JButton(BROWSE_TEXT);
		setBoundsAndAdd(browseButton, BROWSE_BUTTON_X, BROWSE_BUTTON_Y,
				BUTTON_WIDTH, BUTTON_HEIGHT);
		browseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(
						RecordFilePanel.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                pathField.setText(file.getAbsolutePath());
	            }
			}
		});
		
		okButton = new JButton(OK_TEXT);
		setBoundsAndAdd(okButton, OK_BUTTON_X, OK_BUTTON_Y,
				BUTTON_WIDTH, BUTTON_HEIGHT);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (RecordFilePanel.this) {
					recordFile = new File(pathField.getText());
					RecordFilePanel.this.notifyAll();
				}
			}
		});
		
		cancelButton = new JButton(CANCEL_TEXT);
		setBoundsAndAdd(cancelButton, CANCEL_BUTTON_X, CANCEL_BUTTON_Y,
				BUTTON_WIDTH, BUTTON_HEIGHT);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (RecordFilePanel.this) {
					recordFile = null;
					RecordFilePanel.this.notifyAll();
				}
			}
		});
	}

	public File getRecordFile() {
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		return recordFile;
	}

	private void setBoundsAndAdd(JComponent component, int x, int y,
			int width, int height) {
		component.setBounds(x, y, width, height);
		add(component);
	}
}
