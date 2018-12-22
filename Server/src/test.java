import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import sun.misc.SignalHandler;

import javax.xml.bind.SchemaOutputResolver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class test extends Thread {
    String a;
    Socket socket;
    BufferedReader bufferedReader;
    PrintStream printStream;
    boolean isConnected = false;

    public static void main(String[] args) {
        new test().start();
    }

    public void run() {
        int i = 0;
        while (i != 10) {
            System.out.println("等待中");
            if (!isConnected) {
                try {
                    socket = new Socket("127.0.0.1", 25565);
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    printStream = new PrintStream(socket.getOutputStream());
                    isConnected = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                a = bufferedReader.readLine();
                switch (a) {
                    case Sign.IsNotRegistered: {
                        System.out.println("还未注册。");
                        break;
                    }
                    case Sign.IsRegistered:{
                        System.out.println("已经注册过请更改id");
                        break;
                    }
                    case Sign.LoginSuccess: {
                        System.out.println("登陆成功");
                        break;
                    }
                    case Sign.RegisterSuccess: {
                        System.out.println("注册成功。");
                        break;
                    }
                    case Sign.SuccessConnected: {
                        System.out.println("与服务器连接成功。");
                        break;
                    }
                    case Sign.WrongPassword:{
                        System.out.println("密码错误无法连接");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("2.lijielogin\r\n" + "3.dengzonglogin\r\n" + "4.lijieregister\r\n"+"5.dengzongregister");
            Scanner scanner = new Scanner(System.in);
            i = scanner.nextInt();
            if(i==1){
            }
            else if (i == 2) {
                printStream.println(Sign.Login + "lijie" + Sign.SplitSign + "254698");
                printStream.flush();
            }else if (i == 3) {
                printStream.println(Sign.Login + "dengzong" + Sign.SplitSign + "123456");
                printStream.flush();
            } else if (i == 4) {
                printStream.println(Sign.Register + "lijie" + Sign.SplitSign + "254698");
                printStream.flush();
            }else if (i == 5) {
                printStream.println(Sign.Register + "dengzong" + Sign.SplitSign + "123456");
                printStream.flush();
            }

        }
    }
}
