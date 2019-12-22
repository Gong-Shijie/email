

import java.util.Scanner;

import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.Socket;

public class javaEmail {

    private static Scanner in = new Scanner(System.in);
    private static sendMail send = new sendMail();
    private static receiveMail receive = new receiveMail();
    private static boolean flag2menu=false;

    public static void main(String[] args) {


        System.out.println("********************Email System********************");
        System.out.println("Please enter account:");
        String account = in.nextLine();
        send.setFrom(account);
        receive.setUSERNAME(account);
        System.out.println("Please enter password:");
        String password = in.nextLine();
        send.setPass(password);
        receive.setPASSWORD(password);
        while (true) {
            System.out.println("\n***************");
            System.out.println("Choices: \n1:Send mail   \n2:Receive mail  \n0:Change acount");
            System.out.println("***************");
            String input = in.nextLine();
            switch (input) {
                case "1":
                    sendMail.sendmain();
                    break;
                case "2":
                    receive.receivemain();
                    break;
                case "0":
                    System.out.println("********************Email System********************");
                    System.out.println("Please enter account:");
                    account = in.nextLine();
                    send.setFrom(account);
                    receive.setUSERNAME(account);
                    System.out.println("Please enter password:");
                    password = in.nextLine();
                    send.setPass(password);
                    receive.setPASSWORD(password);
                    break;
                default:

                    System.out.println("\nInput error!\n");

                    break;
            }
        }
    }

}