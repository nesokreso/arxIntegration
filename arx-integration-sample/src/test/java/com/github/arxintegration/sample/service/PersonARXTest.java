/**
 * Trivadis AG: 2020
 * 
 * Project: IVZ & IUBH ARX analysis
 */
package com.github.arxintegration.sample.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.MicroAggregationFunction;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.DataGeneralizationScheme;
import org.deidentifier.arx.DataGeneralizationScheme.GeneralizationDegree;
import org.deidentifier.arx.DataType;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased.Order;
import org.deidentifier.arx.criteria.AverageReidentificationRisk;
import org.deidentifier.arx.criteria.EDDifferentialPrivacy;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.metric.Metric;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.arxintegration.DbUnitTestExecutionListener;
import com.github.arxintegration.sample.entity.PersonArx;

/**
 * Test cases for {@link PersonRepositoryJPA} and ARX library
 * 
 * @author nej
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class PersonARXTest {
	/** Column names of person data input */
	protected static final String ID = "ID";
	protected static final String ORGANISATION_NAME = "ORGANISATION_NAME";
	protected static final String ORGANISATION_ADDITIONAL_NAME = "ORGANISATION_ADDITIONAL_NAME";
	protected static final String DEPARTMENT = "DEPARTMENT";
	protected static final String OFFICIAL_NAME = "OFFICIAL_NAME";
	protected static final String ORIGINAL_NAME = "ORIGINAL_NAME";
	protected static final String FIRST_NAME = "FIRST_NAME";
	protected static final String DATE_OF_BIRTH = "DATE_OF_BIRTH";
	protected static final String PLACE_OF_ORIGIN_NAME = "PLACE_OF_ORIGIN_NAME";
	protected static final String SECOND_PLACE_OF_ORIGIN_NAME = "SECOND_PLACE_OF_ORIGIN_NAME";
	protected static final String PLACE_OF_BIRTH_COUNTRY = "PLACE_OF_BIRTH_COUNTRY";
	protected static final String SEX = "SEX";
	protected static final String LANGUAGE = "LANGUAGE";
	protected static final String NATIONALITY = "NATIONALITY";
	protected static final String COUNTRY_OF_ORIGIN = "COUNTRY_OF_ORIGIN";
	protected static final String DATE_OF_DEATH = "DATE_OF_DEATH";
	protected static final String REMARK = "REMARK";
	protected static final String LAST_MEDICAL_CHECKUP = "LAST_MEDICAL_CHECKUP";
	protected static final String NEXT_MEDICAL_CHECKUP = "NEXT_MEDICAL_CHECKUP";
	protected static final String PHONE_NUMBER = "PHONE_NUMBER";
	protected static final String CELL_NUMBER = "CELL_NUMBER";
	protected static final String EMAIL = "EMAIL";
	protected static final String GUARDIANSHIP = "GUARDIANSHIP";
	protected static final String CURRENT_TOWN = "CURRENT_TOWN";
	protected static final String CURRENT_ZIP_CODE = "CURRENT_ZIP_CODE";
	protected static final String MANDATOR = "MANDATOR";
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

	@Autowired
	private PersonService personService;
	
    public PersonARXTest() {
        printer = new Printer();
    }

    @Before
    public void setup() throws Exception {
        persons = personService.executeQuery(sqlP);
        data.add(ID, ORGANISATION_NAME, ORGANISATION_ADDITIONAL_NAME, DEPARTMENT, OFFICIAL_NAME, ORIGINAL_NAME,
                FIRST_NAME, DATE_OF_BIRTH);

        for (PersonArx p : persons) {
            data.add(String.valueOf(p.getId()), String.valueOf(p.getOrganisationName()),
                    String.valueOf(p.getOrganisationAdditionalName()), String.valueOf(p.getDepartment()),
                    String.valueOf(p.getOfficialName()), String.valueOf(p.getOriginalName()),
                    String.valueOf(p.getFirstName()), formatIvzDate(p.getDateOfBirth()));
        }
        createHierarchyString(data, ORGANISATION_NAME);
		createHierarchyString(data, ORGANISATION_ADDITIONAL_NAME);
		createHierarchyString(data, DEPARTMENT);
		createHierarchyString(data, OFFICIAL_NAME);
		createHierarchyString(data, ORIGINAL_NAME);
		createHierarchyString(data, FIRST_NAME);
		data.getDefinition().setAttributeType(DATE_OF_BIRTH, AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
		data.getDefinition().setDataType(DATE_OF_BIRTH, DataType.DATE);
		assertTrue("Person list is empty", persons.size() > 1);
    }
    
    @Test
    public void testAverageReindentificationRisk() throws Exception {
    	data.getDefinition().setAttributeType(DATE_OF_BIRTH, MicroAggregationFunction.createArithmeticMean());
    	config.addPrivacyModel(new AverageReidentificationRisk(0.5d));
        result = anonymizer.anonymize(data, config);
        System.out.println("AverageReindentificationRisk------------------------------");
        printer.printResult(result, data);
    }

    @Test
    public void testKAnonyimity() throws Exception {
    	data.getDefinition().setAttributeType(DATE_OF_BIRTH, MicroAggregationFunction.createArithmeticMean());
    	config.addPrivacyModel(new KAnonymity(2));
        config.setSuppressionLimit(1d);
        config.setQualityModel(Metric.createEntropyMetric());
        result = anonymizer.anonymize(data, config);
        System.out.println("K-Anonyimity----------------------------------------------");
        printer.printResult(result, data);
    }

    @After
    public void updateDb() throws ParseException {
        final int initialPersonCount = persons.size();
        assertTrue("At least two persons are required in the dataset!", initialPersonCount > 1);
        // Print info
        printer.printResult(result, data);
        // Process results
        System.out.println(" - Transformed data:");
        // Load result in StringArray Iterator
        Iterator<String[]> transformed = result.getOutput(false).iterator();
        // First next() call to omit the header
        transformed.next();
        List<String> anonymized;
        PersonArx person;
        while (transformed.hasNext()) {
            // Print results
            String[] transformedStrings = transformed.next();
            System.out.println(Arrays.toString(transformedStrings));
            // Set transformed values to person
            anonymized = Arrays.asList(transformedStrings);
            // Search person on db with ID
            person = personService.readById(Integer.valueOf(anonymized.get(0)));
            // Override the values with the transformed ones
            person.setOrganisationName(checkNull(anonymized.get(1)));
            person.setOrganisationAdditionalName(checkNull(anonymized.get(2)));
            person.setDepartment(checkNull(anonymized.get(3)));
            person.setOfficialName(checkNull(anonymized.get(4)));
            person.setOriginalName(checkNull(anonymized.get(5)));
            person.setFirstName(checkNull(anonymized.get(6)));
            person.setDateOfBirth(parseArxDate(anonymized.get(7)));
            // Update person object on db
            personService.update(person);
            // Commit the update to the db immediately
            personService.flush();
        }
        persons = personService.executeQuery(sqlP);
        assertEquals(initialPersonCount, persons.size());
        assertEquals("nenad", persons.get(1).getFirstName());
        assertEquals("jevdjenic", persons.get(1).getOfficialName());
    }

    private HierarchyBuilderRedactionBased<?> createHierarchy() {
        HierarchyBuilderRedactionBased<?> builderOfficialName = HierarchyBuilderRedactionBased
                .create(Order.RIGHT_TO_LEFT, Order.RIGHT_TO_LEFT, ' ', generateRandomString());
        return builderOfficialName;
    }

    private HierarchyBuilderRedactionBased<?> createHierarchyString(Data data, String attribute) {
		HierarchyBuilderRedactionBased<?> builder = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
				Order.RIGHT_TO_LEFT, ' ', generateRandomString());
		data.getDefinition().setAttributeType(attribute, builder);
		data.getDefinition().setDataType(attribute, DataType.STRING);
		return builder;
	}
    
    private char generateRandomString() {
        Random r = new Random();
        char c = (char) (r.nextInt(26) + 'a');
        return c;
    }

    private String checkNull(String input) {
        if (input.equalsIgnoreCase("null") || input.equalsIgnoreCase("*")) {
            return null;
        } else {
            return input;
        }
    }

    private String formatIvzDate(Date date) {
        if (date == null) {
            return "null";
        } else {
            return arxFormat.format(date);
        }
    }

    private Date parseArxDate(String date) {
        try {
            return arxFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
	
}
