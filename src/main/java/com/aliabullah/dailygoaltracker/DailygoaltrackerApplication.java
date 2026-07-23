package com.aliabullah.dailygoaltracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DailygoaltrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailygoaltrackerApplication.class, args);
	}

}

