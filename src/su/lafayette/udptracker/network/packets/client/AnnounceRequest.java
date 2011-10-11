package su.lafayette.udptracker.network.packets.client;

import com.google.code.morphia.MapreduceType;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateOpsImpl;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import su.lafayette.udptracker.Config;
import su.lafayette.udptracker.Datastore;
import su.lafayette.udptracker.Server;
import su.lafayette.udptracker.Utils;
import su.lafayette.udptracker.models.Peer;
import su.lafayette.udptracker.models.TorrentStats;
import su.lafayette.udptracker.network.packets.ClientRequest;
import su.lafayette.udptracker.network.packets.server.AnnounceResponse;
import su.lafayette.udptracker.network.packets.server.ErrorResponse;
import su.lafayette.udptracker.structures.Event;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class AnnounceRequest extends ClientRequest {
	private static final Logger logger = Logger.getLogger(AnnounceRequest.class);

	public void read() throws Exception {
		logger.debug("AnnounceRequest::read from " + this.getMessageEvent().getRemoteAddress());

		ChannelBuffer buffer = this.getChannelBuffer();

		if (buffer.readableBytes() < 20 + 20 + 8 + 8 + 8 + 4 + 4 + 4 + 2 + 2) {
			ErrorResponse.send(this.getMessageEvent(), this.getTransactionId(), "Too small announce packet!");
		}

		Peer peer = new Peer();
		peer.infoHash = buffer.readBytes(20).array();
		peer.peerId = buffer.readBytes(20).array();
		peer.downloaded = buffer.readLong();
		peer.left = buffer.readLong();
		peer.uploaded = buffer.readLong();
		peer.event = buffer.readInt();
		peer.ip = buffer.readInt();
		peer.key = buffer.readInt();
		peer.numWant = buffer.readInt();
		peer.port = buffer.readShort();
		peer.extensions = buffer.readShort();

		if (peer.extensions == 1) {
			// TODO: Тут можно реализовать авторизацию.
		}

		int maxNumWant = Config.getInstance().getInt("maxNumWant");
		if (peer.numWant < 0 || peer.numWant > maxNumWant) {
			peer.numWant = maxNumWant;
		}

		if (peer.ip == 0 && this.getMessageEvent().getRemoteAddress() instanceof InetSocketAddress) {
			InetSocketAddress remoteAddress = (InetSocketAddress)this.getMessageEvent().getRemoteAddress();

			ByteBuffer addressBytes = ByteBuffer.wrap(remoteAddress.getAddress().getAddress());
			peer.ip = addressBytes.getInt();
		}

		Query<Peer> query = Datastore.instance().createQuery(Peer.class);
		query.field("infoHash").equal(peer.infoHash);
		query.field("peerId").equal(peer.peerId);

		UpdateOperations<Peer> updateOperations = Datastore.instance().createUpdateOperations(Peer.class);
		updateOperations.set("downloaded", peer.downloaded);
		updateOperations.set("left", peer.left);
		updateOperations.set("uploaded", peer.uploaded);
		updateOperations.set("ip", peer.ip);
		updateOperations.set("port", peer.port);
		updateOperations.set("lastUpdate", System.currentTimeMillis());

		Datastore.instance().update(query, updateOperations, true);

		if (peer.event != Event.STOPPED.getId() && peer.ip != 0) {
			Datastore.instance().save(peer);
			logger.debug("Peer saved: " + peer.id);
		}

		int randomOffset = 0;
		int maxOffset = (int)Datastore.instance().find(Peer.class).countAll() - peer.numWant;
		if (maxOffset > 0) {
			Utils.random.nextInt(maxOffset);
		}

		Query<Peer> peersQuery = Datastore.instance().find(Peer.class);
		peersQuery.field("infoHash").equal(peer.infoHash);
//		peersQuery.field("peerId").notEqual(peer.peerId); // Для удобства отладки мы пока не будем удалять себя из ответа.
		peersQuery.limit(peer.numWant).offset(randomOffset);

		int announceInterval = Config.getInstance().getInt("interval", 60);

		// Мы можем получить эти значения по аналогии с тем, как это сделано в ScrapeRequest. Пока просто оставим нулями.
		int seeders = 0;
		int leechers = 0;

		AnnounceResponse.send(this.getMessageEvent(), this.getTransactionId(), announceInterval, 0, 0, peersQuery.asList());
	}
}
