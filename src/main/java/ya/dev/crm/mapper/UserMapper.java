package ya.dev.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import ya.dev.crm.model.User;
import ya.dev.crm.api.v1.dto.UserDto;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
	
	UserDto mapUserToUserDto(User user);
	
	User mapUserDtoToUser(UserDto userDto);

}
