package org.example.expert.domain.user.service;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    private static final int BATCH_SIZE = 1000;
    @Test
    void 유저_100만개_생성(){
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            String email = "user" + i + "@test.com";
            String password = "password";
            String nickname = "user" + i;

            users.add(new User(email, password, UserRole.ROLE_USER, nickname));

            if(i % BATCH_SIZE == 0){
                userRepository.saveAll(users);
                users.clear();
                System.gc();
            }
        }
    }
}
