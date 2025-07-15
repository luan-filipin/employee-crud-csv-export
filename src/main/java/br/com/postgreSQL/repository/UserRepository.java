package br.com.postgreSQL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.postgreSQL.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	UserDetails findUserDetailsByLogin(String username);
	
	User findByLogin(String login);

	boolean existsByLogin(String login);

	
}
