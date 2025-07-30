package org.pqkkkkk.ticsys.identity_service.dao.jpa_repository;


import org.pqkkkkk.ticsys.identity_service.Constants.RoleEnum;
import org.pqkkkkk.ticsys.identity_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum name);
}
