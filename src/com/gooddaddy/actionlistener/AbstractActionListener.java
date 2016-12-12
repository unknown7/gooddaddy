package com.gooddaddy.actionlistener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalBorders.ButtonBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gooddaddy.model.Order;
import com.gooddaddy.model.OrderItem;
import com.gooddaddy.utils.StringUtils;

public abstract class AbstractActionListener implements ActionListener {
	
	private Map<String, String> data = new HashMap<String, String>();
	
	protected JFrame frame = new JFrame("好爸爸进销存管理系统V1.0");// 框架布局
	private Container con = new Container();
	private JLabel label1 = new JLabel("姓名");
	private JLabel label2 = new JLabel("电话");
	private JLabel label3 = new JLabel("地址");
	private JLabel label4 = new JLabel("订单信息");
	private JCheckBox checkbox5 = new JCheckBox();
	private JLabel label5 = new JLabel("黑椒酱");
	private JCheckBox checkbox6 = new JCheckBox();
	private JLabel label6 = new JLabel("蘑菇酱");
	private JCheckBox checkbox7 = new JCheckBox();
	private JLabel label7 = new JLabel("番茄酱");
	private JTable table;
	private JComboBox<String> combobox;
	protected JTextField text1 = new JTextField();
	protected JTextField text2 = new JTextField();
	protected JTextField text3 = new JTextField();
	private JButton addButton = new JButton("增加一条");
	private JButton deleteButton = new JButton("删除一条");
	private JButton print = new JButton("打印订单");

	public AbstractActionListener() {
		
		initData();
		
//		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//
//		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		frame.setLocation(new Point(10, 10));// 设定窗口出现位置
		frame.setSize(900, 660);// 设定窗口大小
		frame.setResizable(false);
		
		label1.setBounds(20, 20, 200, 30);
		label1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		text1.setBounds(70, 23, 200, 30);
		label2.setBounds(20, 70, 200, 30);
		label2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		text2.setBounds(70, 73, 200, 30);
		label3.setBounds(20, 120, 200, 30);
		label3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		text3.setBounds(70, 123, 700, 30);
		JSeparator js1 = new JSeparator();
		js1.setBounds(10, 170, 860, 1);
		js1.setForeground(Color.GRAY);
		label4.setBounds(20, 190, 200, 30);
		label4.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		addButton.setBounds(110, 194, 100, 25);
		addButton.setBackground(Color.WHITE);
		addButton.addActionListener(this);
		deleteButton.setBounds(220, 194, 100, 25);
		deleteButton.setBackground(Color.WHITE);
		deleteButton.addActionListener(this);
		checkbox5.setBounds(20, 500, 20, 30);
		label5.setBounds(40, 500, 200, 30);
		label5.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		checkbox6.setBounds(130, 500, 20, 30);
		label6.setBounds(150, 500, 200, 30);
		label6.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		checkbox7.setBounds(240, 500, 20, 30);
		label7.setBounds(260, 500, 200, 30);
		label7.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
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
		table.setSelectionBackground(Color.WHITE);
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
						table.clearSelection();
					}
				}
			}
		});
		tcm.getColumn(2).setCellEditor(new DefaultCellEditor(textField));
		scrollPane.setViewportView(table);
		scrollPane.setBounds(20, 230, 800, 260);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, table.getRowCount(), "提示", 2);
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(new Object[] {});
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, table.getRowCount(), "提示", 2);
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.removeRow(selectedRow);
				}
			}
		});
		JSeparator js2 = new JSeparator();
		js2.setBounds(10, 540, 860, 1);
		js2.setForeground(Color.GRAY);
		print.setBounds(740, 560, 100, 60);
		print.setBackground(Color.WHITE);
		print.setBorder(new ButtonBorder());
		print.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		print.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtils.isBlank(text1.getText())) {
					JOptionPane.showMessageDialog(null, "请输入客户姓名", "提示", 2);
				} else if (StringUtils.isBlank(text2.getText())) {
					JOptionPane.showMessageDialog(null, "请输入客户电话", "提示", 2);
				} else if (StringUtils.isBlank(text3.getText())) {
					JOptionPane.showMessageDialog(null, "请输入配送地址", "提示", 2);
				} else if (table.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "订单为空", "提示", 2);
				} else {
					TableModel model = table.getModel();
					int rowCount = table.getRowCount();
					Order order = new Order();
					order.setName(text1.getText());
					order.setPhone(text2.getText());
					order.setAddress(text3.getText());
					order.setBlackPepper(checkbox5.isSelected());
					order.setMushroom(checkbox6.isSelected());
					order.setTomato(checkbox7.isSelected());
					List<OrderItem> item = new ArrayList<OrderItem>();
					order.setItem(item);
					for (int i = 0; i < rowCount; i++) {
						String itemName = String.valueOf(model.getValueAt(i, 0));
						Double price = Double.valueOf(String.valueOf(model.getValueAt(i, 1)));
						Integer number = Integer.valueOf(String.valueOf(model.getValueAt(i, 2)));
						OrderItem oi = new OrderItem();
						oi.setName(itemName);
						oi.setPrice(price);
						oi.setNumber(number);
						item.add(oi);
					}
					onSubmit(order);
				}
			}
		});
		
		JLabel jlpic = new JLabel();
		ImageIcon icon = new ImageIcon("resources/gooddaddy.jpg");
		icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth(),
			    icon.getIconHeight(), Image.SCALE_DEFAULT));
		jlpic.setBounds(10, 535, 100, 100);
		jlpic.setHorizontalAlignment(0);
		jlpic.setIcon(icon);

		con.add(label1);
		con.add(text1);
		con.add(label2);
		con.add(text2);
		con.add(label3);
		con.add(text3);
		con.add(js1);
		con.add(js2);
		con.add(label4);
		con.add(addButton);
		con.add(deleteButton);
		con.add(checkbox5);
		con.add(label5);
		con.add(checkbox6);
		con.add(label6);
		con.add(checkbox7);
		con.add(label7);
		con.add(scrollPane);
		con.add(print);
		
		con.add(jlpic);
		frame.setVisible(true);// 窗口可见
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程�?
		frame.add(con);
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
				DecimalFormat df = new DecimalFormat("0.00");   
				price = df.format(Double.parseDouble(price));   
				data.put(name, price);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 事件监听的方法
	 */
	public void actionPerformed(ActionEvent e) {
	}

	public abstract void onSubmit(Order order);
}
