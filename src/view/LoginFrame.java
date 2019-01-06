package view;

import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import static view.ClientPort.*;

/**
 * 登陆界面覆写bylijie
 */
public class LoginFrame{
    private JFrame loginJFrame;
    private JFrame superiorMenu;
    private boolean isConnected;//标记是否与服务器连接
    private JTextArea contentArea;
    private JLabel accountlable;
    private JLabel passwordlable;
    private JButton btn_register;
    private JButton btn_link;
    private JTextField txt_account;
    private JPasswordField txt_password;
    private LoginPanel loginPanel;
    private RegisterFrame registerFrame;
    private boolean isStartRegisterFrame=false;
    //private JList userlist;
    //private DefaultListModel listModel;
    //TODO:多人联机使用未完成bylijie
    //private MessageThread messageThread;
    //private  volatile boolean  isConnected = false;
    //private Map<String,User> onlineUsers=new HashMap<String, User>();

    LoginFrame(JFrame superiorMenu,String ip){
        this.superiorMenu=superiorMenu;
        loginJFrame=new JFrame();
        loginJFrame.setTitle("登陆界面");
        loginJFrame.setSize(600,435);
        loginPanel=new LoginPanel();
        loginJFrame.setResizable(false);
        loginJFrame.setLocationRelativeTo(null);
        loginJFrame.add(loginPanel);
        loginJFrame.setLayout(null);
        loginJFrame.setVisible(true);
        //设置相对屏幕绝对位置l
        loginJFrame.setLocationRelativeTo(null);
        loginJFrame.setVisible(true);
        try {
            /**
             *
             * 多人游戏点击后直接连接服务器
             */
            //将静态保存有在线玩家和房间的静态链表清空再开始连接
            allOnlineClient.removeAll(allOnlineClient);
            allServerRoom.removeAll(allServerRoom);
            //开始连接服务器
            ClientPort.socket=new Socket();
            InetSocketAddress address=new InetSocketAddress(ip, 25565);
            socket.connect(address,2000);
            ClientPort.sendStream=new PrintStream(ClientPort.socket.getOutputStream());//获取写出流
            ClientPort.getStream=new BufferedReader(new InputStreamReader(ClientPort.socket.getInputStream()));//获取写入流

            String connectResult="failed";
            connectResult=ClientPort.getStream.readLine();
            if(connectResult.equals(Sign.SuccessConnected)){
                //TODO:与服务器成功建立连接
                isConnected=true;
                contentArea.append("请输入账号密码进行登陆。\r\n");
            }
        } catch (IOException e) {
            //如果没有连接上服务器则采取如下操作
            JOptionPane.showMessageDialog(loginJFrame, "服务器出现问题未响应连接请求，多人游戏不可使用。", "提示", JOptionPane.INFORMATION_MESSAGE);//弹出提示框
            loginJFrame.dispose();//使登陆界面消失
            if(superiorMenu!=null) superiorMenu.setVisible(true);//使游戏选择菜单出现
        }
        /**
         * 设置关闭连接的消息
         */
        loginJFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (isConnected) {
                    //先使登陆界面消失
                    loginJFrame.setVisible(false);
                    //发送Disconnect
                    ClientPort.sendStream.println(Sign.Disconnect);
                    isConnected=false;//连接状态置为false
                }
                try {
                    //睡眠500毫秒以将断开连接消息发送给服务端
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                loginJFrame.dispose();//使登陆界面消失
                if(superiorMenu!=null) superiorMenu.setVisible(true);//使游戏选择菜单出现
            }
        });
        /**
         * 登陆监听
         */
        btn_link.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                String result = null;//获得服务器发送过来的检验结果
                String allInfo=null;//初始化开启大厅信息
                String playerid=txt_account.getText().trim();
                String password=String.valueOf(txt_password.getPassword());
                if(playerid==null||password==null)
                {
                    JOptionPane.showMessageDialog(loginJFrame,"账号密码不能为空","提示",JOptionPane.ERROR_MESSAGE);//弹出警告框
                    return;
                }
                ClientPort.sendStream.println(Sign.Login+playerid+Sign.SplitSign+password);
                ClientPort.sendStream.flush();

                    result=ClientPort.getStream.readLine();//获取登陆结果

                System.out.println("接收到的回复消息为 "+result);
                switch (result){
                    case Sign.LoginSuccess:
                        {//登陆成功
                        JOptionPane.showMessageDialog(loginJFrame, "登陆成功", "提示", JOptionPane.INFORMATION_MESSAGE);//弹出提示框
                            allInfo=ClientPort.getStream.readLine();
                            String allclientsStr=allInfo.split(Sign.SplitSign)[0];
                            String roomStr=allInfo.split(Sign.SplitSign)[1];
                            String clientStr=allInfo.split(Sign.SplitSign)[2];
                            Gson gson=new Gson();
                            //解密读入数组
                            Client[] clients=gson.fromJson(allclientsStr,Client[].class);
                            ServerGameRoom[] rooms=gson.fromJson(roomStr,ServerGameRoom[].class);
                            Client me=gson.fromJson(clientStr,Client.class);
                            for(Client c:clients){
                                ClientPort.allOnlineClient.add(c);
                            }
                            for (ServerGameRoom r:rooms){
                                allServerRoom.add(r);
                            }
                        ClientPort.gameHall=new GameHall(me,ip);//创建游戏大厅并传入登陆者的实例对象
                            //登陆界面关闭
                            loginJFrame.dispose();
                        break;
                    }
                    case Sign.WrongPassword:{//密码错误
                        JOptionPane.showMessageDialog(loginJFrame,"账号密码错误请重新尝试","提示",JOptionPane.ERROR_MESSAGE);//弹出警告框
                        break;
                    }
                    case  Sign.IsNotRegistered:{//还未注册
                        JOptionPane.showMessageDialog(loginJFrame,"您的账号还未注册","提示",JOptionPane.ERROR_MESSAGE);//弹出警告框
                        break;
                    }
                }
             } catch (IOException e1) {
                e1.printStackTrace();
            }
            }
        });
        /**
         * 注册监听
         */
        btn_register.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
              new RegisterFrame(ClientPort.sendStream,ClientPort.getStream);
            }
        });
    }
    //内部类区域
    //公告区域
    class LoginPanel extends JPanel
    {
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
            txt_password=new JPasswordField();
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


}
