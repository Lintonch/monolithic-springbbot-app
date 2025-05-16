package com.ffma.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ffma.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{
	
	Optional<Role> findByName(String name);

}
