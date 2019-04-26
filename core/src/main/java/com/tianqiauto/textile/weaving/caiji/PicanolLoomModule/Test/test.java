package com.tianqiauto.textile.weaving.caiji.PicanolLoomModule.Test;

import com.tianqiauto.textile.weaving.caiji.PicanolLoomModule.bean.PCN;
import com.tianqiauto.textile.weaving.caiji.PicanolLoomModule.utils.socket.Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class test {

    public static void main(String[] args) {


        String ip = "210.10.101.1";
        int port = 8004;
        String machineNumber = "sn612631";


        PCN requestPcn = new PCN();
        requestPcn.getHeader().setSourceId("Bicom");        //来源识别
        requestPcn.getHeader().setMessageType("Monitoring");
        requestPcn.getHeader().setMessageCode("bicom");
        requestPcn.getHeader().setDataFormat("VDI");

        PCN.Body body = requestPcn.getBody();
        body.setId((byte)30);
        byte[] date = {(byte)3};
        body.setData(date);


        requestPcn.getHeader().setDestinationId(machineNumber);


//        byte[] message =Client.send(ip,port,requestPcn.toString().getBytes());
//        PCN pcn =  new PCN(message);

        byte[] message1 =sendMessage(ip,port,requestPcn.toString().getBytes());
        PCN pcn1 =  new PCN(message1);

//        System.out.println(pcn.toString());
        System.out.println("===="+pcn1.toString());



    }


    public static byte[] sendMessage(String ip, int port, String pcn) {
        return sendMessage(ip, port, pcn.getBytes());
    }



    public static byte[] sendMessage(String ip, int port, byte[] request) {
        byte[] res = null;

        try (
                Socket socket = new Socket(ip, port);
                InputStream is = socket.getInputStream();
                OutputStream os =  socket.getOutputStream();
                ){
            os.write(request);
            os.flush();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            do {
                count = is.read(buffer);
                bos.write(buffer, 0, count);
            } while (is.available() != 0);
            res = bos.toByteArray();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
