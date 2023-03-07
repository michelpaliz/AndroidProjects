package com.user_manager_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementApplication {

	public static boolean DEBUG = true;
	public static String CONFIG_FILE =  null;

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}

}
