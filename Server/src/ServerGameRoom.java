import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 分配管理房间数量的玩家
 */
public class ServerGameRoom {
    private String id;
    private Client master;
    private String name;
    private List<Client> allClients=new LinkedList<>();
    public ServerGameRoom(String id, Client master, String name){
        this.id=id;
        this.master=master;
        this.name=name;
    }

    public String getId(){
        return id;
    }

    public Client getMaster(){
        return master;
    }

    public String getName(){
        return name;
    }

    public boolean  addClient(Client client){
        if(allClients.size()==4) return false;
        allClients.add(client);
        return true;
    }

    /**
     * 房间T除玩家函数
     * @param clientid 被T 除的忘记id
     * @return 成功则true 反之
     */
    public boolean removeClient(String  clientid){
        Client targetclient=null;
        for(Client client:allClients){
            if(client.getId().equals(clientid)) {
                targetclient=client;
                break;
            }
        }
        targetclient.setRoomNull();//设置该玩家所属房间为空
        if(allClients.remove(targetclient)) return true;
        return false;
    }
    /**
     * 清空房间所有的玩家(房主退出时)
     */
    public boolean removeAllClient(){
        try{
            //遍历全部在线玩家
            PrintStream sendStream=null;
        for(Client c:creatServer.onlineClients)
        {
            //如果玩家属于该房间
            if(allClients.contains(c))
            {
                //告知房间里面其他人房间已经被删除
                sendStream = creatServer.clientPrintStreamMap.get(c);
                sendStream.println(Sign.RoomDismiss);
                //删除该房间内该玩家
                allClients.remove(c);
            }
            //如果不属于该房间
            else
                {
                    //告知其他不在此房间中的其他在线用户房间删除的信息
                    sendStream=creatServer.clientPrintStreamMap.get(c);
                    sendStream.println(Sign.DelRoom+id);
                }
        }
        return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  true;
    }

    /**
     * 获得该房间所有玩家列表
     * @return 所有玩家列表（该房间）
     */
    public List<Client> getAllClients(){
        return allClients;
    }

    /*public boolean equals(ServerGameRoom serverGameRoom) {
        if (this.getId().equals(serverGameRoom.getId())) return true;
        else return false;
    }*/
    }
