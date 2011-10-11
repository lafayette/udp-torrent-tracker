package su.lafayette.udptracker.network.packets.client;

import com.google.code.morphia.query.Query;
import org.apache.log4j.Logger;
import su.lafayette.udptracker.Datastore;
import su.lafayette.udptracker.models.Peer;
import su.lafayette.udptracker.models.TorrentStats;
import su.lafayette.udptracker.network.packets.ClientRequest;
import su.lafayette.udptracker.network.packets.server.ScrapeResponse;

import java.util.ArrayList;
import java.util.List;

public class ScrapeRequest extends ClientRequest {
	private static final Logger logger = Logger.getLogger(ScrapeRequest.class);

	public void read() throws Exception {
		// TODO: На нагруженном сервере Scrape создает большую нагрузку и обязательно должен кешироваться!

		logger.debug("ScrapeRequest::read from " + this.getMessageEvent().getRemoteAddress());

		List<byte[]> infoHashes = new ArrayList<byte[]>();

		while (this.getChannelBuffer().readableBytes() >= 20) {
			infoHashes.add(this.getChannelBuffer().readBytes(20).array());

			if (infoHashes.size() >= 74) {
				break;
			}
		}

		List<TorrentStats> torrentStatsList = new ArrayList<TorrentStats>();
		for (byte[] infoHash : infoHashes) {
			Query seedersQuery = Datastore.instance().createQuery(Peer.class);
			seedersQuery.field("infoHash").equal(infoHash);
			seedersQuery.field("left").equal(0);

			Query leechersQuery = Datastore.instance().createQuery(Peer.class);
			leechersQuery.field("infoHash").equal(infoHash);
			leechersQuery.field("left").greaterThan(0);

			TorrentStats torrentStats = new TorrentStats();
			torrentStats.infoHash = infoHash;
			torrentStats.seeders = (int)seedersQuery.countAll();
			torrentStats.completed = 0; // TODO: Для этого поля нам понадобится вести статистику загрузок.
			torrentStats.leechers = (int)leechersQuery.countAll();

			torrentStatsList.add(torrentStats);
		}

		ScrapeResponse.send(this.getMessageEvent(), this.getTransactionId(), torrentStatsList);
	}
}
