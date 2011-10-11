package su.lafayette.udptracker.network.packets.server;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.MessageEvent;
import su.lafayette.udptracker.structures.Action;
import su.lafayette.udptracker.Utils;
import su.lafayette.udptracker.models.Peer;

import java.util.List;

public class AnnounceResponse {
	private static final Logger logger = Logger.getLogger(ErrorResponse.class);

	public static void send(MessageEvent event, int transactionId, int interval, int leechers, int seeders, List<Peer> peers) throws Exception {
		logger.debug("AnnounceResponse::send to " + event.getRemoteAddress());

		ChannelBuffer responseBuffer = ChannelBuffers.buffer(4 + 4 + 4 + 4 + 4 + peers.size() * 6);

		responseBuffer.writeInt(Action.ANNOUNCE.getId());
		responseBuffer.writeInt(transactionId);
		responseBuffer.writeInt(interval);
		responseBuffer.writeInt(leechers);
		responseBuffer.writeInt(seeders);

		for (Peer peer : peers) {
			responseBuffer.writeInt(peer.ip);
			responseBuffer.writeShort(peer.port);
		}

		logger.debug("AnnounceResponse DUMP: " + Utils.getHexString(responseBuffer.array()));

		event.getChannel().write(responseBuffer, event.getRemoteAddress());
	}
}
