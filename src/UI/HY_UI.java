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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import USB.HY_USB;

/*
 * JMenuBar : https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
 * keyboard/mmouse listener http://blog.csdn.net/crpxnmmafq/article/details/71023654
 */

public class HY_UI {
	
	private JFrame jframe = null;
	
	private final Color CLR_KEY_BG = new Color(193,193,193);
	private final Color CLR_KEY_MOUSE_IN = new Color(129,129,129);
	
	private final String N4375_PANEL = "N4375";// 64-key
	private final String N4374_PANEL = "N4374";// Compact Alpha
	
	private final int KB_N4375 = 0;
	private final int KB_N4374 = 1;
	
	private int selected_kb = KB_N4375;
	
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
        jframe.setSize(900, 600);
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
            	
            	
            	/*
            	HY_USB hyUSB = new HY_USB();
            	
            	int init_usb = hyUSB.init_USB_Device(HY_USB.NCR_N4375_HID_SIZE); 
            	if(init_usb == 0){
            		JOptionPane.showMessageDialog(jframe, hyUSB.get_Firmware());
            	}else
            		JOptionPane.showMessageDialog(jframe, "USB init ret=" + init_usb);
            	
            	hyUSB.usb_Close();*/
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
	
	private void init_SetKeyCode(String key_index) {
		JFrame jfKeycode = null;
		
		jframe.setEnabled(false);
		
		jfKeycode = new JFrame(key_index);
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
				  System.out.println("Cancel");
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
	
	private JPanel N4375_KB_Cfg_Page1() {
		int size_height = 20;
		JPanel jp = new JPanel();
		
		jp.setLayout(null);
		jp.setName("Page 1");
		
		//--------------------------------------------------------------------
		JLabel labKeylockDataMode = new JLabel("Keylock data mode");
		labKeylockDataMode.setBounds(10, 10, 200, size_height);
		jp.add(labKeylockDataMode);
		
		JRadioButton jbSolicited = new JRadioButton("Solicited");
		jbSolicited.setBounds(250, 10, 100, size_height);		
		jp.add(jbSolicited);
	    JRadioButton jbUnsolicited = new JRadioButton("Unsolicited");
	    jbUnsolicited.setBounds(350, 10, 100, size_height);
	    jp.add(jbUnsolicited);
	    ButtonGroup jbgKeylock = new ButtonGroup();	    
	    jbgKeylock.add(jbSolicited);
	    jbgKeylock.add(jbUnsolicited);
	    ActionListener alKeylock = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == jbSolicited) {
                	keymap_buf[1] &= 0xFE;
                } else if (radio == jbUnsolicited) {
                	keymap_buf[1] |= 0x01;
                } 
            }
        };
        jbSolicited.addActionListener(alKeylock);
        jbUnsolicited.addActionListener(alKeylock);
        if((keymap_buf[1] & 0x01) == 0x01){
        	jbUnsolicited.setSelected(true);
        }else
        	jbSolicited.setSelected(true);
        
        //--------------------------------------------------------------------
        JLabel labNumericKeypadLayout = new JLabel("Numeric keypad layout");
        labNumericKeypadLayout.setBounds(10, 10 + size_height, 200, size_height);
		jp.add(labNumericKeypadLayout);
        
        JRadioButton jbCalculator = new JRadioButton("Calculator");
        jbCalculator.setBounds(250, 10 + size_height, 100, size_height);		
		jp.add(jbCalculator);
	    JRadioButton jbTelephone = new JRadioButton("Telephone");
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
        if((keymap_buf[0x11] & 0x01) == 0x01){
        	jbTelephone.setSelected(true);
        }else
        	jbCalculator.setSelected(true); 
        
        //--------------------------------------------------------------------
        JLabel labDoubleKeyErrorDetection = new JLabel("Double key error detection");
        labDoubleKeyErrorDetection.setBounds(10, 10 + 2*size_height, 200, size_height);
		jp.add(labDoubleKeyErrorDetection);
        
        JRadioButton jbDoubleKeyEnable = new JRadioButton("Enable");
        jbDoubleKeyEnable.setBounds(250, 10 + 2*size_height, 100, size_height);		
		jp.add(jbDoubleKeyEnable);
	    JRadioButton jbDoubleKeyDisable = new JRadioButton("Disable");
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
        if((keymap_buf[0x11] & 0x08) == 0x08){
        	jbDoubleKeyDisable.setSelected(true);
        }else
        	jbDoubleKeyEnable.setSelected(true);         
	    
        //--------------------------------------------------------------------
        JLabel labSpeakerSound = new JLabel("Speaker Sound");
        labSpeakerSound.setBounds(10, 10 + 3*size_height, 200, size_height);
		jp.add(labSpeakerSound);
        
        JRadioButton jbSpeakerSoundEnable = new JRadioButton("Enable");
        jbSpeakerSoundEnable.setBounds(250, 10 + 3*size_height, 100, size_height);		
		jp.add(jbSpeakerSoundEnable);
	    JRadioButton jbSpeakerSoundDisable = new JRadioButton("Disable");
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
        if((keymap_buf[0x01] & 0x80) == 0x80){
        	jbSpeakerSoundEnable.setSelected(true);
        }else
        	jbSpeakerSoundDisable.setSelected(true);
        
        
        //--------------------------------------------------------------------
        JLabel labKeyLockL = new JLabel("KBD lock on Keylock 'L' Position");
        labKeyLockL.setBounds(10, 10 + 4*size_height, 200, size_height);
		jp.add(labKeyLockL);
        
        JRadioButton jbKeyLockLUnlock = new JRadioButton("Unlock");
        jbKeyLockLUnlock.setBounds(250, 10 + 4*size_height, 100, size_height);		
		jp.add(jbKeyLockLUnlock);
	    JRadioButton jbKeyLockLLock = new JRadioButton("Lock");
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
        if((keymap_buf[0x01] & 0x02) == 0x02){
        	jbKeyLockLLock.setSelected(true);
        }else
        	jbKeyLockLUnlock.setSelected(true);
        
      //--------------------------------------------------------------------
        JLabel labCADprotection = new JLabel("Ctrl,Alt,Del protection");
        labCADprotection.setBounds(10, 10 + 5*size_height, 200, size_height);
		jp.add(labCADprotection);
        
        JRadioButton jbCADprotectionEnable = new JRadioButton("Enable");
        jbCADprotectionEnable.setBounds(250, 10 + 5*size_height, 100, size_height);		
		jp.add(jbCADprotectionEnable);
	    JRadioButton jbCADprotectionDisable = new JRadioButton("Disabel");
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
        if((keymap_buf[0x10] & 0x02) == 0x02){
        	jbCADprotectionEnable.setSelected(true);
        }else
        	jbCADprotectionDisable.setSelected(true);
        
      //--------------------------------------------------------------------
        JLabel labKeyLockDataMode = new JLabel("Keylock data mode");
        labKeyLockDataMode.setBounds(10, 10 + 6*size_height, 200, size_height);
		jp.add(labKeyLockDataMode);
        
        JRadioButton jbKeyLockDataModeHID = new JRadioButton("HID");
        jbKeyLockDataModeHID.setBounds(250, 10 + 6*size_height, 100, size_height);		
		jp.add(jbKeyLockDataModeHID);
	    JRadioButton jbKeyLockDataModeEmulation = new JRadioButton("Emulation");
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
        if((keymap_buf[0x10] & 0x08) == 0x08){
        	jbKeyLockDataModeEmulation.setSelected(true);
        }else
        	jbKeyLockDataModeHID.setSelected(true);
        
        
      //--------------------------------------------------------------------
        JLabel labMSRDataMode = new JLabel("MSR data mode");
        labMSRDataMode.setBounds(10, 10 + 7*size_height, 200, size_height);
		jp.add(labMSRDataMode);
        
        JRadioButton jbMSRDataModeHID = new JRadioButton("HID");
        jbMSRDataModeHID.setBounds(250, 10 + 7*size_height, 100, size_height);		
		jp.add(jbMSRDataModeHID);
	    JRadioButton jbMSRDataModeEmulation = new JRadioButton("Emulation");
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
        if((keymap_buf[0x10] & 0x10) == 0x10){
        	jbMSRDataModeEmulation.setSelected(true);
        }else
        	jbMSRDataModeHID.setSelected(true);
        
      //--------------------------------------------------------------------
        JLabel labKeyClickVolume = new JLabel("Keyclick volume");
        labKeyClickVolume.setBounds(10, 30 + 8*size_height, 200, size_height - 5);
		jp.add(labKeyClickVolume);
		
		JTextField tfKeylockVolume =  new JTextField();
		tfKeylockVolume.setBounds(180, 30 + 8*size_height, 50, size_height - 5);
		LimitedDocument ldKeylockVolume = new LimitedDocument(2);
		ldKeylockVolume.setAllowChar("0123456789");
		tfKeylockVolume.setDocument(ldKeylockVolume);
		tfKeylockVolume.setText(String.valueOf(keymap_buf[0x03] & 0x0F));
		jp.add(tfKeylockVolume);
        
		JLabel labKeylockVolumeValue = new JLabel("(0 ~ 15)");
		labKeylockVolumeValue.setBounds(240, 30 + 8*size_height, 200, size_height - 5);
		jp.add(labKeylockVolumeValue); 
		
		//--------------------------------------------------------------------
        JLabel labErrorToneVolume = new JLabel("Error tone volume");
        labErrorToneVolume.setBounds(10, 30 + 9*size_height, 200, size_height - 5);
		jp.add(labErrorToneVolume);
		
		JTextField tfErrorToneVolume =  new JTextField();
		tfErrorToneVolume.setBounds(180, 30 + 9*size_height, 50, size_height - 5);
		LimitedDocument ldErrorToneVolume = new LimitedDocument(2);
		ldErrorToneVolume.setAllowChar("0123456789");
		tfErrorToneVolume.setDocument(ldErrorToneVolume);
		tfErrorToneVolume.setText(String.valueOf((keymap_buf[0x03] >> 4) & 0x0F));
		jp.add(tfErrorToneVolume);
        
		JLabel labErrorToneVolumeValue = new JLabel("(0 ~ 15)");
		labErrorToneVolumeValue.setBounds(240, 30 + 9*size_height, 200, size_height - 5);
		jp.add(labErrorToneVolumeValue);
        
		//--------------------------------------------------------------------
        JLabel labKeyclickFre = new JLabel("Keyclick frequency");
        labKeyclickFre.setBounds(10, 30 + 10*size_height, 200, size_height - 5);
		jp.add(labKeyclickFre);
		
		JTextField tfKeyclickFre =  new JTextField();
		tfKeyclickFre.setBounds(180, 30 + 10*size_height, 50, size_height - 5);
		LimitedDocument ldKeyclickFre = new LimitedDocument(3);
		ldKeyclickFre.setAllowChar("0123456789");
		tfKeyclickFre.setDocument(ldKeyclickFre);
		tfKeyclickFre.setText(String.valueOf(keymap_buf[0x04]));
		jp.add(tfKeyclickFre);
        
		JLabel labKeyclickFreValue = new JLabel("(0 ~ 127)");
		labKeyclickFreValue.setBounds(240, 30 + 10*size_height, 200, size_height - 5);
		jp.add(labKeyclickFreValue);
		
		//--------------------------------------------------------------------
        JLabel labErrorToneFre = new JLabel("Error tone frequency");
        labErrorToneFre.setBounds(10, 30 + 11*size_height, 200, size_height - 5);
		jp.add(labErrorToneFre);
		
		JTextField tfErrorToneFre =  new JTextField();
		tfErrorToneFre.setBounds(180, 30 + 11*size_height, 50, size_height - 5);
		LimitedDocument ldErrorToneFre = new LimitedDocument(3);
		ldErrorToneFre.setAllowChar("0123456789");
		tfErrorToneFre.setDocument(ldErrorToneFre);
		tfErrorToneFre.setText(String.valueOf(keymap_buf[0x05]));
		jp.add(tfErrorToneFre);
        
		JLabel labErrorToneFreValue = new JLabel("(0 ~ 127)");
		labErrorToneFreValue.setBounds(240, 30 + 11*size_height, 200, size_height - 5);
		jp.add(labErrorToneFreValue);
		
		//--------------------------------------------------------------------
        JLabel labKeyclickDuration = new JLabel("Keyclick duration");
        labKeyclickDuration.setBounds(10, 30 + 12*size_height, 200, size_height - 5);
		jp.add(labKeyclickDuration);
		
		JTextField tfKeyclickDuration =  new JTextField();
		tfKeyclickDuration.setBounds(180, 30 + 12*size_height, 50, size_height - 5);
		LimitedDocument ldKeyclickDuration = new LimitedDocument(3);
		ldKeyclickDuration.setAllowChar("0123456789");
		tfKeyclickDuration.setDocument(ldKeyclickDuration);
		tfKeyclickDuration.setText(String.valueOf(keymap_buf[0x06]));
		jp.add(tfKeyclickDuration);
        
		JLabel labKeyclickDurationValue = new JLabel("(0 ~ 255)");
		labKeyclickDurationValue.setBounds(240, 30 + 12*size_height, 200, size_height - 5);
		jp.add(labKeyclickDurationValue);
		
		//--------------------------------------------------------------------
        JLabel labErrorToneDuration = new JLabel("Error tone duration");
        labErrorToneDuration.setBounds(10, 30 + 13*size_height, 200, size_height - 5);
		jp.add(labErrorToneDuration);
		
		JTextField tfErrorToneDuration =  new JTextField();
		tfErrorToneDuration.setBounds(180, 30 + 13*size_height, 50, size_height - 5);
		LimitedDocument ldErrorToneDuration = new LimitedDocument(3);
		ldErrorToneDuration.setAllowChar("0123456789");
		tfErrorToneDuration.setDocument(ldErrorToneDuration);
		tfErrorToneDuration.setText(String.valueOf(keymap_buf[0x07]));
		jp.add(tfErrorToneDuration);
        
		JLabel labErrorToneDurationValue = new JLabel("(0 ~ 255)");
		labErrorToneDurationValue.setBounds(240, 30 + 13*size_height, 200, size_height - 5);
		jp.add(labErrorToneDurationValue);
		
		return jp;
	}
	
	private void N4375_Keyboard_Configuration(){
		
		final JTabbedPane  jTabbedPane = new JTabbedPane();
		JFrame jfKbCfg = new JFrame();
		
		jfKbCfg.setSize(500, 550);
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
        jTabbedPane.add("Page 2", N4375_KB_Cfg_Page1());
        jTabbedPane.setBounds(0, 0, jfKbCfg.getWidth(), jfKbCfg.getHeight() - 200);
        jTabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				selected_kb = jTabbedPane.getSelectedIndex();								
			}
        	
        });
        jfKbCfg.add(jTabbedPane);
        
        
        JButton bntGet = new JButton("Get");
		JButton bntSet = new JButton("Set");
		
		bntGet.setBounds(160, 390, 60, 30);
		bntGet.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  //System.out.println("OK");
				  bntSet.setEnabled(true);
			  }
		});
		jfKbCfg.add(bntGet);		
		
		bntSet.setBounds(250, 390, 60, 30);
		bntSet.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  //System.out.println("Cancel");
			  }
		});
		bntSet.setEnabled(false);
		jfKbCfg.add(bntSet);
        
        jframe.setEnabled(false);
	}
	
	private JPanel init_N4374() {//Compact Alpha
		int i, j;
		
		HY_JPanel GImage = new HY_JPanel("pics\\N4374-over.gif");
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
    				
    				init_SetKeyCode(bntN4374[m].getName());   				
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

		load_CFG_File("pics/NCR_64.dat");
		
		HY_JPanel GImage = new HY_JPanel("pics\\N4375-down.gif");
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
    				
    				init_SetKeyCode(bntN4375[m].getName());
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
            				bntN4375[32 + jj*9 + ii].setIcon(new ImageIcon("pics//N4375_Up_7.jpg")); 
            				
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
        //bntN4375[35].setIcon(new ImageIcon("pics\\N4375_Up_7.jpg"));
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
				bntN4375[35].setIcon(new ImageIcon("pics\\N4375_Down_7.jpg"));
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

