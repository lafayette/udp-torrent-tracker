package su.lafayette.udptracker.structures;

public enum Action {
	CONNECT(0),
	ANNOUNCE(1),
	SCRAPE(2),
	ERROR(3);

	private final int id;

	Action(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static Action byId(int id) {
		switch (id) {
			case 0:
				return CONNECT;
			case 1:
				return ANNOUNCE;
			case 2:
				return SCRAPE;
			case 3:
				return ERROR;
			default:
				return null;
		}
	}
}