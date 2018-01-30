package remoteserver;


import java.awt.BorderLayout;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;


/**
 * This is the entry class of the server
 */
public class ServerInitiator {
    //Main server frame
	Socket socket = null;
	
    private JFrame frame = new JFrame();
    //JDesktopPane represents the main container that will contain all
    //connected clients' screens
    private JDesktopPane desktop = new JDesktopPane();

    public static void main(String args[]){
    		String ip = args[0];
        String port = args[1];
        new ServerInitiator().initialize(ip, Integer.parseInt(port));
    }

    public void initialize(String ip, int port){

        try {
        	System.out.println("Connecting to client ..........");
        	socket = new Socket(ip, port);
        	System.out.println("Connection Established.");
            
            //Show Server GUI
            drawGUI();
            //Listen to server port and accept clients connections
            new ClientHandler(socket,desktop);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Draws the main server GUI
     */
    public void drawGUI(){
            frame.add(desktop,BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                	System.out.println("close event");
                	try {
						socket.close();
						System.out.println("socket closed");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            });
            //Show the frame in a maximized state
            frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
    }
}
