package su.lafayette.udptracker.network.packets.server;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.MessageEvent;
import su.lafayette.udptracker.structures.Action;
import su.lafayette.udptracker.Utils;

public class ErrorResponse {
	private static final Logger logger = Logger.getLogger(ErrorResponse.class);

	public static void send(MessageEvent event, Integer transactionId, String message) throws Exception {
		logger.debug("ErrorResponse::send to " + event.getRemoteAddress());

		byte[] messageBuffer = message.getBytes("UTF-8");

		ChannelBuffer responseBuffer = ChannelBuffers.buffer(4 + 4 + messageBuffer.length + 2);
		responseBuffer.writeInt(Action.ERROR.getId());
		responseBuffer.writeInt(transactionId);
		responseBuffer.writeBytes(messageBuffer);
		responseBuffer.writeByte(0);
		responseBuffer.writeByte(0);

		logger.debug("ErrorResponse DUMP: " + Utils.getHexString(responseBuffer.array()));

		event.getChannel().write(responseBuffer, event.getRemoteAddress());
	}
}
