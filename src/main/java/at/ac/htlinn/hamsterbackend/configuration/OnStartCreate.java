package at.ac.htlinn.hamsterbackend.configuration;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import at.ac.htlinn.hamsterbackend.role.RoleRepository;
import at.ac.htlinn.hamsterbackend.role.model.Role;
import at.ac.htlinn.hamsterbackend.security.CustomPasswordEncoder;
import at.ac.htlinn.hamsterbackend.user.UserRepository;
import at.ac.htlinn.hamsterbackend.user.model.User;

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
