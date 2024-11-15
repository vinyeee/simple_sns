package dev.vinyeee.mysns.service;

import dev.vinyeee.mysns.exception.ErrorCode;
import dev.vinyeee.mysns.exception.SnsApplicationException;
import dev.vinyeee.mysns.model.entity.PostEntity;
import dev.vinyeee.mysns.model.entity.UserEntity;
import dev.vinyeee.mysns.repository.PostEntityRepository;
import dev.vinyeee.mysns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;

    @Transactional
    public void create(String title, String body, String userName){

        // user find
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found",userName)));

        // post save
        postEntityRepository.save(new PostEntity());
        // return

    }
}
