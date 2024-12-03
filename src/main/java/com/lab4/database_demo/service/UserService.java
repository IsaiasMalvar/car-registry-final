package com.lab4.database_demo.service;

import com.lab4.database_demo.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    public UserEntity save(UserEntity entity);

    byte[] getUserImage(long id);

    void addUserImage(Long id, MultipartFile file) throws IOException;
}
