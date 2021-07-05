package ya.dev.crm.api.v1;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ya.dev.crm.api.v1.dto.CustomerDto;
import ya.dev.crm.exception.UnknownResourceException;
import ya.dev.crm.mapper.CustomerMapper;
import ya.dev.crm.service.CustomerService;

@RestController
@RequestMapping("/v1/customers")
@CrossOrigin(value = {"*"}, allowedHeaders = {"*"} )
public class CustomerApi {
	
	Logger log = LoggerFactory.getLogger(CustomerApi.class);
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	@ApiOperation(value = "Returns the list of all the customers", 
				  nickname = "Get all the customers", 
				  response = CustomerDto.class, 
				  responseContainer = "List")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Here is the list of all the customers.")})
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<List<CustomerDto>> getAll(){
		return ResponseEntity.ok(customerService.getAllCustomersSortByLastnameAscending().stream().map(customerMapper::mapCustomerToCustomerDto)
				.collect(Collectors.toList()));
	}
	
	@GetMapping(value = "{/id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	@ApiOperation(value = "Returns the customer with the given id", 
				  nickname = "Get a customer by its ID.",
				  response = CustomerDto.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Here is the customer.")})
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<CustomerDto> getById(@PathVariable final Integer id){
		try {
			return ResponseEntity.ok(customerMapper.mapCustomerToCustomerDto(customerService.getCustomerById(id)));
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found");
		}
	}
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, 
				 consumes = {MediaType.APPLICATION_JSON_VALUE, 
							MediaType.TEXT_PLAIN_VALUE})
	@Transactional
	@ApiOperation(value = "Creates a new customer", 
				 nickname = "Customer creation",
				 response = CustomerDto.class)
	@ApiResponses(value = {@ApiResponse(code = 201, message = "Customer creation complete !")})
	@PreAuthorize("hasROLE('ROLE_ADMIN')")
	public ResponseEntity<CustomerDto> createCustomer(@RequestBody final CustomerDto customerDto){
		log.debug("Attempting to create customer with name {}", customerDto.getLastname());
		CustomerDto newCustomer = customerMapper.mapCustomerToCustomerDto(customerService.createCustomer(customerMapper.mapCustomerDtoToCustomer(customerDto)));
		return ResponseEntity.created(URI.create("/v1/customers/" + newCustomer.getId())).body(newCustomer);
	}
	
	@PutMapping(path = "{/id}", produces = {MediaType.APPLICATION_JSON_VALUE}, 
				 consumes = {MediaType.APPLICATION_JSON_VALUE, 
							MediaType.TEXT_PLAIN_VALUE} )
	@Transactional
	@ApiOperation(value = "Updates a customer",
				  nickname = "Customer update",
				  response = CustomerDto.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Customer update complete !") })
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity <Void> updateCustomer(@PathVariable final Integer id, @RequestBody CustomerDto customerDto){
		try {
			log.debug("Updating customer {}", id);
			customerDto.setId(id);
			customerService.updateCustomer(customerMapper.mapCustomerDtoToCustomer(customerDto));
			log.debug("Successfully updated customer {}", id);
			return ResponseEntity.noContent().build();
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found!");
		}
	}
	
	@PatchMapping(path = "{/id}", produces = {MediaType.APPLICATION_JSON_VALUE},
				  consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
	@Transactional
	@ApiOperation(value = "Updates the status of a customer (active or not)",
				  nickname = "Customer status update.",
				  response = CustomerDto.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Customer status update complete!"),
						    @ApiResponse(code = 404, message = "Customer not found !")})
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Void> patchCustomerStatus(@PathVariable final Integer id, @RequestBody CustomerDto customerDto){
		try {
			log.debug("Patching customer {}", id);
			customerService.patchCustomerStatus(id, customerDto.getActive());
			log.debug("Successfully patched customer {}", id);
			return ResponseEntity.noContent().build();
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found!");
		}
		
	}
	
	@DeleteMapping(path = "{/id}")
	@Transactional
	@ApiOperation(value = "Deletes a customer",
				  nickname = "Customer deletion",
				  response = CustomerDto.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Customer deletion complete"),
							@ApiResponse(code = 404, message = "Customer Not Found!")})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteCustomer(@PathVariable final Integer id){
		try {
			log.debug("Preparing to delete customer with id {}",id);
			customerService.deleteCustomer(id);
			log.debug("Successfully deletion customer");
			return ResponseEntity.noContent().build();
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found!");
		}
	}
	
	

}
