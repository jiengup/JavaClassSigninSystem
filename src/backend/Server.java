package backend;

import connection.MySqlConnect;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server extends Thread{
    private Socket serverClient;
    private int serverID;
    private String clientUserName;
    private String clientPassWord;
    private String clientName;
    private String messageFromClient;
    private PrintWriter outStream;
    private BufferedReader inStream;
    private int totalStudent;
    private int lessonTimes;
    private String userInfo;
    public Server(Socket client_socket, int clientNum, int totalStudent, int lessonTimes) {
        serverClient = client_socket;
        serverID = clientNum;
        this.totalStudent = totalStudent;
        this.lessonTimes = lessonTimes;
        System.out.println("Connect from : \n" + serverClient.toString());
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            inStream = new BufferedReader(new InputStreamReader(serverClient.getInputStream()));
            outStream = new PrintWriter(serverClient.getOutputStream());
            outStream.println(lessonTimes);
            outStream.flush();
            boolean keepGoing = true;
            while(keepGoing) {
                //System.out.println("waiting for " + serverID + "'s" + "message");
                try{
                    messageFromClient = inStream.readLine();
                    System.out.println("Message from Client " + serverID + ": " + messageFromClient + "...");
                    if(messageFromClient.equals("Login Request")){
                        clientUserName = inStream.readLine();
                        clientPassWord = inStream.readLine();
                        System.out.println("Received user name : " + clientUserName + "\npassword : " + clientPassWord);
                        if(checkLoginInfoFromDatabase()){
                            System.out.println("Client " + serverID + " Password test successfully!");
                            try{
                                outStream.println("Login Passed");
                                outStream.flush();
                                outStream.println(clientName);
                                outStream.flush();
                                loginRecord();
                                userInfo = clientUserName + "    " + clientName;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            System.out.println("Client " + serverID + "Password test failed.");
                            try{
                                outStream.println("Login Failed");
                                outStream.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if(messageFromClient.equals("exit")){
                        System.out.println("Message from Client " + serverID + ": " + messageFromClient + "...");
                        System.out.println("Client " + serverID + " has disturbed.");
                        logoutRecord();
                        keepGoing = false;
                    }

                    if(messageFromClient.equals("logout")){
                        System.out.println("Message from Client " + serverID + ": " + messageFromClient + "...");
                        System.out.println("Client " + serverID + " has logged out.");
                        logoutRecord();
                        keepGoing = false;
                    }
                    if(messageFromClient.equals("sendfile")){
                        System.out.println("Receiving File...");
                        DataInputStream dis =new DataInputStream(serverClient.getInputStream());
                        long size =dis.readLong();
                        String suffix =dis.readUTF();
                        FileSystemView fsv = FileSystemView.getFileSystemView();
                        File path = new File(fsv.getHomeDirectory().getPath() + "/temp");
                        if(!path.exists()){
                            path.mkdir();
                            System.out.println("Creat dir " + path.getPath());
                        }
                        path = new File(fsv.getHomeDirectory().getPath() + "/temp/" + userInfo);
                        if(!path.exists()){
                            path.mkdir();
                            System.out.println("Creat dir " + path.getPath());
                        }
                        File dest = new File(path.getPath() + "/" + userInfo + "." + suffix);
                        if(!dest.exists()){
                            dest.createNewFile();
                        }
                        BufferedInputStream bis = new BufferedInputStream(serverClient.getInputStream());
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
                        long readSize = 0;
                        while(true){
                            if(readSize > size){
                                break;
                            }
                            bos.write(bis.read());
                            readSize++;
                        }
                        System.out.println("Upload Competed");
                        bos.flush();
                        userInfo += "*";
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    keepGoing = false;
                }
            }
            System.out.println("Disconnect from : \n" + serverClient.toString());
            inStream.close();
            outStream.close();
            serverClient.close();
        }
        catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void logoutRecord() {
        String sql = "insert into logOutRecord values(null, ?, ?, ?)";
        Connection conn = MySqlConnect.connectDB();
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String recordDate = df.format(new Date());
            assert conn != null;
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, clientName);
            pst.setString(2, recordDate);
            pst.setString(3, clientUserName);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkLoginInfoFromDatabase(){
        String sql = "select student_password, student_name from students where student_id = ?";
        Connection conn = MySqlConnect.connectDB();
        try{
            String correctPassWord = "";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, clientUserName);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                correctPassWord = rs.getString(1);
                clientName= rs.getString(2);
            }
            if(clientPassWord.equals(correctPassWord)){
                return true;
            }
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sentBroadcast(String broadcast){
        try{
            outStream.println("broadcast");
            outStream.flush();
            outStream.println(broadcast);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserInfo(){
        return userInfo;
    }

    private void loginRecord(){
        String sql = "insert into signInRecord values(null, ?, ?, ?)";
        Connection conn = MySqlConnect.connectDB();
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String recordDate = df.format(new Date());
            assert conn != null;
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, clientName);
            pst.setString(2, recordDate);
            pst.setString(3, clientUserName);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}