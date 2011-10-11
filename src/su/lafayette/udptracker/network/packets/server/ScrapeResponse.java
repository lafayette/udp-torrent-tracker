package su.lafayette.udptracker.network.packets.server;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.MessageEvent;
import su.lafayette.udptracker.Utils;
import su.lafayette.udptracker.models.TorrentStats;
import su.lafayette.udptracker.structures.Action;

import java.util.List;

public class ScrapeResponse {
	private static final Logger logger = Logger.getLogger(ScrapeResponse.class);

	public static void send(MessageEvent event, Integer transactionId, List<TorrentStats> torrentStatsList) throws Exception {
		logger.debug("ScrapeResponse::send to " + event.getRemoteAddress());

		ChannelBuffer responseBuffer = ChannelBuffers.buffer(4 + 4 + torrentStatsList.size() * 12);
		responseBuffer.writeInt(Action.SCRAPE.getId());
		responseBuffer.writeInt(transactionId);

		for (TorrentStats torrentStats : torrentStatsList) {
			responseBuffer.writeInt(torrentStats.seeders);
			responseBuffer.writeInt(torrentStats.completed);
			responseBuffer.writeInt(torrentStats.leechers);
		}

		logger.debug("ScrapeResponse DUMP: " + Utils.getHexString(responseBuffer.array()));

		event.getChannel().write(responseBuffer, event.getRemoteAddress());
	}
}
