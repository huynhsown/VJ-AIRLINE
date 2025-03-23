package com.vietjoke.vn.config.seeding;

import com.vietjoke.vn.entity.user.RoleEntity;
import com.vietjoke.vn.repository.user.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(generateRoles());
            log.info("Roles saved");
        }
    }

    private List<RoleEntity> generateRoles() {
        List<RoleEntity> roles = new ArrayList<>();

        RoleEntity adminRole = RoleEntity.builder()
                .roleName("ADMIN")
                .roleCode("ADMIN")
                .build();

        RoleEntity customerRole = RoleEntity.builder()
                .roleName("CUSTOMER")
                .roleCode("CUSTOMER")
                .build();

        roles.add(adminRole);
        roles.add(customerRole);

        return roles;
    }
}
