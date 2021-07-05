package ya.dev.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import ya.dev.crm.model.Order;
import ya.dev.crm.api.v1.dto.OrderDto;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
	
	@Mapping(target="customerId", source="customer.id")
	OrderDto mapOrderToOrderDto(Order order);
	
	@Mapping(target="customer.id", source="customerId")
	Order mapOrderDtoToOrder(OrderDto orderDto);
}
