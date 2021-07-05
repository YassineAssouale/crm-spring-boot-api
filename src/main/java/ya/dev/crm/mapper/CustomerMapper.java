package ya.dev.crm.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import ya.dev.crm.model.Customer;
import ya.dev.crm.api.v1.dto.CustomerDto;
import ya.dev.crm.api.v1.dto.OrderDto;

@Component
/**
 * Mapstruct simplifies the implementation of mappings between Java bean types
 * Mapping DTO classes and Model classes
 *
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
	
	@Mapping(target = "orders", expression = "java(getOrders(customer))")
    CustomerDto mapCustomerToCustomerDto(Customer customer);

	default List<OrderDto> getOrders(Customer customer) {
		List<OrderDto> orders = new ArrayList<>();
		if (null != customer.getOrders()) {
			orders = customer.getOrders().stream().map(order -> new OrderDto(order.getId(), order.getLabel(), order.getAdrEt(),
					order.getNumberOfDays(), order.getTva(), order.getStatus(), order.getType(), order.getNotes(), customer.getId()))
	                .collect(Collectors.toList());
		}
        return orders;
    }
	
    Customer mapCustomerDtoToCustomer(CustomerDto customerDto);

}