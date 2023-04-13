package net.javaguides.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BACKEND.Models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
