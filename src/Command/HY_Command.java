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
	
	public boolean N4375_Get_Keyboard_Configuration(byte[] rev_data){
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
	
	public boolean N4375_Set_Keyboard_Configuration(byte[] send_data){
		boolean bRet = false;
		
		if(!this.bUsbCon)
			return false;
		
		byte[] send_buf = new byte[this.hid_size];
		byte[] rev_data = new byte[this.hid_size];
		Arrays.fill(send_buf, (byte)0);
    	
    	send_buf[0] = (byte)0x03;// command
    	send_buf[1] = (byte)0x00;// offset
    	send_buf[2] = (byte)0x17;// number of bytes to be updated
    	System.arraycopy(send_data, 0, send_buf, 3, 0x17);
    	
    	if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
    		if(rev_data[0] == (byte)0x03){
    			bRet = true;
    		}   		
    	}
    	
    	hyUSB.usb_Close();
		
		return bRet;
	}
	
	public boolean N4375_Write_CfgData_To_Flash(){
		boolean bRet = false;
		
		if(!this.bUsbCon)
			return false;
		
		byte[] send_buf = new byte[this.hid_size];
		byte[] rev_data = new byte[this.hid_size];
		Arrays.fill(send_buf, (byte)0);
    	
    	send_buf[0] = (byte)0x07;// command
    	
    	if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
    		//if(rev_data[0] == (byte)0x07){
    			bRet = true;
    		//}   		
    	}
    	
    	hyUSB.usb_Close();
		
		return bRet;
	}
	
	public boolean N4375_Get_Status(byte[] keylockStatus, byte[] cashDrawer){

		if(!this.bUsbCon)
			return false;

		boolean bRet = false;
		
		byte[] send_buf = new byte[this.hid_size];
		byte[] rev_data = new byte[this.hid_size];
		Arrays.fill(send_buf, (byte)0);
    	
    	send_buf[0] = (byte)0x00;// command
    	
    	if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
    		if(rev_data[0] == (byte)0x00){
    			bRet = true;
    			
    			keylockStatus[0] = rev_data[2];
    			cashDrawer[0] = rev_data[5];
    		}   		
    	}
    	
    	hyUSB.usb_Close();
		
		return bRet;
	}
	
	/*
	 * Return:
	 * 0:success	 * 
	 * 1:Command has not been completed due to there is not enough RS232 TX buffer available.
	 * 2:can't find USB keyboard
	 */
	public int N4375_Send_UART_Data(byte[] data){

		if(!this.bUsbCon)
			return 2;

		int bRet = 0;
		
		byte[] send_buf = new byte[this.hid_size];
		byte[] rev_data = new byte[this.hid_size];
		Arrays.fill(send_buf, (byte)0);
    	
    	send_buf[0] = (byte)0x0B;// command    	
    	
    	for(int i = 0; i < data.length; i = i + send_buf[1]){
    		send_buf[1]  = (byte)(data.length%0x18);
    		if(send_buf[1] == 0)
    			send_buf[1] = 0x18;
    		
    		System.arraycopy(data, i, send_buf, 2, send_buf[1]);
    		
    		if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
    			if((rev_data[0] != (byte)0x0B) || (rev_data[1] != (byte)0x00)){
    				bRet = rev_data[1];
    				
    				break;
    			}
    		}
    	}
    	
    	hyUSB.usb_Close();
		
		return bRet;
	}
	
	/*
	 * @Para: byte ctl
	 *  0x00: No operation
		0x01: Fire cash Drawer
		0x02~0xFF: Reserved
	 */
	public boolean N4375_Cash_Drawer_Ctl(byte ctl){

		if(!this.bUsbCon)
			return false;

		boolean bRet = false;
		
		byte[] send_buf = new byte[this.hid_size];
		byte[] rev_data = new byte[this.hid_size];
		Arrays.fill(send_buf, (byte)0);
    	
    	send_buf[0] = (byte)0x0A;// command
    	send_buf[1] = ctl;
    	
    	if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
   			bRet = true;
    	}
    	
    	hyUSB.usb_Close();
		
		return bRet;
	}
	
}
