package com.lxh.userlibrary.message;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class Message {
    public final static int PRIORITY_NORMAL = 1;//priority(优先级),值越大，优先级越高
    public final static int PRIORITY_HIGH = 2;
    public final static int PRIORITY_EXTREMELY_HIGH = 3;//priority_extremely_high
    private final static int MAX_CACHED_MESSAGE_OBJ = 15;//max_cached_message_obj  最大缓存Message对象个数为15个
    //并发队列ConcurrentLinkedQueue    阻塞队列LinkedBlockingQueue
    private static ConcurrentLinkedQueue<Message> mCachedMessagePool = new ConcurrentLinkedQueue<Message>();

    public enum Type {
        NONE,
        // this message is used to destroy the message pump,消息队列                  pump(泵)
        // we use the "Poison Pill Shutdown" approach, see: http://stackoverflow.com/a/812362/668963
        DESTROY_MESSAGE_PUMP,//destroy_message_pump  销毁消息队列


        PACKAGE_INSTALL_PREPARE,//prepare 有包正在准备安装
        PACKAGE_INSTALL,  //有包安装
        PACKAGE_UNINSTALL, //有包卸载
        RESET_APPLIST,// 重置手机pplist

        UPDATE_CALENDAR,//更新日历
        UPDATE_CALENDAR2,//更新日历2

        USER_DATA_PHOTO_CHAANGE,//头像更新
        USER_DATA_STATE,//用户状态更新
        USER_EXIT_SATEA,//用户退出状态

        USER_START_LOGIN,//开始登陆
        USER_LOGIN_END,//登陆成功或者失败

    }


    public Type type;
    public Object data;
    public int priority;//优先级
    public Object sender;

    public int referenceCount;
    public Message(Type type, Object data, int priority, Object sender) {
        this.type = type;
        this.data = data;
        this.priority = priority;
        this.sender = sender;
    }

    public Message(Type type, Object data, int priority) {
        this(type, data, priority, null);
    }

    public Message(Type type, Object data) {
        this(type, data, PRIORITY_NORMAL, null);
    }

    public Message(Type type, int priority) {
        this(type, null, priority);
    }

    /**
     * 重置Message
     */
    public void reset() {
        type = Type.NONE;
        data = null;
        priority = PRIORITY_NORMAL;
        sender = null;
    }
    /**
     * 回收Message对象，重复利用
     */
    public void recycle() {
        /**
         while (queue.size()>0)      耗时  .size()是要遍历一遍集合的
         while (!queue.isEmpty())    节约时间
         */
        if (mCachedMessagePool.size() < MAX_CACHED_MESSAGE_OBJ) {
            reset();
            mCachedMessagePool.add(this);
        }
    }

    /**
     * 获取Message对象
     * @param msgType
     * @param data
     * @param priority
     * @param sender
     * @return  Message
     */
    public static Message obtainMessage(Type msgType, Object data, int priority, Object sender) {
        Message message = mCachedMessagePool.poll();
        if (message != null) {
            message.type = msgType;
            message.data = data;
            message.priority = priority;
            message.sender = sender;
        } else {
            message = new Message(msgType, data, priority, sender);
        }
        return message;
    }


}
