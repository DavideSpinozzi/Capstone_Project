package davidespinozzi.S6Giorno1.users;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class NewUserResponsePayload {

	private UUID id;
}