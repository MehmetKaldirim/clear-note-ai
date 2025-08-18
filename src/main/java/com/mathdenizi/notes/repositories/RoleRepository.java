package com.mathdenizi.notes.repositories;

import com.mathdenizi.notes.models.AppRole;
import com.mathdenizi.notes.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

}