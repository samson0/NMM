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
	
	public void HY_Command_Close(){
		if(hyUSB != null)
			hyUSB.usb_Close();
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
	
	public boolean N4375_Set_Sentinel_Table(byte[] key_buf){

		if(!this.bUsbCon)
			return false;

		boolean bRet = true;
		
		byte[] send_buf = new byte[this.hid_size];
		byte[] rev_data = new byte[this.hid_size];
		Arrays.fill(send_buf, (byte)0);
		
		send_buf[0] = 0x05;// command
		send_buf[2] = 0x10;// length
		
		for(int i = 0; i < 5; i++){
			send_buf[1] =(byte)(0x60 + i * 0x10);
			System.arraycopy(key_buf, i * 0x10, send_buf, 3, 0x10);
			
			rev_data[0] = 0;
			if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
	   			if(rev_data[0] != 0x05){
	   				bRet = false;
	   				
	   				break;
	   			}	   				
	    	}
		}
		
		return bRet;
	}
	
	public boolean N4375_Get_Sentinel_Table(byte[] key_buf){

		if(!this.bUsbCon)
			return false;

		boolean bRet = true;
		
		byte[] send_buf = new byte[this.hid_size];
		byte[] rev_data = new byte[this.hid_size];
		Arrays.fill(send_buf, (byte)0);
		
		send_buf[0] = 0x04;// command
		
		for(int i = 0; i < 5; i++){
			send_buf[1] =(byte)(0x60 + i * 0x10);			
			
			rev_data[0] = 0;
			if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
	   			if(rev_data[0] != 0x04){
	   				bRet = false;
	   				
	   				break;
	   			}else{
	   				System.arraycopy(rev_data, 2, key_buf, i * 0x10, 0x10);
	   			}
	    	}
		}
		
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
	
	public boolean N4375_Update_Whole_Keyboard(byte[] send_data){
		boolean bRet = true;
				
		bRet = N4375_Set_Keyboard_Configuration(Arrays.copyOfRange(send_data, 0x00, 0x17));
		
		if(bRet){
			bRet = N4375_Update_Key_Mappings(send_data);
		}	

		return bRet;
	}
	
	public boolean N4375_Update_Key_Mappings(byte[] key_buf){

		if(!this.bUsbCon)
			return false;

		boolean bRet = true;
		
		byte[] send_buf = new byte[this.hid_size];
		byte[] rev_data = new byte[this.hid_size];
		Arrays.fill(send_buf, (byte)0);
		
		send_buf[0] = 0x05;// command
		send_buf[2] = 0x10;
		
		// Set programmble key code
		for(int i = 0; i < 4; i++){
			send_buf[1] =(byte)(0x20 + i * 0x10);
			System.arraycopy(key_buf, 0x20 + i * 0x10, send_buf, 3, 0x10);
			
			rev_data[0] = 0;
			if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
	   			if(rev_data[0] != 0x05){
	   				bRet = false;
	   				
	   				break;
	   			}	   				
	    	}
		}
		
		if(bRet){		
			// Set key mapping table
			send_buf[0] = 0x09;// command
			for(int i = 0; i < 8; i++){
				send_buf[1] = (byte)(i * 0x10);
				System.arraycopy(key_buf, (0x100 + i * 0x10), send_buf, 3, 0x10);
			
				rev_data[0] = 0;
				if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
					if(rev_data[0] != 0x09){
						bRet = false;
	   				
						break;
					}	   				
				}
			}
		}
		
		return bRet;
	}
	
	public boolean N4375_Retrieve_Keyboard(byte[] key_buf){

		if(!this.bUsbCon)
			return false;

		boolean bRet = true;
		
		byte[] send_buf = new byte[this.hid_size];
		byte[] rev_data = new byte[this.hid_size];
		
		
		//Get Keyboard configuration				
		send_buf[0] = 0x02;// command		
		if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
			if(rev_data[0] != 0x02){
				bRet = false;
			}else{
				System.arraycopy(rev_data, 1, key_buf, 0, 0x18);
			}
		}	
		
		//Get programmable key code 
		if(bRet){		
			send_buf[0] = 0x04;// command
			for(int i = 0; i < 0x100; i=i+0x10){
				send_buf[1] = (byte)i;

				if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
					if(rev_data[0] != 0x04){
						bRet = false;
	   				
						break;
					}else{
						System.arraycopy(rev_data, 2, key_buf, i, 0x10);
					}
				}
			}
		}
		
		if(bRet){		
			// Get key mapping table
			send_buf[0] = 0x08;// command
			for(int i = 0; i < 8; i++){
				send_buf[1] = (byte)(i * 0x10);
				
				if(hyUSB.NCR_SendUsbCmd(send_buf, send_buf.length, rev_data)){    		
					if(rev_data[0] != 0x08){
						bRet = false;
	   				
						break;
					}else{
						System.arraycopy(rev_data, 2, key_buf, 0x100 + i*0x10, 0x10);
					}
				}
			}
		}

    	hyUSB.usb_Close();
		
		return bRet;
	}
	
}
