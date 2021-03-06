
/*TEST2
 */

package remoteclient;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * This class is responsible for connecting to the server
 * and starting ScreenSpyer and ServerDelegate classes
 */
public class ClientInitiator{

    

    private ServerSocket sc;

	public static void main(String[] args){
    		String port = args[0];
        new ClientInitiator().initialize(Integer.parseInt(port));
    }

    public void initialize(int port ){

        Robot robot = null; //Used to capture the screen
        Rectangle rectangle = null; //Used to represent screen dimensions

        try {
            
        	sc = new ServerSocket(port);
        	while(true){
        		
                Socket client = sc.accept();
                System.out.println("Connecté");
                //Per each client create a ClientHandler
                
                
              //Get default screen device
                GraphicsEnvironment gEnv=GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gDev=gEnv.getDefaultScreenDevice();

                //Get screen dimensions
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                rectangle = new Rectangle(dim);

                //Prepare Robot object
                robot = new Robot(gDev);

                //draw client gui
                //ScreenSpyer sends screenshots of the client screen
                new ScreenSpyer(client,robot,rectangle);
                //ServerDelegate recieves server commands and execute them
                new ServerDelegate(client,robot);
        	 }

            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (AWTException ex) {
                ex.printStackTrace();
        }
    }


}
