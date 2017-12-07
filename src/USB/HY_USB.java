package USB;

import java.nio.ByteBuffer;

/*
 * For Windows machine, 
 * https://github.com/libusb/libusb/wiki/Windows#How_to_use_libusb_on_Windows
 * 
 */
import java.util.Arrays;

import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class HY_USB {
	/** The USB communication timeout. */
    private final int TIMEOUT = 1000000;
    
    /*
     * NCR_N4374 ==> hid_size = 19
     * NCR_N4375 ==> hid_size = 26
     */
    private final int NCR_VID = 0x0404;
    private final int NCR_PID_NCR_N4374 = 0x0343; // NCR  COMPACT_ALPHA 
    private final int NCR_PID_NCR_N4375	= 0x0376; // NCR 64-Key
    
    public static final int NCR_N4374_HID_SIZE = 19;
    public static final int NCR_N4375_HID_SIZE = 26;
    
    private final byte SEND_HEADER = (byte)0xEA;
    private final byte RECV_HEADER = (byte)0xED;
    
	private DeviceHandle myDeviceHandle = null;
	
	private int hid_size = NCR_N4375_HID_SIZE;
	
	/*
	 * Return: 
	 * 0: Success
	 * -1:Unable to initialize libusb
	 * -2:Unable to get device list
	 * -3:Unable to read device descriptor
	 * -4:Unable to check kernel driver active, interface 0
	 * -5:Unable to detach kernel driver
	 * -6:Unable to claim interface 0
	 * -7:Can't find the USB device 
	 */
	public int init_USB_Device(int kb_hid_size){
		
		int result = LibUsb.init(null);        
		if (result != LibUsb.SUCCESS){
            System.out.println("Unable to initialize libusb. Result=" + result);
			return -1;
        }
		
		DeviceList list = new DeviceList();
		
		result = LibUsb.getDeviceList(null, list);
		if(result < 0){
			System.out.println("Unable to get device list. Result=" + result);
			return -2;
		}
		try{			
	        for (Device device: list){
	            DeviceDescriptor descriptor = new DeviceDescriptor();
	            result = LibUsb.getDeviceDescriptor(device, descriptor);
	            
	            if (result < 0){
	            	System.out.println("Unable to read device descriptor. Result=" + result);
	            	return -3;
	            }
	            
	            if ((descriptor.idVendor() == NCR_VID) && 
	            	(descriptor.idProduct() == NCR_PID_NCR_N4374) || (descriptor.idProduct() == NCR_PID_NCR_N4375)) {
	            	//System.out.println(String.format("Find device, VID=0x%04X,PID=0x%04X",descriptor.idVendor(), descriptor.idProduct()));
	                
	            	this.myDeviceHandle = LibUsb.openDeviceWithVidPid(null, descriptor.idVendor(), descriptor.idProduct());           	        	

	                // Check if kernel driver is attached to the interface	                
	            	int attached = LibUsb.kernelDriverActive(this.myDeviceHandle, 0);
	                if (attached < 0){
	                    System.out.println("Unable to check kernel driver active, interface 0");
	                    return -4;
	                }

	                result = LibUsb.detachKernelDriver(this.myDeviceHandle, 0);
	                if (result != LibUsb.SUCCESS &&
	                    result != LibUsb.ERROR_NOT_SUPPORTED &&
	                    result != LibUsb.ERROR_NOT_FOUND)
	                {
	        			System.out.println("Unable to detach kernel driver 0");
	        			return -5;
	                }              
	                    
	                // Claim interface 0	            	
	                result = LibUsb.claimInterface(this.myDeviceHandle, 0);
	                if (result != LibUsb.SUCCESS){
	                   System.out.println("Unable to claim interface 0");
	                   return -6;	        				
	                }
	            	
	            	return 0;
	            }
	        }
	    }catch(LibUsbException ex){
		    LibUsb.close(this.myDeviceHandle);
			ex.printStackTrace();
		}
		
		this.hid_size = kb_hid_size;
		
		return -7;
	}
	
	
	
/*
 * HengYu Keyboard	
 */
/*
	public int init_USB_Device(){
		
		//Loader.load();
		
		int result = LibUsb.init(null);        
		if (result != LibUsb.SUCCESS){
            System.out.println("Unable to initialize libusb. Result=" + result);
			return -1;
        }
		
		DeviceList list = new DeviceList();
		
		result = LibUsb.getDeviceList(null, list);
		if(result < 0){
			System.out.println("Unable to get device list. Result=" + result);
			return -2;
		}
		try{
	        for (Device device: list){
	            DeviceDescriptor descriptor = new DeviceDescriptor();
	            result = LibUsb.getDeviceDescriptor(device, descriptor);
	            
	            if (result < 0){
	            	System.out.println("Unable to read device descriptor. Result=" + result);
	            	return -3;
	            }
	            
	            if ((descriptor.idVendor() == 0x0F39) || (descriptor.idVendor() == 0x058F) && (descriptor.idProduct() != 0x6254)) {
	            	System.out.println(String.format("Find device, VID=0x%04X,PID=0x%04X",descriptor.idVendor(), descriptor.idProduct()));
	                
	            	this.myDeviceHandle = LibUsb.openDeviceWithVidPid(null, descriptor.idVendor(), descriptor.idProduct());
	            	        	

	                // Check if kernel driver is attached to the interface
	                //int attached = LibUsb.kernelDriverActive(this.myDeviceHandle, 1);
	                

	               
	                    
	                // Claim interface
	                result = LibUsb.claimInterface(this.myDeviceHandle, 0);
	                if (result != LibUsb.SUCCESS){
	                   System.out.println("Unable to claim interface 0");
	                   return -6;	        				
	                }
	                
	                result = LibUsb.claimInterface(this.myDeviceHandle, 1);
	                if (result != LibUsb.SUCCESS){
	                   System.out.println("Unable to claim interface 1");
	                   return -6;	        				
	                }
	            	
	            	return 0;
	            }
	        }
	    }catch(LibUsbException ex){
		    LibUsb.close(this.myDeviceHandle);
			ex.printStackTrace();
		}
		
		return -7;
	}	
*/
	
	public void usb_Close(){ 	
		
		if(this.myDeviceHandle == null)
			return;
		
        try{
            // Release the interface            
			int result = LibUsb.releaseInterface(this.myDeviceHandle, 0);
            if (result != LibUsb.SUCCESS)
            {
			    System.out.println("Unable to release interface");
            }

            LibUsb.attachKernelDriver(this.myDeviceHandle, 0);
            if (result != LibUsb.SUCCESS){
            	System.out.println("Unable to re-attach kernel driver");
            }
		}catch(LibUsbException ex){
			ex.printStackTrace();
		}
     
        LibUsb.close(this.myDeviceHandle);
        LibUsb.exit(null);		
	}
	
	private byte cal_Checksum(byte[] ptr, int size) {
	    byte ret_val = 0x00;

	    for (int i = 0; i < size; i++)
		    ret_val ^= ptr[i];

	    return (ret_val);
    }
	

    /**
     * Sends a message to the missile launcher.
     * 
     * @param handle
     *            The USB device handle.
     * @param message
     *            The message to send.
     */
    public boolean send_Message(byte[] message, int len)
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(8);		
		int sent_len = 0;
		boolean end_flag = false;
		
		do{
		   buffer.clear();
		   buffer.rewind();
		   
		   buffer.put((byte)0x01);
		   if (len - sent_len <= 7){
              buffer.put(message, sent_len, len - sent_len);
			  end_flag = true;
		   }else{
              buffer.put(message, sent_len, 7);
			  sent_len += 7;
           }		   
		
           buffer.rewind();
		
           int transfered = LibUsb.controlTransfer(this.myDeviceHandle,
                                                  (byte) (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE),/*0x21*/
                                                  (byte) 0x09, (short) 0x0301, (short) 1, buffer, TIMEOUT);

           //System.out.printf(String.format("transfered = %d", transfered));
           
           if (transfered < 0)
               return false;			   
           if (transfered != buffer.capacity())
               return false;
			   
        }while(!end_flag);
		
		return true;
	}
	
	public int receive_Message(byte[] message)
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(8);	
        byte[] bRev = new byte[8];		
		boolean flag = false;
		int cnt = 0, ret = -1, i;
	
        do{		
		    buffer.clear();
		    buffer.rewind();
            
			LibUsb.controlTransfer(this.myDeviceHandle, (byte)0xA1,(byte)0x01, (short)0x0301, (short)1, buffer, TIMEOUT);
		   
		    if ( buffer.hasRemaining() == true){
			    buffer.get(bRev);			
		
				for(i = 0; i < bRev.length; i++){
				    if ((bRev[i] == RECV_HEADER) || (flag == true)){			
					    flag = true;
				   
					    message[cnt++] = bRev[i];
					
						if ((cnt == 2) && (message[1] < 2))//length of command must > 2
						   ret = 0;
												
						if (cnt == message[1] + 2){
                            if (this.cal_Checksum(message, message[1] + 1) == message[message[1] + 1]){							  

							   ret = 1;
                            }else
                               ret = 0;                             
							   
                        }							
					}
				}
				
				if ( ( i == bRev.length) && ( flag == false ) )				 
 				   break;
				
			}
			
			if (ret >= 0)
                break;	
			
		}while(true);

        return ret;		
    }


    /*
     * Sends a command to the missile launcher.
     * 
     * @param handle
     *            The USB device handle.
     * @param command
     *            The command to send.
	 * @param rev_data
	 *            received data, from header to checksum
	 * Return: 0:status
	 *        -1:fail
     */
    public int send_Command(byte[] command, int cmd_len, byte[] rev_data)
    {
        byte[] message = new byte[1024];
	
	    if ( command != null){	
            message[0] = SEND_HEADER;
			message[1] = (byte)(cmd_len + 1);
			for (int i = 0; i < cmd_len; i++)
				message[2 + i] =  command[i];
			message[cmd_len + 2] = cal_Checksum(message, cmd_len + 2);

			if ( this.send_Message(message, cmd_len + 3) == false)
				return (-1);
		
			Arrays.fill(message, (byte)0);
	    }	
	
	    if ( rev_data != null){
			if ( this.receive_Message(message) != 1)
				return (-1);
		   
			System.arraycopy(message, 0, rev_data, 0, message[1] + 2);
			
			/*for(int i = 0; i < message[1] + 2; i++){
				System.out.printf(String.format("%02X", rev_data[i]));
			}
			System.out.println("");*/
        }    
	
	    if ( (command != null) && (rev_data != null) )
	       return rev_data[3];	
		   
		return 0;		
    }
    
    /*
     * NCR keyboard => send command
     */
    public boolean Write_Command_Feature(byte[] command, int cmdlen){
    	ByteBuffer buffer = ByteBuffer.allocateDirect(this.hid_size);
        int transfered;

        buffer.clear();
		buffer.rewind();   
		buffer.put(command, 0, cmdlen); 
        buffer.rewind();
        /*
        System.out.println(String.format("0x%02X", buffer.get(0)));
	    System.out.println(String.format("0x%02X", buffer.get(1)));
	    System.out.println(String.format("0x%02X", buffer.get(2)));
	    System.out.println(String.format("0x%02X", buffer.get(3)));
	    System.out.println(String.format("buffer size = %d", buffer.capacity()));
    	*/	
        transfered = LibUsb.controlTransfer(this.myDeviceHandle, 
    								(byte) (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE),/*0x21*/
    								(byte)0x09, (short)0x300, (short)0, buffer, TIMEOUT);	

        //System.out.printf(String.format("transfered = %d", transfered));

        
    	/*if (transfered < 0)
            return false;			   
        if (transfered != buffer.capacity())
            return false;*/
    	
    	return true;
    }
    
    public boolean NCR_SendUsbCmd(byte[] command, int cmd_len, byte[] rev_data)
    {
    	ByteBuffer buffer = ByteBuffer.allocateDirect(this.hid_size);   
    	int ret;   	
    	
    	if(!Write_Command_Feature(command, cmd_len))
    		return false;

    	buffer.clear();
	    buffer.rewind();
	   
    	ret = LibUsb.controlTransfer(this.myDeviceHandle, 
									(byte)0xA1,
									(byte)0x01, (short)0x300, (short)0, buffer, TIMEOUT);	
    	if ( buffer.hasRemaining() == true){
		    buffer.get(rev_data);						
		}
    	
    	/*
    	System.out.println("Receive data:");
    	for(int i = 0; i < rev_data.length; i++){
			System.out.printf(String.format("%02X", rev_data[i]));
		}
		System.out.println("");*/
    		
    	return true;
    }
    
    /*
    *Send to keyboard: EA 02 69 CS
    *Keyboard to host:ED len 69 00 mode(1 byte) codes(N bytes) CS
    *
    *@param
    *	codes, [output], mode(1 byte) codes(N bytes)
    *	response_len, [output], the length of received data, (1 + N) bytes 
    */
    public int get_KeyLockStatus(byte codes[], byte response_len[])
    {
    	byte[] send_dat = new byte[1];
		byte[] rev_dat = new byte[64];
    	int ret;

    	send_dat[0] = (byte)0x69;
    	
    	if( (ret = this.send_Command(send_dat, 1, rev_dat)) == 0){
    		response_len[0] = (byte)(rev_dat[1] - 3);
 		    System.arraycopy(rev_dat, 4, codes, 0, rev_dat[1] - 3);	
 		}else{ 			
 			System.out.printf("Get keylock position fail\n");
        }    	

    	return ret;
    }
    
    public String get_Firmware(){
	    byte[] send_dat = new byte[1];
		byte[] rev_dat = new byte[20];
		byte[] ret_dat = new byte[20];
		String sRet;
		
		send_dat[0] = (byte)0xB0;
		
		if( this.send_Command(send_dat, 1, rev_dat) < 0){
           System.out.printf("Get firmware version fail\n");
		   sRet = "";
		}else{   
		   System.arraycopy(rev_dat, 4, ret_dat, 0, rev_dat[1] - 3);	  
		   sRet = new String(ret_dat); 
		   //System.out.println(sRet);	
        }			
		
		return sRet;
	}
    
    public String NCR_Get_FirmwareVersion(){
    	byte[] send_buf = new byte[4];
    	byte[] rev_buf = new byte[this.hid_size];
    	
    	send_buf[0] = (byte)0xA0;
    	send_buf[1] = (byte)0x56;
    	send_buf[2] = (byte)0x45;
    	send_buf[3] = (byte)0x52;
    	
    	if(NCR_SendUsbCmd(send_buf, 4, rev_buf)){
    		if(rev_buf[0] != (byte)0xA0){
    			return "";
    		}
    		
    		return new String(Arrays.copyOfRange(rev_buf, 1, 9));
    	}else
    		return "";
    }
}
