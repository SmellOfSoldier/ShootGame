package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class LoginFrame extends JFrame {
    private boolean isConnected;//标记是否与服务器连接
    private JFrame frame;
    private JTextArea contentArea;
    private JLabel accountlable;
    private JLabel passwordlable;
    private JButton btn_register;
    private JButton btn_link;
    private JTextField txt_account;
    private JTextField txt_password;
    private LoginPanel loginPanel;
    //private JList userlist;
    //private DefaultListModel listModel;
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    //TODO:多人联机使用未完成bylijie
    //private MessageThread messageThread;
    //private  volatile boolean  isConnected = false;
    //private Map<String,User> onlineUsers=new HashMap<String, User>();

    LoginFrame(){
        this.setTitle("登陆界面");
        this.setSize(600,435);
        loginPanel=new LoginPanel();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(loginPanel);
        this.setLayout(null);
        this.setVisible(true);
        //设置相对屏幕绝对位置
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setLocation((screen_width - this.getWidth()) / 2,
                (screen_height - this.getHeight()) / 2);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (isConnected) {
                    //closeConnection();// 关闭连接
                }
                System.exit(0);// 退出程序
            }
        });
    }
    //内部类区域
    //公告区域
    class LoginPanel extends JPanel{
        LoginPanel(){
            initial();
            this.setSize(600,435);
            this.setLocation(0,0);
            this.setLayout(null);
        }
        private void initial(){
            accountlable=new JLabel("账号");
            passwordlable=new JLabel("密码");
            btn_link=new JButton("登陆");
            btn_register=new JButton("注册");
            txt_account=new JTextField();
            txt_password=new JTextField();
            contentArea=new JTextArea();
            this.add(accountlable);
            this.add(passwordlable);
            this.add(btn_link);
            this.add(btn_register);
            this.add(txt_account);
            this.add(txt_password);
            this.add(contentArea);
            //公告位置大小
            contentArea.setSize(520,220);
            contentArea.setLocation(40,20);
            //账号位置
            accountlable.setLocation(100,260);
            accountlable.setSize(60,30);
            txt_account.setLocation(150,260);
            txt_account.setSize(300,30);
            //密码位置
            passwordlable.setLocation(100,310);
            passwordlable.setSize(60,30);
            txt_password.setLocation(150,310);
            txt_password.setSize(300,30);
            //登陆注册位置
            btn_link.setLocation(225,360);
            btn_link.setSize(150,30);
            btn_register.setLocation(540,375);
            btn_register.setSize(60,25);
            //btn_link.setLocation();
            contentArea.append("公告区显示测试。");
            this.setVisible(true);
        }
    }
    public static void main(String[] args) {
        new LoginFrame();
    }

}
