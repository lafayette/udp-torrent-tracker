package su.lafayette.udptracker;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import su.lafayette.udptracker.network.packets.ClientRequest;
import su.lafayette.udptracker.network.packets.client.AnnounceRequest;
import su.lafayette.udptracker.network.packets.client.ConnectionRequest;
import su.lafayette.udptracker.network.packets.client.ScrapeRequest;
import su.lafayette.udptracker.network.packets.server.ErrorResponse;
import su.lafayette.udptracker.structures.Action;

public class Handler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger.getLogger(Handler.class);

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		ChannelBuffer channelBuffer = (ChannelBuffer)e.getMessage();

		if (channelBuffer.readableBytes() < 16) {
			logger.debug("Incorrect packet received from " + e.getRemoteAddress());
		}

		long connectionId = channelBuffer.readLong(); // TODO: Можно проверять connectionId.
		int actionId = channelBuffer.readInt();
		int transactionId = channelBuffer.readInt();

		Action action = Action.byId(actionId);

		ClientRequest request;

		switch (action) {
			case CONNECT:
				request = new ConnectionRequest();
				break;
			case ANNOUNCE:
				request = new AnnounceRequest();
				break;
			case SCRAPE:
				request = new ScrapeRequest();
				break;
			default:
				logger.debug("Incorrect action supplied");
				ErrorResponse.send(e, transactionId, "Incorrect action");
				return;
		}

		request.setContext(ctx);
		request.setMessageEvent(e);
		request.setChannelBuffer(channelBuffer);
		request.setConnectionId(connectionId);
		request.setAction(action);
		request.setTransactionId(transactionId);

		request.read();
	}
}
