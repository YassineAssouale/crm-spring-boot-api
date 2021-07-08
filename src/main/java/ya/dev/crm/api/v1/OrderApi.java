package ya.dev.crm.api.v1;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.Response;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import ya.dev.crm.api.v1.dto.OrderDto;
import ya.dev.crm.exception.UnknownResourceException;
import ya.dev.crm.mapper.OrderMapper;
import ya.dev.crm.service.OrderService;
@RestController
@RequestMapping("/v1/orders/")
@CrossOrigin(value = {"*"}, allowedHeaders = {"*"})
public class OrderApi {
	
	Logger log = LoggerFactory.getLogger(OrderApi.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	@ApiOperation(value = "Returns the list of all the orders.",nickname = "Get all the orders", 
				  response = OrderDto.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Here is the list of all the orders.") })
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<OrderDto>> getAll(){
		return ResponseEntity.ok(orderService.getAllOrders().stream().map(orderMapper::mapOrderToOrderDto)
				.collect(Collectors.toList()));
	}
	
	@GetMapping(value = "{/id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	@ApiOperation(value ="Returns an order by its ID.", nickname = "Get an order by its ID.",
				  response = OrderDto.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Here is the order.")})
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<OrderDto> getById(@PathVariable final Integer id){
		try {
			return ResponseEntity.ok(orderMapper.mapOrderToOrderDto(orderService.getById(id)));
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Not Found!");
		}
	}
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE},
			     consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	@ApiOperation(value ="Create a new order.", nickname = "Order creation.",
				  response = OrderDto.class)
	@ApiResponses(value = {@ApiResponse(code = 201, message = "Successfully creating order.")})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){
		log.debug("Attempting to create order with label {}", orderDto.getLabel());
		OrderDto newOrder = orderMapper.mapOrderToOrderDto(
				orderService.createOrder(orderMapper.mapOrderDtoToOrder(orderDto)));
		return ResponseEntity.created(URI.create("/v1/orders/" + newOrder.getId())).body(newOrder);
	}
	
	@DeleteMapping(path = "{id}")
	@ApiOperation(value = "Deletes an order", nickname = "Order deletion.")
	@ApiResponses(value = {@ApiResponse(code = 204, message = "Order deletion complete."),
						   @ApiResponse(code = 404, message = "Order Not Found!")})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteOrder(@PathVariable final Integer id){
		try {
			log.debug("Preparing to delete order with id {}", id);
			orderService.deleteOrder(id);
			return ResponseEntity.noContent().build();
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Order Not Found!");
		}
	}
	
	@PutMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
				consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
	@ApiOperation(value = "Updates an order", nickname = "Order Updating.")
	@ApiResponses(value = {@ApiResponse(code = 204, message = "Order update complete."),
						   @ApiResponse(code = 404, message = "Order Not Found!")})
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Void> updateOrder(@PathVariable final Integer id, @RequestBody OrderDto orderDto){
		try {
			log.debug("Preparing to update order with id {}", id);
			orderDto.setId(id);
			orderService.updateOrder(orderMapper.mapOrderDtoToOrder(orderDto));
			log.debug("Successfully updated order {}", id);
			return ResponseEntity.noContent().build();
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Order Not Found!");
		}	
	}
	
	@PatchMapping(path = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
				consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
	@ApiOperation(value = "Updates the lable of an order", nickname = "Order label update.")
	@ApiResponses(value = {@ApiResponse(code = 204, message = "Order's label update complete."),
						  @ApiResponse(code = 404, message = "Order Not Found!")})
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity patchOrderLabel(@PathVariable final Integer id, @RequestParam String label) {
		try {
			log.debug("Update order's label with id {}", id);
			orderService.patchOrderLabel(id, label);
			log.debug("Successfully updated order's label : {}", id);
			return ResponseEntity.noContent().build();
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Order Not Found!");
		}
	}
	
}
