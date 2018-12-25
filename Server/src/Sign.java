public interface Sign
{
    String Register="REGISTER_COMMEND";                       //注册
    String RegisterSuccess="REGISTER_SUCCESS_COMMEND";        //注册成功
    String Login="LOGIN_COMMEND";                          //登录
    String LoginSuccess="LOGIN_SUCCESS";                   //登陆成功
    String OpenGame="OPEN_GAME";                           //玩家打开了点击了多人游戏
    String Disconnect="DISCONNEXTE";                             //关闭连接请求（玩家关闭多人游戏界面）
    String EnterRoom="ENTER_ROOM";                               //加入房间
    String CreateRoom="CREATE_ROOM";                             //创建房间
    String TickFromRoom="TICK_FROM_ROOM";                         //T出房间
    String LeaveRoom="LEAVE_ROOM";                                //离开房间 （客户端收）
    String StartGame="START_GAME";                                //开始游戏
    String NewClientEnter="NEW_CLIENT_ENTER";                    //新玩家加入房间
    String NewRoomCreate="NEW_ROOM_CREATE";                     //新房间创建
    String OneClientTicked="ONE_CLIENT_TICKED";                 //玩家被提出房间
    String ClientLeaveRoom="CLIENT_LEAVE_ROOM";                  //玩家离开房间
    String BeenTicked="BEEN_TICKED";                            //已经被T出的玩家
    String SuccessDisconnected="SUCCESS_DISCONNEXTED";            //用于返还给客户端成功断开连接的消息
    String FailedDisconnected="FAILED_DISCONNEXTED";            //用于返还给客户端连接失败服务器目前不可用的消息
    String SuccessConnected="SUCCESS_CONNEXTED";            //用于返还给客户端成功连接的消息
    String WrongPassword="WRONG_PASSWORD";                 //错误的密码
    String IsNotRegistered="IS_NOT_REGISTERED";                   //还没有注册过
    String IsRegistered="IS_REGISTERED";                       //已经注册过了
    String Logout="LOGOUT";                                   //玩家注销（退出多人联机）
    String LogoutSuccess="LOGOUT_SUCCESS";                    //成功注销
    String SendPrivateMessage="SEND_PRIVATE_MESSAGE_COMMEND";           //给个人发送信息
    String SendPublicMessage="SEND_PUBLIC_MESSAGE_COMMEND";          //给所有人发送信息
    String ClientExit="CLIENT_EXIT_COMMEND";                 //退出客户端
    String SplitSign="SPLIT_SIGN_COMMEND";                       //信息分隔符
    String Pass="PASS_COMMEND";                             //密码正确
    String UnPass="UN_PASS_COMMEND";                //密码错误
    String SendObject="SEND_OBJECT_COMMEND";                     //发送对象
    String ServerExit="SERVER_EXIT_COMMEND";                     //服务器退出
    String RepeatOnline="REPEAT_ONLINE_COMMEND";                   //帐号被重复登录
    String FromServerMessage="FROM_SERVER_MESSAGE_COMMEND";              //来自服务器的消息
    String OneUserIsOnline="ONE_USER_IS_ONLINE";                        //用户上线
    String OneUserOffOnline="ONE_USER_OFF_ONLINE";                      //用户离线
    String YoursInformation="YOURS_INFORMATION";                        //用户的信息
    String ClientLeaveRoom="CLIENT_LEAVE_ROOM";                         //一个用户用户离开房间（服务端收）

}
