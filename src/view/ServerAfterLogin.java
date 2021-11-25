/*
 * Created by JFormDesigner on Fri Dec 13 20:53:48 CST 2019
 */

package view;

import backend.MultithreadSocketServer;
import connection.MySqlConnect;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.filechooser.FileSystemView;

/**
 * @author jiengup
 */
public class ServerAfterLogin extends JFrame {
    private int lessonTimes;
    private int totalStudents;
    private int presentStudents;
    private MultithreadSocketServer multiSocket;
    private ArrayList<String> multiClientName;

    public ServerAfterLogin() {
        initComponents();
    }
    private void comfigBtnMouseClicked(MouseEvent e) {
        // TODO add your code here
        if(!comfigBtn.isEnabled()) return;
        lessonTimes = Integer.parseInt(lesson_times.getText());
        totalStudents = Integer.parseInt(total_students.getText());
        System.out.println("Lesson Times : " + lessonTimes + "Total Student : " + totalStudents);
        multiSocket = new MultithreadSocketServer(totalStudents, lessonTimes);
        new Thread(multiSocket).start();
        sentBtn.setEnabled(true);
        endBtn.setEnabled(true);
        textArea1.setEnabled(true);
        comfigBtn.setEnabled(false);
        list1.setEnabled(true);
        run();
    }


    private void changePasswordActionPerformed(ActionEvent e) {
        ChangePasswordForm cpf = new ChangePasswordForm();
        cpf.setVisible(true);
    }

    private void changePasswordMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void sentBtnMouseClicked(MouseEvent e) {
        if(!sentBtn.isEnabled()) return;
        String broadcast = textArea1.getText();
        multiSocket.broadCast(broadcast);
    }

    private void endBtnMouseClicked(MouseEvent e) {
        System.out.println("Class End");
        multiSocket.endServer();
        System.exit(0);
    }

    private void ExportSignupTableMouseClicked(MouseEvent e) {

    }

    private void ExportSignupTableActionPerformed(ActionEvent e) {
        String sql = "select distinct a1.userId ID, a1.name, a1.time signinTime, a2.time logoutTime from(select userId, name, time from signInRecord where time in (select min(time) from signInRecord where to_days(time) = to_days(now()) group by signInRecord.userId)) as a1, (select userId, name, time from logOutRecord where time in (select max(time) from logOutRecord where to_days(time) = to_days(now()) group by logOutRecord.userId)) as a2 where a1.userId = a2.userId";
        Connection conn = MySqlConnect.connectDB();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date()).substring(0, 10);
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String dirPath = fsv.getHomeDirectory() + "/temp/";
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        try{
            File recordFile = new File(dir.getPath() + "/" + date + "signin&logouttable.csv");
            if(recordFile.exists()){
                recordFile.createNewFile();
            }

            OutputStreamWriter op = new OutputStreamWriter(new FileOutputStream(recordFile), "utf-8");
            op.append("ID,name,signinTime,logoutTime\n");
            op.flush();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String record = rs.getString(1) + "," + rs.getString(2) + "," + rs.getDate(3) + "," + rs.getDate(4) + "\n";
                System.out.println(record);
                op.append(record);
                op.flush();
            }
            op.append(("present:" + presentStudents + "total:" + totalStudents));
            op.flush();
            op.close();
            System.out.println("Export Success at : " + recordFile.getPath());
            JOptionPane.showMessageDialog(null, "Export Success at : " + recordFile.getPath(), "Message", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        changePassword = new JMenuItem();
        ExportSignupTable = new JMenuItem();
        scrollPane1 = new JScrollPane();
        list1 = new JList();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        comfigBtn = new JButton();
        lesson_times = new JTextField();
        total_students = new JTextField();
        label4 = new JLabel();
        content_lb9 = new JLabel();
        scrollPane2 = new JScrollPane();
        textArea1 = new JTextArea();
        sentBtn = new JButton();
        endBtn = new JButton();
        presentInfo = new JLabel();
        presentInfo2 = new JLabel();

        //======== this ========
        setTitle("Server");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        Container contentPane = getContentPane();

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("Tools");

                //---- changePassword ----
                changePassword.setText("Change Password");
                changePassword.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        changePasswordMouseClicked(e);
                    }
                });
                changePassword.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePasswordActionPerformed(e);
                    }
                });
                menu1.add(changePassword);

                //---- ExportSignupTable ----
                ExportSignupTable.setText("Export Signup Table");
                ExportSignupTable.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ExportSignupTableMouseClicked(e);
                    }
                });
                ExportSignupTable.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ExportSignupTableActionPerformed(e);
                    }
                });
                menu1.add(ExportSignupTable);
            }
            menuBar1.add(menu1);
        }
        setJMenuBar(menuBar1);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(list1);
        }

        //---- label1 ----
        label1.setIcon(new ImageIcon(getClass().getResource("/resource/head.png")));

        //---- label2 ----
        label2.setText("Lesson Times");
        label2.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 16));

        //---- label3 ----
        label3.setText("Total Students");
        label3.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 16));

        //---- comfigBtn ----
        comfigBtn.setText("Config and start class");
        comfigBtn.setFont(comfigBtn.getFont().deriveFont(comfigBtn.getFont().getSize() + 3f));
        comfigBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                comfigBtnMouseClicked(e);
            }
        });

        //---- lesson_times ----
        lesson_times.setFont(lesson_times.getFont().deriveFont(lesson_times.getFont().getSize() + 2f));

        //---- total_students ----
        total_students.setFont(total_students.getFont().deriveFont(total_students.getFont().getSize() + 2f));

        //---- label4 ----
        label4.setText("Powered By javax.Swing");
        label4.setForeground(Color.gray);
        label4.setFont(new Font("DejaVu Sans", Font.ITALIC, 10));

        //---- content_lb9 ----
        content_lb9.setFont(new Font("MathJax_Typewriter", Font.PLAIN, 20));
        content_lb9.setText("Broadcast");

        //======== scrollPane2 ========
        {

            //---- textArea1 ----
            textArea1.setEnabled(false);
            scrollPane2.setViewportView(textArea1);
        }

        //---- sentBtn ----
        sentBtn.setText("Sent");
        sentBtn.setFont(sentBtn.getFont().deriveFont(sentBtn.getFont().getSize() + 3f));
        sentBtn.setEnabled(false);
        sentBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sentBtnMouseClicked(e);
            }
        });

        //---- endBtn ----
        endBtn.setText("End Class");
        endBtn.setFont(endBtn.getFont().deriveFont(endBtn.getFont().getSize() + 3f));
        endBtn.setForeground(Color.red);
        endBtn.setEnabled(false);
        endBtn.setBackground(Color.red);
        endBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                endBtnMouseClicked(e);
            }
        });

        //---- presentInfo ----
        presentInfo.setFont(presentInfo.getFont().deriveFont(presentInfo.getFont().getSize() + 2f));
        presentInfo.setText("present:10/total:31");

        //---- presentInfo2 ----
        presentInfo2.setFont(presentInfo2.getFont().deriveFont(presentInfo2.getFont().getSize() + 2f));
        presentInfo2.setText("*:task competed");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(238, 238, 238)
                    .addComponent(label1)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addGap(313, 313, 313)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(label4)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 247, Short.MAX_VALUE)
                            .addComponent(endBtn)
                            .addContainerGap())
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(sentBtn)
                            .addGap(84, 410, Short.MAX_VALUE))))
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addGap(93, 93, 93)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(11, 11, 11)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(comfigBtn)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label3)
                                        .addComponent(label2))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(lesson_times, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(total_students, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))))
                        .addComponent(content_lb9)
                        .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(presentInfo2)
                                .addComponent(presentInfo))))
                    .addGap(52, 52, 52))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(label1)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label2)
                                .addComponent(lesson_times, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label3)
                                .addComponent(total_students, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addComponent(comfigBtn)
                            .addGap(18, 18, 18)
                            .addComponent(content_lb9)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(sentBtn)
                            .addGap(50, 50, 50)
                            .addComponent(label4))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(presentInfo)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(presentInfo2)
                            .addGap(11, 11, 11)
                            .addComponent(endBtn, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(7, Short.MAX_VALUE))
        );
        setSize(790, 555);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    private void run(){
        new Thread(){
            public void run(){
                while(true) {
                    presentStudents = 0;
                    multiClientName = multiSocket.getTotalClientName();
                    if (!multiClientName.isEmpty()) {
                        DefaultListModel<String> listModel = new DefaultListModel<>();
                        for(Iterator<String> item = multiClientName.iterator(); item.hasNext(); ){
                            listModel.addElement(item.next());
                            presentStudents++;
                        }
                        list1.setModel(listModel);
                    }
                    presentInfo.setText("present:" + presentStudents + "total:" + totalStudents);
                }
                }
        }.start();
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem changePassword;
    private JMenuItem ExportSignupTable;
    private JScrollPane scrollPane1;
    private JList list1;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JButton comfigBtn;
    private JTextField lesson_times;
    private JTextField total_students;
    private JLabel label4;
    private JLabel content_lb9;
    private JScrollPane scrollPane2;
    private JTextArea textArea1;
    private JButton sentBtn;
    private JButton endBtn;
    private JLabel presentInfo;
    private JLabel presentInfo2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
class ChangePasswordForm extends JFrame {
    public ChangePasswordForm() {
        initComponents();
    }

    private void okButtonMouseClicked(MouseEvent e) {
        String oldPassword = old_password.getText();
        String newPassword = new_password.getText();
        String newPasswordAgain = new_password_again.getText();
        if(newPassword.equalsIgnoreCase(newPasswordAgain)){
            try{
                System.out.println(ServerLogin.superPassword);
                ServerLogin.changePassword(newPassword, oldPassword);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Two New Password Doesn't Match", "Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelButtonMouseClicked(MouseEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        old_password = new JTextField();
        new_password = new JTextField();
        label2 = new JLabel();
        new_password_again = new JTextField();
        label3 = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- label1 ----
                label1.setText("Old Password");
                label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 3f));

                //---- label2 ----
                label2.setText("New Password");
                label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 3f));

                //---- label3 ----
                label3.setText("New Password Again");
                label3.setFont(label3.getFont().deriveFont(label3.getFont().getSize() + 3f));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addGap(129, 129, 129)
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addComponent(label1)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(old_password, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(GroupLayout.Alignment.LEADING, contentPanelLayout.createSequentialGroup()
                                                        .addComponent(label2)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(new_password, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(GroupLayout.Alignment.LEADING, contentPanelLayout.createSequentialGroup()
                                                        .addComponent(label3)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(new_password_again, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)))
                                        .addContainerGap(149, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addGap(125, 125, 125)
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(old_password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label1))
                                        .addGap(18, 18, 18)
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(new_password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label2))
                                        .addGap(18, 18, 18)
                                        .addGroup(contentPanelLayout.createParallelGroup()
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addGap(1, 1, 1)
                                                        .addComponent(label3))
                                                .addComponent(new_password_again, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(160, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        okButtonMouseClicked(e);
                    }
                });
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
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

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField old_password;
    private JTextField new_password;
    private JLabel label2;
    private JTextField new_password_again;
    private JLabel label3;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

