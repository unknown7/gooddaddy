package com.gooddaddy.actionlistener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private JButton addButton = new JButton();
	private JButton deleteButton = new JButton();// 选择

	public AbstractActionListener() {
		
		initData();
		
//		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//
//		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		frame.setLocation(new Point(10, 10));// 设定窗口出现位置
		frame.setSize(1000, 600);// 设定窗口大小
		frame.setResizable(false);
		
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
		addButton.setText("增加一条");
		addButton.setBounds(100, 194, 100, 25);
		addButton.setBackground(Color.WHITE);
		addButton.addActionListener(this);
		deleteButton.setText("删除一条");
		deleteButton.setBounds(210, 194, 100, 25);
		deleteButton.setBackground(Color.WHITE);
		deleteButton.addActionListener(this);
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
		table.setBackground(Color.WHITE);
		JTextField textField = new JTextField();
		TableColumnModel tcm = table.getColumnModel();
		combobox = new JComboBox<String>(data.keySet().toArray(new String[] {}));
		tcm.getColumn(0).setCellEditor(new DefaultCellEditor(combobox));
		combobox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String price = data.get(combobox.getSelectedItem());
					int selectedRow = table.getSelectedRow();
					if (selectedRow != -1) {
						DefaultTableModel model = (DefaultTableModel) table.getModel();
						model.setValueAt(price, selectedRow, 1);
					}
				}
			}
		});
		tcm.getColumn(2).setCellEditor(new DefaultCellEditor(textField));
		scrollPane.setViewportView(table);
		scrollPane.setBounds(10, 230, 800, 260);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(new Object[] {});
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.removeRow(selectedRow);
			}
		});
		con.add(label1);
		con.add(text1);
		con.add(label2);
		con.add(text2);
		con.add(label3);
		con.add(text3);
		con.add(js);
		con.add(label4);
		con.add(addButton);
		con.add(deleteButton);
		con.add(scrollPane);
		frame.setVisible(true);// 窗口可见
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程�?
		frame.add(con);
	}

	/**
	 * 事件监听的方法
	 */
	public void actionPerformed(ActionEvent e) {
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
