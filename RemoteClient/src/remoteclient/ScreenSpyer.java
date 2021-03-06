
package remoteclient;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;

/**
 * This class is responisble for sending sreenshot every predefined duration
 */
class ScreenSpyer extends Thread {

    Socket socket = null; 
    Robot robot = null; // Used to capture screen
    Rectangle rectangle = null; //Used to represent screen dimensions
    boolean continueLoop = true; //Used to exit the program
    
    public ScreenSpyer(Socket socket, Robot robot,Rectangle rect) {
        this.socket = socket;
        this.robot = robot;
        rectangle = rect;
        start();
    }

    public void run(){
        ObjectOutputStream oos = null; //Used to write an object to the streem


        try{
            //Prepare ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            /*
             * Send screen size to the server in order to calculate correct mouse
             * location on the server's panel
             */
            oos.writeObject(rectangle);
        }catch(IOException ex){
            ex.printStackTrace();
        }

       while(continueLoop){
    	   
    	   System.out.println("0");
            //Capture screen
            /*
            try {
				oos.writeObject(robot.createScreenCapture(rectangle));
				oos.flush();
				oos.reset();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			*/
            /* I have to wrap BufferedImage with ImageIcon because BufferedImage class
             * does not implement Serializable interface
             */

            BufferedImage image = robot.createScreenCapture(rectangle);

            ImageIcon imageIcon = new ImageIcon(image);
            
            BufferedImage resizedImg = new BufferedImage(1280, 720, Transparency.TRANSLUCENT);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(imageIcon.getImage(), 0, 0, 1280, 720, null);
            g2.dispose();
            
            ImageIcon imageIcon2 = new ImageIcon(resizedImg);
            //Send captured screen to the server
            try {
            	System.out.println("1");
                oos.writeObject(imageIcon2);
                System.out.println("2");
                oos.flush();
                oos.reset(); //Clear ObjectOutputStream cache
                System.out.println("3");
            } catch (IOException ex) {
               try {
       			if(socket.getInputStream().read()==-1)
       			   {
       				   System.out.println("Socket fermée !");
       				   continueLoop = false;
       			   }
       			   else{
       				   System.out.println("Socket ouverte !");
       			   }
       			} catch (IOException e1) {
       				// TODO Auto-generated catch block
       			}
            }
            //wait for 100ms to reduce network traffic
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

    }

}
