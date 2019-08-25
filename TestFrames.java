import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        //actionlisteners for button actions
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
            //checks for where the buttonPressed action is coming from and does according anction
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
        }

        public void right() {
            xCoord+=5;
            repaint();
        }
        public void up() {
            yCoord-=5;
            repaint();
        }

        public void down() {
            yCoord+=5;
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
            System.out.println("X" + getWidth());
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
}