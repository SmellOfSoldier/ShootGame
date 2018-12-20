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
    private AnnouncementArea announcementArea;
    private JLabel accountlable;
    private JLabel passwordlable;
    private JButton btn_register;
    private JButton btn_link;
    private JTextField txt_account;
    private JTextField txt_password;
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
        frame = new JFrame("登陆界面");
        frame.setLayout(null);
        announcementArea=new AnnouncementArea();
        accountlable=new JLabel("账号");
        passwordlable=new JLabel("密码");
        btn_link=new JButton("登陆");
        btn_register=new JButton("注册");
        txt_account=new JTextField();
        txt_password=new JTextField();
        frame.add(announcementArea);
        frame.add(accountlable);
        frame.add(passwordlable);
        frame.add(btn_link);
        frame.add(btn_register);
        frame.add(txt_account);
        frame.add(txt_password);
        txt_password.setLocation(50,260);
        txt_password.setSize(300,30);
        frame.setSize(600, 400);
        //设置相对屏幕绝对位置
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation((screen_width - frame.getWidth()) / 2,
                (screen_height - frame.getHeight()) / 2);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
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
    class AnnouncementArea extends JPanel{
        AnnouncementArea(){
            initial();
            this.setSize(550,225);
            this.setLayout(null);
            this.setLocation(25,25);
        }
        private void initial(){
            contentArea=new JTextArea();
            this.add(contentArea);
        }
    }




    public static void main(String[] args) {
        new LoginFrame();
    }

}
