package com.monitor.monitor;

import java.io.IOException;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SampleController {

	@GetMapping("/")
	public String hello(HttpServletResponse response) throws IOException {
		log.info("Attempted access to / endpoint resulted in 403 Forbidden");
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
		return null;

	}
}
