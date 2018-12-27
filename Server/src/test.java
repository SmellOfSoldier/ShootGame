import com.google.gson.Gson;
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

    public static void main(String[] args)
    {
        /*Client client=new Client("1","2");
        ServerGameRoom room=new ServerGameRoom("1",client,"djf");
        room.addClient(client);
        client.setGameRoom(room);
        System.out.println(room.getMaster().getRoom());
        System.out.println(client.getRoom());
        Gson gson=new Gson();
        String s=gson.toJson(room);
        System.out.println("ok");
        ServerGameRoom serverGameRoom1=gson.fromJson(s,ServerGameRoom.class);
        System.out.println(serverGameRoom1.getId());
        System.out.println(serverGameRoom1.getMaster().getId());*/
    }


}
