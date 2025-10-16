package com.sdcompany.tadak.message.repository;

import com.sdcompany.tadak.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByLoginId(String loginId);
}
