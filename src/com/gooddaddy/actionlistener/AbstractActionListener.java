package com.gooddaddy.actionlistener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gooddaddy.utils.StringUtils;

public abstract class AbstractActionListener implements ActionListener {
	
	private Map<String, String> data = new HashMap<String, String>();
	
	protected JFrame frame = new JFrame("好爸爸进销存管理系统V1.0");// 框架布局
	private Container con = new Container();
	private JLabel label1 = new JLabel("姓名");
	private JLabel label2 = new JLabel("电话");
	private JLabel label3 = new JLabel("地址");
	private JLabel label4 = new JLabel("订单信息");
	private JTable table;
	private JComboBox<String> combobox;
	protected JTextField text1 = new JTextField();// SVN更新信息文件的路�?
	protected JTextField text2 = new JTextField();// 项目的路�?
	protected JTextField text3 = new JTextField();// 生成文件的路�?
	private JButton button1 = new JButton("...");// 选择
	private JButton button2 = new JButton("...");// 选择
	private JButton button3 = new JButton("...");// 选择
	private JFileChooser jfc = new JFileChooser();// 文件选择�?
	private JButton button4 = new JButton("一键生成");//

	public AbstractActionListener() {
		
		initData();
		
		jfc.setCurrentDirectory(new File("d://"));// 文件选择器的初始目录定为d盘

//		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//
//		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		frame.setLocation(new Point(10, 10));// 设定窗口出现位置
		frame.setSize(1000, 600);// 设定窗口大小
		frame.setResizable(true);
		
		label1.setBounds(10, 20, 200, 30);
		label1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		text1.setBounds(60, 23, 200, 30);
//		button1.setBounds(364, 18, 50, 21);
//		button1.setBackground(Color.WHITE);
		label2.setBounds(10, 70, 200, 30);
		label2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		text2.setBounds(60, 73, 200, 30);
//		button2.setBounds(364, 55, 50, 21);
//		button2.setBackground(Color.WHITE);
		label3.setBounds(10, 120, 200, 30);
		label3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		text3.setBounds(60, 123, 700, 30);
//		button3.setBounds(364, 93, 50, 21);
//		button3.setBackground(Color.WHITE);
		JSeparator js = new JSeparator();
		js.setBounds(10, 170, 800, 1);
		js.setForeground(Color.GRAY);
		label4.setBounds(10, 190, 200, 30);
		label4.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		String[][] row = new String[1][3];
		String[] column = { "名称", "金额", "数量" };
		DefaultTableModel dtm = new DefaultTableModel(row, column);
		table = new JTable(dtm) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1 ? false : true;
			}
		};
		table.setRowHeight(40);
		JTextField textField = new JTextField();
		TableColumnModel tcm = table.getColumnModel();
		combobox = new JComboBox<String>(data.keySet().toArray(new String[] {}));
		tcm.getColumn(0).setCellEditor(new DefaultCellEditor(combobox));
		combobox.addActionListener(this);
		tcm.getColumn(2).setCellEditor(new DefaultCellEditor(textField));
		scrollPane.setViewportView(table);
		int cur = 260;
		scrollPane.setBounds(10, 230, 800, cur);
//		new Thread() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						DefaultTableModel model = (DefaultTableModel) table.getModel();
//						model.addRow(new Object[] {"a","a","a"});
//						scrollPane.setBounds(10, 230, 800, cur + 40);
//						TimeUnit.SECONDS.sleep(3);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}.start();
		
//		JComboBox<String> combobox = new JComboBox<String>();
//		combobox.addItem("身份证");
//		combobox.addItem("驾驶证");
//		combobox.addItem("军官证");
//		combobox.setBounds(10, 270, 200, 30);
		button1.addActionListener(this); // 添加事件处理
		button2.addActionListener(this); // 添加事件处理
		button3.addActionListener(this); // 添加事件处理
		con.add(label1);
		con.add(text1);
		con.add(label2);
		con.add(text2);
		con.add(label3);
		con.add(text3);
		con.add(js);
		con.add(label4);
		con.add(scrollPane);
//		con.add(combobox);
		frame.setVisible(true);// 窗口可见
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程�?
		frame.add(con);
	}

	/**
	 * 事件监听的方�?
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(combobox)) {
			String price = data.get(combobox.getSelectedItem());
			int selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setValueAt(price, selectedRow, 1);
			}
		}
		// TODO Auto-generated method stub
		if (e.getSource().equals(button1)) {// 判断触发方法的按钮是哪个
			jfc.setFileSelectionMode(0);// 设定只能选择到文�?
			int state = jfc.showOpenDialog(null);// 此句是打�?文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc.getSelectedFile();// f为�?�择到的目录
				text1.setText(f.getAbsolutePath());
			}
		}
		// 绑定到�?�择文件，先择文件事�?
		if (e.getSource().equals(button2)) {
			jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc.showOpenDialog(null);// 此句是打�?文件选择器界面的触发语句
			if (state == 1) {
				return;// 撤销则返�?
			} else {
				File f = jfc.getSelectedFile();// f为�?�择到的文件
				text2.setText(f.getAbsolutePath());
			}
		}
		// 绑定到�?�择文件，先择文件事�?
		if (e.getSource().equals(button3)) {
			jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc.showOpenDialog(null);// 此句是打�?文件选择器界面的触发语句
			if (state == 1) {
				return;// 撤销则返�?
			} else {
				File f = jfc.getSelectedFile();// f为�?�择到的文件
				text3.setText(f.getAbsolutePath());
			}
		}
		if (e.getSource().equals(button4)) {
			if (StringUtils.isBlank(text1.getText())) {
				JOptionPane.showMessageDialog(null, "请�?�择SVN更新信息文件", "提示", 2);
			} else if (StringUtils.isBlank(text2.getText())) {
				JOptionPane.showMessageDialog(null, "请�?�择项目根目�?", "提示", 2);
			} else if (StringUtils.isBlank(text3.getText())) {
				JOptionPane.showMessageDialog(null, "请�?�文件生成目�?", "提示", 2);
			} else {
				onSubmit(text1.getText(), text2.getText(), text3.getText());
			}
		}
	}
	
	private void initData() {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File("resources/data.xml"));
			Element root = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> elements = root.elements();
			for (Element element : elements) {
				String name = element.attribute("name").getText();
				String price = element.attribute("price").getText();
				data.put(name, price);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public abstract void onSubmit(String filePath, String projectPath, String createPath);
}
