package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.Role;
import com.manageyourtools.toolroom.domains.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(RoleEnum roleEnum);
}
