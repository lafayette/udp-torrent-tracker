package su.lafayette.udptracker;

import com.google.code.morphia.Morphia;
import com.mongodb.*;
import org.apache.log4j.Logger;
import su.lafayette.udptracker.models.Peer;

public class Datastore {
	private static final Logger logger = Logger.getLogger(Server.class);

	private static Mongo mongo;
	private static Morphia morphia;
	private static volatile com.google.code.morphia.Datastore datastore;

	public static com.google.code.morphia.Datastore instance() throws Exception {
		if (datastore == null) {
			synchronized (Datastore.class) {
				if (datastore == null) {
					morphia = new Morphia();
					morphia.map(Peer.class);

					String host = Config.getInstance().getString("database.host", "127.0.0.1");
					Integer port = Config.getInstance().getInt("database.port", 27017);

					mongo = new Mongo(host, port);
					datastore = morphia.createDatastore(mongo, "udptracker");

					logger.info("Connected to MongoDB on " + host + ":" + port);
				}
			}
		}

		return datastore;
	}

	public static Mongo getMongo() {
		return mongo;
	}

	public static Morphia getMorphia() {
		return morphia;
	}

	private Datastore() { }
}
