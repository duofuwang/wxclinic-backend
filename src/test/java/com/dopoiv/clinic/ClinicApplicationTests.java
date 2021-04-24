package com.dopoiv.clinic;

import com.dopoiv.clinic.project.user.controller.UserController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApplication.class)
public class ClinicApplicationTests {

    @Autowired
    UserController userController;

    @Test
    void contextLoads() {
    }

}
