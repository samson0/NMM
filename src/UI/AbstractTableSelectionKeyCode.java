package UI;

import javax.swing.table.AbstractTableModel;

public class AbstractTableSelectionKeyCode extends AbstractTableModel {
	
	String[] head = {"", ""}; 
	Object[][] data = new Object[250][head.length];
	Class<?>[] typeArray = { Object.class, Object.class};
	
	public AbstractTableSelectionKeyCode(){
		int i = 0;
		
		data[0][0] = "Null key";
		data[0][1] = "0x00";
		
		for(i = 1; i <= 26; i++){
			data[i][0] = String.format("%c %c", (i - 1) + 'a', (i - 1) + 'A');
			data[i][1] = String.format("0x%02X", (i - 1) + 4);
		}
		
		//0x1B - 0xA4
		for(i = 27; i <= 161; i++){			
			data[i][1] = new String(String.format("0x%02X", i + 3));
		}	
		
		//0xB0
		for(i = 162; i <= 207; i++){			
			data[i][1] = new String(String.format("0x%02X", i + 14));
		}
		
		//E0
		for(i = 208; i <= 215; i++){			
			data[i][1] = new String(String.format("0x%02X", i + 15));
		}
	
		
		// 0x1E - 0x27
		data[27][0] = "1 !";
		data[28][0] = "2 @";
		data[29][0] = "3 #";
		data[30][0] = "4 $";
		data[31][0] = "5 %";
		data[32][0] = "6 ^";
		data[33][0] = "7 &";
		data[34][0] = "8 *";
		data[35][0] = "9 (";
		data[36][0] = "0 )";
		
		// 0x28 - 0x31
		data[37][0] = "Keyboard Return";
		data[38][0] = "Keyboard Escape";
		data[39][0] = "Keyboard Delete";
		data[40][0] = "Keyboard Tab";
		data[41][0] = "Keyboard Spacebar";
		data[42][0] = "- _";
		data[43][0] = "= +";
		data[44][0] = "[ {";
		data[45][0] = "] }";
		data[46][0] = "\\ |";
		
		// 0x32 - 0x3B
		data[47][0] = "Europe 1";
		data[48][0] = "; :";
		data[49][0] = "' \"";
		data[50][0] = "` ~";
		data[51][0] = ", <";
		data[52][0] = ". >";
		data[53][0] = "/ ?";
		data[54][0] = "Keyboard Caps Lock";
		data[55][0] = "Keyboard F1";
		data[56][0] = "Keyboard F2";
		
		data[57][0] = "Keyboard F3";
		data[58][0] = "Keyboard F4";
		data[59][0] = "Keyboard F5";
		data[60][0] = "Keyboard F6";
		data[61][0] = "Keyboard F7";
		data[62][0] = "Keyboard F8";
		data[63][0] = "Keyboard F9";
		data[64][0] = "Keyboard F10";
		data[65][0] = "Keyboard F11";
		data[66][0] = "Keyboard F12";
		
		data[67][0] = "Keyboard Print Screen";
		data[68][0] = "Keyboard Scroll Lock";
		data[69][0] = "Keyboard Pause";
		data[70][0] = "Keyboard Insert";
		data[71][0] = "Keyboard Home";
		data[72][0] = "Keybaord Page Up";
		data[73][0] = "Keyboard Delete";
		data[74][0] = "Keybaord End";
		data[75][0] = "Keyvoard Page Down";
		data[76][0] = "Keyvoard Right Arrow";
		
		data[77][0] = "Keyvoard Left Arrow";
		data[78][0] = "Keyvoard Down Arrow";
		data[79][0] = "Keyvoard Up Arrow";
		data[80][0] = "Keyvoard Num Lock";
		data[81][0] = "Keypad /";
		data[82][0] = "Keypad *";
		data[83][0] = "Keypad -";
		data[84][0] = "Keypad +";
		data[85][0] = "Keypad Enter";
		data[86][0] = "Keypad 1 and End";
		
		data[87][0] = "Keypad 2 and Down Arrow";
		data[88][0] = "Keypad 3 and Page Down";
		data[89][0] = "Keypad 4 and Left Arrow";
		data[90][0] = "Keypad 5";
		data[91][0] = "Keypad 6 and Right Arrow";
		data[92][0] = "Keypad 7 and Home";
		data[93][0] = "Keypad 8 and Up Arrow";
		data[94][0] = "Keypad 9 and Page Up";
		data[95][0] = "Keypad 0 and Insert";
		data[96][0] = "Keypad . and Delete";
		
		data[97][0] = "Europe 2";
		data[98][0] = "Keyboard Application";
		data[99][0] = "Keyboard Power";
		data[100][0] = "Keypad =";
		data[101][0] = "Keyboard F13";
		data[102][0] = "Keyboard F14";
		data[103][0] = "Keyboard F15";
		data[104][0] = "Keyboard F16";
		data[105][0] = "Keyboard F17";
		data[106][0] = "Keyboard F18";
		
		data[107][0] = "Keyboard F19";
		data[108][0] = "Keyboard F20";
		data[109][0] = "Keyboard F21";
		data[110][0] = "Keyboard F22";
		data[111][0] = "Keyboard F23";
		data[112][0] = "Keyboard F24";
		data[113][0] = "Keyboard Execute";
		data[114][0] = "Keyboard Help";
		data[115][0] = "Keyboard Menu";
		data[116][0] = "Keyboard Select";
		
		data[117][0] = "Keyvoard Stop";
		data[118][0] = "Keyvoard Again";
		data[119][0] = "Keyvoard Undo";
		data[120][0] = "Keyvoard Cut";
		data[121][0] = "Keyvoard Copy";
		data[122][0] = "Keyvoard Past";
		data[123][0] = "Keyvoard Find";
		data[124][0] = "Keyvoard Mute";
		data[125][0] = "Keyvoard Volume Up";
		data[126][0] = "Keyvoard Volume Down";

		data[127][0] = "Keyboard Locking Caps Lock";
		data[128][0] = "Keyboard Locking Num Lock";
		data[129][0] = "Keyboard Locking Scroll Lock";
		data[130][0] = "Keypad comma";
		data[131][0] = "Keypad Equal Sign";
		data[132][0] = "Keyboard International 1";
		data[133][0] = "Keyboard International 2(Katakana)";
		data[134][0] = "Keyboard International 3(¥)";
		data[135][0] = "Keyboard International 4(XFER)";
		data[136][0] = "Keyboard International 5(NFER)";
		
		data[137][0] = "Keyboard International 6";
		data[138][0] = "Keyboard International 7";
		data[139][0] = "Keyboard International 8";
		data[140][0] = "Keyboard International 9";
		data[141][0] = "Keyboard LANG 1(Hangul/English)";
		data[142][0] = "Keyboard LANG 2(Hanja)";
		data[143][0] = "Keyboard LANG 3";
		data[144][0] = "Keyboard LANG 4";
		data[145][0] = "Keyboard LANG 5";
		data[146][0] = "Keyboard LANG 6";
		
		data[147][0] = "Keyboard LANG 7";
		data[148][0] = "Keyboard LANG 8";
		data[149][0] = "Keyboard LANG 9";
		data[150][0] = "Keyboard Akternate Erase";
		data[151][0] = "Keyboard SysReq/Attention";
		data[152][0] = "Keyboard Cancel";
		data[153][0] = "Keyboard Clear";
		data[154][0] = "Keyboard Prior";
		data[155][0] = "Keyboard Return";
		data[156][0] = "Keyboard Separator";
		
		data[157][0] = "Keyboard Out";
		data[158][0] = "Keyboard Oper";
		data[159][0] = "Keyboard Clear/Again";
		data[160][0] = "Keyboard CrSel/Props";
		data[161][0] = "Keyboard Exsel";	
		
		data[162][0] = "Keypad 00";
		data[163][0] = "Keypad 000";
		data[164][0] = "Thousands Separator";
		data[165][0] = "Decimal Separator";
		data[166][0] = "Currency Unit";
		data[167][0] = "Currency Sub-unit";
		data[168][0] = "Keypad (";
		data[169][0] = "Keypad )";
		data[170][0] = "Keypad {";
		data[171][0] = "Keypad }";
		
		data[172][0] = "Keypad Tab";
		data[173][0] = "Keypad Backspace";
		data[174][0] = "Keypad A";
		data[175][0] = "Keypad B";
		data[176][0] = "Keypad C";
		data[177][0] = "Keypad D";
		data[178][0] = "Keypad E";
		data[179][0] = "Keypad F";
		data[180][0] = "Keypad XOR";
		data[181][0] = "Keypad ^";
		
		data[182][0] = "Keypad %";
		data[183][0] = "Keypad <";
		data[184][0] = "Keypad >";
		data[185][0] = "Keypad &";
		data[186][0] = "Keypad &&";
		data[187][0] = "Keypad |";
		data[188][0] = "Keypad ||";
		data[189][0] = "Keypad :";
		data[190][0] = "Keypad #";
		data[191][0] = "Keypad Space";
		
		data[192][0] = "Keypad @";
		data[193][0] = "Keypad !";
		data[194][0] = "Keypad Memory Store";
		data[195][0] = "Keypad Memory Recall";
		data[196][0] = "Keypad Memory Clear";
		data[197][0] = "Keypad Memory Add";
		data[198][0] = "Keypad Memory Subtract";
		data[199][0] = "Keypad Memory Multiply";
		data[200][0] = "Keypad Memory Divide";
		data[201][0] = "Keypad +/-";
		
		data[202][0] = "Keypad Clear";
		data[203][0] = "Keypad Clear Entry";
		data[204][0] = "Keypad Binary";
		data[205][0] = "Keypad Octal";
		data[206][0] = "Keypad Decimal";
		data[207][0] = "Keypad Hexadecimal";
		
		//0xE0
		data[208][0] = "Left control key";
		data[209][0] = "Left shift key";
		data[210][0] = "Left alt key";
		data[211][0] = "Left GUI key";		
		data[212][0] = "Right control key";
		data[213][0] = "Right shift key";
		data[214][0] = "Right alt key";
		data[215][0] = "Right GUI key";
		
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
