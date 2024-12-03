package com.lab4.database_demo.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.lab4.database_demo.entity.UserEntity;
import com.lab4.database_demo.repository.UserRepository;
import com.lab4.database_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserEntity save(UserEntity entity) {
        return userRepository.save(entity);
    }

    @Override
    public byte[] getUserImage(long id) {
        UserEntity entity = userRepository.findById(id).orElseThrow(RuntimeException::new);

        return Base64.getDecoder().decode(entity.getImage());
    }

    @Override
    public void addUserImage(Long id, MultipartFile file) throws IOException {
        UserEntity entity = userRepository.findById(id).orElseThrow(RuntimeException::new);
        log.info("Saving picture");
        entity.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        userRepository.save(entity);
    }


    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByMail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
