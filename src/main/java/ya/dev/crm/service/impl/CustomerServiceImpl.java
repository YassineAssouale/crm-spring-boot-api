package ya.dev.crm.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ya.dev.crm.model.Customer;
import ya.dev.crm.exception.UnknownResourceException;
import ya.dev.crm.repository.CustomerRepository;
import ya.dev.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{
	
	Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	CustomerRepository customerRepository;

	/**
	 * Get a customer by id 
	 * @param id the customer id
	 * @return the customer
	 */
	@Override
	public Customer getCustomerById(Integer id) {
		return customerRepository.findById(id).orElseThrow(UnknownResourceException::new);
	}
	/**
	 * Get all customers
	 * @return the list of customers
	 */
	@Override
	public List<Customer> getAllCustomersSortByLastnameAscending() {
		return customerRepository.findAll(Sort.by("lastname").ascending());
	}
	
	/**
	 * @param active : status
	 *@return list fo active customers
	 */
	@Override
	public List<Customer> getCustomersByActive(Boolean active) {
		return customerRepository.findByActive(active);
	}

	/**
	 * Create customer
	 * @param customer the customer to create
	 * @return the created customer
	 */
	@Override
	public Customer createCustomer(Customer customer) {
		log.debug("Attempting to create new customer - ID : {}", customer.getId());
		return customerRepository.save(customer);
	}

	/**
	 * Update customer
	 * @param customer the customer to update
	 * @return the updated customer
	 */
	@Override
	public Customer updateCustomer(Customer customer) {
		log.debug("Attempting to update customer {}...", customer.getId());
		
		Customer existingCustomer = customerRepository.findById(customer.getId()).orElseThrow(UnknownResourceException::new); 
		existingCustomer.setLastname(customer.getLastname()); 
		existingCustomer.setFirstname(customer.getFirstname());
		existingCustomer.setCompany(customer.getCompany());
		existingCustomer.setMail(customer.getMail());
		existingCustomer.setPhone(customer.getPhone());
		existingCustomer.setMobile(customer.getMobile());
		existingCustomer.setNotes(customer.getNotes());
		existingCustomer.setActive(customer.isActive());
		
		return customerRepository.save(existingCustomer);
	}

	@Override
	public void patchCustomerStatus(Integer customerId, boolean active) {
		log.debug("Attempting to patch customer {} with active = {}...",customerId, active);
		Customer existingCustomer = customerRepository.findById(customerId).orElseThrow(UnknownResourceException::new);
		existingCustomer.setActive(active);
		customerRepository.save(existingCustomer);
		
	}

	/**
	 * Delete customer
	 * @param id the customer id
	 */
	@Override
	public void deleteCustomer(Integer id) {
		Customer customer = customerRepository.findById(id).orElseThrow(UnknownResourceException::new);
		log.debug("Attempting to delete customer {}",id);
		if(null != customer.getOrders() && !customer.getOrders().isEmpty())
			customerRepository.delete(customer);
	}
}
