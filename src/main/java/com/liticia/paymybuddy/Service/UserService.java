package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getAll();
    List<User> search(String keyword);
    Page<User> findPaginated(Pageable pageable);
}
