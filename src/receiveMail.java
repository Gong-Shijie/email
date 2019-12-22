
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class receiveMail {
    /*
         用来测试的邮箱账户和密码信息
        "pop.163.com""lqh777_scu@163.com""lqh7777777"
         "2994118178@qq.com""hkogvqvmlhkpddff"
    */
    private static String POP3Server;
    private static String USERNAME;
    private static String PASSWORD;
    private static int PORT = 110;
    public  String getUSERNAME() {
        return USERNAME;
    }
    public  void setUSERNAME(String USERNAME) {
        receiveMail.USERNAME = USERNAME;
    }
    public  String getPASSWORD() {
        return PASSWORD;
    }
    public void setPASSWORD(String PASSWORD) {
        receiveMail.PASSWORD = PASSWORD;
    }
    public static void receivemain() {
        try {
            String string[] = (receiveMail.USERNAME).split("@");
            POP3Server = "pop." + string[1];
            //创建套接字绑定端口号
            Socket socketReceiver = new Socket(receiveMail.POP3Server, receiveMail.PORT);
            //封装输入输出流
            DataInputStream input = new DataInputStream(socketReceiver.getInputStream());
            DataOutputStream output = new DataOutputStream(socketReceiver.getOutputStream());
            //使用POP3规定的规范和邮件服务器对话
            //用户名密码验证
            System.out.println(input.readLine());
            output.writeBytes("user " + receiveMail.USERNAME + "\r\n");
            System.out.println(input.readLine());
            output.writeBytes("pass " + receiveMail.PASSWORD + "\r\n");
            System.out.println(input.readLine());
            //获取邮件总数
            output.writeBytes("stat" + "\r\n");
            String[] temp = input.readLine().split(" ");
            int count = Integer.parseInt(temp[1]);

            //通过循环向服务器查看每一封邮件 通过retr命令实现
            for (int i = 1; i <= count; i++) {
                output.writeBytes("retr " + i + "\r\n");
                System.out.println("\n-----------------------------------------------------------------------------------------------------------------------\nMail:" + i);
                while (true) {
                    String reply = input.readLine();
                    String str = "";
                    str = new String(reply.getBytes("iso-8859-1"));
                    {
                        str = MimeUtility.decodeText(reply);
                        System.out.println(str);
                    }
                    if (reply.toLowerCase().equals(".")) {//把所有的英文字符转换成小写字母
                        break;
                    }

                }
            }
            input.close();
            output.close();
            socketReceiver.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


