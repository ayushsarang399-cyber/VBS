package com.vbs.demo.repositories;
// #majdur (table se baat karne wala) ye banda data save karega table mei
import com.vbs.demo.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// majdur ko mysql queries mei help lagtay JpaRepository ka<tablename,Primary key ka datatype>
@Repository //ye system ko batane keliye ki ye majdur hai
public interface UserRepo extends JpaRepository<User,Integer>{
    User findByUsername(String username);

    User findByEmail(String value);

    List<User> findAllByRole(String customer, Sort sort);

    List<User> findByUsernameContainingIgnoreCaseAndRole(String keyword, String customer);
}
