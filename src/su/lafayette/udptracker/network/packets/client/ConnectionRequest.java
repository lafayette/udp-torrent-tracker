package su.lafayette.udptracker.network.packets.client;

import org.apache.log4j.Logger;
import su.lafayette.udptracker.network.packets.ClientRequest;
import su.lafayette.udptracker.network.packets.server.ConnectionResponse;
import su.lafayette.udptracker.network.packets.server.ErrorResponse;

import java.util.Random;

public class ConnectionRequest extends ClientRequest {
	public final static long PROTOCOL_ID = 0x41727101980L;

	private static final Logger logger = Logger.getLogger(ConnectionRequest.class);

	public void read() throws Exception {
		logger.debug("ConnectionRequest::read from " + this.getMessageEvent().getRemoteAddress());

		if (this.connectionId != PROTOCOL_ID) {
			ErrorResponse.send(this.getMessageEvent(), this.getTransactionId(), "Wrong protocol.");
			return;
		}

		Random random = new Random();
		do {
			this.connectionId = random.nextLong();
		} while (this.connectionId == PROTOCOL_ID);
		// TODO: В будущем можно сохранять выданные connectionId для определения пользователей.

		ConnectionResponse.send(this.getMessageEvent(), this.getTransactionId(), this.getConnectionId());
	}
}
