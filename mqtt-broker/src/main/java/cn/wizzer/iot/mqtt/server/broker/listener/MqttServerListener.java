package cn.wizzer.iot.mqtt.server.broker.listener;

import cn.wizzer.iot.mqtt.server.broker.protocol.ProtocolProcess;
import cn.wizzer.iot.mqtt.server.common.session.SessionStore;
import cn.wizzer.iot.mqtt.server.store.session.SessionStoreService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioListener;

/**
 * Created by wizzer on 2018/5/10.
 */
@IocBean
public class MqttServerListener implements ServerAioListener {
    private static Logger LOGGER = LoggerFactory.getLogger(MqttServerListener.class);
    @Inject
    private SessionStoreService sessionStoreService;
    @Inject
    private ProtocolProcess protocolProcess;

    /**
     * 建链后触发本方法，注：建链不一定成功，需要关注参数isConnected
     *
     * @param channelContext
     * @param isConnected    是否连接成功,true:表示连接成功，false:表示连接失败
     * @param isReconnect    是否是重连, true: 表示这是重新连接，false: 表示这是第一次连接
     * @author tanyaowu
     */
    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
//        log.debug("建链后触发onAfterConnected");
    }

    /**
     * 解码成功后触发本方法
     *
     * @param channelContext
     * @param packet
     * @author tanyaowu
     */
    public void onAfterReceived(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
//        log.debug("解码成功后触发onAfterReceived");
    }

    /**
     * 消息包发送之后触发本方法
     *
     * @param channelContext
     * @param packet
     * @param isSentSuccess  true:发送成功，false:发送失败
     * @author tanyaowu
     */
    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
//        log.debug("消息包发送之后触发onAfterSent");
    }

    /**
     * 连接关闭前触发本方法
     *
     * @param channelContext the channelcontext
     * @param throwable      the throwable 有可能为空
     * @param remark         the remark 有可能为空
     * @param isRemove
     * @author tanyaowu
     */
    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) {
        LOGGER.debug("连接关闭前触发onBeforeClose");
        String clientId = (String) channelContext.getAttribute("clientId");
        // 发送遗嘱消息
        if (this.sessionStoreService.containsKey(clientId)) {
            SessionStore sessionStore = this.sessionStoreService.get(clientId);
            if (sessionStore.getWillMessage() != null) {
                this.protocolProcess.publish().processPublish(channelContext, sessionStore.getWillMessage());
            }
        }
    }

    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
        // TODO Auto-generated method stub
    }
}
