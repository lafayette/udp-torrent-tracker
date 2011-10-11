package su.lafayette.udptracker;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;

public class Config extends XMLConfiguration {
	private static final Logger logger = Logger.getLogger(Server.class);

	private Config() throws IOException, ConfigurationException {
		URL configResource = this.getClass().getResource("/config.xml");
		logger.warn("Configuration loaded from " + configResource);
		this.load(configResource);
	}

	private static volatile Config instance;

	public static Config getInstance() throws IOException, ConfigurationException {
		if (instance == null) {
			synchronized (Config.class) {
				if (instance == null) {
					instance = new Config();
				}
			}
		}

		return instance;
	}
}
