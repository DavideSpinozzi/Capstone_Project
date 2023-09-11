package davidespinozzi.S6Giorno1.exceptions;

import java.util.UUID;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(UUID  id) {
		super(id + " non trovato!");
	}
}
