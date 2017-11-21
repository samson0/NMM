package UI;

import javax.swing.table.AbstractTableModel;

public class AbstractTableSelectionKeyCode extends AbstractTableModel {
	
	String[] head = {"", ""}; 
	Object[][] data = new Object[250][head.length];
	Class<?>[] typeArray = { Object.class, Object.class};
	
	public AbstractTableSelectionKeyCode(){
		int i = 0;
		
		for(i = 0; i < 26; i++){
			data[i][0] = String.format("%c %c", i + 'a', i + 'A');
			data[i][1] = String.format("0x%02X", i + 4);
		}
		
		for(i = 26; i < 212; i++){
			data[i][1] = new String(String.format("0x%02X", i + 4));
		}		
		
		data[26][0] = "1 !";
		data[27][0] = "2 @";
		data[28][0] = "3 #";
		data[29][0] = "4 $";
		data[30][0] = "5 %";
		data[31][0] = "6 ^";
		data[32][0] = "7 &";
		data[33][0] = "8 *";
		data[34][0] = "9 (";
		data[35][0] = "0 )";
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
