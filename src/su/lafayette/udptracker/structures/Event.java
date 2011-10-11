package su.lafayette.udptracker.structures;

public enum Event {
	NONE(0),
	COMPLETED(1),
	STARTED(2),
	STOPPED(3);

	private final int id;

	Event(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static Event byId(int id) {
		switch (id) {
			case 0:
				return NONE;
			case 1:
				return COMPLETED;
			case 2:
				return STARTED;
			case 3:
				return STOPPED;
			default:
				return null;
		}
	}
}