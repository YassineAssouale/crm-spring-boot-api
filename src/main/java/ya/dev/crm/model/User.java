package ya.dev.crm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 30)
	private String username;
	
	@Column(length = 255)
	private String password;
	
	@Column(length = 255)
	private String mail;
	
	@Column(length = 255)
	private String grants;
	
	public User() {
		super();
	}

	public User(Integer id, String username, String password, String mail, String grants) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.mail = mail;
		this.grants = grants;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", mail=" + mail + "]";
	}

	public String getGrants() {
		
		return this.grants;
	}
	public void setGrants(String grants) {
		this.grants = grants;
	}

}
