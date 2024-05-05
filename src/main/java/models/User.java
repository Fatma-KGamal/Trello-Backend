package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull(message = "Username cannot be a null value")
	private String username;
	@NotNull(message = "Email cannot be a null value")
	private String email;
	@NotNull(message = "Passsword cannot be a null value")
	private String password;
	@NotNull(message = "Admin cannot be a null value")
	private boolean admin;

	private static boolean currentUser;

	public static boolean isCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(boolean currentUser) {
		User.currentUser = currentUser;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public String toString() {
		return " , User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", admin=" + admin + "]";
	}

	@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Board> userBoards = new ArrayList<Board>();

	public List<Board> getUserBoards() {
		return userBoards;
	}

	public void setUserBoards(List<Board> userBoards) {
		this.userBoards = userBoards;
	}

}
