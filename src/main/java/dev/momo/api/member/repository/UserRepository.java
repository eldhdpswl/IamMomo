package dev.momo.api.member.repository;


import dev.momo.api.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);


}