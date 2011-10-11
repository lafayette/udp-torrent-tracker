package su.lafayette.udptracker.models;

import com.google.code.morphia.annotations.*;
import org.bson.types.ObjectId;

@Entity("torrentStats")
public class TorrentStats {
	public @Id ObjectId id;
	public byte[] infoHash;
	public int seeders;
	public int completed;
	public int leechers;
}
