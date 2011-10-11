package su.lafayette.udptracker.network.packets.server;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.MessageEvent;
import su.lafayette.udptracker.structures.Action;
import su.lafayette.udptracker.Utils;

public class ConnectionResponse {
	private static final Logger logger = Logger.getLogger(ConnectionResponse.class);

	public static void send(MessageEvent event, Integer transactionId, Long connectionId) throws Exception {
		logger.debug("ConnectionResponse::send to " + event.getRemoteAddress());

		ChannelBuffer responseBuffer = ChannelBuffers.buffer(4 + 4 + 8);
		responseBuffer.writeInt(Action.CONNECT.getId());
		responseBuffer.writeInt(transactionId);
		responseBuffer.writeLong(connectionId);

		logger.debug("ConnectionResponse DUMP: " + Utils.getHexString(responseBuffer.array()));

		event.getChannel().write(responseBuffer, event.getRemoteAddress());
	}
}
