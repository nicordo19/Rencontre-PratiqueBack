package com.rencontrR.controllers.repository;

import com.rencontrR.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
