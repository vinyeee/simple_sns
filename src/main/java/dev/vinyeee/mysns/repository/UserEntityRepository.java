package dev.vinyeee.mysns.repository;

import dev.vinyeee.mysns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface
UserEntityRepository extends JpaRepository<UserEntity,Integer> {

    Optional<UserEntity> findByUserName(String userName);
}
