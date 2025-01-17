package net.javaguides.springboot.controller;

import java.sql.Timestamp;
import java.util.List;

import BACKEND.Controllers.UserController;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BACKEND.Models.User;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

@RestController
@RequestMapping("/api/users")
public class ApiController {

	@Autowired
	private UserRepository userRepository;
	private SessionFactory sessionFactory;
	private UserController userController;

	public ApiController(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
		this.userController = new UserController(sessionFactory);
	}

	// get all users
	@GetMapping
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	// get user by id
	@GetMapping("/{id}")
	public User getUserById(@PathVariable (value = "id") int userId) throws ClassNotFoundException {
		return this.userController.getById(userId);
//		return this.userRepository.findById(userId)
//				.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
	}

	// create user
	@PostMapping
	public int createUser(@RequestBody User user) {
		user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt() ));
		return this.userController.create(user);
//		return this.userRepository.save(user);
	}
	
	// update user
	@PutMapping("/{id}")
	public User updateUser(@RequestBody User user, @PathVariable ("id") long userId) {
//		this.userController.updateByNewData(userId, User)
		 User existingUser = this.userRepository.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
		 existingUser.setFirstName(user.getFirstName());
		 existingUser.setLastName(user.getLastName());
		 existingUser.setEmail(user.getEmail());
		 return this.userRepository.save(existingUser);
	}
	
	// delete user by id
	@DeleteMapping("/{id}")
	public boolean deleteUser(@PathVariable ("id") int userId) throws ClassNotFoundException { //ResponseEntity<User>
//		 User existingUser = this.userRepository.findById(userId)
//					.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
//		 this.userRepository.delete(existingUser);
//		 return ResponseEntity.ok().build();
		return this.userController.delete(userId);
	}
}
