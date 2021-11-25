/*
 * Created by JFormDesigner on Thu Dec 12 17:13:13 CST 2019
 */

package view;

import com.sun.security.ntlm.Client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.Buffer;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

/**
 * @author jiengup
 */
public class ClientLogin extends JFrame {
    private static String address = "127.0.0.1";
    private static int port = 9090;
    private Socket socket;
    private String userName = "";
    private char[] passWord;
    private String messageFromServer;
    static PrintWriter outStream;
    static BufferedReader inStream;
    private String lessonTimes;

    public ClientLogin() {
        initComponents();
    }

    private void loginButtonMouseClicked(MouseEvent e){
        try{
            System.out.println("Connecting to " + address + " on port " + port);
            socket = new Socket(address, port);
            System.out.println("Just Connected to " + socket.getInetAddress());

            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outStream = new PrintWriter(socket.getOutputStream());
            lessonTimes = inStream.readLine();
            System.out.println("Received Lesson Times : " + lessonTimes);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "CONNECT FAILED!\nPLEASE CHECK SERVER", "Alert", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "CONNECT FAILED!\nPLEASE CHECK SERVER", "Alert", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        userName = user_name.getText();
        passWord = user_passwd.getPassword();
        String passWordTest = new String(passWord);
        if(userName.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Please input your user name", "Alert", JOptionPane.WARNING_MESSAGE);
        }
        else{
            try{
                outStream.println("Login Request");
                outStream.flush();
                outStream.println(userName);
                outStream.flush();
                outStream.println(passWordTest);
                outStream.flush();
                messageFromServer = inStream.readLine();
                System.out.println("Receive from server : " + messageFromServer);
                if(messageFromServer.equals("Login Passed")){
                    System.out.println("Login successfully");
                    final String userName = inStream.readLine();
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            ClientAfterLogin cal = new ClientAfterLogin(socket, userName, lessonTimes);
                            cal.setVisible(true);
                            cal.run();
                        }
                    });
                    this.dispose();
                }
                else if(messageFromServer.equals("Login Failed")){
                    JOptionPane.showMessageDialog(null, "Incorrect User name of Password\nplease check again", "Alert", JOptionPane.WARNING_MESSAGE);
                    System.out.println("Login failed");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void cancelButtonMouseClicked(MouseEvent e) {
        try{
            outStream.println("exit");
            outStream.flush();
            inStream.close();
            outStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            System.exit(0);
        }
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
        finally {
            System.exit(0);
        }
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        usrname_lb = new JLabel();
        user_name = new JTextField();
        passwd_lb = new JLabel();
        user_passwd = new JPasswordField();
        title_lb = new JLabel();
        javalogo_lb = new JLabel();
        buttonBar = new JPanel();
        loginButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Client Login");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- usrname_lb ----
                usrname_lb.setText("User name:");
                usrname_lb.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 17));

                //---- user_name ----
                user_name.setToolTipText("your user name here");
                user_name.setFont(user_name.getFont().deriveFont(user_name.getFont().getStyle() | Font.ITALIC));

                //---- passwd_lb ----
                passwd_lb.setText("Password:");
                passwd_lb.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 17));

                //---- user_passwd ----
                user_passwd.setToolTipText("your password here");
                user_passwd.setFont(user_passwd.getFont().deriveFont(user_passwd.getFont().getStyle() | Font.ITALIC));

                //---- title_lb ----
                title_lb.setText("Welcome To The Java Class");
                title_lb.setFont(new Font("MathJax_Typewriter", Font.BOLD, 22));
                title_lb.setForeground(Color.darkGray);

                //---- javalogo_lb ----
                javalogo_lb.setIcon(new ImageIcon(getClass().getResource("/resource/javalogo2.png")));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGap(123, 123, 123)
                                    .addComponent(javalogo_lb)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(title_lb, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGap(101, 101, 101)
                                    .addGroup(contentPanelLayout.createParallelGroup()
                                        .addComponent(usrname_lb)
                                        .addComponent(passwd_lb, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(user_name, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                        .addComponent(user_passwd, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))))
                            .addContainerGap(99, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                                    .addComponent(title_lb)
                                    .addGap(11, 11, 11))
                                .addComponent(javalogo_lb, GroupLayout.Alignment.TRAILING))
                            .addGap(80, 80, 80)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usrname_lb)
                                .addComponent(user_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(21, 21, 21)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwd_lb)
                                .addComponent(user_passwd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(123, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- loginButton ----
                loginButton.setText("Sign up");
                loginButton.setIcon(new ImageIcon(getClass().getResource("/resource/login.png")));
                loginButton.setFont(loginButton.getFont().deriveFont(loginButton.getFont().getSize() + 3f));
                loginButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        loginButtonMouseClicked(e);
                    }
                });
                buttonBar.add(loginButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.setIcon(new ImageIcon(getClass().getResource("/resource/cancel.png")));
                cancelButton.setFont(cancelButton.getFont().deriveFont(cancelButton.getFont().getSize() + 3f));
                cancelButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cancelButtonMouseClicked(e);
                    }
                });
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public static void main(String args[]){
        changeLookAndFeel();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientLogin().setVisible(true);
            }
        });
    }
    static void changeLookAndFeel() {
        try{
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
                if("Windows".equals(info.getName())){
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel usrname_lb;
    private JTextField user_name;
    private JLabel passwd_lb;
    private JPasswordField user_passwd;
    private JLabel title_lb;
    private JLabel javalogo_lb;
    private JPanel buttonBar;
    private JButton loginButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
