import com.google.gson.Gson;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

/**
 * 玩家服务线程
 */
class ServerClientThread extends Thread {
    private Client client = null;
    private Socket socket;
    private PrintStream sendStream;
    private BufferedReader getStream;
    private JTextArea GuiShowMes;
    private boolean isConnected = false;//是否连接
    private boolean isLogin = false;//是否登陆

    /**
     * 获取此线程实例对象的Gamer
     *
     * @return
     */
    public Client getClient() {
        return client;
    }

    /**
     * 玩家服务线程构造
     *
     * @param socket 属于实例对象(一位玩家)的Socket通道
     */
    public ServerClientThread(Socket socket, PrintStream sendStream, BufferedReader getStream, JTextArea GuiShowMes) {
        this.socket = socket;
        this.sendStream = sendStream;//获取写出流
        this.getStream = getStream;//获取写入流
        this.GuiShowMes=GuiShowMes;//获得界面JTextarea对象
        System.out.println("成功创建一个玩家服务线程");
    }

    /**
     * 玩家服务线程run函数
     */
    public void run() {
        String line = null;//接收到的初始字符串（信息）
        String command = null;//当前获取的信息需要执行的命令
        String realMessage = null;//去除头部命令的信息
        //线程不被interrupted则持续接收玩家发来的信息
        try {
        while (!this.isInterrupted() &&(line=getStream.readLine())!=null) {
            if (isConnected) {

                    //TODO:服务线程run待完成
                    System.out.println("线程阻塞中等待命令。");
                    System.out.println("收到一个命令信息" + line);
                    /**
                     * 如果是登陆则采取如下操作
                     */
                    if (!isLogin && line.startsWith(Sign.Login))
                    {
                        try {
                            System.out.println("进度登陆函数");
                            int loginResult = check.checkLoginInfo(line);
                            System.out.println("登陆结果为" + loginResult);//1为成功 -1为账号未注册  为密码错误
                            switch (loginResult) {
                                case 1: {
                                    Gson gson = new Gson();
                                    realMessage=check.getRealMessage(line,Sign.Login);
                                    String id=realMessage.split(Sign.SplitSign)[0];
                                    isLogin = true;//密码成功则将当前玩家的服务线程登陆状态置为true
                                    for(Client c:CreatServer.allPlayer)
                                    {
                                        if(c.getId().equals(id))
                                        {
                                            client=c;
                                        }
                                    }
                                    client.setOline(true);//将玩家置为在线状态
                                    GuiShowMes.append("服务器消息：玩家： "+client.getId()+" 成登陆进入服务器。\n");//在guii显示登陆信息
                                    String clientStr = gson.toJson(client);//将登陆玩家序列化方便发送给其他玩家
                                    // 通知其他所有在线玩家该玩家上线
                                    for (PrintStream allsendstream : CreatServer.clientPrintStreamMap.values()) {
                                        allsendstream.println(Sign.OneClientOnline + client.getId());
                                    }

                                    CreatServer.onlineClients.add(client);//在在线玩家列表中加入玩家

                                    CreatServer.clientPrintStreamMap.put(client, sendStream);//加入玩家写流
                                    System.out.println("发送序列化在线玩家列表。 大小为"+CreatServer.onlineClients.size());
                                    //打包发送初始化消息
                                    String allclientsStr = gson.toJson(CreatServer.onlineClients);
                                    String roomStr = gson.toJson(CreatServer.allGameRoom);
                                    //打包发送
                                    sendStream.println(Sign.LoginSuccess);
                                    sendStream.println(allclientsStr + Sign.SplitSign + roomStr + Sign.SplitSign + clientStr);
                                    break;
                                }
                                case -1: {
                                    sendCommand(Sign.IsNotRegistered);//返回账号还未注册的消息
                                    break;
                                }
                                case 0: {
                                    sendCommand(Sign.WrongPassword);//返回密码错误的消息
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    /**
                     * 如果接收到注册请求
                     */
                    else if (!isLogin && line.startsWith(Sign.Register)) {
                        System.out.println("收到注册请求开始注册流程。");
                        //分割命令与内容
                        realMessage = check.getRealMessage(line, Sign.Register);
                        String playerid = realMessage.split(Sign.SplitSign)[0];
                        String playerPassword = realMessage.split(Sign.SplitSign)[1];
                        System.out.println("注册请求名字："+playerid);
                        if (!check.isRegistered(playerid)) {
                            System.out.println(CreatServer.allPlayer.size());
                            saveorreadInfo.addClient(new Client(playerid,playerPassword));//注册一个玩家到内存
                            System.out.println(CreatServer.allPlayer.size());
                            sendCommand(Sign.RegisterSuccess);//返回注册成功信息
                            GuiShowMes.append("服务器消息：玩家："+playerid+" 成功注册。\n");//gui界面显示注册成功消息
                        } else sendCommand(Sign.IsRegistered);//否则返回已经注册过的消息
                    }
                    /**
                     * 如果收到创建房间信息
                     */
                    else if (isLogin && line.startsWith(Sign.CreateRoom)) {
                        Gson gson=new Gson();
                        System.out.println("收到创建房间请求。");
                        realMessage = check.getRealMessage(line, Sign.CreateRoom);
                        ServerGameRoom serverGameRoom = new ServerGameRoom(client.getId(), client, realMessage);//以玩家的id和对象还有发来的房间名字创建房间
                        System.out.println(client.getId());
                        System.out.println(realMessage);
                        CreatServer.allGameRoom.add(serverGameRoom);
                        //设置当前玩家房间为自己创建的房间
                        client.setGameRoomID(serverGameRoom.getId());
                        String roomStr=gson.toJson(serverGameRoom);
                        System.out.println("成功创建名字为" + realMessage + "的房间。");
                        GuiShowMes.append("服务器消息：玩家："+client.getId()+" 成功创建名字为 "+client.getId()+" 的房间。\n");//gui界面显示成功创建房间信息
                        sendStream.println(Sign.PermissionCreateRoom +roomStr);
                        int i=0;
                        for (PrintStream sendstream : CreatServer.clientPrintStreamMap.values()) {
                            System.out.println(i++);
                            sendstream.println(Sign.NewRoomCreate + roomStr);
                        }//发送给所有玩家房间创建信息
                        System.out.println("向所有玩家发送玩家房间信息创建标识。");
                    }
                    /**
                     * 如果收到加入房间信息
                     */
                    else if (isLogin && line.startsWith(Sign.EnterRoom))
                    {
                        realMessage = check.getRealMessage(line, Sign.EnterRoom);
                        String clientid = realMessage.split(Sign.SplitSign)[0];//获取创建者的名字与房间名字
                        String roomid = realMessage.split(Sign.SplitSign)[1]; //获取需要加入的房间名
                        ServerGameRoom serverGameRoom = null;
                        System.out.println("收到来自" + clientid + "加入" + roomid + "房间的请求");
                        System.out.println(CreatServer.allGameRoom.size());
                        //找到房间
                        for (int i = 0; i < CreatServer.allGameRoom.size(); i++)
                        {
                            if (CreatServer.allGameRoom.get(i).getId().equals(roomid));
                            serverGameRoom = CreatServer.allGameRoom.get(i);
                            break;
                        }
                        System.out.println("找到需要加入的房间名字为" + serverGameRoom.getId());
                        //如果房价没有满
                        if(serverGameRoom.getAllClients().size()<=4)
                        {
                        //转发给这房间内所有其他玩家
                        List<Client> list = serverGameRoom.getAllClients();
                            System.out.println("房间中的人数："+list.size());
                        for (Client c : list)
                        {
                            PrintStream sendstream;
                            //在房间内
                                sendstream = CreatServer.clientPrintStreamMap.get(c);
                                sendstream.println(Sign.NewClientEnter + clientid);//转发给房间其他在线玩家xxx进入
                        }
                        System.out.println("开始转发给该房间其他玩家" + client.getId() + "加入了房间");
                        //将当前玩家加入到指定的房间内
                            //System.out.println("添加新的玩家到房间"+serverGameRoom.getId()+"的id"+client.getId()+"大小为"+serverGameRoom.getAllClients().size());
                        serverGameRoom.addClient(client);
                            //System.out.println("添加新的玩家到房间"+serverGameRoom.getId()+"的id"+client.getId()+"大小为"+serverGameRoom.getAllClients().size());
                            GuiShowMes.append("服务器消息：添加新的玩家到房间："+serverGameRoom.getId()+" 玩家的id为："+client.getId());
                        //将当前玩家所属房间指定为此房间
                        client.setGameRoomID(serverGameRoom.getId());
                            System.out.println(serverGameRoom.getAllClients().size());
                        //服务端允许用户加入房间请求，并且发送房间对象序列化
                        Gson gson=new Gson();
                        String roomStr=gson.toJson(serverGameRoom);
                        sendStream.println(Sign.PermissionEnterRoom+roomStr);
                        }
                        //否则
                        else
                        {
                            System.out.println("返回房间已满");
                            sendStream.println(Sign.RoomFull);
                        }
                    }
                    /**
                     * 如果收到踢人的消息（房主可用）
                     */
                    else if (isLogin && line.startsWith(Sign.TickFromRoom)) {
                        realMessage = check.getRealMessage(line, Sign.TickFromRoom);
                        String targetId = realMessage.split(Sign.SplitSign)[0];//获取被T玩家id
                        String roomid = realMessage.split(Sign.SplitSign)[1];//获取房间ID
                        ServerGameRoom serverGameRoom = null;
                        System.out.println("收到来自" + client.getId() + "的T人请求。");
                        for (ServerGameRoom room : CreatServer.allGameRoom) {
                            if (roomid.equals(room.getId())) serverGameRoom = room;
                            break;
                        }
                        if (client.equals(serverGameRoom.getMaster()))
                        {//如果为房主
                            List<Client> list = serverGameRoom.getAllClients();
                            for (Client c : list)
                            {
                                PrintStream printStream = CreatServer.clientPrintStreamMap.get(c);
                                if (c.getId().equals(targetId))
                                {
                                    //发送给被T玩家被T信息
                                    printStream.println(Sign.BeenTicked);
                                    continue;
                                }
                                printStream.println(Sign.ClientLeaveRoom + targetId + Sign.SplitSign + roomid);

                            }
                            //发送给房间内所有玩家xxx被T除
                        }
                        //该房间移除该玩家(同时将该玩家的所属房间重新置空)
                        serverGameRoom.removeClient(targetId);

                    }
                    /**
                     * 如果收到离开房间的消息(房间内的人)
                     */
                    else if (isLogin && line.startsWith(Sign.ClientLeaveRoom)) {
                        System.out.println("服务器收到" + client.getId() + "发来的离开房间的信息。");
                        leaveRoom();//离开房间
                    }
                    /*else if (isLogin && line.startsWith(Sign.Disconnect)) {
                        System.out.println("服务器收到来自" + client.getId() + "的下线请求");
                        //首先判断当前玩家是否正在房间中  是否是房间房主
                        //如果又在房间又是房主
                        if (client.isInRoom()) {
                            leaveRoom();
                        }
                        //如果不在房中
                        for (PrintStream sendstream : CreatServer.clientPrintStreamMap.values()) {
                            sendstream.println(Sign.Disconnect + client.getId());
                        }
                    }*/
                    /**
                     * 如果收到注销请求(玩家返回到登陆界面)
                     */
                    else if (isLogin && line.startsWith(Sign.Logout))
                    {
                        System.out.println(client.getId()+"注销中，在线玩家列表大小为："+CreatServer.onlineClients.size());
                        CreatServer.clientPrintStreamMap.remove(client);
                        CreatServer.onlineClients.remove(client);
                        System.out.println(client.getId()+"注销完成，在线玩家列表大小为："+CreatServer.onlineClients.size());
                        for(PrintStream sendStream:CreatServer.clientPrintStreamMap.values())
                        {
                            sendStream.println(Sign.OneClientOffline+client.getId());
                        }
                        GuiShowMes.append("服务器消息：玩家："+client.getId()+" 下线。\n");
                        client=null;
                    }
                    /**
                     * 收到聊天信息命令
                     */
                    else if (isLogin && line.startsWith(Sign.SendPublicMessage)) {
                        realMessage = check.getRealMessage(line, Sign.SendPublicMessage);
                        ServerGameRoom serverGameRoom = null;
                        String roomID = client.getRoomID();
                        for(ServerGameRoom room:CreatServer.allGameRoom)
                        {
                            if(room.getId().equals(roomID))
                            {
                                serverGameRoom=room;
                            }
                        }
                        //转发消息
                        for (Client c : serverGameRoom.getAllClients())
                        {
                            PrintStream printStream = CreatServer.clientPrintStreamMap.get(c);
                            printStream.println(Sign.FromServerMessage  + client.getId() + ": " + realMessage);
                        }
                    }
                    /**
                     * 如果收到开始游戏的命令
                     */
                    else if(line.startsWith(Sign.StartGame))
                    {

                        for(ServerGameRoom room:CreatServer.allGameRoom)//找到要开始游戏的房间
                        {

                            if(room.getMaster().equals(client))//如果发送开始游戏命令的玩家是房主
                            {

                                for (Client c : room.getAllClients())//开始游戏并告知房间内其他所有人
                                {
                                    CreatServer.clientPrintStreamMap.get(c).println(Sign.GameStart);
                                }
                                GuiShowMes.append("服务器消息：房间："+room.getId()+" 开始游戏。\n");
                            }
                        }

                    }
                    /**
                     * 如果收到断开连接请求（返回到单人与多人游戏选择界面)
                     */
                    else if (line.startsWith(Sign.Disconnect)) {
                        CreatServer.clientPrintStreamMap.remove(client);
                        stopThisClient( sendStream, getStream);
                        //关闭此服务线程 tips:原因：玩家请求断开连接退回到单人多人游戏选择界面
                    }

                    //TODO:待完成的玩家服务线程

                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出当前所处房间
     * @return
     */
    public void leaveRoom()
    {
        ServerGameRoom serverGameRoom = null;
        String roomId = client.getRoomID();//获取玩家当前所在房间
        for(ServerGameRoom room:CreatServer.allGameRoom)
        {
            if(room.getId().equals(roomId))
            {
                serverGameRoom=room;
            }
        }
        System.out.println(serverGameRoom==null);
        List<Client> roomClientList=serverGameRoom.getAllClients();
        System.out.println("循环房间玩家链表大小为"+roomClientList.size());
        //如果退出的玩家不是房主
        if (!serverGameRoom.getMaster().equals(client))
        {
            serverGameRoom.removeClient(client.getId());//当前所在房间移除当前玩家
            List<Client> allClientsIn = serverGameRoom.getAllClients();
            //向房间所有玩家发该玩家退出信息
            for (Client c : allClientsIn)
            {
                if (c.getRoomID().equals(serverGameRoom.getId()))
                {
                    PrintStream printStream = CreatServer.clientPrintStreamMap.get(c);
                    printStream.println(Sign.ClientLeaveRoom + client.getId() + Sign.SplitSign + client.getRoomID());//发送玩家退出房间指令加退出玩家的id
                }
            }
        }
        //如果是房主
        else {
            //清除所有房间内玩家并且T出房间
            try{
                //遍历全部在线玩家
                PrintStream sendStream=null;
                for(Client c: CreatServer.onlineClients)
                {
                    //如果玩家属于该房间
                    if(roomClientList.contains(c))
                    {
                        System.out.println("给房间内"+c.getId()+"发送房间关闭消息");
                        //告知房间里面其他人房间已经被删除
                        sendStream = CreatServer.clientPrintStreamMap.get(c);
                        sendStream.println(Sign.RoomDismiss);
                        //删除该房间内该玩家
                        roomClientList.remove(c);
                    }
                    //如果不属于该房间
                    else
                    {
                        System.out.println("给不是房间内的玩家"+c.getId()+"发送房间删除消息");
                        //告知其他不在此房间中的其他在线用户房间删除的信息
                        sendStream= CreatServer.clientPrintStreamMap.get(c);
                        sendStream.println(Sign.DeleteRoom +serverGameRoom.getId());
                    }
                }
                GuiShowMes.append("服务器消息：房间："+client.getId()+" 被注销（房主退出）。\n");//gui显示房间注销的消息
            }catch (Exception e){
                e.printStackTrace();
            }
            //清除此房间
            CreatServer.allGameRoom.remove(serverGameRoom);
        }
    }
    /**
     *
     * @param flag
     */
    public void setisConnected(boolean flag){
        isConnected=flag;
    }

    /**
     * 发送命令函数
     * @param command 发送的命令
     */
    public void sendCommand(String command){
        sendStream.println(command);
        sendStream.flush();
    }

    /**
     * 用于注销当前服务线程服务的玩家账号
     * //TODO:
     */
    public void LogoutPlayer(){

    }

    /**
     *
     * @return 返回发送流
     */
    public PrintStream getSendStream(){
        return sendStream;
    }

    /**
     *
     * @return 收取流
     */
    public BufferedReader getGetStream(){
        return getStream;
    }
    /**
     * 停止当前服务线程实例对象的运行并进行扫尾工作
     *
     * @param sendStream 获取输出流以回复客户端消息和扫尾停止
     * @param getStream 获取输入流进行扫尾停止
     */
    private void stopThisClient(PrintStream sendStream,BufferedReader getStream) throws IOException {
                //扫尾工作
                sendStream.close();
                getStream.close();
                socket.close();
                this.interrupt();//停止玩家服务线程
    }
}
