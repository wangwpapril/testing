package com.cuc.miti.phone.xmc.xmpp;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/** 
 * This class parses incoming IQ packets to NotificationIQ objects.
 *
 * @author SongQing
 */
public class MessageIQProvider implements IQProvider {

    public MessageIQProvider() {
    }

    public IQ parseIQ(XmlPullParser parser) throws Exception {

        MessageIQ message = new MessageIQ();
        for (boolean done = false; !done;) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {			//��ʼ�ڵ�
                if ("id".equals(parser.getName())) {
                	message.setId(parser.nextText());
                }
                if ("sender".equals(parser.getName())) {
                	message.setSender(parser.nextText());
                }
                if ("receiver".equals(parser.getName())) {
                	message.setReceiver(parser.nextText());
                }
                if ("content".equals(parser.getName())) {
                	message.setContent(parser.nextText());
                }
                if ("msgtype".equals(parser.getName())) {
                	message.setMsgtype(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG && "message".equals(parser.getName())) {
                done = true;
            }
        }

        return message;
    }

}
