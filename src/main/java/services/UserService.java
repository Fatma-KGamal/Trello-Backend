package services;

import java.util.List;

import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import models.User;

@Stateless
public class UserService {

	User user;

	@PersistenceContext(name = "trello")
	private EntityManager entityManager;

	// Register user using email and password (create account)
	public User register(User user) throws IllegalArgumentException {

		if (!validEmail(user.getEmail())) {
			throw new IllegalArgumentException("Invalid email address");
		}

		/*
		 * if (!validPassword(user.getPassword())) { throw new
		 * IllegalArgumentException("Invalid password"); }
		 */

		// check if email is already in use
		List<User> EmailList = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
				.setParameter("email", user.getEmail()).getResultList();
		if (!EmailList.isEmpty()) {
			throw new IllegalArgumentException("Email already in use");
		}

		// check if userName is already in use
		List<User> userNameList = entityManager
				.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
				.setParameter("username", user.getUsername()).getResultList();
		if (!userNameList.isEmpty()) {
			throw new IllegalArgumentException("Username already in use");
		}

		entityManager.persist(user);
		return user;
	}

	// Login user using email and password
	public User login(String email, String password) throws IllegalArgumentException {
		user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
				.setParameter("email", email).getSingleResult();

		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}

		if (!user.getPassword().equals(password)) {
			throw new IllegalArgumentException("Incorrect password");
		}

		user.setCurrentUser(true);
		return user;
	}

	// Edit user profile (userName,email,password)
	public void editProfile(long id, String newUsername, String newEmail, String newPassword)
			throws IllegalArgumentException {
		user = entityManager.find(User.class, id);
		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}
		if (!validEmail(newEmail)) {
			throw new IllegalArgumentException("Invalid email address");
		}
		/*
		 * if (!validPassword(newPassword)) { throw new
		 * IllegalArgumentException("Invalid password"); }
		 */

		// check if email is already in use
		List<User> EmailList = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
				.setParameter("email", user.getEmail()).getResultList();
		if (!EmailList.isEmpty()) {
			throw new IllegalArgumentException("Email already in use");
		}

		// check if userName is already in use
		List<User> userNameList = entityManager
				.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
				.setParameter("username", user.getUsername()).getResultList();
		if (!userNameList.isEmpty()) {
			throw new IllegalArgumentException("Username already in use");
		}

		user.setUsername(newUsername);
		user.setEmail(newEmail);
		user.setPassword(newPassword);
		entityManager.merge(user);
	}

	// checks the validity of the email
	public boolean validEmail(String email) {
		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return Pattern.matches(regex, email);
	}

	// checks the validity of the password
	// (has 1 digit, lowerCase, 1 upperCase and no whitespace )
	public boolean validPassword(String password) {
		String regex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+)\r\n";
		return Pattern.matches(regex, password);
	}

}
