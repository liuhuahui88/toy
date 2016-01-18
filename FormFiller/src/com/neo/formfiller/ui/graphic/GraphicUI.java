package com.neo.formfiller.ui.graphic;

import java.awt.Image;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.neo.formfiller.ui.AbstractUI;

public class GraphicUI implements AbstractUI {
	
	private static final String TITLE = "Form Filler";

	private static final int WIDTH = 800;
	private static final int HEIGHT = 200;
	
	private JFrame frame;
	
	private WaitingDialog waitingDialog;
	
	public GraphicUI() {
		frame = new JFrame(TITLE);
		frame.setBounds(GraphicUIUtility.getCentralRectangle(WIDTH, HEIGHT));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void showMessage(String message) {
		stopWaiting();
		JOptionPane.showMessageDialog(frame, message, TITLE,
				JOptionPane.INFORMATION_MESSAGE);
		startWaiting();
	}

	@Override
	public SystemLoginInfo getSystemLoginInfo(Image codeImage) {
		stopWaiting();
		SystemLoginPanel systemLoginPanel = new SystemLoginPanel(codeImage);
		setPanelAndSetVisible(systemLoginPanel);
		SystemLoginInfo info = systemLoginPanel.getSystemLoginInfo();
		startWaiting();
		return info;
	}

	@Override
	public ClassLoginInfo getClassLoginInfo(List<String> classnameList) {
		stopWaiting();
		ClassLoginPanel classLoginPanel = new ClassLoginPanel(classnameList);
		setPanelAndSetVisible(classLoginPanel);
		ClassLoginInfo info = classLoginPanel.getClassLoginInfo();
		startWaiting();
		return info;
	}

	@Override
	public File getRecordFile(String classname) {
		stopWaiting();
		RecordFilePanel recordFilePanel = new RecordFilePanel(classname);
		setPanelAndSetVisible(recordFilePanel);
		File recordFile = recordFilePanel.getRecordFile();
		startWaiting();
		return recordFile;
	}
	
	private void startWaiting() {
		if (waitingDialog == null) {
			waitingDialog = new WaitingDialog(frame);
			waitingDialog.startWaiting();
		}
	}
	
	private void stopWaiting() {
		if (waitingDialog != null) {
			waitingDialog.stopWaiting();
			waitingDialog = null;
		}
	}
	
	private void setPanelAndSetVisible(JPanel panel) {
		frame.setContentPane(panel);
		frame.setVisible(true);
	}
}
