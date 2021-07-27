package com.fan.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// requires java, spring boot, jquery, ajax, and maven installed
// written in eclipse ide and the web service runs on apache tomcat
//
// to build the web service, in git bash...
//    cd eclipse-workspace/FanWebService
//    mvn clean install
//    ./mvnw spring-boot:run
//
// to run, in a web browser type http://localhost:8080 and press enter
//
// this app demonstrates back-end web services (java, spring boot, json) and front-end client (javascript, jquery, ajax, json, css), MVC software design

@SpringBootApplication
@EnableScheduling
public class FanWebService {

	public static void main(String[] args) {
		SpringApplication.run(FanWebService.class, args);
	}
}
