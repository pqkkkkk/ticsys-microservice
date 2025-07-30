package org.pqkkkkk.ticsys.identity_service.dao.jpa_repository;

import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findByEmail(String email);
}
    