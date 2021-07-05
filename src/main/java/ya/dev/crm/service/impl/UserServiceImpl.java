package ya.dev.crm.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ya.dev.crm.model.User;
import ya.dev.crm.exception.UnknownResourceException;
import ya.dev.crm.repository.UserRepository;
import ya.dev.crm.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public User getUserById(Integer id) {
		return userRepository.findById(id).orElseThrow(UnknownResourceException::new);
	}

	@Override
	public List<User> getAllUsersSortedByUsernameAscending() {
		return userRepository.findAll(Sort.by("username").ascending());
	}

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		log.debug("attempting to update a user {} ...", user.getId());
		User existingUser = userRepository.findById(user.getId()).orElseThrow(UnknownResourceException::new);
		existingUser.setUsername(user.getUsername());
		existingUser.setPassword(user.getPassword());
		existingUser.setMail(user.getMail());
		return userRepository.save(existingUser);
	}

	@Override
	public void patchUserMail(Integer userId, String mail) {
		log.debug("attempting to patch user {} with mail = {}...", userId, mail);
		User existingUser = userRepository.findById(userId).orElseThrow(UnknownResourceException::new);
		existingUser.setMail(mail);
		userRepository.save(existingUser);
	}

	@Override
	public void deleteUser(Integer id) {
		User user = userRepository.findById(id).orElseThrow(UnknownResourceException::new);
		log.debug("attempting to delete user {} ...",id);
		userRepository.delete(user);
		
	}

}
