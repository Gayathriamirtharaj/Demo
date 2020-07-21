package com.example.Demo;

import service.UserService;
import model.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import vo.User;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class DemoApplication  implements CommandLineRunner {

	@Autowired
	UserService userService;



	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}


	@Override
	public void run(String... params) throws Exception {
		User admin = new User();
		admin.setFirstName("admin");
		admin.setLastName("admin");
		admin.setUsername("admin");
		admin.setPassword("admin");
		admin.setEmail("admin@example.com");
		admin.setMobile("1000010000");
		admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_ADMIN)));

		userService.register(admin);

		User user = new User();
		user.setFirstName("user");
		user.setLastName("user");
		user.setUsername("user");
		user.setPassword("user");
		user.setMobile("9000090000");
		user.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_USER)));

		userService.register(user);

	}

}


