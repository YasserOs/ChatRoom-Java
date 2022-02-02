/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatRoom;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatHandler extends Thread{
 
    DataInputStream dis ;
    PrintStream ps;
    static Vector<ChatHandler> clients = new Vector<ChatHandler>();
    public ChatHandler(Socket clientSocket) throws IOException{              
        dis = new DataInputStream(clientSocket.getInputStream());
        ps = new PrintStream(clientSocket.getOutputStream());
        clients.add(this);
        System.out.println(this.getId()+" is connected ");
        System.out.println(clients.size());
        sendMsgToAll("Client Id : "+this.getId()+" Entered Chat Room ");        
        start();
    }
    public void run(){        
        while(true){            
            try {
                // socket conneted to this thread is closed so we check on the inputstream.read() if it's = -1
                if(dis.read()==-1){
                    sendMsgToAll("Client : "+this.getId()+" Has left the chat .");
                    System.out.println("Client : "+this.getId()+" is closed");
                    System.out.println("Removing Client : "+this.getId());
                    clients.remove(this);
                    System.out.println("Clients Number : " + clients.size());
                    this.stop();
                }else{
                    String msg = dis.readLine();
                    sendMsgToAll(this.getId()+": "+msg);                             
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    void sendMsgToAll(String msg){
        for(ChatHandler ch : clients){
                ch.ps.println(msg);                  
        }
    }
    
}
