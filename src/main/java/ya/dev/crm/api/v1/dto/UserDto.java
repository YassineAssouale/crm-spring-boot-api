package ya.dev.crm.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Jackson Serialization
public class UserDto {
	
private Integer id;
	
	private String username;
	
	private String password;
	
	private String mail;

	public UserDto() {
		super();
	}

	public UserDto(Integer id, String username, String password, String mail) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.mail = mail;
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

}
