package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Locale;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.ObjectChangeListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import Command.HY_Command;
import USB.HY_USB;

/*
 * JMenuBar : https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
 * keyboard/mmouse listener http://blog.csdn.net/crpxnmmafq/article/details/71023654
 */
/*
 * 
 *  for(int i = 0; i<0x17;i++){
						  System.out.print(String.format(" %02X", keymap_buf[i]));
					  }
					  System.out.println("");
 */

public class HY_UI {
	
	private JFrame jframe = null;
	
	private final Color CLR_KEY_BG = new Color(193,193,193);
	private final Color CLR_KEY_MOUSE_IN = new Color(129,129,129);
	
	private final String N4375_PANEL = "N4375";// 64-key
	private final String N4374_PANEL = "N4374";// Compact Alpha
	
	public static final int KB_N4375 = 0;
	public static final int KB_N4374 = 1;
	
	private int selected_kb = KB_N4375, kb_hid_size = HY_USB.NCR_N4375_HID_SIZE;
	
	private byte[] keymap_buf = new byte[400];
	
	private void init_UI(){
		final JTabbedPane  jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		
		/*
	    //Check if the program is already running.
		{
		   ServerSocket serverTest = null;
		   try{
			   serverTest = new ServerSocket(); 
			   serverTest.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), TCP_Port));			    
		   }catch(BindException io){
			   io.printStackTrace(); 
			   JFrame frame = new JFrame();
			   frame.setVisible(true);
			   frame.setLocationRelativeTo(null);
			   JOptionPane.showMessageDialog(frame, new String("The program is already running."));
			   frame.dispose();

			   System.exit(0);
		   }catch(IOException e){
			   e.printStackTrace();
		   }finally{
			   try{
				   if(serverTest!=null)
					   serverTest.close();
				   System.out.println("Closed");
			   }catch(IOException e){
				   e.printStackTrace();
			   }
		   }
		}*/				
		
		
		Locale.setDefault(Locale.ENGLISH); 
		
        jframe = new JFrame("NCR Ver1.00");
        jframe.setSize(850, 500);
        jframe.setResizable(false);
		
		/*Toolkit tk= Toolkit.getDefaultToolkit();  
		Image img = tk.getImage("D:\\HY\\EclipseProject\\PriceChecker\\null.jpg");  
		jframe.setIconImage(img);*/
        
        JMenu jmFile = new JMenu("File");
        JMenuItem miOpenFile = new JMenuItem("Open");
        JMenuItem miSaveFile = new JMenuItem("Save");
        JMenuItem miExit = new JMenuItem("Exit");
        
        miOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });
        jmFile.add(miOpenFile);
        
        miSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });
        jmFile.add(miSaveFile);
        
        miExit.add(new JSeparator());
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	System.exit(0);
            }
        });
        jmFile.add(miExit);           
        
        JMenu jmKeyboard = new JMenu("Keyboard");
        JMenuItem miWriteAllToFlash = new JMenuItem("Write all to flash");        
        miWriteAllToFlash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	HY_Command hyCommand = new HY_Command(kb_hid_size);

				if(hyCommand.Write_CfgData_To_Flash()){
					JOptionPane.showMessageDialog(jframe, "Write all to flash successfully.");					  
				}else
					JOptionPane.showMessageDialog(jframe, "Fail to connect to USB keyboard.");
            }
        });
        jmKeyboard.add(miWriteAllToFlash);
        JMenuItem miKeyboardConfiguration = new JMenuItem("Keyboard Configuration");        
        miKeyboardConfiguration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if(selected_kb == KB_N4375)
            		N4375_Keyboard_Configuration();
            }
        });
        jmKeyboard.add(miKeyboardConfiguration);
        JMenuItem miRS232Control = new JMenuItem("RS232 Control");
        miRS232Control.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	
            }
        });
        jmKeyboard.add(miRS232Control);
        /*JMenuItem miSentinelTable = new JMenuItem("Sentinel Table");
        miSentinelTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	
            }
        });
        jmKeyboard.add(miSentinelTable);*/
        
        
        JMenu jmDiagnostic = new JMenu("Diagnostic");
        JMenuItem miGetVersion = new JMenuItem("Get firmware version");        
        miGetVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	HY_USB hyUSB = new HY_USB();
            	
            	int init_usb = hyUSB.init_USB_Device(HY_USB.NCR_N4375_HID_SIZE); 
            	if(init_usb == 0){
            		JOptionPane.showMessageDialog(jframe, hyUSB.NCR_Get_FirmwareVersion());
            	}else
            		JOptionPane.showMessageDialog(jframe, "USB init ret=" + init_usb);
            	
            	hyUSB.usb_Close();
            }
        });
        jmDiagnostic.add(miGetVersion);   
               
        
        JMenuBar  jMenuBar = new  JMenuBar();        
        jMenuBar.add(jmFile);
        jMenuBar.add(jmKeyboard);
        jMenuBar.add(jmDiagnostic);
        jframe.setJMenuBar(jMenuBar);          
          
        jTabbedPane.add("64-Key", init_N4375());
        jTabbedPane.add("Compact Alpha", init_N4374());
        jTabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				selected_kb = jTabbedPane.getSelectedIndex();
				if(selected_kb == KB_N4375)
					kb_hid_size = HY_USB.NCR_N4375_HID_SIZE;
				else if(selected_kb == KB_N4374)
					kb_hid_size = HY_USB.NCR_N4374_HID_SIZE;
			}
        	
        });
        jframe.add(jTabbedPane, BorderLayout.CENTER);
        
/*        
        final JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem(new AbstractAction("Option 1") {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jframe, "Option 1 selected");
            }
        }));
        popup.add(new JMenuItem(new AbstractAction("Option 2") {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jframe, "Option 2 selected");
            }
        }));
*/             
        jframe.setVisible(true);
		jframe.setLocationRelativeTo(null);
		
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}
	
	/*
	 * Return: 0:success
	 * 		   -1:can't find configuration file
	 * 		   -2:wrong configuration file
	 * 		   -3:configuration file not match the selected keyboard
	 */
	private int load_CFG_File(String cfgFilePath){
		int iRet = 0;

		try{
			File f = new File(cfgFilePath);  
			if(!f.exists()){ 
				iRet = -1;
				throw new FileNotFoundException(cfgFilePath);  
			}
			
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));  

			byte[] tmp_buf = new byte[keymap_buf.length + 1];
			
            if( in.read(tmp_buf, 0, tmp_buf.length) == tmp_buf.length){   
            	//System.out.println("tmp_buf[0] =" + tmp_buf[0]);
            	if( ((selected_kb == KB_N4375) && (tmp_buf[0] == 0x02)) ||
            		((selected_kb == KB_N4374) && (tmp_buf[0] == 0x01)) )
            	{
            		keymap_buf = Arrays.copyOfRange(tmp_buf, 1, tmp_buf.length);
            	}else{
           			iRet = -3;
            	}
            }else{
            	iRet = -2;
            }

		}catch(FileNotFoundException fx){
			fx.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		return iRet;
	}
	
	/*
	private void N4375_Cfg_UI_Setting(){
		
		//Load default configuration file
		if(load_CFG_File("pics/NCR_64.dat") != 0)
			return;
		
		
	}*/
	
	private void UI_SetKeyCode(String key_index) {
		final JFrame jfKeycode = new JFrame(key_index);
		
		jframe.setEnabled(false);

		jfKeycode.setSize(700, 500);
		jfKeycode.setLayout(null);
		
		jfKeycode.setVisible(true);
		jfKeycode.setLocationRelativeTo(null);
		jfKeycode.setAlwaysOnTop(true);		
		jfKeycode.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				jframe.setEnabled(true);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				//JList jList
			}
			
		});
		
		AbstractTableSelectionMappingSequence atsMappingSequence = new AbstractTableSelectionMappingSequence();
		JTable tableMappingSequence = new JTable(atsMappingSequence);
		tableMappingSequence.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableMappingSequence.setShowHorizontalLines(false);
		tableMappingSequence.setShowVerticalLines(false);
		tableMappingSequence.setFont(new Font("Menu.font", Font.PLAIN, 20));
		tableMappingSequence.setRowHeight(20);
		tableMappingSequence.getColumnModel().getColumn(0).setPreferredWidth(30);
		tableMappingSequence.getColumnModel().getColumn(1).setPreferredWidth(150);
		tableMappingSequence.getColumnModel().getColumn(2).setPreferredWidth(60);
		JScrollPane jspTableMappingSequence = new JScrollPane(tableMappingSequence, 
															  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  
                											  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
		jspTableMappingSequence.setBounds(10, 10, 260, 200);
		jfKeycode.add(jspTableMappingSequence);
		
		AbstractTableSelectionKeyCode atsKeyCode = new AbstractTableSelectionKeyCode();
		JTable tableKeyCode = new JTable(atsKeyCode);
		tableKeyCode.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableKeyCode.setShowHorizontalLines(false);
		tableKeyCode.setShowVerticalLines(false);
		tableKeyCode.setFont(new Font("Menu.font", Font.PLAIN, 18));
		tableKeyCode.setRowHeight(20);
		tableKeyCode.getColumnModel().getColumn(0).setPreferredWidth(190);
		tableKeyCode.getColumnModel().getColumn(1).setPreferredWidth(50);
		tableKeyCode.addMouseListener(new MouseAdapter(){ 
			public void mouseClicked(MouseEvent e){ 
				if(e.getClickCount() == 2){
					tableMappingSequence.setValueAt("1", 0, 0);
					tableMappingSequence.setValueAt(tableKeyCode.getValueAt(tableKeyCode.getSelectedRow(), 0), 0, 1);
					tableMappingSequence.setValueAt(tableKeyCode.getValueAt(tableKeyCode.getSelectedRow(), 1), 0, 2);
				}
			}
		});
		JScrollPane jspTableKeyCode = new JScrollPane(tableKeyCode, 
													  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  
                									  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
		jspTableKeyCode.setBounds(280, 10, 260, 200);
		jfKeycode.add(jspTableKeyCode);
		
		JButton bntOK = new JButton("OK");
		bntOK.setBounds(570, 20, 80, 30);
		bntOK.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  System.out.println("OK");
			  }
		});
		jfKeycode.add(bntOK);
		
		JButton bntCancel = new JButton("Cancel");
		bntCancel.setBounds(570, 60, 80, 30);
		bntCancel.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
					jfKeycode.dispose();
					
					jframe.setEnabled(true);
					jframe.requestFocus();
			  }
		});
		jfKeycode.add(bntCancel);
		
		/*
		DefaultListModel<String> lm = new DefaultListModel<String>();		
		lm.addElement("123");
		lm.addElement("abc");
		lm.addElement("789");
		
		JList jList = new JList(lm);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println(jList.getSelectedIndex());
			}
			
		});
		jfKeycode.add(jList);*/
	
	}
	
	
	private JRadioButton jbSolicited,jbUnsolicited,jbCalculator,jbTelephone,jbDoubleKeyEnable,
						 jbDoubleKeyDisable,jbSpeakerSoundEnable,jbSpeakerSoundDisable,
						 jbKeyLockLUnlock,jbKeyLockLLock,jbCADprotectionEnable,jbCADprotectionDisable,
						 jbKeyLockDataModeHID,jbKeyLockDataModeEmulation,
						 jbMSRDataModeHID,jbMSRDataModeEmulation;
	private JTextField tfKeylockVolume,tfErrorToneVolume,tfKeyclickFre,tfErrorToneFre,
					   tfKeyclickDuration,tfErrorToneDuration;
	
	private JPanel N4375_KB_Cfg_Page1() {
		int size_height = 20;
		JPanel jp = new JPanel();
		
		jp.setLayout(null);
		jp.setName("Page 1");
		
		//--------------------------------------------------------------------
		JLabel labKeylockDataMode = new JLabel("Keylock data mode");
		labKeylockDataMode.setBounds(10, 10, 200, size_height);
		jp.add(labKeylockDataMode);
		
		jbSolicited = new JRadioButton("Solicited");
		jbSolicited.setBounds(250, 10, 100, size_height);		
		jp.add(jbSolicited);
	    jbUnsolicited = new JRadioButton("Unsolicited");
	    jbUnsolicited.setBounds(350, 10, 150, size_height);
	    jp.add(jbUnsolicited);
	    ButtonGroup jbgKeylock = new ButtonGroup();	    
	    jbgKeylock.add(jbSolicited);
	    jbgKeylock.add(jbUnsolicited);
	    ActionListener alKeylock = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbSolicited) {
                	keymap_buf[0] &= 0xFE;
                } else if (radio == jbUnsolicited) {
                	keymap_buf[0] |= 0x01;
                } 
            }
        };
        jbSolicited.addActionListener(alKeylock);
        jbUnsolicited.addActionListener(alKeylock);
        
        
        //--------------------------------------------------------------------
        JLabel labNumericKeypadLayout = new JLabel("Numeric keypad layout");
        labNumericKeypadLayout.setBounds(10, 10 + size_height, 200, size_height);
		jp.add(labNumericKeypadLayout);
        
        jbCalculator = new JRadioButton("Calculator");
        jbCalculator.setBounds(250, 10 + size_height, 100, size_height);		
		jp.add(jbCalculator);
	    jbTelephone = new JRadioButton("Telephone");
	    jbTelephone.setBounds(350, 10 + size_height, 100, size_height);
	    jp.add(jbTelephone);
	    ButtonGroup jbgNumeric = new ButtonGroup();	    
	    jbgNumeric.add(jbCalculator);
	    jbgNumeric.add(jbTelephone);
	    ActionListener alNumeric = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbCalculator) {
                	keymap_buf[0x11] &= 0xFE;
                } else if (radio == jbTelephone) {
                	keymap_buf[0x11] |= 0x01;
                }
            }
        };
        jbCalculator.addActionListener(alNumeric);
        jbTelephone.addActionListener(alNumeric);                 
        
        //--------------------------------------------------------------------
        JLabel labDoubleKeyErrorDetection = new JLabel("Double key error detection");
        labDoubleKeyErrorDetection.setBounds(10, 10 + 2*size_height, 200, size_height);
		jp.add(labDoubleKeyErrorDetection);
        
        jbDoubleKeyEnable = new JRadioButton("Enable");
        jbDoubleKeyEnable.setBounds(250, 10 + 2*size_height, 100, size_height);		
		jp.add(jbDoubleKeyEnable);
	    jbDoubleKeyDisable = new JRadioButton("Disable");
	    jbDoubleKeyDisable.setBounds(350, 10 + 2*size_height, 100, size_height);
	    jp.add(jbDoubleKeyDisable);
	    ButtonGroup jbgDoubleKey = new ButtonGroup();	    
	    jbgDoubleKey.add(jbDoubleKeyEnable);
	    jbgDoubleKey.add(jbDoubleKeyDisable);
	    ActionListener alDoubleKey = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbDoubleKeyDisable) {
                	keymap_buf[0x11] |= 0x08;
                } else if (radio == jbDoubleKeyEnable) {
                	keymap_buf[0x11] &= 0xF7;
                }
            }
        };
        jbDoubleKeyEnable.addActionListener(alDoubleKey);
        jbDoubleKeyDisable.addActionListener(alDoubleKey);        
	    
        //--------------------------------------------------------------------
        JLabel labSpeakerSound = new JLabel("Speaker Sound");
        labSpeakerSound.setBounds(10, 10 + 3*size_height, 200, size_height);
		jp.add(labSpeakerSound);
        
        jbSpeakerSoundEnable = new JRadioButton("Enable");
        jbSpeakerSoundEnable.setBounds(250, 10 + 3*size_height, 100, size_height);		
		jp.add(jbSpeakerSoundEnable);
	    jbSpeakerSoundDisable = new JRadioButton("Disable");
	    jbSpeakerSoundDisable.setBounds(350, 10 + 3*size_height, 100, size_height);
	    jp.add(jbSpeakerSoundDisable);
	    ButtonGroup jbgSpeakerSound = new ButtonGroup();	    
	    jbgSpeakerSound.add(jbSpeakerSoundEnable);
	    jbgSpeakerSound.add(jbSpeakerSoundDisable);
	    ActionListener alSpeakerSound = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbSpeakerSoundDisable) {
                	keymap_buf[0x01] &= 0x7F;
                } else if (radio == jbSpeakerSoundEnable) {
                	keymap_buf[0x01] |= 0x80;
                }
            }
        };
        jbSpeakerSoundEnable.addActionListener(alSpeakerSound);
        jbSpeakerSoundDisable.addActionListener(alSpeakerSound);       
        
        //--------------------------------------------------------------------
        JLabel labKeyLockL = new JLabel("KBD lock on Keylock 'L' Position");
        labKeyLockL.setBounds(10, 10 + 4*size_height, 250, size_height);
		jp.add(labKeyLockL);
		
        jbKeyLockLUnlock = new JRadioButton("Unlock");
        jbKeyLockLUnlock.setBounds(250, 10 + 4*size_height, 100, size_height);		
		jp.add(jbKeyLockLUnlock);
	    jbKeyLockLLock = new JRadioButton("Lock");
	    jbKeyLockLLock.setBounds(350, 10 + 4*size_height, 100, size_height);
	    jp.add(jbKeyLockLLock);
	    ButtonGroup jbgKeyLockL = new ButtonGroup();	    
	    jbgKeyLockL.add(jbKeyLockLUnlock);
	    jbgKeyLockL.add(jbKeyLockLLock);
	    ActionListener alKeyLockL = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbKeyLockLUnlock) {
                	keymap_buf[0x01] &= 0xFD;
                } else if (radio == jbKeyLockLLock) {
                	keymap_buf[0x01] |= 0x02;
                }
            }
        };
        jbKeyLockLUnlock.addActionListener(alKeyLockL);
        jbKeyLockLLock.addActionListener(alKeyLockL);
      
      //--------------------------------------------------------------------
        JLabel labCADprotection = new JLabel("Ctrl,Alt,Del protection");
        labCADprotection.setBounds(10, 10 + 5*size_height, 200, size_height);
		jp.add(labCADprotection);
		
        jbCADprotectionEnable = new JRadioButton("Enable");
        jbCADprotectionEnable.setBounds(250, 10 + 5*size_height, 100, size_height);		
		jp.add(jbCADprotectionEnable);
	    jbCADprotectionDisable = new JRadioButton("Disable");
	    jbCADprotectionDisable.setBounds(350, 10 + 5*size_height, 100, size_height);
	    jp.add(jbCADprotectionDisable);
	    ButtonGroup jbgCADprotection = new ButtonGroup();	    
	    jbgCADprotection.add(jbCADprotectionEnable);
	    jbgCADprotection.add(jbCADprotectionDisable);
	    ActionListener alCADprotection = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbCADprotectionEnable) {
                	keymap_buf[0x10] |= 0x02;
                } else if (radio == jbCADprotectionDisable) {
                	keymap_buf[0x10] &= 0xFD;
                }
            }
        };
        jbCADprotectionEnable.addActionListener(alCADprotection);
        jbCADprotectionDisable.addActionListener(alCADprotection);
        
      //--------------------------------------------------------------------
        JLabel labKeyLockDataMode = new JLabel("Keylock data mode");
        labKeyLockDataMode.setBounds(10, 10 + 6*size_height, 200, size_height);
		jp.add(labKeyLockDataMode);
		
        jbKeyLockDataModeHID = new JRadioButton("HID");
        jbKeyLockDataModeHID.setBounds(250, 10 + 6*size_height, 100, size_height);		
		jp.add(jbKeyLockDataModeHID);
	    jbKeyLockDataModeEmulation = new JRadioButton("Emulation");
	    jbKeyLockDataModeEmulation.setBounds(350, 10 + 6*size_height, 100, size_height);
	    jp.add(jbKeyLockDataModeEmulation);
	    ButtonGroup jbgKeyLockDataMode = new ButtonGroup();	    
	    jbgKeyLockDataMode.add(jbKeyLockDataModeHID);
	    jbgKeyLockDataMode.add(jbKeyLockDataModeEmulation);
	    ActionListener alKeyLockDataMode = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbKeyLockDataModeHID) {
                	keymap_buf[0x10] &= 0xF7;
                } else if (radio == jbKeyLockDataModeEmulation) {
                	keymap_buf[0x10] |= 0x08;
                }
            }
        };
        jbKeyLockDataModeHID.addActionListener(alKeyLockDataMode);
        jbKeyLockDataModeEmulation.addActionListener(alKeyLockDataMode);
   
      //--------------------------------------------------------------------
        JLabel labMSRDataMode = new JLabel("MSR data mode");
        labMSRDataMode.setBounds(10, 10 + 7*size_height, 200, size_height);
		jp.add(labMSRDataMode);
		
        jbMSRDataModeHID = new JRadioButton("HID");
        jbMSRDataModeHID.setBounds(250, 10 + 7*size_height, 100, size_height);		
		jp.add(jbMSRDataModeHID);
	    jbMSRDataModeEmulation = new JRadioButton("Emulation");
	    jbMSRDataModeEmulation.setBounds(350, 10 + 7*size_height, 100, size_height);
	    jp.add(jbMSRDataModeEmulation);
	    ButtonGroup jbgMSRDataMode = new ButtonGroup();	    
	    jbgMSRDataMode.add(jbMSRDataModeHID);
	    jbgMSRDataMode.add(jbMSRDataModeEmulation);
	    ActionListener alMSRDataMode = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRDataModeHID) {
                	keymap_buf[0x10] &= 0xEF;
                } else if (radio == jbMSRDataModeEmulation) {
                	keymap_buf[0x10] |= 0x10;
                }
            }
        };
        jbMSRDataModeHID.addActionListener(alMSRDataMode);
        jbMSRDataModeEmulation.addActionListener(alMSRDataMode);
    
      //--------------------------------------------------------------------
        JLabel labKeyClickVolume = new JLabel("Keyclick volume");
        labKeyClickVolume.setBounds(10, 30 + 8*size_height, 200, size_height - 5);
		jp.add(labKeyClickVolume);
		
		tfKeylockVolume =  new JTextField();
		tfKeylockVolume.setBounds(180, 30 + 8*size_height, 50, size_height - 5);
		LimitedDocument ldKeylockVolume = new LimitedDocument(2);
		ldKeylockVolume.setAllowChar("0123456789");
		tfKeylockVolume.setDocument(ldKeylockVolume);		
		jp.add(tfKeylockVolume);
        
		JLabel labKeylockVolumeValue = new JLabel("(0 ~ 15)");
		labKeylockVolumeValue.setBounds(240, 30 + 8*size_height, 200, size_height - 5);
		jp.add(labKeylockVolumeValue); 
		
		//--------------------------------------------------------------------
        JLabel labErrorToneVolume = new JLabel("Error tone volume");
        labErrorToneVolume.setBounds(10, 30 + 9*size_height, 200, size_height - 5);
		jp.add(labErrorToneVolume);
		
		tfErrorToneVolume =  new JTextField();
		tfErrorToneVolume.setBounds(180, 30 + 9*size_height, 50, size_height - 5);
		LimitedDocument ldErrorToneVolume = new LimitedDocument(2);
		ldErrorToneVolume.setAllowChar("0123456789");
		tfErrorToneVolume.setDocument(ldErrorToneVolume);
		jp.add(tfErrorToneVolume);
        
		JLabel labErrorToneVolumeValue = new JLabel("(0 ~ 15)");
		labErrorToneVolumeValue.setBounds(240, 30 + 9*size_height, 200, size_height - 5);
		jp.add(labErrorToneVolumeValue);
        
		//--------------------------------------------------------------------
        JLabel labKeyclickFre = new JLabel("Keyclick frequency");
        labKeyclickFre.setBounds(10, 30 + 10*size_height, 200, size_height - 5);
		jp.add(labKeyclickFre);
		
		tfKeyclickFre =  new JTextField();
		tfKeyclickFre.setBounds(180, 30 + 10*size_height, 50, size_height - 5);
		LimitedDocument ldKeyclickFre = new LimitedDocument(3);
		ldKeyclickFre.setAllowChar("0123456789");
		tfKeyclickFre.setDocument(ldKeyclickFre);
		jp.add(tfKeyclickFre);
        
		JLabel labKeyclickFreValue = new JLabel("(0 ~ 127)");
		labKeyclickFreValue.setBounds(240, 30 + 10*size_height, 200, size_height - 5);
		jp.add(labKeyclickFreValue);
		
		//--------------------------------------------------------------------
        JLabel labErrorToneFre = new JLabel("Error tone frequency");
        labErrorToneFre.setBounds(10, 30 + 11*size_height, 200, size_height - 5);
		jp.add(labErrorToneFre);
		
		tfErrorToneFre =  new JTextField();
		tfErrorToneFre.setBounds(180, 30 + 11*size_height, 50, size_height - 5);
		LimitedDocument ldErrorToneFre = new LimitedDocument(3);
		ldErrorToneFre.setAllowChar("0123456789");
		tfErrorToneFre.setDocument(ldErrorToneFre);
		jp.add(tfErrorToneFre);
        
		JLabel labErrorToneFreValue = new JLabel("(0 ~ 127)");
		labErrorToneFreValue.setBounds(240, 30 + 11*size_height, 200, size_height - 5);
		jp.add(labErrorToneFreValue);
		
		//--------------------------------------------------------------------
        JLabel labKeyclickDuration = new JLabel("Keyclick duration");
        labKeyclickDuration.setBounds(10, 30 + 12*size_height, 200, size_height - 5);
		jp.add(labKeyclickDuration);
		
		tfKeyclickDuration =  new JTextField();
		tfKeyclickDuration.setBounds(180, 30 + 12*size_height, 50, size_height - 5);
		LimitedDocument ldKeyclickDuration = new LimitedDocument(3);
		ldKeyclickDuration.setAllowChar("0123456789");
		tfKeyclickDuration.setDocument(ldKeyclickDuration);
		jp.add(tfKeyclickDuration);
        
		JLabel labKeyclickDurationValue = new JLabel("(0 ~ 255)");
		labKeyclickDurationValue.setBounds(240, 30 + 12*size_height, 200, size_height - 5);
		jp.add(labKeyclickDurationValue);
		
		//--------------------------------------------------------------------
        JLabel labErrorToneDuration = new JLabel("Error tone duration");
        labErrorToneDuration.setBounds(10, 30 + 13*size_height, 200, size_height - 5);
		jp.add(labErrorToneDuration);
		
		tfErrorToneDuration =  new JTextField();
		tfErrorToneDuration.setBounds(180, 30 + 13*size_height, 50, size_height - 5);
		LimitedDocument ldErrorToneDuration = new LimitedDocument(3);
		ldErrorToneDuration.setAllowChar("0123456789");
		tfErrorToneDuration.setDocument(ldErrorToneDuration);
		jp.add(tfErrorToneDuration);
        
		JLabel labErrorToneDurationValue = new JLabel("(0 ~ 255)");
		labErrorToneDurationValue.setBounds(240, 30 + 13*size_height, 200, size_height - 5);
		jp.add(labErrorToneDurationValue);		
		
		return jp;
	}
	
	private JRadioButton jbMSRT1ESEnable, jbMSRT1ESDisable, jbMSRT2ESEnable, jbMSRT2ESDisable,
						 jbMSRT3ESEnable, jbMSRT3ESDisable, jbMSRRawModeDeactived, jbMSRRawModeActived,
						 jbMSRT1JISDeactived, jbMSRT1JISActived, jbMSRT1Disable, jbMSRT1Enable,
						 jbMSRT2Disable, jbMSRT2Enable, jbMSRT3Disable, jbMSRT3Enable,
						 jbMSRT3PEnable, jbMSRT3PDisable;
	
	private JTextField tfMSRT3SS, tfMSRT3ES, tfMSRT3PL, tfMSRT3SV;
	
	private JPanel N4375_KB_Cfg_Page2() {
		int size_height = 20;
		JPanel jp = new JPanel();
		
		jp.setLayout(null);
		jp.setName("Page 2");
		
		//--------------------------------------------------------------------
		JLabel labMSRT1ES = new JLabel("MSR track 1 end sentinel always sent");
		labMSRT1ES.setBounds(10, 10, 300, size_height);
		jp.add(labMSRT1ES);
		
		jbMSRT1ESDisable = new JRadioButton("Disable");
		jbMSRT1ESDisable.setBounds(310, 10, 100, size_height);		
		jp.add(jbMSRT1ESDisable);
	    jbMSRT1ESEnable = new JRadioButton("Enable");
	    jbMSRT1ESEnable.setBounds(410, 10, 100, size_height);
	    jp.add(jbMSRT1ESEnable);
	    ButtonGroup jbgMSRT1ES = new ButtonGroup();	    
	    jbgMSRT1ES.add(jbMSRT1ESDisable);
	    jbgMSRT1ES.add(jbMSRT1ESEnable);
	    ActionListener alMSRT1ES = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRT1ESDisable) {
                	keymap_buf[0x10] &= 0xDF;
                } else if (radio == jbMSRT1ESEnable) {
                	keymap_buf[0x10] |= 0x20;
                } 
            }
        };
        jbMSRT1ESDisable.addActionListener(alMSRT1ES);
        jbMSRT1ESEnable.addActionListener(alMSRT1ES);
        
        
        //--------------------------------------------------------------------
        JLabel labMSRT2ES = new JLabel("MSR track 2 end sentinel always sent");
        labMSRT2ES.setBounds(10, 10 + size_height, 300, size_height);
		jp.add(labMSRT2ES);
        
		jbMSRT2ESDisable = new JRadioButton("Disable");
		jbMSRT2ESDisable.setBounds(310, 10 + size_height, 100, size_height);		
		jp.add(jbMSRT2ESDisable);
	    jbMSRT2ESEnable = new JRadioButton("Enable");
	    jbMSRT2ESEnable.setBounds(410, 10 + size_height, 100, size_height);
	    jp.add(jbMSRT2ESEnable);
	    ButtonGroup jbgMSRT2ES = new ButtonGroup();	    
	    jbgMSRT2ES.add(jbMSRT2ESDisable);
	    jbgMSRT2ES.add(jbMSRT2ESEnable);
	    ActionListener alMSRT2ES = new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRT2ESDisable) {
                	keymap_buf[0x10] &= 0xBF;
                } else if (radio == jbMSRT2ESEnable) {
                	keymap_buf[0x10] |= 0x40;
                } 
            }
        };
        jbMSRT2ESDisable.addActionListener(alMSRT2ES);
        jbMSRT2ESEnable.addActionListener(alMSRT2ES);                 
        
        //--------------------------------------------------------------------
        JLabel labMSRT3ESSent = new JLabel("MSR track 3 end sentinel always sent");
        labMSRT3ESSent.setBounds(10, 10 + 2*size_height, 300, size_height);
		jp.add(labMSRT3ESSent);
        
		jbMSRT3ESDisable = new JRadioButton("Disable");
		jbMSRT3ESDisable.setBounds(310, 10 + 2*size_height, 100, size_height);		
		jp.add(jbMSRT3ESDisable);
	    jbMSRT3ESEnable = new JRadioButton("Enable");
	    jbMSRT3ESEnable.setBounds(410, 10 + 2*size_height, 100, size_height);
	    jp.add(jbMSRT3ESEnable);
	    ButtonGroup jbgMSRT3ES = new ButtonGroup();	    
	    jbgMSRT3ES.add(jbMSRT3ESDisable);
	    jbgMSRT3ES.add(jbMSRT3ESEnable);
	    ActionListener alMSRT3ES = new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRT3ESDisable) {
                	keymap_buf[0x10] &= 0x7F;
                } else if (radio == jbMSRT3ESEnable) {
                	keymap_buf[0x10] |= 0x80;
                } 
            }
        };
        jbMSRT3ESDisable.addActionListener(alMSRT3ES);
        jbMSRT3ESEnable.addActionListener(alMSRT3ES);        
	    
//??
        //--------------------------------------------------------------------
        JLabel labMSRRawMode = new JLabel("MSR raw mode(do not check this)");
        labMSRRawMode.setBounds(10, 10 + 3*size_height, 300, size_height);
		jp.add(labMSRRawMode);
        
        jbMSRRawModeDeactived = new JRadioButton("Deactived");
        jbMSRRawModeDeactived.setBounds(310, 10 + 3*size_height, 100, size_height);	 
      jbMSRRawModeDeactived.setSelected(true); 
		jp.add(jbMSRRawModeDeactived);
	    jbMSRRawModeActived = new JRadioButton("Actived");
	    jbMSRRawModeActived.setBounds(410, 10 + 3*size_height, 100, size_height);
	    jp.add(jbMSRRawModeActived);
	    ButtonGroup jbgMSRRawMode = new ButtonGroup();	    
	    jbgMSRRawMode.add(jbMSRRawModeDeactived);
	    jbgMSRRawMode.add(jbMSRRawModeActived);
	    ActionListener alMSRRawMode = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRRawModeDeactived) {
                	//??//keymap_buf[0x01] &= 0x7F;
                } else if (radio == jbMSRRawModeActived) {
                	//??//keymap_buf[0x01] |= 0x80;
                }
            }
        };
        jbMSRRawModeDeactived.addActionListener(alMSRRawMode);
        jbMSRRawModeActived.addActionListener(alMSRRawMode);      
        
        //--------------------------------------------------------------------
        JLabel labMSRT1JIS = new JLabel("MSR track 1 select to JIS");
        labMSRT1JIS.setBounds(10, 10 + 4*size_height, 300, size_height);
		jp.add(labMSRT1JIS);
		
        jbMSRT1JISDeactived = new JRadioButton("Deactived");
        jbMSRT1JISDeactived.setBounds(310, 10 + 4*size_height, 100, size_height);		
		jp.add(jbMSRT1JISDeactived);
	    jbMSRT1JISActived = new JRadioButton("Actived");
	    jbMSRT1JISActived.setBounds(410, 10 + 4*size_height, 100, size_height);
	    jp.add(jbMSRT1JISActived);
	    ButtonGroup jbgMSRT1JIS = new ButtonGroup();	    
	    jbgMSRT1JIS.add(jbMSRT1JISDeactived);
	    jbgMSRT1JIS.add(jbMSRT1JISActived);
	    ActionListener alKeyLockL = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRT1JISDeactived) {
                	keymap_buf[0] &= 0xEF;
                } else if (radio == jbMSRT1JISActived) {
                	keymap_buf[0] |= 0x10;
                }
            }
        };
        jbMSRT1JISDeactived.addActionListener(alKeyLockL);
        jbMSRT1JISActived.addActionListener(alKeyLockL);
      
      //--------------------------------------------------------------------
        JLabel labMSRT1 = new JLabel("MSR track 1");
        labMSRT1.setBounds(10, 10 + 5*size_height, 300, size_height);
		jp.add(labMSRT1);
		
        jbMSRT1Disable = new JRadioButton("Disable");
        jbMSRT1Disable.setBounds(310, 10 + 5*size_height, 100, size_height);		
		jp.add(jbMSRT1Disable);
	    jbMSRT1Enable = new JRadioButton("Enabel");
	    jbMSRT1Enable.setBounds(410, 10 + 5*size_height, 100, size_height);
	    jp.add(jbMSRT1Enable);
	    ButtonGroup jbgMSRT1 = new ButtonGroup();	    
	    jbgMSRT1.add(jbMSRT1Disable);
	    jbgMSRT1.add(jbMSRT1Enable);
	    ActionListener alMSRT1 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRT1Disable) {
                	keymap_buf[0] &= 0xDF;
                } else if (radio == jbMSRT1Enable) {
                	keymap_buf[0] |= 0x20;
                }
            }
        };
        jbMSRT1Disable.addActionListener(alMSRT1);
        jbMSRT1Enable.addActionListener(alMSRT1);
        
      //--------------------------------------------------------------------
        JLabel labMSRT2 = new JLabel("MSR track 2");
        labMSRT2.setBounds(10, 10 + 6*size_height, 300, size_height);
		jp.add(labMSRT2);
		
        jbMSRT2Disable = new JRadioButton("Disable");
        jbMSRT2Disable.setBounds(310, 10 + 6*size_height, 100, size_height);		
		jp.add(jbMSRT2Disable);
	    jbMSRT2Enable = new JRadioButton("Enabel");
	    jbMSRT2Enable.setBounds(410, 10 + 6*size_height, 100, size_height);
	    jp.add(jbMSRT2Enable);
	    ButtonGroup jbgMSRT2 = new ButtonGroup();	    
	    jbgMSRT2.add(jbMSRT2Disable);
	    jbgMSRT2.add(jbMSRT2Enable);
	    ActionListener alMSRT2 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRT2Disable) {
                	keymap_buf[0] &= 0xBF;
                } else if (radio == jbMSRT2Enable) {
                	keymap_buf[0] |= 0x40;
                }
            }
        };
        jbMSRT2Disable.addActionListener(alMSRT2);
        jbMSRT2Enable.addActionListener(alMSRT2);
   
      //--------------------------------------------------------------------
        JLabel labMSRT3 = new JLabel("MSR track 3");
        labMSRT3.setBounds(10, 10 + 7*size_height, 300, size_height);
		jp.add(labMSRT3);
		
        jbMSRT3Disable = new JRadioButton("Disable");
        jbMSRT3Disable.setBounds(310, 10 + 7*size_height, 100, size_height);		
		jp.add(jbMSRT3Disable);
	    jbMSRT3Enable = new JRadioButton("Enabel");
	    jbMSRT3Enable.setBounds(410, 10 + 7*size_height, 100, size_height);
	    jp.add(jbMSRT3Enable);
	    ButtonGroup jbgMSRT3 = new ButtonGroup();	    
	    jbgMSRT3.add(jbMSRT3Disable);
	    jbgMSRT3.add(jbMSRT3Enable);
	    ActionListener alMSRT3 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRT3Disable) {
                	keymap_buf[0] &= 0x7F;
                } else if (radio == jbMSRT3Enable) {
                	keymap_buf[0] |= 0x80;
                }
            }
        };
        jbMSRT3Disable.addActionListener(alMSRT3);
        jbMSRT3Enable.addActionListener(alMSRT3);
    
      //--------------------------------------------------------------------
        JLabel labMSRT3SS = new JLabel("MSR track 3 start sentinel");
        labMSRT3SS.setBounds(10, 30 + 8*size_height, 300, size_height - 5);
		jp.add(labMSRT3SS);
		
		tfMSRT3SS =  new JTextField();
		tfMSRT3SS.setBounds(240, 30 + 8*size_height, 50, size_height - 5);
		LimitedDocument ldMSRT3SS = new LimitedDocument(3);
		ldMSRT3SS.setAllowChar("0123456789");
		tfMSRT3SS.setDocument(ldMSRT3SS);		
		jp.add(tfMSRT3SS);
        
		JLabel labMSRT3SSValue = new JLabel("(0 ~ 255)");
		labMSRT3SSValue.setBounds(300, 30 + 8*size_height, 200, size_height - 5);
		jp.add(labMSRT3SSValue); 
		
		//--------------------------------------------------------------------
		JLabel labMSRT3ES = new JLabel("MSR track 3 end sentinel");
		labMSRT3ES.setBounds(10, 30 + 9*size_height, 200, size_height - 5);
		jp.add(labMSRT3ES);
		
		tfMSRT3ES =  new JTextField();
		tfMSRT3ES.setBounds(240, 30 + 9*size_height, 50, size_height - 5);
		LimitedDocument ldMSRT3ES = new LimitedDocument(3);
		ldMSRT3ES.setAllowChar("0123456789");
		tfMSRT3ES.setDocument(ldMSRT3ES);		
		jp.add(tfMSRT3ES);
        
		JLabel labMSRT3ESValue = new JLabel("(0 ~ 255)");
		labMSRT3ESValue.setBounds(300, 30 + 9*size_height, 200, size_height - 5);
		jp.add(labMSRT3ESValue); 
        
		//--------------------------------------------------------------------
		JLabel labMSRT3PL = new JLabel("MSR track 3 pad length");
		labMSRT3PL.setBounds(10, 30 + 10*size_height, 200, size_height - 5);
		jp.add(labMSRT3PL);
		
		tfMSRT3PL =  new JTextField();
		tfMSRT3PL.setBounds(240, 30 + 10*size_height, 50, size_height - 5);
		LimitedDocument ldMSRT3PL = new LimitedDocument(1);
		ldMSRT3PL.setAllowChar("01234567");
		tfMSRT3PL.setDocument(ldMSRT3PL);		
		jp.add(tfMSRT3PL);
        
		JLabel labMSRT3PLValue = new JLabel("(0 ~ 7)");
		labMSRT3PLValue.setBounds(300, 30 + 10*size_height, 200, size_height - 5);
		jp.add(labMSRT3PLValue); 
		
		//--------------------------------------------------------------------
        JLabel labMSRT3Parity = new JLabel("MSR track 3 parity");
        labMSRT3Parity.setBounds(10, 30 + 11*size_height, 200, size_height - 5);
		jp.add(labMSRT3Parity);
		
		jbMSRT3PDisable = new JRadioButton("Disable");
		jbMSRT3PDisable.setBounds(235, 30 + 11*size_height, 100, size_height);		
		jp.add(jbMSRT3PDisable);
		jbMSRT3PEnable = new JRadioButton("Enabel");
		jbMSRT3PEnable.setBounds(335, 30 + 11*size_height, 100, size_height);
	    jp.add(jbMSRT3PEnable);
	    ButtonGroup jbgMSRT3P = new ButtonGroup();	    
	    jbgMSRT3P.add(jbMSRT3PDisable);
	    jbgMSRT3P.add(jbMSRT3PEnable);
	    ActionListener alMSRT3P = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbMSRT3PDisable) {
                	keymap_buf[0x0E] &= 0xF7;
                } else if (radio == jbMSRT3PEnable) {
                	keymap_buf[0x0E] |= 0x08;
                }
            }
        };
        jbMSRT3PDisable.addActionListener(alMSRT3P);
        jbMSRT3PEnable.addActionListener(alMSRT3P);
		
		//--------------------------------------------------------------------
        JLabel labMSRT3SV = new JLabel("MSR track 3 shift value");
        labMSRT3SV.setBounds(10, 30 + 12*size_height, 200, size_height - 5);
		jp.add(labMSRT3SV);
		
		tfMSRT3SV =  new JTextField();
		tfMSRT3SV.setBounds(240, 30 + 12*size_height, 50, size_height - 5);
		LimitedDocument ldMSRT3SV = new LimitedDocument(2);
		ldMSRT3SV.setAllowChar("0123456789");
		tfMSRT3SV.setDocument(ldMSRT3SV);
		jp.add(tfMSRT3SV);
        
		JLabel labMSRT3SVValue = new JLabel("(0 ~ 15)");
		labMSRT3SVValue.setBounds(300, 30 + 12*size_height, 200, size_height - 5);
		jp.add(labMSRT3SVValue);
			
		return jp;
	}
	
	private JRadioButton jbAutoDetectionEnable, jbAutoDetectionDisable,
						 jbN7Deactivated, jbN7Activated,
						 jbP7Deactivated, jbP7Activated,
						 jbP11Deactivated, jbP11Activated,
						 jbP13Deactivated, jbP13Activated,
						 jbP17Deactivated, jbP17Activated,
						 jbP19Deactivated, jbP19Activated,
						 jbP22Deactivated, jbP22Activated,
						 jbP24Deactivated, jbP24Activated,
						 jbP28Deactivated, jbP28Activated,
						 jbP30Deactivated, jbP30Activated,
						 jbP9Deactivated, jbP9Activated,
						 jbP26Deactivated, jbP26Activated;
	private JRadioButton[] jbBlockPairLeft = new JRadioButton[9];
	private JRadioButton[] jbBlockPairRight = new JRadioButton[9];
	
	private JPanel N4375_KB_Cfg_Page3() {
		JPanel jp = new JPanel();
		
		jp.setLayout(null);
		jp.setName("Page 3");
		
		//--------------------------------------------
		JPanel jp1 = new JPanel();
		Border line1 = BorderFactory.createLineBorder(Color.black);
		jp1.setBorder(BorderFactory.createTitledBorder(line1));
		jp1.setLayout(null);
		jp1.setBounds(50, 15, 450, 50);
		
		JLabel labAutoDetection = new JLabel("Auto detection of blocking keys");
		labAutoDetection.setBounds(10, 10, 250, 30);
		jp1.add(labAutoDetection);
		
		jbAutoDetectionEnable = new JRadioButton("Enable");
		jbAutoDetectionEnable.setBounds(250, 10, 85, 30);		
        jp1.add(jbAutoDetectionEnable);
        jbAutoDetectionDisable = new JRadioButton("Disable");
        jbAutoDetectionDisable.setBounds(340, 10, 85, 30);
	    jp1.add(jbAutoDetectionDisable);
	    ButtonGroup jbgMSRT2 = new ButtonGroup();	    
	    jbgMSRT2.add(jbAutoDetectionEnable);
	    jbgMSRT2.add(jbAutoDetectionDisable);
	    ActionListener alAutoDetection = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbAutoDetectionEnable) {
                	keymap_buf[0x10] &= 0xFE;                	
                } else if (radio == jbAutoDetectionDisable) {
                	keymap_buf[0x10] |= 0x01;
                }
                
                N4375_Update_KB_CFG_UI();
            }
        };
        jbAutoDetectionEnable.addActionListener(alAutoDetection);
        jbAutoDetectionDisable.addActionListener(alAutoDetection);
		
		jp.add(jp1);
		
		//--------------------------------------------
		JPanel jp2 = new JPanel();
		Border line2 = BorderFactory.createLineBorder(Color.black);
		jp2.setBorder(BorderFactory.createTitledBorder(line2, "Double wide keycap"));
		jp2.setLayout(null);
		jp2.setBounds(10, 80, 260, 280);
		
		//++++++++++++++++++
		int height = 20;
		JLabel labN7 = new JLabel("#N7");
		labN7.setBounds(10, 20, 50, height);
		jp2.add(labN7);
		
        jbN7Deactivated = new JRadioButton("Deactivated");
        jbN7Deactivated.setBounds(50, 20, 110, height);		
		jp2.add(jbN7Deactivated);
	    jbN7Activated = new JRadioButton("Activated");
	    jbN7Activated.setBounds(160, 20, 95, height);
	    jp2.add(jbN7Activated);
	    ButtonGroup jbgN7 = new ButtonGroup();	    
	    jbgN7.add(jbN7Deactivated);
	    jbgN7.add(jbN7Activated);
	    ActionListener alN7 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbN7Deactivated) {                	
                	keymap_buf[0x11] &= 0xFD;
                } else if (radio == jbN7Activated) {
                	keymap_buf[0x11] |= 0x02;
                }
            }
        };
        jbN7Deactivated.addActionListener(alN7);
        jbN7Activated.addActionListener(alN7);
        
        //++++++++++++++++++
        JLabel labP7 = new JLabel("#P7");
        labP7.setBounds(10, 20 + height, 50, height);
		jp2.add(labP7);
		
        jbP7Deactivated = new JRadioButton("Deactivated");
        jbP7Deactivated.setBounds(50, 20 + height, 110, height);		
		jp2.add(jbP7Deactivated);
	    jbP7Activated = new JRadioButton("Activated");
	    jbP7Activated.setBounds(160, 20 + height, 95, height);
	    jp2.add(jbP7Activated);
	    ButtonGroup jbgP7 = new ButtonGroup();	    
	    jbgP7.add(jbP7Deactivated);
	    jbgP7.add(jbP7Activated);
	    ActionListener alP7 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP7Deactivated) {                	
                	keymap_buf[0x11] &= 0xFB;
                } else if (radio == jbP7Activated) {
                	keymap_buf[0x11] |= 0x04;
                }
            }
        };
        jbP7Deactivated.addActionListener(alP7);
        jbP7Activated.addActionListener(alP7);
        
      //++++++++++++++++++
        JLabel labP11 = new JLabel("#P11");
        labP11.setBounds(10, 20 + 2*height, 50, height);
		jp2.add(labP11);
		
        jbP11Deactivated = new JRadioButton("Deactivated");
        jbP11Deactivated.setBounds(50, 20 + 2*height, 110, height);		
		jp2.add(jbP11Deactivated);
	    jbP11Activated = new JRadioButton("Activated");
	    jbP11Activated.setBounds(160, 20 + 2*height, 95, height);
	    jp2.add(jbP11Activated);
	    ButtonGroup jbgP11 = new ButtonGroup();	    
	    jbgP11.add(jbP11Deactivated);
	    jbgP11.add(jbP11Activated);
	    ActionListener alP11 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP11Deactivated) {                	
                	keymap_buf[0x11] &= 0xEF;
                } else if (radio == jbP11Activated) {
                	keymap_buf[0x11] |= 0x10;
                }
            }
        };
        jbP11Deactivated.addActionListener(alP11);
        jbP11Activated.addActionListener(alP11);
		
      //++++++++++++++++++
        JLabel labP13 = new JLabel("#P13");
        labP13.setBounds(10, 20 + 3*height, 50, height);
		jp2.add(labP13);
		
        jbP13Deactivated = new JRadioButton("Deactivated");
        jbP13Deactivated.setBounds(50, 20 + 3*height, 110, height);		
		jp2.add(jbP13Deactivated);
	    jbP13Activated = new JRadioButton("Activated");
	    jbP13Activated.setBounds(160, 20 + 3*height, 95, height);
	    jp2.add(jbP13Activated);
	    ButtonGroup jbgP13 = new ButtonGroup();	    
	    jbgP13.add(jbP13Deactivated);
	    jbgP13.add(jbP13Activated);
	    ActionListener alP13 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP13Deactivated) {                	
                	keymap_buf[0x11] &= 0xDF;
                } else if (radio == jbP13Activated) {
                	keymap_buf[0x11] |= 0x20;
                }
            }
        };
        jbP13Deactivated.addActionListener(alP13);
        jbP13Activated.addActionListener(alP13);
        
      //++++++++++++++++++
        JLabel labP17 = new JLabel("#P17");
        labP17.setBounds(10, 20 + 4*height, 50, height);
		jp2.add(labP17);
		
        jbP17Deactivated = new JRadioButton("Deactivated");
        jbP17Deactivated.setBounds(50, 20 + 4*height, 110, height);		
		jp2.add(jbP17Deactivated);
	    jbP17Activated = new JRadioButton("Activated");
	    jbP17Activated.setBounds(160, 20 + 4*height, 95, height);
	    jp2.add(jbP17Activated);
	    ButtonGroup jbgP17 = new ButtonGroup();	    
	    jbgP17.add(jbP17Deactivated);
	    jbgP17.add(jbP17Activated);
	    ActionListener alP17 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP17Deactivated) {                	
                	keymap_buf[0x11] &= 0xBF;
                } else if (radio == jbP17Activated) {
                	keymap_buf[0x11] |= 0x40;
                }
            }
        };
        jbP17Deactivated.addActionListener(alP17);
        jbP17Activated.addActionListener(alP17);
        
      //++++++++++++++++++
        JLabel labP19 = new JLabel("#P19");
        labP19.setBounds(10, 20 + 5*height, 50, height);
		jp2.add(labP19);
		
        jbP19Deactivated = new JRadioButton("Deactivated");
        jbP19Deactivated.setBounds(50, 20 + 5*height, 110, height);		
		jp2.add(jbP19Deactivated);
	    jbP19Activated = new JRadioButton("Activated");
	    jbP19Activated.setBounds(160, 20 + 5*height, 95, height);
	    jp2.add(jbP19Activated);
	    ButtonGroup jbgP19 = new ButtonGroup();	    
	    jbgP19.add(jbP19Deactivated);
	    jbgP19.add(jbP19Activated);
	    ActionListener alP19 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP19Deactivated) {                	
                	keymap_buf[0x11] &= 0xBF;
                } else if (radio == jbP19Activated) {
                	keymap_buf[0x11] |= 0x40;
                }
            }
        };
        jbP19Deactivated.addActionListener(alP19);
        jbP19Activated.addActionListener(alP19);
        
      //++++++++++++++++++
        JLabel labP22 = new JLabel("#P22");
        labP22.setBounds(10, 20 + 6*height, 50, height);
		jp2.add(labP22);
		
        jbP22Deactivated = new JRadioButton("Deactivated");
        jbP22Deactivated.setBounds(50, 20 + 6*height, 110, height);		
		jp2.add(jbP22Deactivated);
	    jbP22Activated = new JRadioButton("Activated");
	    jbP22Activated.setBounds(160, 20 + 6*height, 95, height);
	    jp2.add(jbP22Activated);
	    ButtonGroup jbgP22 = new ButtonGroup();	    
	    jbgP22.add(jbP22Deactivated);
	    jbgP22.add(jbP22Activated);
	    ActionListener alP22 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP22Deactivated) {                	
                	keymap_buf[0x12] &= 0xFD;
                } else if (radio == jbP22Activated) {
                	keymap_buf[0x12] |= 0x02;
                }
            }
        };
        jbP22Deactivated.addActionListener(alP22);
        jbP22Activated.addActionListener(alP22);
        
      //++++++++++++++++++
        JLabel labP24 = new JLabel("#P24");
        labP24.setBounds(10, 20 + 7*height, 50, height);
		jp2.add(labP24);
		
        jbP24Deactivated = new JRadioButton("Deactivated");
        jbP24Deactivated.setBounds(50, 20 + 7*height, 110, height);		
		jp2.add(jbP24Deactivated);
	    jbP24Activated = new JRadioButton("Activated");
	    jbP24Activated.setBounds(160, 20 + 7*height, 95, height);
	    jp2.add(jbP24Activated);
	    ButtonGroup jbgP24 = new ButtonGroup();	    
	    jbgP24.add(jbP24Deactivated);
	    jbgP24.add(jbP24Activated);
	    ActionListener alP24 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP24Deactivated) {                	
                	keymap_buf[0x12] &= 0xFB;
                } else if (radio == jbP24Activated) {
                	keymap_buf[0x12] |= 0x04;
                }
            }
        };
        jbP24Deactivated.addActionListener(alP24);
        jbP24Activated.addActionListener(alP24);         
        
      //++++++++++++++++++
        JLabel labP28 = new JLabel("#P28");
        labP28.setBounds(10, 20 + 8*height, 50, height);
		jp2.add(labP28);
		
        jbP28Deactivated = new JRadioButton("Deactivated");
        jbP28Deactivated.setBounds(50, 20 + 8*height, 110, height);		
		jp2.add(jbP28Deactivated);
	    jbP28Activated = new JRadioButton("Activated");
	    jbP28Activated.setBounds(160, 20 + 8*height, 95, height);
	    jp2.add(jbP28Activated);
	    ButtonGroup jbgP28 = new ButtonGroup();	    
	    jbgP28.add(jbP28Deactivated);
	    jbgP28.add(jbP28Activated);
	    ActionListener alP28 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP28Deactivated) {                	
                	keymap_buf[0x12] &= 0xFB;
                } else if (radio == jbP28Activated) {
                	keymap_buf[0x12] |= 0x04;
                }
            }
        };
        jbP28Deactivated.addActionListener(alP28);
        jbP28Activated.addActionListener(alP28);
        
      //++++++++++++++++++
        JLabel labP30 = new JLabel("#P30");
        labP30.setBounds(10, 20 + 9*height, 50, height);
		jp2.add(labP30);
		
        jbP30Deactivated = new JRadioButton("Deactivated");
        jbP30Deactivated.setBounds(50, 20 + 9*height, 110, height);		
		jp2.add(jbP30Deactivated);
	    jbP30Activated = new JRadioButton("Activated");
	    jbP30Activated.setBounds(160, 20 + 9*height, 95, height);
	    jp2.add(jbP30Activated);
	    ButtonGroup jbgP30 = new ButtonGroup();	    
	    jbgP30.add(jbP30Deactivated);
	    jbgP30.add(jbP30Activated);
	    ActionListener alP30 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP30Deactivated) {                	
                	keymap_buf[0x12] &= 0xFB;
                } else if (radio == jbP30Activated) {
                	keymap_buf[0x12] |= 0x04;
                }
            }
        };
        jbP30Deactivated.addActionListener(alP30);
        jbP30Activated.addActionListener(alP30);
        
      //++++++++++++++++++
        JLabel labP9 = new JLabel("#P9");
        labP9.setBounds(10, 20 + 10*height, 50, height);
		jp2.add(labP9);
		
        jbP9Deactivated = new JRadioButton("Deactivated");
        jbP9Deactivated.setBounds(50, 20 + 10*height, 110, height);		
		jp2.add(jbP9Deactivated);
	    jbP9Activated = new JRadioButton("Activated");
	    jbP9Activated.setBounds(160, 20 + 10*height, 95, height);
	    jp2.add(jbP9Activated);
	    ButtonGroup jbgP9 = new ButtonGroup();	    
	    jbgP9.add(jbP9Deactivated);
	    jbgP9.add(jbP9Activated);
	    ActionListener alP9 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP9Deactivated) {                	
                	keymap_buf[0x12] &= 0xFB;
                } else if (radio == jbP9Activated) {
                	keymap_buf[0x12] |= 0x04;
                }
            }
        };
        jbP9Deactivated.addActionListener(alP9);
        jbP9Activated.addActionListener(alP9);
        
      //++++++++++++++++++
        JLabel labP26 = new JLabel("#P26");
        labP26.setBounds(10, 20 + 11*height, 50, height);
		jp2.add(labP26);
		
        jbP26Deactivated = new JRadioButton("Deactivated");
        jbP26Deactivated.setBounds(50, 20 + 11*height, 110, height);		
		jp2.add(jbP26Deactivated);
	    jbP26Activated = new JRadioButton("Activated");
	    jbP26Activated.setBounds(160, 20 + 11*height, 95, height);
	    jp2.add(jbP26Activated);
	    ButtonGroup jbgP26 = new ButtonGroup();	    
	    jbgP26.add(jbP26Deactivated);
	    jbgP26.add(jbP26Activated);
	    ActionListener alP26 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbP26Deactivated) {                	
                	keymap_buf[0x12] &= 0xFB;
                } else if (radio == jbP26Activated) {
                	keymap_buf[0x12] |= 0x04;
                }
            }
        };
        jbP26Deactivated.addActionListener(alP26);
        jbP26Activated.addActionListener(alP26);
        
		jp.add(jp2);
		
		
		//--------------------------------------------
		JPanel jp3 = new JPanel();
		Border line3 = BorderFactory.createLineBorder(Color.black);
		jp3.setBorder(BorderFactory.createTitledBorder(line3, "Blocking pair"));
		jp3.setLayout(null);
		jp3.setBounds(270, 80, 260, 280);
		
		
		JLabel[] labBlockPair = new JLabel[9];		
		ButtonGroup[] jbgBlockPair = new ButtonGroup[9];
		ActionListener[] alBlockPair = new ActionListener[9];
		
		//P7
		alBlockPair[0] = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbBlockPairLeft[0]) {                	
                	keymap_buf[0x14] &= 0xFB;
                } else if (radio == jbBlockPairRight[0]) {
                	keymap_buf[0x14] |= 0x04;
                }
            }
        };
        //P11
        alBlockPair[1] = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbBlockPairLeft[1]) {                	
                	keymap_buf[0x14] &= 0xEF;
                } else if (radio == jbBlockPairRight[1]) {
                	keymap_buf[0x14] |= 0x10;
                }
            }
        };
        //P13
        alBlockPair[2] = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbBlockPairLeft[2]) {                	
                	keymap_buf[0x14] &= 0xDF;
                } else if (radio == jbBlockPairRight[2]) {
                	keymap_buf[0x14] |= 0x20;
                }
            }
        };
        //P17
        alBlockPair[3] = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbBlockPairLeft[3]) {                	
                	keymap_buf[0x14] &= 0xBF;
                } else if (radio == jbBlockPairRight[3]) {
                	keymap_buf[0x14] |= 0x40;
                }
            }
        };
        //P19
        alBlockPair[4] = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbBlockPairLeft[0]) {                	
                	keymap_buf[0x15] &= 0xFE;
                } else if (radio == jbBlockPairRight[0]) {
                	keymap_buf[0x15] |= 0x01;
                }
            }
        };
        //P22
        alBlockPair[5] = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbBlockPairLeft[5]) {                	
                	keymap_buf[0x15] &= 0xFD;
                } else if (radio == jbBlockPairRight[5]) {
                	keymap_buf[0x15] |= 0x02;
                }
            }
        };
        //P24
        alBlockPair[6] = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbBlockPairLeft[6]) {                	
                	keymap_buf[0x15] &= 0xFB;
                } else if (radio == jbBlockPairRight[6]) {
                	keymap_buf[0x15] |= 0x04;
                }
            }
        };
        //P28
        alBlockPair[7] = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbBlockPairLeft[7]) {                	
                	keymap_buf[0x15] &= 0xF7;
                } else if (radio == jbBlockPairRight[7]) {
                	keymap_buf[0x15] |= 0x08;
                }
            }
        };
        //P30
        alBlockPair[8] = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();             
                if (radio == jbBlockPairLeft[8]) {                	
                	keymap_buf[0x15] &= 0xBF;
                } else if (radio == jbBlockPairRight[8]) {
                	keymap_buf[0x15] |= 0x40;
                }
            }
        };        
		
		for(int i = 0; i < jbBlockPairLeft.length; i++){
			labBlockPair[i] = new JLabel();
			labBlockPair[i].setBounds(10, 20 + i*height, 125, height);
			jp3.add(labBlockPair[i]);
			
			jbBlockPairLeft[i] = new JRadioButton("1");
			jbBlockPairLeft[i].setBounds(130, 20 + i*height, 60, height);		
			jp3.add(jbBlockPairLeft[i]);
			jbBlockPairRight[i] = new JRadioButton("2");
			jbBlockPairRight[i].setBounds(190, 20 + i*height, 60, height);
			jp3.add(jbBlockPairRight[i]);
			jbgBlockPair[i] = new ButtonGroup();	    
			jbgBlockPair[i].add(jbBlockPairLeft[i]);
			jbgBlockPair[i].add(jbBlockPairRight[i]);	    
			jbBlockPairLeft[i].addActionListener(alBlockPair[i] );
			jbBlockPairRight[i].addActionListener(alBlockPair[i] );
		}
		labBlockPair[0].setText("#P7 Pairing with");
		labBlockPair[1].setText("#P11 Pairing with");
		labBlockPair[2].setText("#P13 Pairing with");
		labBlockPair[3].setText("#P17 Pairing with");
		labBlockPair[4].setText("#P19 Pairing with");
		labBlockPair[5].setText("#P22 Pairing with");
		labBlockPair[6].setText("#P24 Pairing with");
		labBlockPair[7].setText("#P28 Pairing with");
		labBlockPair[8].setText("#P30 Pairing with");
				
		jbBlockPairLeft[0].setText("#P8");
		jbBlockPairLeft[1].setText("#P12");
		jbBlockPairLeft[2].setText("#P14");
		jbBlockPairLeft[3].setText("#P18");
		jbBlockPairLeft[4].setText("#P20");
		jbBlockPairLeft[5].setText("#P23");
		jbBlockPairLeft[6].setText("#P25");
		jbBlockPairLeft[7].setText("#P29");
		jbBlockPairLeft[8].setText("#P31");
		
		jbBlockPairRight[0].setText("#P6");
		jbBlockPairRight[1].setText("#P10");
		jbBlockPairRight[2].setText("#P12");
		jbBlockPairRight[3].setText("#P16");
		jbBlockPairRight[4].setText("#P18");
		jbBlockPairRight[5].setText("#P21");
		jbBlockPairRight[6].setText("#P23");
		jbBlockPairRight[7].setText("#P27");
		jbBlockPairRight[8].setText("#P29");
		
		jp.add(jp3);		
		
		return jp;	
	}
	
	private void N4375_Update_KB_CFG_UI(){
		
		//Page 1
        if((keymap_buf[0] & 0x01) == 0x01){
        	jbUnsolicited.setSelected(true);
        }else
        	jbSolicited.setSelected(true);
        
        if((keymap_buf[0x11] & 0x01) == 0x01){
        	jbTelephone.setSelected(true);
        }else
        	jbCalculator.setSelected(true);
        
        if((keymap_buf[0x11] & 0x08) == 0x08){
        	jbDoubleKeyDisable.setSelected(true);
        }else
        	jbDoubleKeyEnable.setSelected(true); 
        
        if((keymap_buf[0x01] & 0x80) == 0x80){
        	jbSpeakerSoundEnable.setSelected(true);
        }else
        	jbSpeakerSoundDisable.setSelected(true);
        
        if((keymap_buf[0x01] & 0x02) == 0x02){
        	jbKeyLockLLock.setSelected(true);
        }else
        	jbKeyLockLUnlock.setSelected(true);
        
        if((keymap_buf[0x10] & 0x02) == 0x02){
        	jbCADprotectionEnable.setSelected(true);
        }else
        	jbCADprotectionDisable.setSelected(true);
        
        if((keymap_buf[0x10] & 0x08) == 0x08){
        	jbKeyLockDataModeEmulation.setSelected(true);
        }else
        	jbKeyLockDataModeHID.setSelected(true);
        
        if((keymap_buf[0x10] & 0x10) == 0x10){
        	jbMSRDataModeEmulation.setSelected(true);
        }else
        	jbMSRDataModeHID.setSelected(true);
              
		tfKeylockVolume.setText(String.valueOf(keymap_buf[0x03] & 0x0F));
		tfErrorToneVolume.setText(String.valueOf((keymap_buf[0x03] >> 4) & 0x0F));
		tfKeyclickFre.setText(String.valueOf(keymap_buf[0x04]));
		tfErrorToneFre.setText(String.valueOf(keymap_buf[0x05]));
		tfKeyclickDuration.setText(String.valueOf(keymap_buf[0x06]));
		tfErrorToneDuration.setText(String.valueOf(keymap_buf[0x07]));
		
		//Page 2		
		if((keymap_buf[0x10] & 0x20) == 0x20){
			jbMSRT1ESEnable.setSelected(true);
        }else
        	jbMSRT1ESDisable.setSelected(true);
        
        if((keymap_buf[0x10] & 0x40) == 0x40){
        	jbMSRT2ESEnable.setSelected(true);
        }else
        	jbMSRT2ESDisable.setSelected(true);
        
        if((keymap_buf[0x10] & 0x80) == 0x80){
        	jbMSRT3ESEnable.setSelected(true);
        }else
        	jbMSRT3ESDisable.setSelected(true);         
//??        
        //if((keymap_buf[0x01] & 0x80) == 0x80){
        //	jbMSRRawModeActived.setSelected(true);
        //}else
        //	jbMSRRawModeDeactived.setSelected(true);
        
        if((keymap_buf[0] & 0x10) == 0x10){
        	jbMSRT1JISActived.setSelected(true);
        }else
        	jbMSRT1JISDeactived.setSelected(true);        
               
        if((keymap_buf[0] & 0x20) == 0x20){
        	jbMSRT1Enable.setSelected(true);
        }else
        	jbMSRT1Disable.setSelected(true);
        
        if((keymap_buf[0] & 0x40) == 0x40){
        	jbMSRT2Enable.setSelected(true);
        }else
        	jbMSRT2Disable.setSelected(true);
        
        if((keymap_buf[0] & 0x80) == 0x80){
        	jbMSRT3Enable.setSelected(true);
        }else
        	jbMSRT3Disable.setSelected(true);
  
        tfMSRT3SS.setText(String.valueOf(keymap_buf[0x0C]));
        tfMSRT3ES.setText(String.valueOf(keymap_buf[0x0D]));
        tfMSRT3PL.setText(String.valueOf(keymap_buf[0x0E] & 0x07));
        
        if((keymap_buf[0x0E] & 0x08) == 0x08){
        	jbMSRT3PEnable.setSelected(true);
        }else
        	jbMSRT3PDisable.setSelected(true);
        
        tfMSRT3SV.setText(String.valueOf((keymap_buf[0x0E]>>4) & 0x0F));
        
        //Page 3
        if ((keymap_buf[0x10] & 0x01) == 0x01) {        	
        	jbAutoDetectionDisable.setSelected(true);
        	
        	//Left        	
        	jbN7Deactivated.setEnabled(true);
        	jbN7Activated.setEnabled(true);
        	if((keymap_buf[0x11] & 0x02) == 0x02)
        		jbN7Activated.setSelected(true);
        	else
        		jbN7Deactivated.setSelected(true);
        	
        	jbP7Deactivated.setEnabled(true);
        	jbP7Activated.setEnabled(true);
        	if((keymap_buf[0x11] & 0x04) == 0x04)
        		jbP7Activated.setSelected(true);
        	else
        		jbP7Deactivated.setSelected(true);
        	
        	jbP11Deactivated.setEnabled(true);
        	jbP11Activated.setEnabled(true);
        	if((keymap_buf[0x11] & 0x10) == 0x10)
        		jbP11Activated.setSelected(true);
        	else
        		jbP11Deactivated.setSelected(true);
        	
        	jbP13Deactivated.setEnabled(true);
        	jbP13Activated.setEnabled(true);
        	if((keymap_buf[0x11] & 0x20) == 0x20)
        		jbP13Activated.setSelected(true);
        	else
        		jbP13Deactivated.setSelected(true);
        	
        	jbP17Deactivated.setEnabled(true);
        	jbP17Activated.setEnabled(true);
        	if((keymap_buf[0x11] & 0x40) == 0x40)
        		jbP17Activated.setSelected(true);
        	else
        		jbP17Deactivated.setSelected(true);
        	
        	jbP19Deactivated.setEnabled(true);
        	jbP19Activated.setEnabled(true);
        	if((keymap_buf[0x11] & 0x40) == 0x40)
        		jbP19Activated.setSelected(true);
        	else
        		jbP19Deactivated.setSelected(true);
        	
        	jbP22Deactivated.setEnabled(true);
        	jbP22Activated.setEnabled(true);
        	if((keymap_buf[0x12] & 0x02) == 0x02)
        		jbP22Activated.setSelected(true);
        	else
        		jbP22Deactivated.setSelected(true);
        	
        	jbP24Deactivated.setEnabled(true);
        	jbP24Activated.setEnabled(true);
        	if((keymap_buf[0x12] & 0x04) == 0x04)
        		jbP24Activated.setSelected(true);
        	else
        		jbP24Deactivated.setSelected(true);
        	
        	jbP28Deactivated.setEnabled(true);
        	jbP28Activated.setEnabled(true);
        	if((keymap_buf[0x12] & 0x08) == 0x08)
        		jbP28Activated.setSelected(true);
        	else
        		jbP28Deactivated.setSelected(true);
        	
        	jbP30Deactivated.setEnabled(true);
        	jbP30Activated.setEnabled(true);
        	if((keymap_buf[0x12] & 0x40) == 0x40)
        		jbP30Activated.setSelected(true);
        	else
        		jbP30Deactivated.setSelected(true);       
        	
        	jbP9Deactivated.setEnabled(true);
        	jbP9Activated.setEnabled(true);
        	if((keymap_buf[0x13] & 0x01) == 0x01)
        		jbP9Activated.setSelected(true);
        	else
        		jbP9Deactivated.setSelected(true);       	
        	
        	jbP26Deactivated.setEnabled(true);
        	jbP26Activated.setEnabled(true);
        	if((keymap_buf[0x13] & 0x02) == 0x02)
        		jbP26Activated.setSelected(true);
        	else
        		jbP26Deactivated.setSelected(true);
        	
        	//right
        	for(int i = 0; i < jbBlockPairLeft.length; i++){
        		jbBlockPairLeft[i].setEnabled(true);
        		jbBlockPairRight[i].setEnabled(true);
        	}
        	//P7
        	if((keymap_buf[0x14] & 0x04) == 0x04)
        		jbBlockPairRight[0].setSelected(true);
        	else
        		jbBlockPairLeft[0].setSelected(true);
        	//P11
        	if((keymap_buf[0x14] & 0x10) == 0x10)
        		jbBlockPairRight[1].setSelected(true);
        	else
        		jbBlockPairLeft[1].setSelected(true);
        	//P13
        	if((keymap_buf[0x14] & 0x20) == 0x20)
        		jbBlockPairRight[2].setSelected(true);
        	else
        		jbBlockPairLeft[2].setSelected(true);
        	//P17
        	if((keymap_buf[0x14] & 0x40) == 0x40)
        		jbBlockPairRight[3].setSelected(true);
        	else
        		jbBlockPairLeft[3].setSelected(true);
        	//P19
        	if((keymap_buf[0x15] & 0x01) == 0x01)
        		jbBlockPairRight[4].setSelected(true);
        	else
        		jbBlockPairLeft[4].setSelected(true);
        	//P22
        	if((keymap_buf[0x15] & 0x02) == 0x02)
        		jbBlockPairRight[5].setSelected(true);
        	else
        		jbBlockPairLeft[5].setSelected(true);
        	//P24
        	if((keymap_buf[0x15] & 0x04) == 0x04)
        		jbBlockPairRight[6].setSelected(true);
        	else
        		jbBlockPairLeft[6].setSelected(true);
        	//P28
        	if((keymap_buf[0x15] & 0x08) == 0x08)
        		jbBlockPairRight[7].setSelected(true);
        	else
        		jbBlockPairLeft[7].setSelected(true);
        	//P30
        	if((keymap_buf[0x15] & 0x40) == 0x40)
        		jbBlockPairRight[8].setSelected(true);
        	else
        		jbBlockPairLeft[8].setSelected(true);
        } else {
        	jbAutoDetectionEnable.setSelected(true);
        	
        	//left
        	jbN7Deactivated.setEnabled(false);
        	jbN7Activated.setEnabled(false);
        	
        	jbP7Deactivated.setEnabled(false);
        	jbP7Activated.setEnabled(false);
        	
        	jbP11Deactivated.setEnabled(false);
        	jbP11Activated.setEnabled(false);
        	
        	jbP13Deactivated.setEnabled(false);
        	jbP13Activated.setEnabled(false);
        	
        	jbP17Deactivated.setEnabled(false);
        	jbP17Activated.setEnabled(false);
        	
        	jbP19Deactivated.setEnabled(false);
        	jbP19Activated.setEnabled(false);
        	
        	jbP22Deactivated.setEnabled(false);
        	jbP22Activated.setEnabled(false);
        	
        	jbP24Deactivated.setEnabled(false);
        	jbP24Activated.setEnabled(false);
        	
        	jbP28Deactivated.setEnabled(false);
        	jbP28Activated.setEnabled(false);
        	
        	jbP30Deactivated.setEnabled(false);
        	jbP30Activated.setEnabled(false);
        	
        	jbP9Deactivated.setEnabled(false);
        	jbP9Activated.setEnabled(false);
        	
        	jbP26Deactivated.setEnabled(false);
        	jbP26Activated.setEnabled(false);
        	
        	//right
        	for(int i = 0; i < jbBlockPairLeft.length; i++){
        		jbBlockPairLeft[i].setEnabled(false);
        		jbBlockPairRight[i].setEnabled(false);
        	}
        }
	}
	
	private String N4375_GetKBCfgFromUI(){
		byte tmp_dat = 0;
		int tmp_int = 0;
		boolean bCheck = true;
		String retStr1 = "Please enter an integer between 0 and 15";
		String retStr2 = "Please enter an integer between 0 and 127";
		String retStr3 = "Please enter an integer between 0 and 255";
				
		//Page 1
		if(!tfKeylockVolume.getText().equals("")){
			tmp_dat = Byte.valueOf(tfKeylockVolume.getText());
			if(tmp_dat > 15){
				tfKeylockVolume.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr1;		
		keymap_buf[0x03] &= 0xF0;
		keymap_buf[0x03] |= tmp_dat;		
		
		if(!tfErrorToneVolume.getText().equals("")){		
			tmp_dat = Byte.valueOf(tfErrorToneVolume.getText());
			if(tmp_dat > 15){
				tfErrorToneVolume.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr1;
		keymap_buf[0x03] &= 0x0F;
		keymap_buf[0x03] |= (tmp_dat<<4);

		if(!tfKeyclickFre.getText().equals("")){
			tmp_int = Integer.valueOf(tfKeyclickFre.getText());
			if(tmp_int > 127){
				tfKeyclickFre.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr2;
		keymap_buf[0x04] = (byte)tmp_int;
		
		if(!tfErrorToneFre.getText().equals("")){
			tmp_int = Integer.valueOf(tfErrorToneFre.getText());
			if(tmp_int > 127){
				tfErrorToneFre.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr2;
		keymap_buf[0x05] = (byte)tmp_int;
		
		if(!tfKeyclickDuration.getText().equals("")){
			tmp_int = Integer.valueOf(tfKeyclickDuration.getText());
			if(tmp_int > 255){
				tfKeyclickDuration.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr3;
		keymap_buf[0x06] = (byte)tmp_int;
		
		if(!tfErrorToneDuration.getText().equals("")){
			tmp_int = Integer.valueOf(tfErrorToneDuration.getText());
			if(tmp_int > 255){
				tfErrorToneDuration.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr3;
		keymap_buf[0x07] = (byte)tmp_int;
		
		//Page 2   
		if(!tfMSRT3SS.getText().equals("")){
			tmp_int = Integer.valueOf(tfMSRT3SS.getText());
			if(tmp_int > 255){
				tfMSRT3SS.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr3;
		keymap_buf[0x0C] = (byte)tmp_int;
		
		if(!tfMSRT3ES.getText().equals("")){
			tmp_int = Integer.valueOf(tfMSRT3ES.getText());
			if(tmp_int > 255){
				tfMSRT3ES.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr3;
		keymap_buf[0x0D] = (byte)tmp_int;
		
		if(!tfMSRT3PL.getText().equals("")){
			tmp_int = Integer.valueOf(tfMSRT3PL.getText());
			if(tmp_int > 7){
				tfMSRT3PL.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr1;
		keymap_buf[0x0E] &= 0xF8;
		keymap_buf[0x0E] |= ((byte)tmp_int) & 0x07;
		
		if(!tfMSRT3SV.getText().equals("")){
			tmp_int = Integer.valueOf(tfMSRT3SV.getText());
			if(tmp_int > 15){
				tfMSRT3SV.setText("");
				bCheck = false;
			}
		}else
			bCheck = false;
		if(!bCheck)
			return retStr3;
		keymap_buf[0x0E] &= 0x0F;
		keymap_buf[0x0E] |= (((byte)tmp_int)<<4) & 0xF0;
		
		return "";
	}

	private int N4375_KbCfg_LastTabIndex = 0;
	private void N4375_Keyboard_Configuration(){
		
		final JTabbedPane  jTabbedPane = new JTabbedPane();
		JFrame jfKbCfg = new JFrame();
		
		jfKbCfg.setSize(550, 550);
		jfKbCfg.setLayout(null);
		jfKbCfg.setResizable(false);
		jfKbCfg.setAlwaysOnTop(true);              
  
        jfKbCfg.setVisible(true);
        jfKbCfg.setLocationRelativeTo(null);

        jfKbCfg.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				jframe.setEnabled(true);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				//JList jList
			}
		});
        
        jTabbedPane.add("Page 1", N4375_KB_Cfg_Page1());
        jTabbedPane.add("Page 2", N4375_KB_Cfg_Page2());
        jTabbedPane.add("Page 3", N4375_KB_Cfg_Page3());
        jTabbedPane.setBounds(0, 0, jfKbCfg.getWidth(), jfKbCfg.getHeight() - 130);
        jTabbedPane.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
            	
            	if(jTabbedPane.getSelectedIndex() == N4375_KbCfg_LastTabIndex)
            		return;
            	
            	String str = N4375_GetKBCfgFromUI();
				if(!str.equals("")){
					JOptionPane.showMessageDialog(jfKbCfg, str);
			
					jTabbedPane.setSelectedIndex(N4375_KbCfg_LastTabIndex);
				}else{
					N4375_KbCfg_LastTabIndex = jTabbedPane.getSelectedIndex();
				}
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

        });
        /*
        jTabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
			
				String str = N4375_Get_KB_CFG_UI();
				if(!str.equals("")){
					JOptionPane.showMessageDialog(jfKbCfg, str);
			
					jTabbedPane.setSelectedIndex(N4375_KbCfg_TabIndex);
				}else{
					N4375_KbCfg_TabIndex = jTabbedPane.getSelectedIndex();
				}
			}        	
        });*/
        jfKbCfg.add(jTabbedPane);
        
        
        JButton bntGet = new JButton("Get");
		JButton bntSet = new JButton("Set");
		
		bntGet.setBounds(160, 450, 60, 30);
		bntGet.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {			  
				  HY_Command hyCommand = new HY_Command(kb_hid_size);
				  byte[] rev_data = new byte[kb_hid_size];
				  if(hyCommand.Get_Keyboard_Configuration(rev_data)){
					  System.arraycopy(rev_data, 1, keymap_buf, 0, 0x17);

					  N4375_Update_KB_CFG_UI();
					  
					  bntSet.setEnabled(true);
				  }else{
					  JOptionPane.showMessageDialog(jfKbCfg, "Fail to connect to the USB keyboard.");					  
				  }
			  }
		});
		jfKbCfg.add(bntGet);		
		
		bntSet.setBounds(250, 450, 60, 30);
		bntSet.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  String str = N4375_GetKBCfgFromUI();
				  if(!str.equals("")){
					  JOptionPane.showMessageDialog(jfKbCfg, str);
				
					  return;
				  }
				  
				  HY_Command hyCommand = new HY_Command(kb_hid_size);

				  if(hyCommand.Set_Keyboard_Configuration(Arrays.copyOf(keymap_buf, 0x17))){
					  JOptionPane.showMessageDialog(jfKbCfg, "Set keyboard configurations successfully.");					  
				  }else
					  JOptionPane.showMessageDialog(jfKbCfg, "Fail to connect to the USB keyboard.");
			  }
		});
		bntSet.setEnabled(false);
		jfKbCfg.add(bntSet);		
		
		N4375_Update_KB_CFG_UI();
        
        jframe.setEnabled(false);
	}
	
	private JPanel init_N4374() {//Compact Alpha
		int i, j;
		
		HY_JPanel GImage = new HY_JPanel("pics/N4374-over.gif");
        GImage.setLayout(null);
        GImage.setName(N4374_PANEL);
        
        JButton[] bntN4374 = new JButton[68];
        
        for(i = 0; i < bntN4374.length; i++) {
        	final int m = i;
        	bntN4374[i] = new JButton("");// programmed value
//bntN4375[i].setName("skjdhasjkdhkjasd"); // programmed key code
        	bntN4374[i].setBorderPainted(false);
        	bntN4374[i].setFocusPainted(false);
    		bntN4374[i].setOpaque(false);
    		bntN4374[i].setForeground(CLR_KEY_BG);
    		bntN4374[i].setBackground(CLR_KEY_BG);
    		bntN4374[i].addMouseListener(new MouseListener(){
    			@Override
    			public void mouseClicked(MouseEvent e) {

    			}
    			@Override
    			public void mousePressed(MouseEvent e) {
    				//popup.show(e.getComponent(), e.getX(), e.getY());
    				//System.out.println(bntN4375[m].getName());
    				
    				UI_SetKeyCode(bntN4374[m].getName());   				
    			}
    			@Override
    			public void mouseReleased(MouseEvent e) {

    			}

    			@Override
    			public void mouseEntered(MouseEvent e) {
    				//bntN4375[m].setBackground(CLR_KEY_MOUSE_IN);
    				//bntN4375[m].setForeground(CLR_KEY_MOUSE_IN);
    			}

    			@Override
    			public void mouseExited(MouseEvent e) {
    				bntN4374[m].setBackground(CLR_KEY_BG);
    				bntN4374[m].setForeground(CLR_KEY_BG);
    			}
    		});
    		GImage.add(bntN4374[i]);
        }
        
        // Line 1 - 4 
        for(j = 0; j < 4; j++) {
        	for(i = 0; i < 8; i++) {
        		int d = 0;
        		if(i == 3 || i == 4)
        			d = 15;
        		else if (i >= 5)
        			d = 15 + 15;
        		bntN4374[j*8 + i].setBounds(143 + i*55 + d, (52 + 37*j), 44, 26);        		
        		bntN4374[j*8 + i].setName(String.format("%d", j) + ";" + String.format("%d", i));// key index
        		//System.out.println(j*8 + i);
        	}
        }
        
        // Line 5 - 8 
        for(j = 0; j < 4; j++) {
        	for(i = 0; i < 9; i++) {
        		int d = 0, m = i;
        		if(i >= 3 && i <= 5) {
        			bntN4374[32 + j*9 + i].setBounds(143 + 3*55 + 15 + (37*(i - 3)), (52 + 37*(j+4)), 25, 26);        		
            		bntN4374[32 + j*9 + i].setName(String.format("%d", (j+4)) + ";" + String.format("%d", i));// key index      
            		continue;
        		}        			
        		else if (i > 5) {
        			d = 15 + 15;
        			m = m - 1;
        		}
        		bntN4374[32 + j*9 + i].setBounds(143 + m*55 + d, (52 + 37*(j+4)), 44, 26);        		
        		bntN4374[32 + j*9 + i].setName(String.format("%d", (j+4)) + ";" + String.format("%d", i));// key index
        		     		
        	}
        }
		
		return GImage;
	}
	
	private JPanel init_N4375() {//64-Key
		int i, j;

		if(load_CFG_File("pics/NCR_64.dat") != 0){
			JOptionPane.showMessageDialog(jframe, "Can't open configuration file");
		}
		
		HY_JPanel GImage = new HY_JPanel("pics/N4375-down.gif");
        GImage.setLayout(null);
        GImage.setName(N4375_PANEL);  
        
        JButton[] bntN4375 = new JButton[68];
        
        for(i = 0; i < bntN4375.length; i++) {
        	final int m = i;
        	bntN4375[i] = new JButton("");// programmed value
//bntN4375[i].setName("skjdhasjkdhkjasd"); // programmed key code
        	bntN4375[i].setBorderPainted(false);
        	bntN4375[i].setFocusPainted(false);
        	bntN4375[i].setOpaque(true);
    		bntN4375[i].setForeground(CLR_KEY_BG);
    		bntN4375[i].setBackground(CLR_KEY_BG);
//if(!( ((i >= 35) && (i <= 37)) || ((i >= 44) && (i <= 46)) || ((i >= 53) && (i <= 55)) || ((i >= 62) && (i <= 64)) )) {    		
			
    		bntN4375[i].addMouseListener(new MouseListener(){
    			@Override
    			public void mouseClicked(MouseEvent e) {

    			}
    			@Override
    			public void mousePressed(MouseEvent e) {
    				//popup.show(e.getComponent(), e.getX(), e.getY());
    				//System.out.println(bntN4375[m].getName());
    				
    				UI_SetKeyCode(bntN4375[m].getName());
    			}
    			@Override
    			public void mouseReleased(MouseEvent e) {

    			}

    			@Override
    			public void mouseEntered(MouseEvent e) {
    				bntN4375[m].setBackground(CLR_KEY_MOUSE_IN);
    				bntN4375[m].setForeground(CLR_KEY_MOUSE_IN);   				
    			}

    			@Override
    			public void mouseExited(MouseEvent e) {
    				bntN4375[m].setBackground(CLR_KEY_BG);
    				bntN4375[m].setForeground(CLR_KEY_BG);
    			}
    		});
//}    		
    		GImage.add(bntN4375[i]);
        }
        
        // Line 1 - 4 
        for(j = 0; j < 4; j++) {
        	for(i = 0; i < 8; i++) {
        		int d = 0;
        		if(i == 3 || i == 4)
        			d = 15;
        		else if (i >= 5)
        			d = 15 + 15;
        		bntN4375[j*8 + i].setBounds(143 + i*55 + d, (52 + 37*j), 44, 26);        		
        		bntN4375[j*8 + i].setName(String.format("%d", j) + ";" + String.format("%d", i));// key index
        		//System.out.println(j*8 + i);
        	}
        }
        
        // Line 5 - 8 
        for(j = 0; j < 4; j++) {
        	for(i = 0; i < 9; i++) {
        		int d = 0, m = i;
        		if(i >= 3 && i <= 5) {
        			//final int jj = j, ii = i;
        			
        			//bntN4375[32 + j*9 + i].setOpaque(false);	
        			bntN4375[32 + j*9 + i].setBounds(143 + 3*55 + 15 + (37*(i - 3)), (52 + 37*(j+4)), 25, 26);        		
            		bntN4375[32 + j*9 + i].setName(String.format("%d", (j+4)) + ";" + String.format("%d", i));// key index
            		/*
            		bntN4375[32 + j*9 + i].addMouseListener(new MouseListener(){
            			@Override
            			public void mouseClicked(MouseEvent e) {

            			}
            			@Override
            			public void mousePressed(MouseEvent e) {
            				//popup.show(e.getComponent(), e.getX(), e.getY());
            				//System.out.println(bntN4375[m].getName());
            				
            				init_SetKeyCode(bntN4375[32 + jj*9 + ii].getName());
            			}
            			@Override
            			public void mouseReleased(MouseEvent e) {

            			}

            			@Override
            			public void mouseEntered(MouseEvent e) {            				
            				
            				//bntN4375[32 + jj*9 + ii].setOpaque(false);
            				bntN4375[32 + jj*9 + ii].setIcon(new ImageIcon("pics/N4375_Up_7.jpg")); 
            				
            				bntN4375[32 + jj*9 + ii].setText("absd");
            			}

            			@Override
            			public void mouseExited(MouseEvent e) {
            				bntN4375[32 + jj*9 + ii].setIcon(null);
            			}
            		});*/
            		continue;
        		}        			
        		else if (i > 5) {
        			d = 15 + 15;
        			m = m - 1;
        		}
        		bntN4375[32 + j*9 + i].setBounds(143 + m*55 + d, (52 + 37*(j+4)), 44, 26);        		
        		bntN4375[32 + j*9 + i].setName(String.format("%d", (j+4)) + ";" + String.format("%d", i));// key index
        		     		
        	}
        }
        
        /*
        bntN4375[32 + 3].setBounds(143 + 3*55 + 15 + 10, (52), 25, 26);
        //bntN4375[35].setIcon(new ImageIcon("pics/N4375_Up_7.jpg"));
        bntN4375[35].addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {

			}
			@Override
			public void mousePressed(MouseEvent e) {
				//popup.show(e.getComponent(), e.getX(), e.getY());
				//System.out.println(bntN4375[m].getName());
				
				init_SetKeyCode(bntN4375[35].getName());
			}
			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//bntN4375[35].setBackground(CLR_KEY_MOUSE_IN);
				//bntN4375[35].setForeground(CLR_KEY_MOUSE_IN); 
				System.out.println("I AM");
				bntN4375[35].setIcon(new ImageIcon("pics/N4375_Down_7.jpg"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//bntN4375[35].setBackground(CLR_KEY_BG);
				//bntN4375[35].setForeground(CLR_KEY_BG);
				bntN4375[35].setIcon(null);
			}
		});*/

		return GImage;
	}
	
	public static void main(String[] args) {
		/*
		String property = System.getProperty("java.library.path");
		System.out.println(property);
		

		    try {
		    	System.loadLibrary("libusb4java");
		    } catch (UnsatisfiedLinkError e) {
		      System.err.println("Native code library failed to load.\n" + e);
		      System.exit(1);
		    }*/

		
		HY_UI hyUI = new HY_UI();
		
		
		hyUI.init_UI();
	}
}

