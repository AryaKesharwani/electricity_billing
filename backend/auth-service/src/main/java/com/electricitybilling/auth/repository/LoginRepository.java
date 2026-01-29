package com.electricitybilling.auth.repository;

import com.electricitybilling.auth.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

    Optional<Login> findByEmail(String email);

    Optional<Login> findByConsumerId(String consumerId);

    Optional<Login> findByUserId(String userId);
}
