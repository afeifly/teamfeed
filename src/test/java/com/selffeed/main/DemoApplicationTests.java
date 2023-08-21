package com.selffeed.main;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@SpringBootTest(classes = DemoApplication.class)
class DemoApplicationTests {

	@Autowired
	DataSource dataSource;
	@Test
	void contextLoads() {
		log.info(dataSource.getClass().toString());
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			log.info(conn.toString());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}


}
