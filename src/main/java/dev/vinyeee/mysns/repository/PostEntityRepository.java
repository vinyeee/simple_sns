package dev.vinyeee.mysns.repository;

import dev.vinyeee.mysns.model.Post;
import dev.vinyeee.mysns.model.entity.PostEntity;
import dev.vinyeee.mysns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity,Integer> {

    Page<PostEntity> findAllByUser(UserEntity entity, Pageable pageable);
}
