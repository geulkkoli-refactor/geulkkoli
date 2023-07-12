package com.geulkkoli.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail( String email);

    Optional<User> findByUserNameAndPhoneNo (String userName, String phoneNo);

    Optional<User> findByEmailAndUserNameAndPhoneNo( String email,  String userName,String phoneNo);

    Optional<User> findByNickName(String nickName);

    Optional<User> findByPhoneNo(String phoneNo);

}
