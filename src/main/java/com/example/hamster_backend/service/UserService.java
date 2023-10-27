package com.example.hamster_backend.service;


import com.example.hamster_backend.model.entities.User;

import java.util.List;

public interface UserService {
    User findUserByID(long id);

    User findUserByUsername(String username);

    boolean saveUser(User user);

    boolean updateUser(User user);

    long getFutureID();

    boolean deleteUser(long id);

    List<User> selectMany();

    long count();

    boolean insertUserRole(long user_id, long role_id);

    boolean removeUserRole(long user_id, long role_id);
}
