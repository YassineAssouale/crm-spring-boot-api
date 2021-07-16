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
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import ya.dev.crm.api.v1.dto.UserDto;
import ya.dev.crm.exception.UnknownResourceException;
import ya.dev.crm.mapper.UserMapper;
import ya.dev.crm.service.UserService;


@RestController
@RequestMapping("/v1/users")
@CrossOrigin(value={"*"}, allowedHeaders = {"*"})
@PreAuthorize("hasRole('ROLE_USER')")
public class UserApi {
	
	Logger log = LoggerFactory.getLogger(UserApi.class);
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	UserService userService;
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	@ApiOperation(value = "Returns the list of all the users", nickname = "Get all the users", 
				  response = UserDto.class, responseContainer = "List")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Here is the list of all the users.")})
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<UserDto>> getAll(){
		return ResponseEntity.ok(userService.getAllUsersSortedByUsernameAscending().stream()
			   .map(userMapper::mapUserToUserDto).collect(Collectors.toList()));
	}
	
	@GetMapping(value = "{/id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	@ApiOperation(value = "Returns a user by its ID.", nickname = "Get a user by its ID.", response = UserDto.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Here is the user.")})
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<UserDto> getById(@PathVariable final Integer id){
		try {
			return ResponseEntity.ok(userMapper.mapUserToUserDto(userService.getUserById(id)));
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found!");
		}
	}
	
	@GetMapping(value = "{/login}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	@ApiOperation(value = "Returns a user by its username ans password.", 
				  nickname = "Get a user by its username and password.",
				  response = UserDto.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Here is the user.")})
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<UserDto> getByUsernameAndPassword(@RequestParam String username, @RequestParam String password){
		try {
			return ResponseEntity.ok(userMapper.mapUserToUserDto(userService.getUserByUsernameAndPassword(username, password)));
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found!");
		}
	}
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE},
				 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
	@ApiOperation(value = "Creates a new user.", nickname = "User creation.", response = UserDto.class)
	@ApiResponses(value = {@ApiResponse(code = 201, message = "User creation complete!")})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
		log.debug("Attempting to create user with username {}", userDto.getUsername());
		UserDto newUser = userMapper.mapUserToUserDto(userService.createUser(userMapper.mapUserDtoToUser(userDto)));
		return ResponseEntity.created(URI.create("/v1/users/" + newUser.getId())).body(newUser);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(path = "{id}")
	@ApiOperation(value = "Deletes a user.", nickname = "User deletion.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "User deletion complete !"), @ApiResponse(code = 404, message = "User not found !") })
	public ResponseEntity<Void> deleteUser(@PathVariable final Integer id){
		try {
			log.debug("Preparing to delete user with id {}", id);
			userService.deleteUser(id);
			return ResponseEntity.noContent().build();
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found!");
		}
	}
	
	@PutMapping(path = "{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	@ApiOperation(value = "Updates a user.", nickname = "User update.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "User update complete !"), @ApiResponse(code = 404, message = "User not found !") })
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Void> updateUser(@PathVariable final Integer id, @RequestBody UserDto userDto){
		try {
			log.debug("Updating user {}", id);
			userDto.setId(id);
			userService.updateUser(userMapper.mapUserDtoToUser(userDto));
			log.debug("Successfully updated user {}", id);
			return ResponseEntity.noContent().build();	
		} catch (UnknownResourceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found!");
		}
	}
	
	@PatchMapping(path = "{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	@ApiOperation(value = "Updates the mail of a user.", nickname = "User mail upadte.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "User mail update complete !"), @ApiResponse(code = 404, message = "User not found !") })
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Void> patchUserMail(@PathVariable final Integer id,
			@RequestBody UserDto userDto) {
		try {
			log.debug("Patching user {} mail", id);
			userService.patchUserMail(id, userDto.getMail());
			log.debug("Successfully patched user {}", id);
			return ResponseEntity.noContent().build();
		} catch (UnknownResourceException ure) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
		}
	}

}
