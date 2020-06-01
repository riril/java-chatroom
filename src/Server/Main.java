package Server;

import Common.Message;
import Common.User;
import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    private final ArrayList<HandlerThread> sockets = new ArrayList<>();

    public static void main(String[] args) {
        Main server = new Main();
        System.out.println("������������...");
        server.init();
    }

    public void init() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1002);
            Socket client;
            while ((client = serverSocket.accept()) != null) {
                sockets.add(new HandlerThread(client));
            }
        } catch (Exception e) {
            System.out.println("�����������쳣: ");
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<User> getClientUsernames() {
        ArrayList<User> users = new ArrayList<>();
        for (HandlerThread thread : sockets) {
            users.add(thread.getUser());
        }
        return users;
    }

    public void showClientCount() {
        System.out.println("��ǰ�ͻ���������" + sockets.size());
    }

    private class HandlerThread extends Thread {
        private final Socket socket;
        private PrintStream ps;

        public String userName;
        public String password;

        public HandlerThread(Socket socket) {
            this.socket = socket;
            this.start();
        }

        public User getUser() {
            return new User(userName);
        }

        public void send(String jsonString) {
            ps.println(jsonString);
        }

        public void sendAll(String jsonString) {
            for (HandlerThread thread : sockets) {
                thread.send(jsonString);
            }
        }

        public void sendAll(String jsonString, boolean skipSender) {
            if (!skipSender) {
                sendAll(jsonString);
            } else {
                for (HandlerThread thread : sockets) {
                    if (thread.userName.equals(this.userName)) continue;
                    thread.send(jsonString);
                }
            }

        }

        public void sendToOne(String jsonString, User user) {
            for (HandlerThread thread : sockets) {
                if (thread.userName.equals(user.userName)) {
                    thread.send(jsonString);
                    break;
                }
            }
        }

        public void run() {
            OutputStream os = null;
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                os = socket.getOutputStream();
                ps = new PrintStream(os);
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String user = br.readLine();
                String[] tmp = user.split(";");
                userName = tmp[0];
                password = tmp[1];
                System.out.println(userName + "����������");
                if (password.contains("a")) {
                    send("ack_" + userName);
                } else {
                    send("403");
                    throw new Exception(userName + "password error");
                }
                String ack = br.readLine();
                if (ack.equals("ack_" + userName)) {
                    System.out.println(userName + "�����ӳɹ�");
                    sendAll(JSON.toJSONString(new Message(getUser(), "join", getClientUsernames())));
                    showClientCount();
                    String jsonString;
                    while ((jsonString = br.readLine()) != null) {
                        System.out.println("�������յ���Ϣ��" + jsonString);
                        Message msg = JSON.parseObject(jsonString, Message.class);
                        if (msg.toUser != null) {
                            sendToOne(jsonString, msg.toUser);
                        } else {
                            sendAll(jsonString);
                        }
                    }
                } else {
                    System.out.println("expect ack_, actually: " + ack);
                }
            } catch (Exception e) {
                System.out.println(userName + " �����쳣��");
                e.printStackTrace();
            } finally {
                System.out.println("���� " + userName);
                sockets.remove(this);
                showClientCount();
                sendAll(JSON.toJSONString(new Message(getUser(), "left", getClientUsernames())));
                try {
                    if (socket != null) socket.close();
                    if (os != null) os.close();
                    if (is != null) is.close();
                    if (ps != null) ps.close();
                    if (isr != null) isr.close();
                    if (br != null) br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
