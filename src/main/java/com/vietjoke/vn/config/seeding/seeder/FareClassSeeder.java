//package com.vietjoke.vn.config.seeding.seeder;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.vietjoke.vn.config.seeding.jsonObject.FareClass;
//import com.vietjoke.vn.config.seeding.jsonObject.Route;
//import com.vietjoke.vn.entity.fleet.AirlineEntity;
//import com.vietjoke.vn.entity.pricing.FareClassEntity;
//import com.vietjoke.vn.repository.fleet.AirlineRepository;
//import com.vietjoke.vn.repository.pricing.FareClassRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import java.io.InputStream;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class FareClassSeeder implements CommandLineRunner {
//
//    private final FareClassRepository fareClassRepository;
//    private final ObjectMapper objectMapper;
//    private final AirlineRepository airlineRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        InputStream inputStream = new ClassPathResource("/data/fareclasses.json").getInputStream();
//        List<FareClass> fareClasses = List.of(objectMapper.readValue(inputStream, FareClass[].class));
//
//        for(FareClass fareClass : fareClasses) {
//
//            Optional<AirlineEntity> airlineEntity = airlineRepository.findByAirlineCode(fareClass.getAirlineCode());
//            if(fareClassRepository.existsByCode(fareClass.getCode())
//            && airlineEntity.isEmpty()) continue;
//
//            FareClassEntity fareClassEntity = new FareClassEntity();
//            fareClassEntity.setCode(fareClass.getCode());
//            fareClassEntity.setName(fareClass.getName());
//            fareClassEntity.setDescription(fareClass.getDescription());
//            fareClassEntity.setIsActive(fareClass.isActive());
//            fareClassEntity.setBaggageAllowance(fareClass.getBaggageAllowance());
//            fareClassEntity.setSeatSelection(fareClass.isSeatSelection());
//            fareClassEntity.setMealIncluded(fareClass.isMealIncluded());
//            fareClassEntity.setChangeAllowed(fareClass.isChangeAllowed());
//            fareClassEntity.setRefundAllowed(fareClass.isRefundAllowed());
//            fareClassEntity.setChangeFee(fareClass.getChangeFee());
//            fareClassEntity.setRefundFee(fareClass.getRefundFee());
//            fareClassEntity.setAirlineEntity(airlineEntity.get());
//            fareClassRepository.save(fareClassEntity);
//
//        }
//    }
//}
