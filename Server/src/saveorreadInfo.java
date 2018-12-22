import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 玩家注册信息保存类
 */
class saveorreadInfo {
    public void saveorreadInfo(){

    }
    /**
     *
     * @return
     */
    /*public static int readPlayerNum(){
        char[] num=new char[3];//最多一百位玩家
        try{
            //创建文件对象如不存在则自动创建一个
            File allPlayerNumFile=new File("","allPlayerNum.data");
            if(!allPlayerNumFile.exists()) {
                allPlayerNumFile.createNewFile();
            }
            //创建文件读取流
            FileReader fileReader=new FileReader(allPlayerNumFile);
            fileReader.read(num);

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return  Integer.parseInt(num.toString());//返回注册玩家数目
    }*/
    /**
     * 将已注册玩家数目保存到allPlayerNum.data
     * @param allPlayerNum 已注册玩家数目
     */
    /*public static void savePlayerNum(int allPlayerNum) {
        try {
            File allPlayerFile=new File("","allPlayerNum.data");
            if(!allPlayerFile.exists()) allPlayerFile.createNewFile();
            //创建文件写入流
            FileOutputStream  writeInfo=new FileOutputStream(allPlayerFile,false);//覆写式写入
            writeInfo.write(allPlayerNum);//写入
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }*/
    /**
     * 保存注册信息到文件
     * @param player 存入文件玩家
     */
    public static void  savePlayerInfo(Player player){
        try {
            File allPlayerFile=new File("","allPlayerInfo.txt");
            if(!allPlayerFile.exists()) allPlayerFile.createNewFile();
            //创建文件写入流
            FileOutputStream  writeInfo=new FileOutputStream(allPlayerFile,true);
            ObjectOutputStream writePlayerInfo=new ObjectOutputStream(writeInfo);
            writePlayerInfo.writeObject(player);
            /**
             * 写入文件后将对象保存到内存中以应对下次访问检查
             */
            creatServer.allPlayer.add(player);
            creatServer.allPlayernum++;
            System.out.println("文件中写入一个玩家数据其ID为"+player.getId());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 从文件读取注册玩家数据到 allPlayer
     * @param allPlayer 暂存已注册玩家数据
     */
    public static void  readAllInfo(ArrayList<Player> allPlayer){
            try {
                //创建文件对象如不存在则自动创建一个
                File allPlayerFile=new File("","allPlayerInfo.txt");
                if(!allPlayerFile.exists()) {
                    allPlayerFile.createNewFile();
                }
                //创建文件对象读取流
                try {
                    FileInputStream fils=new FileInputStream(allPlayerFile);
                    ObjectInputStream readInfo=new ObjectInputStream(fils);
                    Player one;
                    while((one=(Player)readInfo.readObject())!=null){
                        allPlayer.add(one);
                        creatServer.allPlayernum++;//服务器注册玩家数目++
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

