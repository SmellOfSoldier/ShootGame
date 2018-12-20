package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterFrame extends JFrame {
    private JTextArea contentArea;
    private JLabel namelable;
    private JLabel accountlable;
    private JLabel passwordlable;
    private JLabel confirmPasswordlable;
    private JButton btn_register;
    private JButton btn_exit;
    private JTextField txt_name;
    private JTextField txt_account;
    private JPasswordField txt_password;
    private JPasswordField txt_conpassword;
    private Registerpanel registerpanel;
    RegisterFrame(){
        registerpanel=new Registerpanel();
        this.setTitle("注册界面");
        this.add(registerpanel);
        this.setSize(520,300);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
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

            }
        });
    }
    class Registerpanel extends JPanel{
        Registerpanel(){
            this.setSize(520,300);
            this.setLocation(0,0);
            this.setLayout(null);
            initial();
        }
        private void initial(){
            contentArea=new JTextArea();
            namelable=new JLabel("昵称");
            accountlable=new JLabel("账号");
            passwordlable=new JLabel("密码");
            confirmPasswordlable=new JLabel("确认密码");
            btn_register=new JButton("注册");
            btn_exit=new JButton("取消");
            txt_name=new JTextField();
            txt_account=new JTextField();
            txt_password=new JPasswordField();
            txt_conpassword=new JPasswordField();
            this.add(contentArea);
            this.add(namelable);
            this.add(accountlable);
            this.add(passwordlable);
            this.add(confirmPasswordlable);
            this.add(btn_register);
            this.add(btn_exit);
            this.add(txt_name);
            this.add(txt_account);
            this.add(txt_password);
            this.add(txt_conpassword);
            //TODO:注册帮助
            contentArea.append("注册帮助（待完成）");
            //设置帮助消息栏为不可更改
            contentArea.setEditable(false);
            //设置位置
            //公告栏位置大小
            contentArea.setSize(200,300);
            contentArea.setLocation(0,0);
            //昵称位置大小
            namelable.setSize(60,30);
            namelable.setLocation(200,10);
            txt_name.setSize(230,30);
            txt_name.setLocation(260,10);
            //账号位置大小
            accountlable.setSize(60,30);
            accountlable.setLocation(200,50);
            txt_account.setSize(230,30);
            txt_account.setLocation(260,50);
            //第一次密码位置大小
            passwordlable.setSize(60,30);
            passwordlable.setLocation(200,90);
            txt_password.setSize(230,30);
            txt_password.setLocation(260,90);
            //确认密码位置大小
            confirmPasswordlable.setSize(60,30);
            confirmPasswordlable.setLocation(200,130);
            txt_conpassword.setSize(230,30);
            txt_conpassword.setLocation(260,130);
            //注册
            btn_register.setSize(110,30);
            btn_register.setLocation(315,170);
            //取消
            btn_exit.setSize(110,30);
            btn_exit.setLocation(315,210);
        }
    }

    //测试
    public static void main(String[] args) {
        new RegisterFrame();
    }

}
