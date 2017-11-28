package UI;

public class HID_To_String {
	static String HidCodeToString(byte hid_code){
		
		String[] table = new String[]{
			"Key null", "", "", "", "A a", "B b", "C c", "D d", //0X00
			"E e", "F f", "G g", "H h", "I i", "J j", "K k", "L l",//0X08
			"M m", "N n", "O o", "P p", "Q q", "R r", "S s", "T t",//0x10
			"U u", "V v", "W w", "X x", "Y y", "Z z", "1 !", "2 @",//0x18
			"3 #", "4 $", "5 %", "6 ^", "7 &", "8 *", "9 (", "0 )",//0x20
			"Keyboard Return", "Keyboard Escape", "Keyboard Delete", "Keyboard Tab", "Keyboard Spacebar", "- _", "= +", "[ {",//0x28
			"] }", "\\ |", "Europe 1", ", :", "' \"", "` ~", ", <", ". >", //0x30
			"/ ?", "Keyboard Caps Lock","Keyboard F1","Keyboard F2","Keyboard F3","Keyboard F4","Keyboard F5","Keyboard F6",//0x38
			"Keyboard F7","Keyboard F8","Keyboard F9","Keyboard F10","Keyboard F11","Keyboard F12","Keyboard Print Screen","Keyboard Scroll Lock",//0x40
			"Keyboard Pause","Keyboard Insert","Keyboard Home","Keybaord Page Up","Keyboard Delete","Keybaord End","Keyvoard Page Down","Keyvoard Right Arrow",//0x48
			"Keyvoard Left Arrow","Keyvoard Down Arrow","Keyvoard Up Arrow","Keyvoard Num Lock","Keypad /","Keypad *","Keypad -","Keypad +",//0x50
			"Keypad Enter","Keypad 1 and End","Keypad 2 and Down Arrow","Keypad 3 and Page Down","Keypad 4 and Left Arrow","Keypad 5","Keypad 6 and Right Arrow","Keypad 7 and Home",//0x58
			"Keypad 8 and Up Arrow","Keypad 9 and Page Up","Keypad 0 and Insert","Keypad . and Delete","Europe 2","Keyboard Application","Keyboard Power","Keypad =",//0x60
			"Keyboard F13","Keyboard F14","Keyboard F15","Keyboard F16","Keyboard F17","Keyboard F18","Keyboard F19","Keyboard F20",//0x68
			"Keyboard F21","Keyboard F22","Keyboard F23","Keyboard F24","Keyboard Execute","Keyboard Help","Keyboard Menu","Keyboard Select",//0x70
			"Keyvoard Stop","Keyvoard Again","Keyvoard Undo","Keyvoard Cut","Keyvoard Copy","Keyvoard Past","Keyvoard Find","Keyvoard Mute",//0x78
			"Keyvoard Volume Up","Keyvoard Volume Down","Keyboard Locking Caps Lock","Keyboard Locking Num Lock","Keyboard Locking Scroll Lock","Keypad comma","Keypad Equal Sign","Keyboard International 1",//0x80
			"Keyboard International 2(Katakana)","Keyboard International 3(¥)","Keyboard International 4(XFER)","Keyboard International 5(NFER)","Keyboard International 6","Keyboard International 7","Keyboard International 8","Keyboard International 9",//0x88
			"Keyboard LANG 1(Hangul/English)","Keyboard LANG 2(Hanja)","Keyboard LANG 3","Keyboard LANG 4","Keyboard LANG 5","Keyboard LANG 6","Keyboard LANG 7","Keyboard LANG 8",//0x90
			"Keyboard LANG 9","Keyboard Akternate Erase","Keyboard SysReq/Attention","Keyboard Cancel","Keyboard Clear","Keyboard Prior","Keyboard Return","Keyboard Separator",//0x98
			"Keyboard Out","Keyboard Oper","Keyboard Clear/Again","Keyboard CrSel/Props","Keyboard Exsel","","","",//0xA0
			"","","","","","","","",//0xA8
			"Keypad 00","Keypad 000","Thousands Separator","Decimal Separator","Currency Unit","Currency Sub-unit","Keypad (","Keypad )",//0xB0
			"Keypad {","Keypad }","Keypad Tab","Keypad Backspace","Keypad A","Keypad B","Keypad C","Keypad D",//0xB8
			"Keypad E","Keypad F","Keypad XOR","Keypad ^","Keypad %","Keypad <","Keypad >","Keypad &",//0xC0
			"Keypad &&","Keypad |","Keypad ||","Keypad :","Keypad #","Keypad Space","Keypad @","Keypad !",//0xC8
			"Keypad Memory Store","Keypad Memory Recall","Keypad Memory Clear","Keypad Memory Add","Keypad Memory Subtract","Keypad Memory Multiply","Keypad Memory Divide","Keypad +/-",//0xD0
			"Keypad Clear","Keypad Clear Entry","Keypad Binary","Keypad Octal","Keypad Decimal","Keypad Hexadecimal","","",//0xD7
			"Left control key","Left shift key","Left alt key","Left GUI key","Right control key","Right shift key","Right alt key","Right GUI key" //0xE0
		};
		
		//System.out.println("hid_code = " + hid_code);
		//System.out.println("table.length = " + table.length);
		
		if((hid_code & 0xFF) >= table.length)
			return "null";

		return table[(hid_code & 0xFF)];
	}
}
