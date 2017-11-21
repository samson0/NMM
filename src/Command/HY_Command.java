package Command;

import java.util.Arrays;

import UI.HY_UI;
import USB.HY_USB;

public class HY_Command {
	private boolean bUsbCon = false;
	private HY_USB hyUSB = null;
	private int hid_size = 0;
	
	public HY_Command(int kb_hid){	
		hyUSB = new HY_USB();
		
		this.hid_size = kb_hid;
		
		if(hyUSB.init_USB_Device(this.hid_size) == 0)
			this.bUsbCon = true;
	}
	
	public boolean Get_Keyboard_Configuration(byte[] rev_data){
		boolean bRet = false;
		
		if(!this.bUsbCon)
			return false;
		
		byte[] send_buf = new byte[1];
    	
    	send_buf[0] = (byte)0x02;
    	
    	if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){
    		if(rev_data[0] == (byte)0x02){
    			bRet = true;
    		}   		
    	}
    	
    	hyUSB.usb_Close();
		
		return bRet;
	}
	
}
