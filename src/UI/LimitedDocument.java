package UI;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class LimitedDocument extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private int maxLength = -1;
	private String allowCharAsString = null;

	public LimitedDocument() {
		super();
	}

	public LimitedDocument(int maxLength) {
		super();
		this.maxLength = maxLength;
	}

	public void insertString(int offset, String str, AttributeSet attrSet) throws BadLocationException {
		if (str == null) {
			return;
		}
		if (allowCharAsString != null && str.length() == 1) {
			if (allowCharAsString.indexOf(str) == -1) {
				return;
			}
		}
		char[] charVal = str.toCharArray();
		String strOldValue = getText(0, getLength());
		char[] tmp = strOldValue.toCharArray();
		if (maxLength != -1 && (tmp.length + charVal.length > maxLength)) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		super.insertString(offset, str, attrSet);
	}

	public void setAllowChar(String str) {
		allowCharAsString = str;
	}
}
