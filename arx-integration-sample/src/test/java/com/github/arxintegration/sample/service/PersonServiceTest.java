package com.github.arxintegration.sample.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.Data.DefaultData;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.arxintegration.DbUnitTestExecutionListener;
import com.github.arxintegration.annotation.DatabaseSetup;
import com.github.arxintegration.sample.entity.PersonArx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonServiceTest {
	@Autowired
	private PersonService personService;

	/** ARX specitfic classes */
	protected static ARXAnonymizer anonymizer = new ARXAnonymizer();
	protected static ARXConfiguration config = ARXConfiguration.create();
	protected static ARXResult result;
	private final DefaultData data = Data.create();
    
	private final String sqlP = "Select p from com.github.arxintegration.sample.entity.PersonArx p";
    /** Helper class */
	private Printer printer = new Printer();
    private List<PersonArx> persons;
    protected static final SimpleDateFormat arxFormat = new SimpleDateFormat("dd.MM.yyyy");
    /** CSV Input files */
	protected static final String CSV_SMALL = "data/20_persons.csv";
	protected static final String CSV_LARGE = "data/146k_persons.csv";
	/** DB connection settings */
	protected static final String ROWNUM = "10000";
	protected static final String TABLE = "PERSON_ARX";
	protected static final String dbUrl = "jdbc:oracle:thin:@localhost:1521/IVZPDB";
	protected static final String dbUser = "ARX";
	protected static final String dbPw = "ARX";
	
	@Test
	@DatabaseSetup("sampleData.xml")
	public void testSearch() throws Exception {
		List<PersonArx> personList = this.personService.find("Nen");
		assertEquals(1, personList.size());
		assertEquals("Jevdjenic", personList.get(0).getOfficialName());
		personList = this.personService.find("Jevd");
		assertEquals(2, personList.size());
	}

	@Test
	@DatabaseSetup("sampleData.xml")
	public void testRemove() throws Exception {
		persons = personService.executeQuery(sqlP);
		int sizeBeforeDelete = persons.size();
		this.personService.remove(0);
		this.personService.remove(1);
		this.personService.remove(40);
		this.personService.remove(42);
		this.personService.remove(43);
		this.personService.remove(44);
		this.personService.remove(45);
		this.personService.remove(46);
		this.personService.remove(47);
		this.personService.remove(48);
		this.personService.remove(49);
		this.personService.remove(50);
		this.personService.remove(52);
		persons = personService.executeQuery(sqlP);
		int sizeAfterDelete = persons.size();
		System.out.println("Size before deletion: " + sizeBeforeDelete);
		System.out.println("Size after deletion: " + sizeAfterDelete);
		assertNotEquals(sizeBeforeDelete, sizeAfterDelete);
	}

}
