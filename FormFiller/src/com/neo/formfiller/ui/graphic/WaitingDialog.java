package com.neo.formfiller.ui.graphic;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class WaitingDialog {
	
	private static final String TITLE = "Please wait!";
	
	private static final int WIDTH = 200;
	private static final int HEIGHT = 20;
	
	private JDialog dialog;
	
	private JProgressBar progressBar;

	public WaitingDialog(JFrame owner) {
		dialog = new JDialog(owner, TITLE, true);
		dialog.setBounds(GraphicUIUtility.getCentralRectangle(WIDTH, HEIGHT));
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		dialog.add(progressBar);
	}
	
	public void startWaiting() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dialog.setVisible(true);
			}
		});
	}
	
	public void stopWaiting() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dialog.dispose();
			}
		});
	}
}
