/*
 * Created by JFormDesigner on Thu Dec 12 19:13:07 CST 2019
 */

package view;

import backend.MultithreadSocketServer;
import connection.MySqlConnect;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;


/**
 * @author jiengup
 */
public class ServerLogin extends JFrame {
    public static String superPassword;
    private static Connection conn = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    public ServerLogin() throws SQLException {
        initComponents();
        String sql = "select super_password from super where super_password_id = (select max(super_password_id) from super)";
        String correctPassword = "";
        conn = MySqlConnect.connectDB();
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                correctPassword = rs.getString(1);
                System.out.println("Return password : " + correctPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            pst.close();
        }
        superPassword = correctPassword;
    }

    public static void changePassword(String newPassword, String oldPassword) throws SQLException {
        if(!oldPassword.equalsIgnoreCase(superPassword)){
            JOptionPane.showMessageDialog(null, "Old Password Incorrect", "Alert", JOptionPane.ERROR_MESSAGE);
        }
        else{
            conn = MySqlConnect.connectDB();
            String sql = "insert into super values(null, ?)";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, newPassword);
                pst.executeUpdate();
                superPassword = newPassword;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                pst.close();
            }
        }
    }

    private void cancelButtonMouseClicked(MouseEvent e) {
        System.exit(0);
    }

    private void enterButtonMouseClicked(MouseEvent e) {
        String inputPassword = new String(passwordField1.getPassword());
        if(inputPassword.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Please input your password", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        else{
            if(inputPassword.equals(superPassword)){

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ServerAfterLogin sal = new ServerAfterLogin();
                        sal.setVisible(true);
                    }
                });
                this.dispose();
            }
            else{
                JOptionPane.showMessageDialog(this, "Incorrect Password\nPlease Enter Again.", "Message", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void thisWindowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        passwd_lb = new JLabel();
        title_lb = new JLabel();
        javalogo_lb = new JLabel();
        passwordField1 = new JPasswordField();
        label2 = new JLabel();
        buttonBar = new JPanel();
        enterButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setResizable(false);
        setTitle("Server Login");
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

                //---- passwd_lb ----
                passwd_lb.setText("Super Administor's Password please:");
                passwd_lb.setFont(passwd_lb.getFont().deriveFont(passwd_lb.getFont().getSize() + 3f));

                //---- title_lb ----
                title_lb.setText("Java Class Management");
                title_lb.setFont(new Font("MathJax_Typewriter", Font.BOLD, 22));
                title_lb.setForeground(Color.darkGray);

                //---- javalogo_lb ----
                javalogo_lb.setIcon(new ImageIcon(getClass().getResource("/resource/javalogo2.png")));

                //---- passwordField1 ----
                passwordField1.setToolTipText("your password here");

                //---- label2 ----
                label2.setText("Dear Mr Yu: ");
                label2.setFont(new Font("Segoe Print", Font.BOLD, 15));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(0, 121, Short.MAX_VALUE)
                            .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, 329, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(114, Short.MAX_VALUE))
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(88, 88, 88)
                            .addComponent(label2)
                            .addContainerGap(376, Short.MAX_VALUE))
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(135, 135, 135)
                            .addComponent(passwd_lb)
                            .addContainerGap(149, Short.MAX_VALUE))
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(120, 120, 120)
                            .addComponent(javalogo_lb)
                            .addGap(18, 18, 18)
                            .addComponent(title_lb, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(125, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(32, 32, 32)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGap(24, 24, 24)
                                    .addComponent(title_lb))
                                .addComponent(javalogo_lb))
                            .addGap(46, 46, 46)
                            .addComponent(label2)
                            .addGap(18, 18, 18)
                            .addComponent(passwd_lb)
                            .addGap(29, 29, 29)
                            .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(73, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- enterButton ----
                enterButton.setText("Enter");
                enterButton.setIcon(new ImageIcon(getClass().getResource("/resource/login.png")));
                enterButton.setFont(enterButton.getFont().deriveFont(enterButton.getFont().getSize() + 3f));
                enterButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        enterButtonMouseClicked(e);
                    }
                });
                buttonBar.add(enterButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
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
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public static void main(String[] args){
        ClientLogin.changeLookAndFeel();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ServerLogin().setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel passwd_lb;
    private JLabel title_lb;
    private JLabel javalogo_lb;
    private JPasswordField passwordField1;
    private JLabel label2;
    private JPanel buttonBar;
    private JButton enterButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
