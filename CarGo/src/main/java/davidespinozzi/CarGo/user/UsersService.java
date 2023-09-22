package davidespinozzi.CarGo.user;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import davidespinozzi.CarGo.bookings.Booking;
import davidespinozzi.CarGo.bookings.Stato;
import davidespinozzi.CarGo.exceptions.BadRequestException;
import davidespinozzi.CarGo.exceptions.NotFoundException;

@Service
public class UsersService {

	@Autowired
	UserRepository userRepository;

	// SALVA NUOVO UTENTE + ECCEZIONE SE VIENE USATA LA STESSA EMAIL
	public User save(NewUserPayload body) {
		userRepository.findByEmail(body.getEmail()).ifPresent(user -> {
			throw new BadRequestException("L'email " + body.getEmail() + " è gia stata utilizzata");
		});
		User newUser = new User(body.getName(), body.getSurname(), body.getEmail(), body.getPassword());
		return userRepository.save(newUser);
	}

	// TORNA LA LISTA DEGLI UTENTI
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	// CERCA UTENTE TRAMITE ID
	public User findById(UUID id) throws NotFoundException {
		return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	// CERCA E MODIFICA UTENTE TRAMITE ID
	public User findByIdAndUpdate(UUID id, NewUserPayload body) throws NotFoundException {
		User found = this.findById(id);
		found.setName(body.getName());
		found.setSurname(body.getSurname());
		found.setEmail(body.getEmail());
		return userRepository.save(found);
	}

	// CERCA E CANCELLA UTENTE TRAMITE ID
	public void findByIdAndDelete(UUID id) throws NotFoundException {
		User found = this.findById(id);
		userRepository.delete(found);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato"));
	}

	public void changeRole(UUID id) throws NotFoundException {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
		if (user.getRole() == Role.USER) {
			user.setRole(Role.ADMIN);
		} else {
			user.setRole(Role.USER);
		}
		userRepository.save(user);
	}

	public List<Booking> findUserBookingsByState(UUID userId, Stato state) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
		return user.getBookings().stream().filter(booking -> booking.getStato() == state).collect(Collectors.toList());
	}

	public List<Booking> findCurrentUserBookingsByState(Stato state) {
	    UUID userId = getCurrentUserId();
	    User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
	    return user.getBookings().stream()
	               .filter(booking -> booking.getStato() == state)
	               .collect(Collectors.toList());
	}

	
	public UUID getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		return currentUser.getId();
	}
}