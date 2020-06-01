package Common;

import java.util.Date;
import java.util.List;

public class Message {
    public User user;
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
     * @param user  ����
     * @param msg   �����ӻ��Ǽ���
     * @param users Ŀǰ�û��б�
     */
    public Message(User user, String msg, List<User> users) {
        this.user = user;
        this.msg = msg;
        this.type = "event";
        this.timeStamp = new Date().getTime();
        this.users = users;
    }

    public Message(User user, String msg, String type) {
        this.user = user;
        this.msg = msg;
        this.type = type;
        timeStamp = new Date().getTime();
    }

    public Message(User user, String msg, String type, String filename) {
        this(user, msg, type);
        this.filename = filename;
    }

    /**
     * ��Ϣ�ֶ�
     *
     * @param msg ��Ϣ����
     */
    public Message(User user, String msg) {
        this(user, msg, "text");
    }
}
