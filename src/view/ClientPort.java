package view;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ClientPort {
    public static Socket socket;
    public static PrintStream sendStream;
    public static BufferedReader getStream;
    public static List<Client> allOnlineClient= Collections.synchronizedList(new LinkedList<>());
    public static List<ServerGameRoom> allServerRoom=Collections.synchronizedList(new LinkedList<>());

    public static void main(String[] args)
    {
        new LoginFrame();

    }
}
