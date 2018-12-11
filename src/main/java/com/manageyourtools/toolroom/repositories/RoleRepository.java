package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.Role;
import com.manageyourtools.toolroom.domains.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//todo delete class and provide data.sql to load data - this class won't be used anyway
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(RoleEnum roleEnum);
}
