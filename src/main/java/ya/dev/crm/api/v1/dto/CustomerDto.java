package ya.dev.crm.api.v1.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Jackson Serialization
public class CustomerDto {

	private Integer id;
	
	private String lastname;
	
	private String firstname;
	
	private String company;
	
	private String mail;
	
	private String phone;
	
	private String mobile;
	
	private String notes;
	
	private Boolean active;
	
	private List<OrderDto> orders;

	public CustomerDto(Integer id, String lastname, String firstname, String company, String mail, String phone,
			String mobile, String notes, Boolean active, List<OrderDto> orders) {
		super();
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstname;
		this.company = company;
		this.mail = mail;
		this.phone = phone;
		this.mobile = mobile;
		this.notes = notes;
		this.active = active;
		this.orders = orders;
	}

	public CustomerDto() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<OrderDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDto> orders) {
		this.orders = orders;
	}
}
