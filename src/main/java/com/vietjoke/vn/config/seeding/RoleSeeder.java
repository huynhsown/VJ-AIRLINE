package com.vietjoke.vn.config.seeding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietjoke.vn.entity.user.RoleEntity;
import com.vietjoke.vn.repository.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;

    public List<RoleEntity> seedRoles() {
        try{
            InputStream inputStream = new ClassPathResource("/data/roles.json").getInputStream();
            return List.of(objectMapper.readValue(inputStream, RoleEntity[].class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) {
        for(RoleEntity role : seedRoles()){
            if(!roleRepository.existsByRoleCode(role.getRoleCode())){
                roleRepository.save(role);
            }
        }
    }
}
