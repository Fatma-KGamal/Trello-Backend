package services;

import java.util.regex.Pattern;

import javax.ejb.Stateless;
//import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import models.User;

@Stateless
public class UserService {
	
	
	User user;
	
	@PersistenceContext (name="trello")
    private EntityManager entityManager;
	
	
	// Register user using email and password (create account)
	public void register(User user) throws IllegalArgumentException {
		if (!validUsername(user.getUsername())) 
		{
            throw new IllegalArgumentException("Invalid username");
        }
		else if (!validEmail(user.getEmail()))
        {
            throw new IllegalArgumentException("Invalid email address");
        } 
        /*else if (!validPassword(user.getPassword()))
        {
            throw new IllegalArgumentException("Invalid password");
        }*/

        entityManager.persist(user);
    }
	
	// Login user using email and password 
	public User login(String email, String password) throws IllegalArgumentException {
        try {
            user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
            if (!user.getPassword().equals(password)) 
            {
                throw new IllegalArgumentException("Incorrect password");
            }
            return user;
        } catch (NoResultException e) {
            throw new IllegalArgumentException("User not found");
        }
    }
	
	// Edit user profile (userName,email,password)
	public void editProfile(long id,String newUsername, String newEmail, String newPassword) throws IllegalArgumentException {
		user = entityManager.find(User.class,id);
		if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
		else if (!validUsername(newUsername)) 
		{
            throw new IllegalArgumentException("Invalid username");
        }
		else if (!validEmail(newEmail))
		{
            throw new IllegalArgumentException("Invalid email address");
        } 
		/*else if (!validPassword(newPassword)) 
		{
            throw new IllegalArgumentException("Invalid password");
        }*/

        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        entityManager.merge(user);
    }
	
	
	// checks the validity of the userName
	// (doen't contain any numbers)
	public boolean validUsername(String username) {
		String regex = ".*\\d.*";
		return Pattern.matches(regex, username);
	}
	
	// checks the validity of the email
	public boolean validEmail(String email) {
		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(regex, email);	
	}
	
	// checks the validity of the password 
	// (strong, has at least 8 chars , 1 digit, lowerCase, 1 upperCase, 1 special char and no whitespace )
	public boolean validPassword(String password) {
		String regex = " (?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		return Pattern.matches(regex, password);	
	}



	
	

}
