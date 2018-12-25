package view;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintStream;

/**
 * 创建房间的GUI界面类
 */
public class creatRoomGui {
    private JFrame creatRoomJFrame;
    private creatRoomJPanel creatRoomjpanel;
    private JLabel roomnamelable;
    private JLabel maxclientnumlable;
    private JButton btn_creat;
    private JButton btn_exit;
    private JTextField txt_room_name;
    private JTextField txt_maxclients;
    private PrintStream sendStream;
    private BufferedReader getStream;
    creatRoomGui(){
        this.sendStream=sendStream;
        this.getStream=getStream;
        creatRoomjpanel=new creatRoomJPanel();
        creatRoomJFrame=new JFrame();
        this.sendStream=sendStream;
        this.getStream=getStream;
        creatRoomJFrame.setTitle("创建房间");
        creatRoomJFrame.add(creatRoomjpanel);
        creatRoomJFrame.setSize(300,200);
        creatRoomJFrame.setResizable(false);
        creatRoomJFrame.setLayout(null);
        creatRoomJFrame.setVisible(true);
        //设置相对屏幕绝对位置
        creatRoomJFrame.setLocationRelativeTo(null);
        creatRoomJFrame.setVisible(true);
    }

    /**
     * 内部类定义房间创建GUI的布局
     */
    class creatRoomJPanel extends  JPanel{
        creatRoomJPanel(){
            this.setSize(300,200);
            this.setLayout(null);
            this.setLocation(0,0);
            initial();
        }
        private void initial(){
            roomnamelable=new JLabel("房间名字");//暂时不考虑
            maxclientnumlable =new JLabel("最大人数");//暂时只能为4
            btn_creat=new JButton("开启");
            btn_exit=new JButton("取消");
            txt_room_name =new JTextField();
            txt_maxclients=new JTextField();
            txt_maxclients.setText("4");
            txt_maxclients.setEditable(false);
            this.add(roomnamelable);
            this.add(maxclientnumlable);
            this.add(btn_creat);
            this.add(btn_exit);
            this.add(txt_room_name);
            this.add(txt_maxclients);
            //TODO:注册帮助
            //设置位置
            //公告栏位置大小
            //昵称位置大小
            roomnamelable.setSize(70,30);
            roomnamelable.setLocation(30,35);
            maxclientnumlable.setSize(70,30);
            maxclientnumlable.setLocation(30,70);
            //账号位置大小
            txt_room_name.setSize(120,30);
            txt_room_name.setLocation(110,35);
            txt_maxclients.setSize(120,30);
            txt_maxclients.setLocation(110,70);
            //注册
            btn_creat.setSize(60,30);
            btn_creat.setLocation(80,110);
            //取消
            btn_exit.setSize(60,30);
            btn_exit.setLocation(170,110);
        }
    }

    public static void main(String[] args) {
        new creatRoomGui();
    }
}
