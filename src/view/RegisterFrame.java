package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class RegisterFrame {
    private JFrame registerJFrame;
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
    private PrintStream sendStream;
    private BufferedReader getStream;
    RegisterFrame(PrintStream sendStream,BufferedReader getStream){
        registerpanel=new Registerpanel();
        registerJFrame=new JFrame();
        this.sendStream=sendStream;
        this.getStream=getStream;
        registerJFrame.setTitle("注册界面");
        registerJFrame.add(registerpanel);
        registerJFrame.setSize(520,300);
        registerJFrame.setResizable(false);
        registerJFrame.setLocationRelativeTo(null);
        registerJFrame.setLayout(null);
        registerJFrame.setVisible(true);
        //设置相对屏幕绝对位置
        registerJFrame.setLocationRelativeTo(null);
        registerJFrame.setVisible(true);
        /**
         * 注册关闭监听
         */
        registerJFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                registerJFrame.setVisible(false);//关闭事件设为此窗口不可见
                registerJFrame.dispose();
            }
        });
        /**
         * 注册监听
         */
        btn_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String account = txt_account.getText().trim();//获取注册ID
                String password = String.valueOf(txt_password.getPassword());//获取注册密码
                String confirmpassword = String.valueOf(txt_conpassword.getPassword());//获取确认密码
                if (confirmpassword.equals(password)) {//检查输入密码是否相同
                    sendStream.println(Sign.Register + account + Sign.SplitSign + password);//发送注册信息
                    sendStream.flush();
                    String registeResult = null;
                    try {
                        registeResult = getStream.readLine();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    switch (registeResult) {
                        case Sign.RegisterSuccess: {
                            JOptionPane.showMessageDialog(registerJFrame, "注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);//弹出提示框
                            //注册成功并开始清空所有输入格
                            clearAllBlanks();
                            break;
                        }
                        case Sign.IsRegistered: {
                            JOptionPane.showMessageDialog(registerJFrame, "账号已经被注册过了", "提示", JOptionPane.INFORMATION_MESSAGE);//弹出提示框
                            break;
                        }
                    }
                }
                else {
                        JOptionPane.showMessageDialog(registerJFrame, "密码输入不相同请重新输入", "提示", JOptionPane.INFORMATION_MESSAGE);//弹出密码输入不同提示框

                }
            }
        });
        btn_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //关闭注册界面
                registerJFrame.dispose();
            }
        });
    }

    /**
     * 输入框全部清空函数 再次开启时清空空格方便多次注册
     */
    public void clearAllBlanks(){
        this.txt_account.setText("");
        this.txt_password.setText("");
        this.txt_name.setText("");
        this.txt_conpassword.setText("");
    }
    /**
     * 设置是否可见
     */
    public void setVisible(boolean flag){
        this.setVisible(flag);
    }
    /**
     * 内部布局管理
     */
    class Registerpanel extends JPanel{
        Registerpanel(){
            this.setSize(520,300);
            this.setLocation(0,0);
            this.setLayout(null);
            initial();
        }
        private void initial(){
            contentArea=new JTextArea();
            namelable=new JLabel("昵称");//暂时不考虑
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
}
