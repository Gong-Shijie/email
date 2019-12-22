
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class sendMail {
    private static String mailServer;
    private static String from;
    private static String pass;
    private static  String to;
    private static String content;
    private static  String subject ;
    private static  Scanner in = new Scanner(System.in);
    Socket socketSender;
    //连接创建套接字
    private boolean link() {
        boolean linkFlag = true;  //linkFlag初始置为true

        //是否正确写入了邮件服务器
        if (mailServer == null || "".equals(mailServer)) {
            return false;
        }

        //连接过程
        try {
            //创建套接字绑定端口25
            socketSender = new Socket(mailServer, 25);


            //判断连接成功
            String rCommend = response();
            if (rCommend.startsWith("220")) {
                System.out.println("连接成功：" + rCommend);
            } else {
                //否则，建立连接失败，将linkFlag设置为false
                System.out.println("建立连接失败：" + rCommend);
                linkFlag = false;
            }

        } catch (UnknownHostException e) {
            System.out.println("建立连接失败！");
            e.printStackTrace();
            linkFlag = false;
        } catch (IOException e) {
            System.out.println("读取流失败！");
            e.printStackTrace();
            linkFlag = false;
        }
        return linkFlag;
    }
    //发送邮件过程和服务器的对话过程
    private boolean send() {
        //如果没有建立连接，则建立连接
        if (socketSender == null) {
            if (link()) {
                //没有连接还不建立连接，返回false
            } else {
                return false;
            }
        }

        //如果没有发送者/发送者为空/没有接受者/接受者为空，返回false
        if (from == null || from.isEmpty() || to == null || to.isEmpty()) {
            System.out.println("fail！information missed！");
            return false;
        }

        //使用SMTP协议来发送邮件
        String helo = sendCommand("HELO " + mailServer + "\r\n");
        //如果返回响应以250开头，则握手成功
        if (!helo.startsWith("250")) {
            System.out.println("握手失败：" + helo);
            return false;
        }

        String auth = sendCommand("AUTH LOGIN" + "\r\n");
        if (auth.startsWith("334")) {
        } else {
            return false;
        }
        //验证用户账户信息账号密码
        String user = sendCommand(new String(Base64.encode(from.getBytes()))
                + "\r\n");
        if (user.startsWith("334")) {
        } else {
            return false;
        }
        String password = sendCommand(new String(Base64.encode(pass.getBytes()))
                + "\r\n");
        if (password.startsWith("235")) {
            System.out.println("用户验证登陆成功！");
        } else {
            return false;
        }

        //邮件具体内容
        String send = sendCommand("Mail From:<" + from + ">" + "\r\n");
        if (send.startsWith("250")) {
        } else {
            return false;
        }
        String receive = sendCommand("RCPT TO:<" + to + ">" + "\r\n");
        if (receive.startsWith("250")) {
        } else {
            return false;
        }
        String data = sendCommand("DATA" + "\r\n");
        if (data.startsWith("354")) {
            System.out.println("开始发送邮件！");
        } else {
            return false;
        }
        //
        StringBuilder contenttosend = new StringBuilder();
        contenttosend.append("From:<" + from + ">" + "\r\n");
        contenttosend.append("To:<" + to + ">" + "\r\n");
        contenttosend.append("Subject:" + subject + "\r\n");
        contenttosend.append("Content-Type:text/plain;charset=\"GB2312\"" + "\r\n");
        contenttosend.append("\r\n");
        contenttosend.append(content);
        contenttosend.append("\r\n" + "." + "\r\n");
        String contentStr = sendCommand(contenttosend.toString());
        if (contentStr.startsWith("250")) {
        } else {
            return false;
        }
        //退出连接
        String quit = sendCommand("QUIT" + "\r\n");
        //如果返回的响应以221开头，则成功退出
        if (quit.startsWith("221")) {
        } else {
            return false;
        }
        //关闭输入输出流
        close();
        return true;
    }
    //程序入口
    public static void sendmain() {
        String string[] = (sendMail.from).split("@");
        mailServer = "smtp." + string[1];
        //输入发送的地址和内容
        System.out.println("\nEnter destination Account：");
        sendMail.to=in.nextLine();
        System.out.println("\nEnter mail Subject:");
        sendMail.subject=in.nextLine();
        System.out.println("\nEnter mail Content(End with .):");
        sendMail.content = "";
        String temp ="";
        while (!(temp = in.nextLine()).equals(".")){
            sendMail.content=sendMail.content+"\n"+temp;
        }
        sendMail mail = new sendMail();
        boolean linkflag = mail.send();
        if (linkflag)
            System.out.println("邮件发送成功！\n");
        else {
            System.out.println("邮件发送失败！\n");
        }
    }
    //封装输出流，发送请求返回服务器答复
    private String sendCommand(String message) {
        String result = null;
        try {
            DataOutputStream output = new DataOutputStream(new DataOutputStream(socketSender.getOutputStream()));
            output.writeBytes(message);
            output.flush();
            result = response();
//            System.out.println("response: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //读取输入流的信息
    private String response() {
        String result = null;
        try {

            BufferedReader input = new BufferedReader(new InputStreamReader(
                    socketSender.getInputStream()));
            result = input.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //关闭输入输出流
    private void close() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    socketSender.getInputStream()));
            DataOutputStream output = new DataOutputStream(new DataOutputStream(
                    socketSender.getOutputStream()));
            output.close();
            input.close();
            socketSender.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //一些用来初始化参数的方法
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
}