/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatRoom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ServerMulti {
    
    public ServerMulti(){
        try {
            ServerSocket myServerSocket = new ServerSocket(9001);
            System.out.println("Server Listening on port "+myServerSocket.getLocalPort()+" ...");
            while(true){
                System.out.println("Hello Github2");
                Socket s = myServerSocket.accept();
                new ChatHandler(s);                
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerMulti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        ServerMulti serverMulti = new ServerMulti();
    }
}
