//code by nick cline
//todo: more buttons like connect button, joystick, add stuff

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.IOException;
import jssc.*;


public class TestFrames extends JFrame {

    private JButton jbtLeft = new JButton("Left");
    private JButton jbtRight = new JButton("Right");
    private JButton jbtUp = new JButton("Up");
    private JButton jbtDown = new JButton("Down");
    private BallPanel ballPanel = new BallPanel();
    private LabelPanel labelPanel = new LabelPanel();
    private JLabel labelX = new JLabel(Integer.toString(ballPanel.xCoord));
    private JLabel labelY = new JLabel(Integer.toString(ballPanel.yCoord));
    private JLabel coordLabel = new JLabel("Coordinates (X then Y)");
    private JTextField xText = new JTextField(5);
    private JTextField yText = new JTextField(5);
    private JButton coordPress = new JButton("Change Coords");
    private static SerialPort serialPort = new SerialPort("COM8");
    private static String testSendString="G0 X100";
    /*
    private SerialTest st = new SerialTest();
    
    //serial output and input
    public static BufferedReader input;
    public static OutputStream output;
    public static SerialTest serial = new SerialTest();
    */
    //instantiate buttons, labels, and panels


    public TestFrames () {
        JPanel buttonPanel = new JPanel();
        JPanel labelPanel = new JPanel();
        //instantiate new panels

        buttonPanel.add(jbtLeft);
        buttonPanel.add(jbtRight);
        buttonPanel.add(jbtUp);
        buttonPanel.add(jbtDown);
        labelPanel.add(coordLabel);
        labelPanel.add(labelX);
        labelPanel.add(labelY);
        labelPanel.add(xText);
        labelPanel.add(yText);
        labelPanel.add(coordPress);
        //add buttons/labels to respective panels

        ballPanel.setBackground(Color.RED);
        this.add(ballPanel);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(labelPanel, BorderLayout.EAST);
        //add ball visualization to JFrame TestFrames, puts buttons on bottom and coordinate panel on right
        
        jbtLeft.addActionListener(new ButtonListener());
        jbtRight.addActionListener(new ButtonListener());
        jbtUp.addActionListener(new ButtonListener());
        jbtDown.addActionListener(new ButtonListener());
        coordPress.addActionListener(new ButtonListener());
        //actionlisteners for button actions
    }
    
    public static void openWait(long ms) {
    	System.out.println("Waiting " +  ms +  " seconds.");
    	try{
    		Thread.sleep(ms*1000);
    	}catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    }

    public static void main(String[] args) {

    	
        TestFrames mainWindow = new TestFrames();
        
        //makes new window
        mainWindow.setTitle("TEST");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();
        //sets title and close operation, mainWindow.pack() sets window to size and layouts preferred
        mainWindow.setVisible(true);
        //sets window visible
        
       
        
    	//jssc part
    	// getting serial ports list into the array
    	String[] portNames = SerialPortList.getPortNames();
    	        
    	if (portNames.length == 0) {
    	    System.out.println("There are no serialports  You can use an emulator, such ad VSPE, to create a virtual serial port.");
    	    System.out.println("Press Enter to exit");
    	    try {
    	        System.in.read();
    	    } catch (IOException e) {
    	          e.printStackTrace();
    	    }
    	    return;
    	}

    	for (int i = 0; i < portNames.length; i++){
    	    System.out.println(portNames[i]);
    	}
    	
    	//jssc write block
    	
    	//serialport declaration is in instance vars
    	System.out.println("Port Opening...");
    	try {
    	    serialPort.openPort();
    	    System.out.println("Port Opened");
    	    serialPort.setParams(SerialPort.BAUDRATE_9600,
    	                         SerialPort.DATABITS_8,
    	                         SerialPort.STOPBITS_1,
    	                         SerialPort.PARITY_NONE);
    	    System.out.println("Port parameters set...");
    	    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
    	                                  SerialPort.FLOWCONTROL_RTSCTS_OUT);
    	    System.out.println("Flow control mode set...");
    	    serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
    	    System.out.println("Event listener added...");
    	    openWait(10);
    	    
    	    serialPort.writeString(testSendString);
    	    System.out.println(testSendString+ " has been sent");
    	}
    	catch (SerialPortException ex) {
    	    System.out.println("There are an error on writing string to port T: " + ex);
    	}
    	
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent buttonPressed) {
        	
            if (buttonPressed.getSource() == jbtLeft) {
                ballPanel.left();
            	labelPanel.update(ballPanel.xCoord, ballPanel.yCoord);
            	
            	//ballPanel.left() moves ball left, labelPanel.update() updates the coordinate labels after ball moves
            	//This is where you'd add some sort of other function that sends data to gcode, so maybe create a new function in labelPanel or ballPanel and call it here, 
            	//or you could put some code in ballPanel.left(), ballPanel.right(), etc. and that would do the job
            	
            }else if (buttonPressed.getSource() == jbtRight) {
                ballPanel.right();
                labelPanel.update(ballPanel.xCoord, ballPanel.yCoord);
            }else if (buttonPressed.getSource() == jbtUp) {
                ballPanel.up();
                labelPanel.update(ballPanel.xCoord, ballPanel.yCoord);
            }else if (buttonPressed.getSource() == jbtDown) {
                ballPanel.down();
                labelPanel.update(ballPanel.xCoord, ballPanel.yCoord);
            }
            
            if(buttonPressed.getSource() == coordPress) {
            	ballPanel.replace(Integer.parseInt(xText.getText()),Integer.parseInt(yText.getText()));
            }
            //checks for where the buttonPressed action is coming from and does according action
        }
    }

    class BallPanel extends JPanel {
        private int xCoord = -1;
        private int yCoord = -1;
        private Dimension preferredSize = new Dimension(300,200);
        //sets coordinates to -1 so the if statement in paintComponent can be called to center the ball
        public void left() {
            xCoord-=5;
            repaint();
            //moving ball left and repainting, same functionality for the different directions
            try {
            	serialPort.writeString(testSendString);
            }catch (SerialPortException ex){
            	System.out.println("There are an error on writing string to port T: " + ex);
            }
        }

        public void right() {
            xCoord+=5;
            repaint();
            //remember to put sample gcode writing here
            try {
            	serialPort.writeString(testSendString);
            }catch (SerialPortException ex){
            	System.out.println("There are an error on writing string to port T: " + ex);
            }
        }
        public void up() {
            yCoord-=5;
            repaint();
            try {
            	serialPort.writeString(testSendString);
            }catch (SerialPortException ex){
            	System.out.println("There are an error on writing string to port T: " + ex);
            }
            
            
        }

        public void down() {
            yCoord+=5;
            repaint();
            try {
            	serialPort.writeString(testSendString);
            }catch (SerialPortException ex){
            	System.out.println("There are an error on writing string to port T: " + ex);
            }
        }
        
        public void replace(int x, int y) {
        	xCoord=x;
        	yCoord=y;
        	repaint();
        }

        public Dimension getPreferredSize() {
            return preferredSize;
        }

        protected void paintComponent(Graphics aBall) {
            super.paintComponent(aBall);

            if (xCoord<0 || yCoord<0) {
                xCoord = getWidth()/2;
                yCoord = getHeight()/2;
                //sets coordinates at half the height and width of the screen
            }
            //System.out.println("X" + getWidth());
            aBall.drawOval(xCoord, yCoord, 10, 10);
        }
    }
    
    class LabelPanel extends JPanel{
    	
    	public void update(int xpos, int ypos) {
    		labelX.setText(Integer.toString(xpos));
    		labelY.setText(Integer.toString(ypos));
    		//updates coordinates by setting label text 
    	}
    	

    }
    
    
    
    
    private static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String receivedData = serialPort.readString(event.getEventValue());
                    System.out.println("Received response: " + receivedData);
                }
                catch (SerialPortException ex) {
                    System.out.println("Error in receiving string from COM-port: " + ex);
                }
            }
        }

    }
}

