package su.lafayette.udptracker.models;

import com.google.code.morphia.annotations.*;
import org.bson.types.ObjectId;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;

@Entity("peers")
public class Peer {
	public @Id ObjectId id;
	public @Indexed byte[] infoHash;
	public byte[] peerId;
	public long downloaded;
	public long left;
	public long uploaded;
	public @Transient int event;
	public int ip;
	public short port;
	public @Transient int key;
	public @Transient int numWant;
	public @Transient short extensions;
	public long lastUpdate;

	@PrePersist
	private void prePersist() {
		this.lastUpdate = System.currentTimeMillis();
	}
}
