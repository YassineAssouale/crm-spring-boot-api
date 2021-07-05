package ya.dev.crm.service;

import java.util.List;

import ya.dev.crm.model.Customer;

public interface CustomerService {
	
	/**
	 * Get a customer by id
	 * @param id the id
	 * @return the Customer
	 */
	Customer getCustomerById(Integer id);
	
	/**
	 * Get all customers
	 * @return a list of customers
	 */
	List<Customer> getAllCustomersSortByLastnameAscending();
	
	/**
	 * Get customers by active status
	 * @param active true or false
	 * @return a list of customers by active status
	 */
	List<Customer> getCustomersByActive(Boolean active);
	
	
	/**
	 * Create a customer
	 * @param customer the customer to create
	 * @return
	 */
	Customer createCustomer(Customer customer);
	
	/**
	 * Update a customer
	 * @param customer the customer to update
	 */
	Customer updateCustomer(Customer customer);
	
	/**
	 * Patch a customer status
	 * @param customerId
	 * @param active
	 */
	void patchCustomerStatus(Integer customerId, boolean active);
	
	/**
	 * Delete a customer
	 * @param customer the customer to delete
	 */
	void deleteCustomer(Integer id);

}
