package com.backend.testing.service.interfaces;

import com.backend.testing.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User createUser(User user);
}
