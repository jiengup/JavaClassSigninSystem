/*
 * Created by JFormDesigner on Fri Dec 13 18:47:00 CST 2019
 */

package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.filechooser.FileSystemView;

/**
 * @author jiengup
 */
public class ClientAfterLogin extends JFrame{
    private Socket socket;
    private String userName;
    private PrintWriter outStream;
    private BufferedReader inStream;
    private String filePath;
    private String lessonTimes;
    private boolean keepgoing;
    public ClientAfterLogin(Socket skt, String name, String classTimes) {
        socket = skt;
        lessonTimes = classTimes;
        System.out.println(classTimes);
        initComponents();
        try{
           outStream = new PrintWriter(socket.getOutputStream());
           inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "CONNECT FAILED!\nPLEASE CHECK SERVER", "Alert", JOptionPane.ERROR_MESSAGE);
            keepgoing = false;
            e.printStackTrace();
            System.exit(0);
        }
        userName = name;
        welcome_lb2.setText(userName);
        if(lessonTimes.equalsIgnoreCase("1")) lessonTimes += "st";
        else if(lessonTimes.equalsIgnoreCase("2")) lessonTimes += "ed";
        else if(lessonTimes.equalsIgnoreCase("3")) lessonTimes += "rd";
        else lessonTimes += "th";
        content_lb4.setText(lessonTimes);
    }

    private void thisWindowClosing(WindowEvent e) {
        try{
            outStream.println("exit");
            outStream.flush();
            inStream.close();
            outStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        keepgoing = false;
        System.exit(0);
    }

    private void submitButtonMouseClicked(MouseEvent e) {
        //Get Desktop;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        filePath = "";
        JFileChooser selectFile = new JFileChooser(fsv.getHomeDirectory());
        if(selectFile.showOpenDialog(this) == selectFile.APPROVE_OPTION){
            filePath = selectFile.getSelectedFile().getPath();
        }
        if(!filePath.equalsIgnoreCase("")){
            outStream.println("sendfile");
            outStream.flush();
            File src = new File(filePath);
            String fileName = src.getName();
            long size = src.length();
            try{
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                DataOutputStream dos =new DataOutputStream(socket.getOutputStream());
                dos.writeLong(size);
                dos.writeUTF(suffix);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                long writeSize = 0;
                while(true){
                    bos.write(bis.read());
                    writeSize++;
                    if(writeSize > size){
                        break;
                    }
                }
                bos.flush();
                fileSendMessage.setText("File Submitted: " + filePath);
                fileSendStatu.setIcon(new ImageIcon(getClass().getResource("/resource/sendpass.png")));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "CONNECT FAILED!\nPLEASE CHECK SERVER", "Alert", JOptionPane.ERROR_MESSAGE);
                keepgoing = false;
                ex.printStackTrace();
                System.exit(0);
                ex.printStackTrace();
            }
        }
    }

    private void checkBox1ActionPerformed(ActionEvent e) {
        if(checkBox1.isSelected()){
            logoutButton.setEnabled(true);
        }
        else{
            logoutButton.setEnabled(false);
        }
    }

    private void logoutButtonMouseClicked(MouseEvent e) {
        if(!logoutButton.isEnabled()) return;
        else{
            try{
                outStream.println("logout");
                outStream.flush();
                inStream.close();
                outStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            keepgoing = false;
            System.exit(0);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        haad_img = new JLabel();
        welcome_lb1 = new JLabel();
        welcome_lb2 = new JLabel();
        submitButton = new JButton();
        content_lb1 = new JLabel();
        content_lb2 = new JLabel();
        content_lb3 = new JLabel();
        content_lb4 = new JLabel();
        content_lb5 = new JLabel();
        content_lb8 = new JLabel();
        content_lb6 = new JLabel();
        content_lb7 = new JLabel();
        label2 = new JLabel();
        logoutButton = new JButton();
        label3 = new JLabel();
        checkBox1 = new JCheckBox();
        scrollPane1 = new JScrollPane();
        textPane1 = new JTextPane();
        content_lb9 = new JLabel();
        fileSendMessage = new JLabel();
        fileSendStatu = new JLabel();

        //======== this ========
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();

        //---- haad_img ----
        haad_img.setIcon(new ImageIcon(getClass().getResource("/resource/head.png")));

        //---- welcome_lb1 ----
        welcome_lb1.setText("Welcome, ");
        welcome_lb1.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 20));

        //---- welcome_lb2 ----
        welcome_lb2.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 20));
        welcome_lb2.setText("Jengup");

        //---- submitButton ----
        submitButton.setText("Submit");
        submitButton.setToolTipText("select a file");
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                submitButtonMouseClicked(e);
            }
        });

        //---- content_lb1 ----
        content_lb1.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 20));
        content_lb1.setText("You hava signup successfully!");

        //---- content_lb2 ----
        content_lb2.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 20));
        content_lb2.setText("This is your");

        //---- content_lb3 ----
        content_lb3.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 20));
        content_lb3.setText("From now on, hand on your task!");

        //---- content_lb4 ----
        content_lb4.setFont(new Font("MathJax_Typewriter", Font.BOLD | Font.ITALIC, 20));
        content_lb4.setText("3rd ");

        //---- content_lb5 ----
        content_lb5.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 20));
        content_lb5.setText("Java Class");

        //---- content_lb8 ----
        content_lb8.setIcon(new ImageIcon(getClass().getResource("/resource/arrow.png")));

        //---- content_lb6 ----
        content_lb6.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 20));
        content_lb6.setText("If you compete:");

        //---- content_lb7 ----
        content_lb7.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 23));
        content_lb7.setText("If you have competed");

        //---- label2 ----
        label2.setText("(Please select sigle file, if you have muiti-files, compress them.)");
        label2.setForeground(Color.darkGray);
        label2.setFont(new Font("DejaVu Sans", Font.ITALIC, 12));

        //---- logoutButton ----
        logoutButton.setText("Log Out");
        logoutButton.setBackground(Color.red);
        logoutButton.setForeground(Color.red);
        logoutButton.setEnabled(false);
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logoutButtonMouseClicked(e);
            }
        });

        //---- label3 ----
        label3.setText("Powered By javax.Swing");
        label3.setForeground(Color.gray);
        label3.setFont(new Font("DejaVu Sans", Font.ITALIC, 10));

        //---- checkBox1 ----
        checkBox1.setText("I'm sure I have submitted all the tasks");
        checkBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBox1ActionPerformed(e);
            }
        });

        //======== scrollPane1 ========
        {

            //---- textPane1 ----
            textPane1.setEditable(false);
            textPane1.setText("\u8bf7\u5c3d\u5feb\u5b8c\u6210\u5b9e\u9a8c");
            textPane1.setFont(textPane1.getFont().deriveFont(textPane1.getFont().getSize() + 3f));
            scrollPane1.setViewportView(textPane1);
        }

        //---- content_lb9 ----
        content_lb9.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 20));
        content_lb9.setText("Message from teacher");

        //---- fileSendMessage ----
        fileSendMessage.setFont(fileSendMessage.getFont().deriveFont(fileSendMessage.getFont().getSize() - 2f));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap(241, Short.MAX_VALUE)
                    .addComponent(haad_img, GroupLayout.PREFERRED_SIZE, 359, GroupLayout.PREFERRED_SIZE)
                    .addGap(233, 233, 233))
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(353, 353, 353)
                    .addComponent(label3)
                    .addContainerGap(363, Short.MAX_VALUE))
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(checkBox1)
                                    .addGap(18, 18, 18)
                                    .addComponent(logoutButton))
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(label2)
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addGap(3, 3, 3)
                                            .addComponent(welcome_lb1)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(welcome_lb2))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addComponent(content_lb7)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(content_lb8)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(submitButton))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addComponent(content_lb2)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(content_lb4)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(content_lb5))
                                        .addComponent(content_lb1)
                                        .addComponent(content_lb3))))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addGap(0, 41, Short.MAX_VALUE)
                            .addComponent(fileSendStatu, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(fileSendMessage, GroupLayout.PREFERRED_SIZE, 307, GroupLayout.PREFERRED_SIZE)
                            .addGap(79, 79, 79)))
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 329, GroupLayout.PREFERRED_SIZE)
                        .addComponent(content_lb9))
                    .addGap(32, 32, 32))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(haad_img)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(welcome_lb1)
                                .addComponent(welcome_lb2))
                            .addGap(24, 24, 24)
                            .addComponent(content_lb1)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(content_lb2)
                                .addComponent(content_lb4)
                                .addComponent(content_lb5))
                            .addGap(18, 18, 18)
                            .addComponent(content_lb3)
                            .addGap(32, 32, 32)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(content_lb7)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(submitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(content_lb8, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(label2)
                            .addGap(32, 32, 32)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(fileSendStatu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(fileSendMessage, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE)))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(checkBox1)
                                .addComponent(logoutButton, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(content_lb9)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE)))
                    .addGap(48, 48, 48)
                    .addComponent(label3)
                    .addContainerGap())
        );
        setSize(835, 610);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public void run(){
        new Thread(){
            public void run(){
                while(true){
                    try{
                        String messageFromSever = inStream.readLine();
                        System.out.println("Messgae from Server : " + messageFromSever);
                        if(messageFromSever.equalsIgnoreCase("broadcast")){
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String broadcastMessage = "<" + df.format(new Date()) + ">";
                            broadcastMessage += inStream.readLine();
                            textPane1.setText(textPane1.getText() + "\n" + broadcastMessage);
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "LOST CONNECT", "Alert", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel haad_img;
    private JLabel welcome_lb1;
    private JLabel welcome_lb2;
    private JButton submitButton;
    private JLabel content_lb1;
    private JLabel content_lb2;
    private JLabel content_lb3;
    private JLabel content_lb4;
    private JLabel content_lb5;
    private JLabel content_lb8;
    private JLabel content_lb6;
    private JLabel content_lb7;
    private JLabel label2;
    private JButton logoutButton;
    private JLabel label3;
    private JCheckBox checkBox1;
    private JScrollPane scrollPane1;
    private JTextPane textPane1;
    private JLabel content_lb9;
    private JLabel fileSendMessage;
    private JLabel fileSendStatu;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
