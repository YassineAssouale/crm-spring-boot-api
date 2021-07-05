package ya.dev.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ya.dev.crm.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * Get a user by mail
	 * @param mail
	 * @return
	 */
	User findByMail(String mail);
	
	/**
	 * Get a user by username
	 * @param username
	 * @return
	 */
	User findByUsername (String username);
	
	/**
	 * Get a user by its username and password
	 * @param username
	 * @param password
	 * @return
	 */
	User findByUsernameAndPassword(String username, String password);
	
}
