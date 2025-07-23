package org.pqkkkkk.identity_service.dao;

import org.pqkkkkk.identity_service.Constants.RoleEnum;
import org.pqkkkkk.identity_service.dao.jpa_repository.RoleRepository;
import org.pqkkkkk.identity_service.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleJpaDao implements RoleDao {
    private final RoleRepository roleRepository;

    public RoleJpaDao(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public Role getRoleByName(RoleEnum name) {
        return roleRepository.findByName(name);
    }

}
