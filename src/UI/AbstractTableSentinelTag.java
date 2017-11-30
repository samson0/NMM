package UI;

import javax.swing.table.AbstractTableModel;

public class AbstractTableSentinelTag extends AbstractTableModel{
	
	String[] head = {""}; 
	Object[][] data = new Object[28][head.length];
	Class<?>[] typeArray = { Object.class};
	
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
