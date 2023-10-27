package com.example.hamster_backend.api;

import com.example.hamster_backend.model.entities.Role;
import com.example.hamster_backend.model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.hamster_backend.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class UserController {

	static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private ObjectMapper mapper;

	/*
	 * 
	 * Get user by username. Needs as @RequestParam username
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/users")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> getUserByUsername(@RequestParam(name = "username", required = false) String username) {
		if(username == null) {
			return new ResponseEntity<>(userServiceImpl.selectMany(), HttpStatus.OK);
		}
		log.info("XXXXUSERNAME:" + username + "|");
		User userFound = userServiceImpl.findUserByUsername(username);
		log.info("USER:" + userFound);

		if (userFound == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(userFound, HttpStatus.OK);
		//TODO send fileNames
	}
	
	/*
	 * 
	 * Get user by id. Needs as @PathVariable id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/users/{id}")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> getUserById(@PathVariable long id) {
		User userFound = userServiceImpl.findUserByID(id);
		if (userFound == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(userFound, HttpStatus.OK);
	}

	/*
	 * Creating new user if not exist Needs as RequestBody user object
	 * 
	 * @param json
	 * @return
	 */

	@PostMapping("/users")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> createUser(@RequestBody JsonNode node) {
		User user = mapper.convertValue(node.get("user"), User.class);
		if (user == null) {
			return new ResponseEntity<>("Could not save user -> wrong data "+ node.toPrettyString(), HttpStatus.BAD_REQUEST);
		}
		if (!userServiceImpl.saveUser(user)) {
			return new ResponseEntity<>("Could not save user -> error in database " + node.toPrettyString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/*
	 * Updating user Needs as RequestBody user object
	 * 
	 * @param json
	 * @return
	 */
	@PutMapping("/users")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> updateUser(@RequestBody JsonNode node) {
		User user = mapper.convertValue(node.get("user"), User.class);
		if (!userServiceImpl.updateUser(user)) {
			return new ResponseEntity<>("Could not update user!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/*
	 * Deleting user Needs as RequestBody user object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/users")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> deleteUser(@RequestBody JsonNode node) {
		User user = mapper.convertValue(node.get("user"), User.class);
		if (!userServiceImpl.deleteUser(user.getId())) {
			return new ResponseEntity<>("Could not delete user!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/*
	 * 
	 * Adding role to user. Needs as RequestBody role_id and user_id
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/roles")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> addRole(@RequestBody JsonNode node) {
		User user = mapper.convertValue(node.get("user"), User.class);
		Role role = mapper.convertValue(node.get("role"), Role.class);
		if (!userServiceImpl.insertUserRole(user.getId(), role.getId())) {
			return new ResponseEntity<>("Could not insert new Role!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/*
	 * 
	 * Removing role from user. Needs as RequestBody role_id and user_id
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/roles")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> removeRole(@RequestBody JsonNode node) {
		User user = mapper.convertValue(node.get("user"), User.class);
		Role role = mapper.convertValue(node.get("role"), Role.class);
		if (!userServiceImpl.removeUserRole(user.getId(), role.getId())) {
			return new ResponseEntity<>("Could not remove Role!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
