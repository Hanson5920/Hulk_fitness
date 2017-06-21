package com.lxh.userlibrary.message;



import com.lxh.userlibrary.UserCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * <p/>
 * Author:xiaxf
 * <p/>
 * Date:2015/8/14.
 */
public class MessageHelper {
	private MessageCallback mMessageCallback;//   void onReceiveMessage(Message message);
	private List<Message.Type> mMessageTypes = new ArrayList<>();

	public void setMessageCallback(MessageCallback messageCallback) {
		this.mMessageCallback = messageCallback;
	}

	public void registerMessages() {
		if (mMessageCallback == null) {
			return;
		}

		for (Message.Type messageType : mMessageTypes) {
			UserCenter.getInstance().getMessagePump().register(messageType, mMessageCallback);
		}
	}

	public void unRegisterMessages() {
		if (mMessageCallback == null)
			return;

		for (Message.Type messageType : mMessageTypes) {
			UserCenter.getInstance().getMessagePump().unregister(messageType, mMessageCallback);
		}
	}

	/**
	 * 监听消息
	 * @param mssageType
	 */
	public void attachMessage(Message.Type mssageType) {
		if (mMessageCallback == null) {
			throw new IllegalStateException("You need call setMessageCallback() at first.");
		}

		mMessageTypes.add(mssageType);
	}

	public void clearMessages(){
		mMessageTypes.clear();
		mMessageTypes = null;
		this.mMessageCallback = null;
	}

}
