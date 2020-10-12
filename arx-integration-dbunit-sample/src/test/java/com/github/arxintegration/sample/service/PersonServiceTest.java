package com.github.arxintegration.sample.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.arxintegration.DbUnitTestExecutionListener;
import com.github.arxintegration.annotation.DatabaseSetup;
import com.github.arxintegration.annotation.ExpectedDatabase;
import com.github.arxintegration.sample.entity.Person;
import com.github.arxintegration.sample.service.PersonService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class PersonServiceTest {

	@Autowired
	private PersonService personService;

	@Test
	@DatabaseSetup("PersonRepositoryArx_dbunit.xml")
	public void testFind() throws Exception {
		List<Person> personList = this.personService.find("jevd");
		assertEquals(1, personList.size());
		assertEquals("Nenad", personList.get(0).getFirstName());
	}

	@Test
	@DatabaseSetup("sampleData.xml")
	@ExpectedDatabase("expectedData.xml")
	public void testRemove() throws Exception {
		this.personService.remove(1);
	}

}
