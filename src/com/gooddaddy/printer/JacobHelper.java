package com.gooddaddy.printer;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobHelper {
	// word�ĵ�
	private Dispatch doc;

	// word���г������
	private ActiveXComponent word;

	// ����word�ĵ�����
	private Dispatch documents;

	// ѡ���ķ�Χ������
	private Dispatch selection;

	private boolean saveOnExit = true;

	public JacobHelper(boolean visible) throws Exception {
		ComThread.InitSTA();// �߳�����
		if (word == null) {
			word = new ActiveXComponent("Word.Application");
			word.setProperty("Visible", new Variant(visible)); // ���ɼ���word
			word.setProperty("AutomationSecurity", new Variant(3)); // ���ú�
		}
		if (documents == null)
			documents = word.getProperty("Documents").toDispatch();
	}

	/**
	 * �����˳�ʱ����
	 * 
	 * @param saveOnExit
	 *            boolean true-�˳�ʱ�����ļ���false-�˳�ʱ�������ļ�
	 */
	public void setSaveOnExit(boolean saveOnExit) {
		this.saveOnExit = saveOnExit;
	}

	/**
	 * ����һ���µ�word�ĵ�
	 * 
	 */
	public void createNewDocument() {
		doc = Dispatch.call(documents, "Add").toDispatch();
		selection = Dispatch.get(word, "Selection").toDispatch();
	}

	/**
	 * ��һ���Ѵ��ڵ��ĵ�
	 * 
	 * @param docPath
	 */
	public void openDocument(String docPath) {
		// closeDocument();
		doc = Dispatch.call(documents, "Open", docPath).toDispatch();
		selection = Dispatch.get(word, "Selection").toDispatch();
	}

	/**
	 * ֻ����ʽ��һ�����ܵ��ĵ�
	 * 
	 * @param docPath
	 *            -�ļ�ȫ��
	 * @param pwd
	 *            -����
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
	 * ��һ�����ܵ��ĵ�
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
	 * ��ѡ�����ݻ����㿪ʼ�����ı�
	 * 
	 * @param toFindText
	 *            Ҫ���ҵ��ı�
	 * @return boolean true-���ҵ���ѡ�и��ı���false-δ���ҵ��ı�
	 */
	@SuppressWarnings("static-access")
	public boolean find(String toFindText) {
		if (toFindText == null || toFindText.equals(""))
			return false;
		// ��selection����λ�ÿ�ʼ��ѯ
		Dispatch find = word.call(selection, "Find").toDispatch();
		// ����Ҫ���ҵ�����
		Dispatch.put(find, "Text", toFindText);
		// ��ǰ����
		Dispatch.put(find, "Forward", "True");
		// ���ø�ʽ
		Dispatch.put(find, "Format", "True");
		// ��Сдƥ��
		Dispatch.put(find, "MatchCase", "True");
		// ȫ��ƥ��
		Dispatch.put(find, "MatchWholeWord", "false");
		// ���Ҳ�ѡ��
		return Dispatch.call(find, "Execute").getBoolean();
	}

	/**
	 * ��ѡ��ѡ�������趨Ϊ�滻�ı�
	 * 
	 * @param toFindText
	 *            �����ַ���
	 * @param newText
	 *            Ҫ�滻������
	 * @return
	 */
	public boolean replaceText(String toFindText, String newText) {
		if (!find(toFindText))
			return false;
		Dispatch.put(selection, "Text", newText);
		return true;
	}

	/**
	 * ȫ���滻�ı�
	 * 
	 * @param toFindText
	 *            �����ַ���
	 * @param newText
	 *            Ҫ�滻������
	 */
	public void replaceAllText(String toFindText, String newText) {
		while (find(toFindText)) {
			Dispatch.put(selection, "Text", newText);
			Dispatch.call(selection, "MoveRight");
		}
	}

	/**
	 * �ڵ�ǰ���������ַ���
	 * 
	 * @param newText
	 *            Ҫ��������ַ���
	 */
	public void insertText(String newText) {
		Dispatch.put(selection, "Text", newText);
	}

	/**
	 * ���õ�ǰѡ�����ݵ�����
	 * 
	 * @param boldSize
	 * @param italicSize
	 * @param underLineSize
	 *            �»���
	 * @param colorSize
	 *            ������ɫ
	 * @param size
	 *            �����С
	 * @param name
	 *            ��������
	 * @param hidden
	 *            �Ƿ�����
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
	 * �ļ���������Ϊ
	 * 
	 * @param savePath
	 *            ��������Ϊ·��
	 */
	public void save(String savePath) {
		Dispatch.call(Dispatch.call(word, "WordBasic").getDispatch(),
				"FileSaveAs", savePath);
	}

	/**
	 * �ļ�����Ϊhtml��ʽ
	 * 
	 * @param savePath
	 * @param htmlPath
	 */
	public void saveAsHtml(String htmlPath) {
		Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
				htmlPath, new Variant(8) }, new int[1]);
	}

	/**
	 * �ر��ĵ�
	 * 
	 * @param val
	 *            0�������޸� -1 �����޸� -2 ��ʾ�Ƿ񱣴��޸�
	 */
	public void closeDocument(int val) {
		Dispatch.call(doc, "Close", new Variant(val));// ע ��documents������doc
		documents = null;
		doc = null;
	}

	/**
	 * �رյ�ǰword�ĵ�
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
	 * ���沢�ر�ȫ��Ӧ��
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
		ComThread.Release();// �ͷ�com�̡߳�����jacob�İ����ĵ���com���̻߳��ղ���java����������������

	}

	/**
	 * ��ӡ��ǰword�ĵ�
	 * 
	 */
	public void printFile() {
		if (doc != null) {
			Dispatch.call(doc, "PrintOut");
		}
	}

	/**
	 * ������ǰ��,���������, ʹ��expression.Protect(Type, NoReset, Password)
	 * 
	 * @param pwd
	 * @param type
	 *            WdProtectionType ����֮һ(int ���ͣ�ֻ��)�� 1-wdAllowOnlyComments ����ע
	 *            2-wdAllowOnlyFormFields ����д���� 0-wdAllowOnlyRevisions ���޶�
	 *            -1-wdNoProtection �ޱ���, 3-wdAllowOnlyReading ֻ��
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
	 * ����ĵ�����,�������
	 * 
	 * @param pwd
	 *            WdProtectionType ����֮һ(int ���ͣ�ֻ��)�� 1-wdAllowOnlyComments ����ע
	 *            2-wdAllowOnlyFormFields ����д���� 0-wdAllowOnlyRevisions ���޶�
	 *            -1-wdNoProtection �ޱ���, 3-wdAllowOnlyReading ֻ��
	 * 
	 */
	public void unProtectedWord(String pwd) {
		String protectionType = Dispatch.get(doc, "ProtectionType").toString();
		if (!protectionType.equals("0") && !protectionType.equals("-1")) {
			Dispatch.call(doc, "Unprotect", pwd);
		}
	}

	/**
	 * �����ĵ��ı�������
	 * 
	 * @return
	 */
	public String getProtectedType() {
		return Dispatch.get(doc, "ProtectionType").toString();
	}

	/**
	 * ����word�ĵ���ȫ����
	 * 
	 * @param value
	 *            1-msoAutomationSecurityByUI ʹ�á���ȫ���Ի���ָ���İ�ȫ���á�
	 *            2-msoAutomationSecurityForceDisable
	 *            �ڳ���򿪵������ļ��н������к꣬������ʾ�κΰ�ȫ���ѡ� 3-msoAutomationSecurityLow
	 *            �������к꣬��������Ӧ�ó���ʱ��Ĭ��ֵ��
	 */
	public void setAutomationSecurity(int value) {
		word.setProperty("AutomationSecurity", new Variant(value));
	}

	/**
	 * ��word�в����ǩ labelName�Ǳ�ǩ����labelValue�Ǳ�ǩֵ
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
			System.out.println("��ǩ���ݣ�" + bookMark1Value);
		} else {
			System.out.println("��ǰ��ǩ������,���½���!");
			// TODO �Ȳ������֣��ٲ���ѡ�����֣��ٲ����ǩ
			this.insertText(labelValue);
			// this.find(labelValue);//�������֣���ѡ��
			this.setFont(true, true, true, "102,92,38", "20", "", true);
			Dispatch.call(bookMarks, "Add", labelName, selection);
			Dispatch.call(bookMarks, "Hidden", labelName);
		}
	}

	/**
	 * ��word�в����ǩ labelName�Ǳ�ǩ��
	 * 
	 * @param labelName
	 */
	public void insertLabel(String labelName) {

		Dispatch bookMarks = Dispatch.call(doc, "Bookmarks").toDispatch();
		boolean isExist = Dispatch.call(bookMarks, "Exists", labelName)
				.getBoolean();
		if (isExist == true) {
			System.out.println("��ǩ�Ѵ���");
		} else {
			System.out.println("������ǩ��" + labelName);
			Dispatch.call(bookMarks, "Add", labelName, selection);
		}
	}

	/**
	 * ������ǩ
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
			System.out.println("��ǰ��ǩ������!");
			return false;
		}
	}

	/**
	 * ģ��������ǩ,������׼ȷ����ǩ����
	 * 
	 * @param labelName
	 * @return
	 */
	public String findLabelLike(String labelName) {
		Dispatch bookMarks = Dispatch.call(doc, "Bookmarks").toDispatch();
		int count = Dispatch.get(bookMarks, "Count").getInt(); // ��ǩ��
		Dispatch rangeItem = null;
		String lname = "";
		for (int i = 1; i <= count; i++) {
			rangeItem = Dispatch.call(bookMarks, "Item", new Variant(i))
					.toDispatch();
			lname = Dispatch.call(rangeItem, "Name").toString();// ��ǩ����
			if (lname.startsWith(labelName)) {// ǰ��ƥ��
				// return lname.replaceFirst(labelName, "");//���غ���ֵ
				return lname;
			}
		}
		return "";
	}

	/**
	 * ģ��ɾ����ǩ
	 * 
	 * @param labelName
	 */
	public void deleteLableLike(String labelName) {
		Dispatch bookMarks = Dispatch.call(doc, "Bookmarks").toDispatch();
		int count = Dispatch.get(bookMarks, "Count").getInt(); // ��ǩ��
		Dispatch rangeItem = null;
		String lname = "";
		for (int i = 1; i <= count; i++) {
			rangeItem = Dispatch.call(bookMarks, "Item", new Variant(i))
					.toDispatch();
			lname = Dispatch.call(rangeItem, "Name").toString();// ��ǩ����
			if (lname.startsWith(labelName)) {// ǰ��ƥ��
				Dispatch.call(rangeItem, "Delete");
				count--;// ��ǩ�ѱ�ɾ������ǩ��Ŀ�͵�ǰ��ǩ��Ҫ��Ӧ��1������ᱨ��:�����Ҳ���
				i--;
			}
		}
	}

	/**
	 * ��ȡ��ǩ����
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
			Dispatch.put(font, "Hidden", new Variant(false)); // ��ʾ��ǩ����
			String bookMark1Value = Dispatch.get(range1, "Text").toString();
			System.out.println("��ǩ���ݣ�" + bookMark1Value);
			// font = Dispatch.get(range1, "Font").toDispatch();
			// Dispatch.put(font, "Hidden", new Variant(true)); //������ǩ����
			return bookMark1Value;
		}
		return "";
	}

	/**
	 * �Ѳ�����ƶ����ļ���λ��
	 * 
	 */
	public void moveStart() {
		if (selection == null)
			selection = Dispatch.get(word, "Selection").toDispatch();
		Dispatch.call(selection, "HomeKey", new Variant(6));
	}

	/**
	 * ��ָ���ĵ�Ԫ������д����
	 * 
	 * @param tableIndex
	 *            �ĵ��еĵ�tIndex��Table����tIndexΪ����ȡ
	 * @param cellRowIdx
	 *            cell��Table��row��
	 * @param cellColIdx
	 *            cell��Talbe��col��
	 * @param txt
	 *            ��д������
	 */
	public void putTxtToCell(int tableIndex, int cellRowIdx, int cellColIdx, String txt) {
		// ���б��
		Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
		// Ҫ���ı��
		Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
		Dispatch cell = Dispatch.call(table, "Cell", new Variant(cellRowIdx), new Variant(cellColIdx)).toDispatch();
		Dispatch.call(cell, "Select");
		Dispatch.put(selection, "Text", txt);
	}
	
	/**
	 * ����һ��
	 * 
	 * @param tableIndex
	 *            word�ĵ��еĵ�N�ű�(��1��ʼ)
	 */
	public void addRow(int tableIndex) {
		Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
		// Ҫ���ı��
		Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
		// ����������
		Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
		Dispatch.call(rows, "Add");
	}

	public static void main(String[] args) throws Exception {
		JacobHelper helper = new JacobHelper(true);
		helper.openDocument("D:\\steak.docx");
		helper.replaceText("name", "κ��");
		helper.moveStart();
		helper.replaceText("phone", "18513794591");
		helper.moveStart();
		helper.replaceText("address", "��̨�������³�7��Ժ");
		helper.moveStart();
		helper.putTxtToCell(1, 2, 1, "����ţ��");
		helper.putTxtToCell(1, 2, 2, "1");
		helper.putTxtToCell(1, 2, 3, "20");
		helper.addRow(1);
		helper.putTxtToCell(1, 3, 1, "֥�Ӹ�ţ��");
		helper.putTxtToCell(1, 3, 2, "1");
		helper.putTxtToCell(1, 3, 3, "23");
		helper.printFile();
		helper.save("D:\\gen.docx");
	}
}