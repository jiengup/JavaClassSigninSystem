package backend;

import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class MultithreadSocketServer implements Runnable {
    static int PortNum = 9090;
    private int totalStudents;
    private int classTime;
    private ArrayList<Server> multiServer;
    private ArrayList<String> totalClientName;
    private ServerSocket server;

    public MultithreadSocketServer(int totalStudents, int classTime) {
        this.totalStudents = totalStudents;
        this.classTime = classTime;
        this.multiServer = new ArrayList<>();
        this.totalClientName = new ArrayList<>();
    }

    @Override
    public void run() {
        int clientSum = 0;
        try{
            server = new ServerSocket(PortNum);
            System.out.println("Waiting for client on port " + server.getLocalPort() + "...");

            while(true){
                clientSum++;
                Socket serverClient = server.accept();
                System.out.println("Just connected to" + serverClient.getRemoteSocketAddress());
                System.out.println(" >> " + "Client No: " + clientSum + " Started!");
                Server s = new Server(serverClient, clientSum, totalStudents, classTime);
                multiServer.add(s);
                s.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadCast(String message){
        for(Iterator<Server> item = multiServer.iterator(); item.hasNext(); ){
            item.next().sentBroadcast(message);
        }
    }

    public ArrayList<String> getTotalClientName(){
        for(Iterator<Server> item = multiServer.iterator(); item.hasNext();){
            String infoItem = item.next().getUserInfo();
            if(infoItem != null){
                if(totalClientName.contains(infoItem)){
                    continue;
                }
                if(totalClientName.contains(infoItem.substring(0, infoItem.length()-1))){
                    totalClientName.remove(infoItem.substring(0, infoItem.length()-1));
                }
                totalClientName.add(infoItem);
            }
        }
        return totalClientName;
    }

    public void endServer() {
        for(Iterator<Server> item = multiServer.iterator(); item.hasNext();){
            item.next().logoutRecord();
        }
        try{
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
