package com.example.hamster_backend.config;

import java.util.Arrays;
import java.util.HashSet;

import com.example.hamster_backend.user.Role;
import com.example.hamster_backend.security.CustomPasswordEncoder;
import com.example.hamster_backend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import com.example.hamster_backend.user.RoleRepository;
import com.example.hamster_backend.user.UserRepository;

import javax.persistence.EntityManager;


@Component
@PropertySource("classpath:settings.properties")
public class OnStartCreate implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	private final CustomPasswordEncoder passwordEncoder;

	// TODO: creating database here not working yet!
	// but a beginning is there
	private final EntityManager entityManager;

	@Value("${database}")
	private String database; 
	
	@Autowired
	public OnStartCreate(UserRepository userRepository, CustomPasswordEncoder passwordEncoder,
			RoleRepository roleRepository, EntityManager entityManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.entityManager = entityManager;
	}

	@Override
	public void run(String... args) {
		try {
			entityManager.getTransaction().begin();
			entityManager.createNativeQuery(String.format("CREATE DATABASE %s", database)).executeUpdate();
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			// database already exists
		}
		if (roleRepository.count() == 0) {
			roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("DEV"));
			roleRepository.save(new Role("TEACHER"));
			roleRepository.save(new Role("USER"));
		}

		if (userRepository.count() == 0) {
			User user = new User();
			user.setUsername("admin");
			user.setActive(true);
			user.setPassword(passwordEncoder.encode("admin"));
			Role userRole = roleRepository.findByRole("ADMIN");
			user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			userRepository.save(user);
		}
	}
}
