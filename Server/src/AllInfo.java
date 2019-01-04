import java.io.*;
import java.util.List;
import java.util.Random;

/**
 * 信息处理类
 */
class AllInfo {

    /**
     * 压入一个注册用户到内存用户数据链表allPlayer中
     * @param player
     * @return
     */
    public static  void addClient(Client player)
    {
        StartServer.allPlayer.add(player);
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
                player.clear();//清空所有对象状态值
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
                        one.clear();
                        allPlayer.add(one);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
            } catch (IOException e) {
                System.out.println("加载用户信息成功！");
            }
        }

    /**
     * 随机指定范围内N个不重复的数
     * 在初始化的无重复待选数组中随机产生一个数放入结果中，
     * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
     * 然后从len-2里随机产生下一个随机数，如此类推
     * @param max  指定范围最大值
     * @param min  指定范围最小值
     * @param n  随机数个数
     * @return int[] 随机数结果集
     */
    public static Integer[] randomArray(int min,int max,int n){
        int len = max-min+1;

        if(max < min || n > len){
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }

        Integer[] result = new Integer[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }
    }

