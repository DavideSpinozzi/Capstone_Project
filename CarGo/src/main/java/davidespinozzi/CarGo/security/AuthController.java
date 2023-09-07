package davidespinozzi.CarGo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import davidespinozzi.CarGo.users.LoginSuccessfullPayload;
import davidespinozzi.CarGo.users.NewUserPayload;
import davidespinozzi.CarGo.users.User;
import davidespinozzi.CarGo.users.UserLoginPayload;
import davidespinozzi.CarGo.users.UsersService;
import davidespinozzi.CarGo.exceptions.UnauthorizedException;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UsersService userService;

	@Autowired
	JWTTools jwtTools;

	@Autowired
	PasswordEncoder bcrypt;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody NewUserPayload body) {

		body.setPassword(bcrypt.encode(body.getPassword()));
		User created = userService.save(body);
		return created;
	}

	@PostMapping("/login")
	public LoginSuccessfullPayload login(@RequestBody UserLoginPayload body) {

		User user = userService.findByEmail(body.getEmail());

		if (bcrypt.matches(body.getPassword(), user.getPassword())) {
			String token = jwtTools.createToken(user);
			return new LoginSuccessfullPayload(token);
		} else {
			throw new UnauthorizedException("Credenziali non valide");
		}
	}
}