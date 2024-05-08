package services;

import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import messaging.JMSClient;
import models.User;

@Stateless
public class UserService {

	User user;
	@Inject
	JMSClient jmsClient;

	@PersistenceContext(name = "trello")
	private EntityManager entityManager;

	// Register user using email and password (create account)
	public User register(User user) throws IllegalArgumentException {

		if (!validEmail(user.getEmail())) {
			throw new IllegalArgumentException("Invalid email address");
		}

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
		
		notifyUser("New user registered: " + user.getUsername());
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
		notifyUser("User logged in: " + user.getUsername());
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
		notifyUser("User profile updated: " + user.getUsername());
	}

	// checks the validity of the email
	public boolean validEmail(String email) {
		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return Pattern.matches(regex, email);
	}
	
	//notify user when a new message is received
	public void notifyUser(String message) {
		jmsClient.sendMessage(message);
	}
	

}
