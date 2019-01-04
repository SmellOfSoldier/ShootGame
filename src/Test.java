import Weapon.Mine;
import com.google.gson.Gson;
import person.Person;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test
{

    public static void main(String[] args)
    {
            /*Lock lock=new ReentrantLock();
            Condition condition=lock.newCondition();
            Thread1 t1 = new Thread1(lock,condition);
            Thread2 t2=new Thread2(lock,condition);
            JFrame jFrame = new JFrame();
            JButton jButton = new JButton("按钮1");
            JButton jButton1 = new JButton("按钮2");
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                   t1.threadWaite();
                   t2.start();

                }
            });
            jButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    t2.flag=true;
                    t1.threadWake();
                }
            });
            jFrame.add(jButton);
            jFrame.add(jButton1, BorderLayout.SOUTH);
            jFrame.pack();
            jFrame.setVisible(true);
            t1.start();*/
        Mine mine=new Mine();
        Gson gson=new Gson();
        String s=gson.toJson(mine);
        System.out.println("ok");
        Mine m=gson.fromJson(s,Mine.class);

    }
}

class A {
}

class B extends A
{

}
class Thread1 extends Thread
{
    Lock lock=new ReentrantLock();
    Condition condition=null;
    public static boolean flag=false;
    Thread1(Lock lock,Condition condition)
    {
        this.lock=lock;
        this.condition=condition;
    }
    public void threadWaite()
    {
        flag=true;
    }
    public void threadWake()
    {
        flag=false;
    }
    public void run()
    {
        try
        {
           lock.lock();
           while(true)
           {
               condition.await();
               System.out.println(1);
           }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

class Thread2 extends Thread
{
    Lock lock=null;
    Condition condition=null;
    public static boolean flag=false;
    Thread2(Lock lock,Condition condition)
    {
        this.lock=lock;
        this.condition=condition;
    }
    public void run()
    {
        try
        {
            lock.lock();
            while (true)
            {
                if (flag)
                {
                    condition.signalAll();
                    lock.unlock();
                    break;

                }
                System.out.println(0.5);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}