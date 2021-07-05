package ya.dev.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurtityConfig extends WebSecurityConfigurerAdapter{
	/**
	 * This section defines the user accounts which can be used for authentication as well as the roles each user has.
	 */
	@Bean
	InMemoryUserDetailsManager userDetailsManager() {
		
		UserBuilder builder = User.withDefaultPasswordEncoder();
		
		UserDetails autre = builder.username("autre").password("autre").roles("USER").build();
		UserDetails autre2 = builder.username("autre2").password("1234").roles("USER","ADMIN").build();
		
		return new InMemoryUserDetailsManager(autre,autre2);
		
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
	
	/**
	 * This section defines the security policy for the app.
	 * <p>
	 * <ul>
	 * <li>BASIC authentication is supported (enough for this REST-based demo).</li>
	 * <li>/employees is secured using URL security shown below.</li>
	 * <li>CSRF headers are disabled since we are only testing the REST interface, not a web one.</li>
	 * </ul>
	 * NOTE: GET is not shown which defaults to permitted.
	 *
	 * @param http
	 * @throws Exception
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http.httpBasic().and().authorizeRequests()
		// ORDERS
		.antMatchers(HttpMethod.GET,"/v1/orders/**").authenticated()
		.antMatchers(HttpMethod.POST,"/v1/orders").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE,"/v1/orders/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT,"/v1/orders/**").hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.PATCH,"/v1/orders/**").hasAnyRole("ADMIN","USER")
		// CUSTOMERS
		.antMatchers(HttpMethod.GET,"/v1/customers/**").permitAll()
		.antMatchers(HttpMethod.POST,"/v1/customers").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE,"/v1/customers/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT,"/v1/customers/**").hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.PATCH,"/v1/customers/**").hasAnyRole("ADMIN","USER")
		// USERS
		.antMatchers(HttpMethod.GET,"/v1/users/**").hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.POST,"/v1/users").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE,"/v1/users/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT,"/v1/users/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.PATCH,"/v1/users/**").hasRole("ADMIN")
		.and().csrf().disable();
	}
}
