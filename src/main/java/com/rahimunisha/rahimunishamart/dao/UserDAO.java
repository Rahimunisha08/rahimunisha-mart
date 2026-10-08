package com.rahimunisha.rahimunishamart.dao;

import com.rahimunisha.rahimunishamart.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User create(User user);
    boolean update(User user);
    List<User> findAll();
    boolean delete(Long id);
}
