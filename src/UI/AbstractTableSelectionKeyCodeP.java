package UI;

import javax.swing.table.AbstractTableModel;

public class AbstractTableSelectionKeyCodeP extends AbstractTableModel{
	
	String[] head = {"", ""}; 
	Object[][] data = new Object[229][head.length];
	Class<?>[] typeArray = { Object.class, Object.class};
	
	public AbstractTableSelectionKeyCodeP(){
		int i = 0;
		
		data[0][0] = "Null key";
		data[0][1] = "0x00";
		
		for(i = 1; i <= 26; i++){
			//data[i][0] = String.format("%c %c", (i - 1) + 'a', (i - 1) + 'A');
			data[i][0] = HID_To_String.HidCodeToStringPKey((byte)((i - 1) + 4));
			data[i][1] = String.format("0x%02X", (i - 1) + 4);
			
		}
		
		//0x1B - 0xA4
		for(i = 27; i <= 228; i++){			
			data[i][0] = HID_To_String.HidCodeToStringPKey((byte)(i + 3));
			data[i][1] = new String(String.format("0x%02X", i + 3));
		}		
	}
	
    public int getColumnCount() {  
        return head.length;  
    }  
  
    public int getRowCount() {  
        return data.length;  
    }  

    @Override  
    public String getColumnName(int column) {  
        return head[column];  
    }  
  

    public Object getValueAt(int rowIndex, int columnIndex) {  
        return data[rowIndex][columnIndex];  
    }  
  

    @Override  
    public boolean isCellEditable(int rowIndex, int columnIndex) { 
    	    	
        return false;  
    }  

    @Override  
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {  
        data[rowIndex][columnIndex] = aValue;  
        fireTableCellUpdated(rowIndex, columnIndex);  
    }  
  
    public Class<?> getColumnClass(int columnIndex) {  
        return typeArray[columnIndex];
    }
}
