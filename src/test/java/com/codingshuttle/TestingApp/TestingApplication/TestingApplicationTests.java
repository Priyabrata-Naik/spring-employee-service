package com.codingshuttle.TestingApp.TestingApplication;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Slf4j
class TestingApplicationTests {

	@BeforeEach
	void setUp() {
		log.info("Starting the method, setting up config");
	}

	@AfterEach
	void tearDown() {
		log.info("Tearing down the method");
	}

	@BeforeAll
	static void setUpOnce() {
		log.info("Set up Once...");
	}

	@AfterAll
	static void tearDownOnce() {
		log.info("Tearing down all...");
	}

	@Test
//	@Disabled
	void testNumberOne() {
		assertThat("Apple")
				.isEqualTo("Apple")
				.startsWith("App")
				.endsWith("le")
				.hasSize(5);

	}

	@Test
//	@DisplayName(value = "displayTestTwo")
	void testNumberTwo() {
		int a = 5;
		int b = 3;
		int result = addTwoNumbers(a, b);

//		Assertions.assertEquals(8, result);

		Assertions.assertThat(result)
				.isEqualTo(8)
				.isCloseTo(9, Offset.offset(1));
	}

	@Test
	void testDivideTwoNumbers_whenDenominatorIsZero_thenArithmeticException() {
		int a = 5;
		int b = 0;

		Assertions.assertThatThrownBy(() -> divideTwoNumbers(a, b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("Tried to divide by zero");
	}

	int addTwoNumbers(int a, int b) {
		return a + b;
	}

	double divideTwoNumbers(int a, int b) {
		try {
			return a / b;
		} catch (ArithmeticException e) {
			log.error("Arithmetic exception occurred: " + e.getLocalizedMessage());
			throw new ArithmeticException("Tried to divide by zero");
		}
	}


}
