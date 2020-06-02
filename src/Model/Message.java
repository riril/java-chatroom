package Model;

import Constants.MessageType;

import java.util.Date;
import java.util.List;

public class Message {
    public User fromUser;
    public User toUser;
    public String msg;
    public String type; // text, event, file, img
    public List<User> users;
    public Long timeStamp;
    public String filename;


    public Message() {
    }

    /**
     * �������ӻ����ʱ���͵���Ϣ
     *
     * @param fromUser  ����
     * @param msg   �����ӻ��Ǽ���
     * @param users Ŀǰ�û��б�
     */
    public Message(User fromUser, String msg, List<User> users) {
        this.fromUser = fromUser;
        this.msg = msg;
        this.type = MessageType.EVENT;
        this.timeStamp = new Date().getTime();
        this.users = users;
    }

    public Message(User fromUser, String msg, String type) {
        this.fromUser = fromUser;
        this.msg = msg;
        this.type = type;
        timeStamp = new Date().getTime();
    }

    public Message(User fromUser, String msg, String type, String filename) {
        this(fromUser, msg, type);
        this.filename = filename;
    }

    /**
     * ��Ϣ�ֶ�
     *
     * @param msg ��Ϣ����
     */
    public Message(User fromUser, String msg) {
        this(fromUser, msg, MessageType.TEXT);
    }
}
