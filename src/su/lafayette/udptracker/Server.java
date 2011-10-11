package su.lafayette.udptracker;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
	private static final Logger logger = Logger.getLogger(Server.class);

	private static ConnectionlessBootstrap bootstrap = null;
	public static Channel channel = null;

	public static void main(String[] args) throws Exception {
		logger.info("Starting BitTorrent UDP tracker...");

		Executor threadPool = Executors.newCachedThreadPool();
		DatagramChannelFactory factory = new NioDatagramChannelFactory(threadPool);

		bootstrap = new ConnectionlessBootstrap(factory);
		bootstrap.getPipeline().addLast("handler", new Handler());

		/**
		 * TODO:
		 * Разобраться, как сделать чтобы при нештатном завершении работы сервера
		 * Netty не оставлял открытым порт.
		 */
		bootstrap.setOption("reuseAddress", true);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				channel.close();
				bootstrap.releaseExternalResources();
			}
		}));

		String host = Config.getInstance().getString("listen.host", "127.0.0.1");
		Integer port = Integer.parseInt(Config.getInstance().getString("listen.port", "8080"));
		InetSocketAddress address = new InetSocketAddress(host, port);

		logger.info("Listening on " + host + ":" + port);

		channel = bootstrap.bind(address);
	}
}
