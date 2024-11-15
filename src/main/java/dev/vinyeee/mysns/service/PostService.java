package dev.vinyeee.mysns.service;

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

        // 유저 find

        // post save

        // return


    }
}
