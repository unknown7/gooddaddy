package com.gooddaddy.printer;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobHelper {
	// word文档
	private Dispatch doc;

	// word运行程序对象
	private ActiveXComponent word;

	// 所有word文档集合
	private Dispatch documents;

	// 选定的范围或插入点
	private Dispatch selection;

	private boolean saveOnExit = true;

	public JacobHelper(boolean visible) throws Exception {
		ComThread.InitSTA();// 线程启动
		if (word == null) {
			word = new ActiveXComponent("Word.Application");
			word.setProperty("Visible", new Variant(visible)); // 不可见打开word
			word.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
		}
		if (documents == null)
			documents = word.getProperty("Documents").toDispatch();
	}

	/**
	 * 设置退出时参数
	 * 
	 * @param saveOnExit
	 *            boolean true-退出时保存文件，false-退出时不保存文件
	 */
	public void setSaveOnExit(boolean saveOnExit) {
		this.saveOnExit = saveOnExit;
	}

	/**
	 * 创建一个新的word文档
	 * 
	 */
	public void createNewDocument() {
		doc = Dispatch.call(documents, "Add").toDispatch();
		selection = Dispatch.get(word, "Selection").toDispatch();
	}

	/**
	 * 打开一个已存在的文档
	 * 
	 * @param docPath
	 */
	public void openDocument(String docPath) {
		// closeDocument();
		doc = Dispatch.call(documents, "Open", docPath).toDispatch();
		selection = Dispatch.get(word, "Selection").toDispatch();
	}

	/**
	 * 只读方式打开一个加密的文档
	 * 
	 * @param docPath
	 *            -文件全名
	 * @param pwd
	 *            -密码
	 */
	public void openDocumentOnlyRead(String docPath, String pwd)
			throws Exception {
		// closeDocument();
		doc = Dispatch.callN(
				documents,
				"Open",
				new Object[] { docPath, new Variant(false), new Variant(true),
						new Variant(true), pwd, "", new Variant(false) })
				.toDispatch();
		selection = Dispatch.get(word, "Selection").toDispatch();
	}

	/**
	 * 打开一个加密的文档
	 * 
	 * @param docPath
	 * @param pwd
	 * @throws Exception
	 */
	public void openDocument(String docPath, String pwd) throws Exception {
		// closeDocument();
		doc = Dispatch.callN(
				documents,
				"Open",
				new Object[] { docPath, new Variant(false), new Variant(false),
						new Variant(true), pwd }).toDispatch();
		selection = Dispatch.get(word, "Selection").toDispatch();
	}

	/**
	 * 从选定内容或插入点开始查找文本
	 * 
	 * @param toFindText
	 *            要查找的文本
	 * @return boolean true-查找到并选中该文本，false-未查找到文本
	 */
	@SuppressWarnings("static-access")
	public boolean find(String toFindText) {
		if (toFindText == null || toFindText.equals(""))
			return false;
		// 从selection所在位置开始查询
		Dispatch find = word.call(selection, "Find").toDispatch();
		// 设置要查找的内容
		Dispatch.put(find, "Text", toFindText);
		// 向前查找
		Dispatch.put(find, "Forward", "True");
		// 设置格式
		Dispatch.put(find, "Format", "True");
		// 大小写匹配
		Dispatch.put(find, "MatchCase", "True");
		// 全字匹配
		Dispatch.put(find, "MatchWholeWord", "false");
		// 查找并选中
		return Dispatch.call(find, "Execute").getBoolean();
	}

	/**
	 * 把选定选定内容设定为替换文本
	 * 
	 * @param toFindText
	 *            查找字符串
	 * @param newText
	 *            要替换的内容
	 * @return
	 */
	public boolean replaceText(String toFindText, String newText) {
		if (!find(toFindText))
			return false;
		Dispatch.put(selection, "Text", newText);
		return true;
	}

	/**
	 * 全局替换文本
	 * 
	 * @param toFindText
	 *            查找字符串
	 * @param newText
	 *            要替换的内容
	 */
	public void replaceAllText(String toFindText, String newText) {
		while (find(toFindText)) {
			Dispatch.put(selection, "Text", newText);
			Dispatch.call(selection, "MoveRight");
		}
	}

	/**
	 * 在当前插入点插入字符串
	 * 
	 * @param newText
	 *            要插入的新字符串
	 */
	public void insertText(String newText) {
		Dispatch.put(selection, "Text", newText);
	}

	/**
	 * 设置当前选定内容的字体
	 * 
	 * @param boldSize
	 * @param italicSize
	 * @param underLineSize
	 *            下划线
	 * @param colorSize
	 *            字体颜色
	 * @param size
	 *            字体大小
	 * @param name
	 *            字体名称
	 * @param hidden
	 *            是否隐藏
	 */
	public void setFont(boolean bold, boolean italic, boolean underLine,
			String colorSize, String size, String name, boolean hidden) {
		Dispatch font = Dispatch.get(selection, "Font").toDispatch();
		Dispatch.put(font, "Name", new Variant(name));
		Dispatch.put(font, "Bold", new Variant(bold));
		Dispatch.put(font, "Italic", new Variant(italic));
		Dispatch.put(font, "Underline", new Variant(underLine));
		Dispatch.put(font, "Color", colorSize);
		Dispatch.put(font, "Size", size);
		Dispatch.put(font, "Hidden", hidden);
	}

	/**
	 * 文件保存或另存为
	 * 
	 * @param savePath
	 *            保存或另存为路径
	 */
	public void save(String savePath) {
		Dispatch.call(Dispatch.call(word, "WordBasic").getDispatch(),
				"FileSaveAs", savePath);
	}

	/**
	 * 文件保存为html格式
	 * 
	 * @param savePath
	 * @param htmlPath
	 */
	public void saveAsHtml(String htmlPath) {
		Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
				htmlPath, new Variant(8) }, new int[1]);
	}

	/**
	 * 关闭文档
	 * 
	 * @param val
	 *            0不保存修改 -1 保存修改 -2 提示是否保存修改
	 */
	public void closeDocument(int val) {
		Dispatch.call(doc, "Close", new Variant(val));// 注 是documents而不是doc
		documents = null;
		doc = null;
	}

	/**
	 * 关闭当前word文档
	 * 
	 */
	public void closeDocument() {
		if (documents != null) {
			Dispatch.call(documents, "Save");
			Dispatch.call(documents, "Close", new Variant(saveOnExit));
			documents = null;
			doc = null;
		}
	}

	public void closeDocumentWithoutSave() {
		if (documents != null) {
			Dispatch.call(documents, "Close", new Variant(false));
			documents = null;
			doc = null;
		}
	}

	/**
	 * 保存并关闭全部应用
	 * 
	 */
	public void close() {
		closeDocument(-1);
		if (word != null) {
			// Dispatch.call(word, "Quit");
			word.invoke("Quit", new Variant[] {});
			word = null;
		}
		selection = null;
		documents = null;
		ComThread.Release();// 释放com线程。根据jacob的帮助文档，com的线程回收不由java的垃圾回收器处理

	}

	/**
	 * 打印当前word文档
	 * 
	 */
	public void printFile() {
		if (doc != null) {
			Dispatch.call(doc, "PrintOut");
		}
	}

	/**
	 * 保护当前档,如果不存在, 使用expression.Protect(Type, NoReset, Password)
	 * 
	 * @param pwd
	 * @param type
	 *            WdProtectionType 常量之一(int 类型，只读)： 1-wdAllowOnlyComments 仅批注
	 *            2-wdAllowOnlyFormFields 仅填写窗体 0-wdAllowOnlyRevisions 仅修订
	 *            -1-wdNoProtection 无保护, 3-wdAllowOnlyReading 只读
	 * 
	 */
	public void protectedWord(String pwd, String type) {
		String protectionType = Dispatch.get(doc, "ProtectionType").toString();
		if (protectionType.equals("-1")) {
			Dispatch.call(doc, "Protect", Integer.parseInt(type), new Variant(
					true), pwd);
		}
	}

	/**
	 * 解除文档保护,如果存在
	 * 
	 * @param pwd
	 *            WdProtectionType 常量之一(int 类型，只读)： 1-wdAllowOnlyComments 仅批注
	 *            2-wdAllowOnlyFormFields 仅填写窗体 0-wdAllowOnlyRevisions 仅修订
	 *            -1-wdNoProtection 无保护, 3-wdAllowOnlyReading 只读
	 * 
	 */
	public void unProtectedWord(String pwd) {
		String protectionType = Dispatch.get(doc, "ProtectionType").toString();
		if (!protectionType.equals("0") && !protectionType.equals("-1")) {
			Dispatch.call(doc, "Unprotect", pwd);
		}
	}

	/**
	 * 返回文档的保护类型
	 * 
	 * @return
	 */
	public String getProtectedType() {
		return Dispatch.get(doc, "ProtectionType").toString();
	}

	/**
	 * 设置word文档安全级别
	 * 
	 * @param value
	 *            1-msoAutomationSecurityByUI 使用“安全”对话框指定的安全设置。
	 *            2-msoAutomationSecurityForceDisable
	 *            在程序打开的所有文件中禁用所有宏，而不显示任何安全提醒。 3-msoAutomationSecurityLow
	 *            启用所有宏，这是启动应用程序时的默认值。
	 */
	public void setAutomationSecurity(int value) {
		word.setProperty("AutomationSecurity", new Variant(value));
	}

	/**
	 * 在word中插入标签 labelName是标签名，labelValue是标签值
	 * 
	 * @param labelName
	 * @param labelValue
	 */
	public void insertLabelValue(String labelName, String labelValue) {

		Dispatch bookMarks = Dispatch.call(doc, "Bookmarks").toDispatch();
		boolean isExist = Dispatch.call(bookMarks, "Exists", labelName)
				.getBoolean();
		if (isExist == true) {
			Dispatch rangeItem1 = Dispatch.call(bookMarks, "Item", labelName)
					.toDispatch();
			Dispatch range1 = Dispatch.call(rangeItem1, "Range").toDispatch();
			String bookMark1Value = Dispatch.get(range1, "Text").toString();
			System.out.println("书签内容：" + bookMark1Value);
		} else {
			System.out.println("当前书签不存在,重新建立!");
			// TODO 先插入文字，再查找选中文字，再插入标签
			this.insertText(labelValue);
			// this.find(labelValue);//查找文字，并选中
			this.setFont(true, true, true, "102,92,38", "20", "", true);
			Dispatch.call(bookMarks, "Add", labelName, selection);
			Dispatch.call(bookMarks, "Hidden", labelName);
		}
	}

	/**
	 * 在word中插入标签 labelName是标签名
	 * 
	 * @param labelName
	 */
	public void insertLabel(String labelName) {

		Dispatch bookMarks = Dispatch.call(doc, "Bookmarks").toDispatch();
		boolean isExist = Dispatch.call(bookMarks, "Exists", labelName)
				.getBoolean();
		if (isExist == true) {
			System.out.println("书签已存在");
		} else {
			System.out.println("建立书签：" + labelName);
			Dispatch.call(bookMarks, "Add", labelName, selection);
		}
	}

	/**
	 * 查找书签
	 * 
	 * @param labelName
	 * @return
	 */
	public boolean findLabel(String labelName) {
		Dispatch bookMarks = Dispatch.call(doc, "Bookmarks").toDispatch();
		boolean isExist = Dispatch.call(bookMarks, "Exists", labelName)
				.getBoolean();
		if (isExist == true) {
			return true;
		} else {
			System.out.println("当前书签不存在!");
			return false;
		}
	}

	/**
	 * 模糊查找书签,并返回准确的书签名称
	 * 
	 * @param labelName
	 * @return
	 */
	public String findLabelLike(String labelName) {
		Dispatch bookMarks = Dispatch.call(doc, "Bookmarks").toDispatch();
		int count = Dispatch.get(bookMarks, "Count").getInt(); // 书签数
		Dispatch rangeItem = null;
		String lname = "";
		for (int i = 1; i <= count; i++) {
			rangeItem = Dispatch.call(bookMarks, "Item", new Variant(i))
					.toDispatch();
			lname = Dispatch.call(rangeItem, "Name").toString();// 书签名称
			if (lname.startsWith(labelName)) {// 前面匹配
				// return lname.replaceFirst(labelName, "");//返回后面值
				return lname;
			}
		}
		return "";
	}

	/**
	 * 模糊删除书签
	 * 
	 * @param labelName
	 */
	public void deleteLableLike(String labelName) {
		Dispatch bookMarks = Dispatch.call(doc, "Bookmarks").toDispatch();
		int count = Dispatch.get(bookMarks, "Count").getInt(); // 书签数
		Dispatch rangeItem = null;
		String lname = "";
		for (int i = 1; i <= count; i++) {
			rangeItem = Dispatch.call(bookMarks, "Item", new Variant(i))
					.toDispatch();
			lname = Dispatch.call(rangeItem, "Name").toString();// 书签名称
			if (lname.startsWith(labelName)) {// 前面匹配
				Dispatch.call(rangeItem, "Delete");
				count--;// 书签已被删除，书签数目和当前书签都要相应减1，否则会报错:集合找不到
				i--;
			}
		}
	}

	/**
	 * 获取书签内容
	 * 
	 * @param labelName
	 * @return
	 */
	public String getLableValue(String labelName) {
		if (this.findLabel(labelName)) {
			Dispatch bookMarks = Dispatch.call(doc, "Bookmarks").toDispatch();
			Dispatch rangeItem1 = Dispatch.call(bookMarks, "Item", labelName).toDispatch();
			Dispatch range1 = Dispatch.call(rangeItem1, "Range").toDispatch();
			Dispatch font = Dispatch.get(range1, "Font").toDispatch();
			Dispatch.put(font, "Hidden", new Variant(false)); // 显示书签内容
			String bookMark1Value = Dispatch.get(range1, "Text").toString();
			System.out.println("书签内容：" + bookMark1Value);
			// font = Dispatch.get(range1, "Font").toDispatch();
			// Dispatch.put(font, "Hidden", new Variant(true)); //隐藏书签内容
			return bookMark1Value;
		}
		return "";
	}

	/**
	 * 把插入点移动到文件首位置
	 * 
	 */
	public void moveStart() {
		if (selection == null)
			selection = Dispatch.get(word, "Selection").toDispatch();
		Dispatch.call(selection, "HomeKey", new Variant(6));
	}

	/**
	 * 在指定的单元格里填写数据
	 * 
	 * @param tableIndex
	 *            文档中的第tIndex个Table，即tIndex为索引取
	 * @param cellRowIdx
	 *            cell在Table第row行
	 * @param cellColIdx
	 *            cell在Talbe第col列
	 * @param txt
	 *            填写的数据
	 */
	public void putTxtToCell(int tableIndex, int cellRowIdx, int cellColIdx, String txt) {
		// 所有表格
		Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
		// 要填充的表格
		Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
		Dispatch cell = Dispatch.call(table, "Cell", new Variant(cellRowIdx), new Variant(cellColIdx)).toDispatch();
		Dispatch.call(cell, "Select");
		Dispatch.put(selection, "Text", txt);
	}
	
	/**
	 * 增加一行
	 * 
	 * @param tableIndex
	 *            word文档中的第N张表(从1开始)
	 */
	public void addRow(int tableIndex) {
		Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
		// 要填充的表格
		Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
		// 表格的所有行
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch.call(rows, "Add");
	}

	public static void main(String[] args) throws Exception {
		JacobHelper helper = new JacobHelper(true);
		helper.openDocument("D:\\steak.docx");
		helper.replaceText("name", "魏峰");
		helper.moveStart();
		helper.replaceText("phone", "18513794591");
		helper.moveStart();
		helper.replaceText("address", "丰台区三环新城7号院");
		helper.moveStart();
		helper.putTxtToCell(1, 2, 1, "菲力牛排");
		helper.putTxtToCell(1, 2, 2, "1");
		helper.putTxtToCell(1, 2, 3, "20");
		helper.addRow(1);
		helper.putTxtToCell(1, 3, 1, "芝加哥牛排");
		helper.putTxtToCell(1, 3, 2, "1");
		helper.putTxtToCell(1, 3, 3, "23");
		helper.printFile();
		helper.save("D:\\gen.docx");
	}
}