package com.gooddaddy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.gooddaddy.actionlistener.AbstractActionListener;
import com.gooddaddy.model.Order;
import com.gooddaddy.model.OrderItem;
import com.gooddaddy.printer.JacobHelper;

public class Controller extends AbstractActionListener {
	
	private static Properties prop;
	
	{
		prop = new Properties();
		try {
			prop.load(new FileInputStream("resources/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onSubmit(Order order) {
		try {
			JacobHelper helper = new JacobHelper(true);
			helper.openDocument(new File("resources/steak.docx").getAbsolutePath());
			helper.replaceText("name", order.getName());
			helper.moveStart();
			helper.replaceText("phone", order.getPhone());
			helper.moveStart();
			helper.replaceText("address", order.getAddress());
			helper.moveStart();
			helper.replaceText("blackPepper", order.getBlackPepper() ? " √ " : "   ");
			helper.moveStart();
			helper.replaceText("mushroom", order.getMushroom() ? " √ " : "   ");
			helper.moveStart();
			helper.replaceText("tomato", order.getTomato() ? " √ " : "   ");
			helper.moveStart();
			helper.replaceText("total", String.valueOf(order.getTotal()));
			helper.moveStart();
			helper.replaceText("date", new SimpleDateFormat("yyyy-MM-dd").format(order.getDate()));
			helper.moveStart();
			for (int i = 0; i < order.getItem().size(); i++) {
				OrderItem item = order.getItem().get(i);
				helper.putTxtToCell(1, i + 2, 1, item.getName());
				helper.putTxtToCell(1, i + 2, 2, String.valueOf(item.getNumber()));
				helper.putTxtToCell(1, i + 2, 3, String.valueOf(item.getPrice()));
				helper.addRow(1);
			}
			helper.printFile();
			StringBuilder builder = new StringBuilder();
			builder.append(prop.getProperty("GENERATED_FOLDER_PATH"));
			builder.append(System.currentTimeMillis());
			builder.append(".docx");
			helper.save(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
