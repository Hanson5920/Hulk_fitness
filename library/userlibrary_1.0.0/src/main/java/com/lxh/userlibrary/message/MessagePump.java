package com.lxh.userlibrary.message;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * Created with IntelliJ IDEA.
 * User: neevek
 * Date: 7/20/13
 * Time: 14:50 AM
 */
public class MessagePump extends Thread {
    /**
     PriorityBlockingQueue里面存储的对象必须是实现Comparable接口。队列通过这个接口的compare方法确定对象的priority。
     规则是：当前和其他对象比较，如果compare方法返回负数，那么在队列里面的优先级就比较搞。
     注意1：它是无界阻塞队列，容量是无限的，它使用与类PriorityQueue相同的顺序规则。
     注意2:它是线程安全的，是阻塞的
     注意3：不允许使用 null 元素。
     注意4：对于put(E o)和offer(E o, long timeout, TimeUnit unit)，由于该队列是无界的，所以此方法永远不会阻塞。
     因此参数timeout和unit没意义，会被忽略掉。
     注意5:iterator() 方法中所提供的迭代器并不保证以特定的顺序遍历 PriorityBlockingQueue 的元素。
     如果需要有序地遍历，则应考虑使用 Arrays.sort(pq.toArray())。
     注意6:关于PriorityBlockingQueue的排序原理请参照《PriorityQueue》
     至于使用和别的BlockingQueue（ArrayBlockingQueue，LinkedBlockingQueue）相似，可以参照它们。
     注意7：此类及其迭代器实现了 Collection 和 Iterator 接口的所有可选 方法。
     关于PriorityBlockingQueue的使用请参考《ArrayBlockingQueue》和《BlockingQueue》
     */
    private PriorityBlockingQueue<Message> mMsgPump;//它是线程安全的，是阻塞的
    private List<List<WeakReference<MessageCallback>>> mMsgAndObserverList;

    public MessagePump() {
        setName(this.getClass().getSimpleName());
        // 使用指定的初始容量创建一个 PriorityBlockingQueue，并根据指定的比较器对其元素进行排序。
        mMsgPump = new PriorityBlockingQueue<Message>(10, new Comparator<Message>() {
            public int compare(Message o1, Message o2) {
                return o1.priority < o2.priority ? -1 : o1.priority > o2.priority ? 1 : 1;
            }
        });
        mMsgAndObserverList = new ArrayList<List<WeakReference<MessageCallback>>>(Collections.<List<WeakReference<MessageCallback>>> nCopies(Message.Type.values().length, null));
        // start the background thread to process messages.
        start();

    }

    public void destroyMessagePump() {
        // this message is used to destroy the message center,
        // we use the "Poison Pill Shutdown" approach, see: http://stackoverflow.com/a/812362/668963
        broadcastMessage(Message.Type.DESTROY_MESSAGE_PUMP, null, Message.PRIORITY_EXTREMELY_HIGH);
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(MIN_PRIORITY);
        dispatchMessages();
    }

    public synchronized void register(Message.Type msgType, MessageCallback callback) {//注册
        List<WeakReference<MessageCallback>> observerList = mMsgAndObserverList.get(msgType.ordinal());

        if (observerList == null) {
            observerList = new ArrayList<WeakReference<MessageCallback>>(1);//容量为1
            mMsgAndObserverList.set(msgType.ordinal(), observerList);
        }

        if (indexOf(callback, observerList) == -1) {
            observerList.add(new WeakReference<MessageCallback>(callback));
        }
    }

    private int indexOf(MessageCallback callback, List<WeakReference<MessageCallback>> observerList) {
        try {
            for (int i = observerList.size() - 1; i >= 0; i--) {
                if (observerList.get(i).get() == callback) {
                    return i;
                }
            }
        } catch (Exception e) {
            // ignore the exception
            // the observerList may be modified from within dispatchMessages() method,
            // we should catch all exceptions in case observerList is not in a right
            // state in terms item count.
        }
        return -1;
    }

    public synchronized void unregister(Message.Type msgType, MessageCallback callback) {//反注册
        List<WeakReference<MessageCallback>> observerList = mMsgAndObserverList.get(msgType.ordinal());

        if (observerList != null) {
            int index = indexOf(callback, observerList);

            if (index != -1) {
                observerList.remove(index);
            }
        }
    }

    @Deprecated
    public synchronized void unregister(MessageCallback callback) {
        Message.Type[] types = Message.Type.values();

        for (int i = 0; i < types.length; ++i) {
            unregister(types[i], callback);
        }
    }

    public void broadcastMessage(Message.Type msgType, Object data) {//将消息存储到队列中
        mMsgPump.put(Message.obtainMessage(msgType, data, Message.PRIORITY_NORMAL, null));
    }

    public void broadcastMessage(Message.Type msgType) {
        mMsgPump.put(Message.obtainMessage(msgType, null, Message.PRIORITY_NORMAL, null));
    }

    public void broadcastMessage(Message.Type msgType, Object data, int priority) {
        mMsgPump.put(Message.obtainMessage(msgType, data, priority, null));
    }

    public void broadcastMessage(Message.Type msgType, Object data, Object sender, int priority) {
        mMsgPump.put(Message.obtainMessage(msgType, data, priority, sender));
    }

    private void dispatchMessages() {//消息分发
        while (true) {
            try {
                final Message message = mMsgPump.take();

                if (message.type == Message.Type.DESTROY_MESSAGE_PUMP)//销毁消息队列
                    break;

                final List<WeakReference<MessageCallback>> observerList = mMsgAndObserverList.get(message.type.ordinal());//message.type.ordinal() 返回对应顺序的枚举常量的值。

                if (observerList != null && observerList.size() > 0) {
                    message.referenceCount = observerList.size();

                    for (int i = 0; i < observerList.size(); ++i) {
                        final MessageCallback callback = observerList.get(i).get();

                        if (callback == null) {
                            observerList.remove(i);
                            --i;

                            if (--message.referenceCount == 0) {
                                message.recycle();
                            }

                        } else {

                            TaskExecutor.runTaskOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // call the target on the UI thread
                                                callback.onReceiveMessage(message);

                                            } catch (Exception e) {
//                                                XUtilLog.log_i("wbb", e + "");
                                            }

                                            // recycle the Message object
                                            if (--message.referenceCount == 0) {
                                                message.recycle();
                                            }
                                        }
                                    });
                        }
                    }

                } else {
                    message.recycle();
                }

            } catch (Exception e) {
//                XUtilLog.log_i("wbb", e + "");
            }
        }
    }
}