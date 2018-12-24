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
