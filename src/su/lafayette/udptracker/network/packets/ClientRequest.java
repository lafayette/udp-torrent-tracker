package su.lafayette.udptracker.network.packets;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import su.lafayette.udptracker.structures.Action;

public abstract class ClientRequest {
	protected ChannelHandlerContext context;
	protected MessageEvent messageEvent;
	protected ChannelBuffer channelBuffer;
	protected Long connectionId;
	protected Action action;
	protected Integer transactionId;

	public abstract void read() throws Exception;

	public ChannelHandlerContext getContext() { return context; }
	public void setContext(ChannelHandlerContext context) { this.context = context; }

	public MessageEvent getMessageEvent() { return messageEvent; }
	public void setMessageEvent(MessageEvent messageEvent) { this.messageEvent = messageEvent; }

	public ChannelBuffer getChannelBuffer() { return this.channelBuffer; }
	public void setChannelBuffer(ChannelBuffer channelBuffer) { this.channelBuffer = channelBuffer; }

	public Long getConnectionId() { return connectionId; }
	public void setConnectionId(Long connectionId) { this.connectionId = connectionId; }

	public Action getAction() { return action; }
	public void setAction(Action action) { this.action = action; }

	public Integer getTransactionId() { return transactionId; }
	public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }
}
