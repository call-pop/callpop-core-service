package com.sdcompany.callpop.message.repository;

import com.sdcompany.callpop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

}
