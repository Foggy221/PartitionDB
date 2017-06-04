package com.de.PDB;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class PartitionGUI {

	private JFrame frame;
	private String srcpath = null,despath = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PartitionGUI window = new PartitionGUI();
					SwingUtilities.updateComponentTreeUI(window.frame);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PartitionGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("调整数量");
		frame.setSize(400, 270);
		
		frame.getContentPane().add(setMainPanel());
		
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	}
	
	private JPanel setMainPanel(){
		File defaule = new File(System.getProperty("user.dir"));
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 400, 270);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel sizelable = new JLabel("数量：");
		sizelable.setBounds(15, 20, 40, 30);
		panel.add(sizelable);
		
		JTextField destext;
		destext = new JTextField();
		destext.setBackground(Color.WHITE);
		destext.setEditable(false);
		destext.setBounds(10, 180, 360, 25);
		panel.add(destext);
		destext.setColumns(10);
		
		JTextField srctext;
		srctext = new JTextField();
		srctext.setBackground(Color.WHITE);
		srctext.setEditable(false);
		srctext.setColumns(10);
		srctext.setBounds(10, 100, 360, 25);
		panel.add(srctext);
		
		JTextField sizetext;
		sizetext = new JTextField();
		sizetext.setBounds(50, 23, 50, 25);
		panel.add(sizetext);
		sizetext.setColumns(10);
		
		JButton srcbutton = new JButton("选择所在文件路径");
		srcbutton.setBounds(10, 60, 200, 30);
		srcbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				if(new File(destext.getText().trim()).exists()){
					fc.setCurrentDirectory(new File(destext.getText().trim()).getParentFile());
				}else{
					fc.setCurrentDirectory(defaule);
				}
				fc.setDialogTitle("选择所在文件路径");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fc.showOpenDialog(frame);
				if(result == JFileChooser.APPROVE_OPTION){
					File select = fc.getSelectedFile();
					srctext.setText(select.getAbsolutePath());
				}
			}
		});
		panel.add(srcbutton);
		
		JButton desbutton = new JButton("选择文件保存路径");
		desbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				if(new File(srctext.getText().trim()).exists()){
					fc.setCurrentDirectory(new File(srctext.getText().trim()).getParentFile());
				}else{
					fc.setCurrentDirectory(defaule);
				}
				fc.setDialogTitle("选择文件保存路径");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fc.showOpenDialog(frame);
				if(result == JFileChooser.APPROVE_OPTION){
					File select = fc.getSelectedFile();
					destext.setText(select.getAbsolutePath());
				}
			}
		});
		desbutton.setBounds(10, 140, 200, 30);
		panel.add(desbutton);
		
		JButton startbutton = new JButton("分割");
		startbutton.setFont(new Font("宋体", Font.PLAIN, 12));
		startbutton.setBounds(300, 20, 70, 40);
		startbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String srcpath,despath;
			try{
				int size = Integer.parseInt(sizetext.getText().trim());
				if(PartitionGUI.this.despath == null){
					srcpath = srctext.getText().trim().replaceAll("^\\$", "^/$");
				}else{
					srcpath = PartitionGUI.this.despath;
				}
				if(PartitionGUI.this.srcpath == null){
					despath = destext.getText().trim().replaceAll("^\\$", "^/$");
				}else{
					despath = PartitionGUI.this.despath+"/data";
				}
				int len = new File(srcpath+"/img").listFiles(new MyFilter(".bin")).length;
				if((len%size) == 0){
					DBOperation dbo = null;
					PartitionFile pf = new PartitionFile(size,srcpath,despath);
					pf.partitionData();
					File[] list = new File(despath).listFiles();
					for(File f:list){
						dbo = new DBOperation(f.getAbsolutePath()+"/thyroid.db",f.getAbsolutePath()+"/img");
						dbo.deleteRemain("noduleInfo");
						dbo.deleteRemain("noduleNum");
					}
				}else{
					JOptionPane.showMessageDialog(frame, "文件总数为： "+len+"\r\n请输入适于均分的值","ERROR",JOptionPane.ERROR_MESSAGE);
				}
			}catch(Exception e1){
				JOptionPane.showMessageDialog(frame, e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
			}
			}
		});
		panel.add(startbutton);
		
		JButton mergebutton = new JButton("合并");
		mergebutton.setFont(new Font("宋体", Font.PLAIN, 12));
		mergebutton.setBounds(220, 20, 70, 40);
		mergebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
			try{
				srcpath = srctext.getText().trim().replaceAll("^\\$", "^/$");;
				despath = destext.getText().trim().replaceAll("^\\$", "^/$");
				DBOperation dbo = null;
				PartitionFile pf = new PartitionFile(srcpath,despath);
				pf.mergeData();
				dbo = new DBOperation(despath+"/thyroid.db",despath+"/img");
				dbo.mergeTable("noduleInfo");
				dbo.mergeTable("noduleNum");
			}catch(Exception e1){
				JOptionPane.showMessageDialog(frame, e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
			}
			}
		});
		panel.add(mergebutton);
		
		return panel;
	}
}
