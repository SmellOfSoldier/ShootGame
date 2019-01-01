import java.io.*;
import java.util.List;

/**
 * 玩家注册信息保存类
 */
class saveorreadInfo {

    /**
     * 压入一个注册用户到内存用户数据链表allPlayer中
     * @param player
     * @return
     */
    public static  void addClient(Client player)
    {
        CreatServer.allPlayer.add(player);
    }
    /**
     * 每次服务器停止运行时将全部用户数据保存到文件
     * @param allPlayer
     */
    public static void  saveAllClientInfo(List<Client> allPlayer){
        try {
            File allPlayerFile=new File(".","allPlayerInfo.txt");
            if(!allPlayerFile.exists()) allPlayerFile.createNewFile();
            //创建文件写入流
            FileOutputStream  writeInfo=new FileOutputStream(allPlayerFile);//设定为可以后接式的文件写入
            ObjectOutputStream writePlayerInfo=new ObjectOutputStream(writeInfo);
            for(Client player:allPlayer)
            {
                writePlayerInfo.writeObject(player);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 从文件读取注册玩家数据到 allPlayer
     * @param allPlayer 暂存已注册玩家数据
     */
    public static void readAllClientInfo(List<Client> allPlayer){
            try {
                //创建文件对象如不存在则自动创建一个
                File allPlayerFile=new File(".","allPlayerInfo.txt");
                if(!allPlayerFile.exists()) {
                    allPlayerFile.createNewFile();
                }
                //创建文件对象读取流
                try {
                    FileInputStream fils=new FileInputStream(allPlayerFile);
                    ObjectInputStream readInfo=new ObjectInputStream(fils);
                    Client one;
                    while((one=(Client)readInfo.readObject())!=null){
                        allPlayer.add(one);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
            } catch (IOException e) {
                System.out.println("加载用户信息成功！");
            }
        }

    }

