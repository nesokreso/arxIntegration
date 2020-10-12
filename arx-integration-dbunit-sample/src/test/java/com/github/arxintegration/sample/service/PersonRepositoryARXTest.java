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
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import ch.admin.astra.ivz.arx.Printer;
import ch.admin.astra.ivz.domain.PersonPrivateRepository;
import ch.admin.astra.ivz.domain.model.Person;
import ch.admin.astra.ivz.spring.testutils.db.ReadOnlyTest;
import ch.admin.astra.ivz.spring.testutils.db.TestDataSet;

/**
 * Test cases for {@link PersonRepositoryJPA} and ARX library
 * 
 * @author nej
 */
@ContextConfiguration(locations = { "PersonRepositoryArxJPA-context.xml" })
public class PersonRepositoryARXTest {
    /**
     * Definition of headers for input data
     */
    private static final String DEPARTMENT = "department";
    private static final String DATE_OF_DEATH = "dateOfDeath";
    private static final String DATE_OF_BIRTH = "dateOfBirth";
    private static final String SEX = "sex";
    private static final String ORIGINAL_NAME = "originalName";
    private static final String OFFICIAL_NAME = "officialName";
    private static final String FIRST_NAME = "firstName";
    private static final String MOFIS_PIN = "mofisPin";
    private static final String PIN = "pin";
    private static final String ID = "id";
    private static final String LANGUAGE = "language";
    private static final String LAST_MEDICAL_CHECKUP = "lastMedicalCheckup";
    private static final String NEXT_MEDICAL_CHECKUP = "nextMedicalCheckup";
    private static final String LEGAL_PERSON = "legalPerson";
    private static final String NATIONALITY = "nationality";
    private static final String ORGANISATION_NAME = "organisationName";
    private static final String ORGANISATION_ADDITIONAL_NAME = "organisationAdditionalName";
    private static final String ORIGIN = "origin";
    private static final String COUNTRY_OF_ORIGIN = "countryOfOrigin";
    private static final String PLACE_OF_BIRTH_COUNTRY = "placeOfBirthCountry";
    private static final String PLACE_OF_ORIGIN_CODE = "placeOfOriginCode";
    private static final String PLACE_OF_ORIGIN_NAME = "placeOfOriginName";
    private static final String REMARK = "remark";
    
    @Autowired
    private PersonPrivateRepository repository;

    private final String sqlP = "Select p from ch.admin.astra.ivz.domain.model.Person p";
    private Printer printer;
    private final DefaultData data = Data.create();
    private List<Person> persons;
    private final SimpleDateFormat arxFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final ARXAnonymizer anonymizer = new ARXAnonymizer();
    private final ARXConfiguration config = ARXConfiguration.create();
    ARXResult result;

    @PersistenceContext(unitName = "data-services")
    private EntityManager entityManager;

    public PersonRepositoryARXTest() {
        printer = new Printer();
    }

    @Before
    public void setup() throws Exception {
        persons = entityManager.createQuery(sqlP).getResultList();
        data.add(ID, ORGANISATION_NAME, ORGANISATION_ADDITIONAL_NAME, DEPARTMENT, OFFICIAL_NAME, ORIGINAL_NAME,
                FIRST_NAME, DATE_OF_BIRTH);

        for (Person p : persons) {
            data.add(String.valueOf(p.getId()), String.valueOf(p.getOrganisationName()),
                    String.valueOf(p.getOrganisationAdditionalName()), String.valueOf(p.getDepartment()),
                    String.valueOf(p.getOfficialName()), String.valueOf(p.getOriginalName()),
                    String.valueOf(p.getFirstName()), formatIvzDate(p.getDateOfBirth()));
        }
        data.getDefinition().setAttributeType(ID, AttributeType.INSENSITIVE_ATTRIBUTE);
        data.getDefinition().setDataType(ID, DataType.INTEGER);

        HierarchyBuilderRedactionBased<?> builderOrganisationName = createHierarchy();
        data.getDefinition().setAttributeType(ORGANISATION_NAME, builderOrganisationName);
        data.getDefinition().setDataType(ORGANISATION_NAME, DataType.STRING);

        HierarchyBuilderRedactionBased<?> builderOrganisationAdditionalName = createHierarchy();
        data.getDefinition().setAttributeType(ORGANISATION_ADDITIONAL_NAME, builderOrganisationAdditionalName);
        data.getDefinition().setDataType(ORGANISATION_ADDITIONAL_NAME, DataType.STRING);

        HierarchyBuilderRedactionBased<?> builderDepartment = createHierarchy();
        data.getDefinition().setAttributeType(DEPARTMENT, builderDepartment);
        data.getDefinition().setDataType(DEPARTMENT, DataType.STRING);

        HierarchyBuilderRedactionBased<?> builderFirstName = createHierarchy();
        data.getDefinition().setAttributeType(FIRST_NAME, builderFirstName);
        data.getDefinition().setDataType(FIRST_NAME, DataType.STRING);

        HierarchyBuilderRedactionBased<?> builderOfficialName = createHierarchy();
        data.getDefinition().setAttributeType(OFFICIAL_NAME, builderOfficialName);
        data.getDefinition().setDataType(OFFICIAL_NAME, DataType.STRING);

        HierarchyBuilderRedactionBased<?> builderOriginallName = createHierarchy();
        data.getDefinition().setAttributeType(ORIGINAL_NAME, builderOriginallName);
        data.getDefinition().setDataType(ORIGINAL_NAME, DataType.STRING);


        // DefaultHierarchy sex = Hierarchy.create();
        // sex.add("MALE", "FEMALE");
        // sex.add("FEMALE", "MALE");
        // sex.add("null", "MALE");
        // data.getDefinition().setAttributeType(SEX, sex);
        // data.getDefinition().setDataType(SEX, DataType.STRING);
        // data.getDefinition().setHierarchy("gender", sex);

        data.getDefinition().setAttributeType(DATE_OF_BIRTH, AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
        data.getDefinition().setDataType(DATE_OF_BIRTH, DataType.DATE);
        data.getDefinition().setAttributeType(DATE_OF_BIRTH, MicroAggregationFunction.createArithmeticMean());

        // DefaultHierarchy lang = Hierarchy.create();
        // lang.add("d", "f");
        // lang.add("i", "d");
        // lang.add("f", "e");
        // lang.add("e", "i");
        // lang.add("null", "d");
        // data.getDefinition().setAttributeType(LANGUAGE, lang);

        // HierarchyBuilderRedactionBased<?> builderLanguage =
        // createHierarchy();
        // data.getDefinition().setAttributeType(LANGUAGE, builderLanguage);

        // data.getDefinition().setDataType(LANGUAGE, DataType.STRING);

        // With the DefaultHierarchy you must set all possible values manually,
        // so it's only preferable for limited possibilities

        // DefaultHierarchy nation = Hierarchy.create();
        // nation.add("CH", "GB");
        // nation.add("GB", "IT");
        // nation.add("SRB", "FR");
        // nation.add("DE", "USA");
        // nation.add("USA", "CAD");
        // nation.add("FR", "MEX");
        // nation.add("IT", "CH");
        // nation.add("null", "SRB");
        //


        // Perform risk analysis
        System.out.println("\n - Input data");
        printer.print(data.getHandle());

        System.out.println("\n - Quasi-identifiers with values (in percent):");
        // printer.analyzeAttributes(data.getHandle());

    }

    @Test
    @ReadOnlyTest
    public void testAverageReindentificationRisk() throws IOException {
        config.addPrivacyModel(new AverageReidentificationRisk(0.5d));
        result = anonymizer.anonymize(data, config);
        System.out.println("AverageReindentificationRisk------------------------------");
    }

    @Test
    @ReadOnlyTest
    public void testKAnonyimity() throws IOException {
        config.addPrivacyModel(new KAnonymity(4));
        config.setSuppressionLimit(0d);
        config.setQualityModel(Metric.createEntropyMetric());
        result = anonymizer.anonymize(data, config);
        System.out.println("K-Anonyimity----------------------------------------------");
    }

    // @Test
    @ReadOnlyTest
    public void testDifferentialPrivacy() throws IOException {
        // Create a differential privacy criterion
        EDDifferentialPrivacy criterion = new EDDifferentialPrivacy(2d, 0.00001d,
                DataGeneralizationScheme.create(data, GeneralizationDegree.MEDIUM));

        ARXConfiguration config = ARXConfiguration.create();
        config.addPrivacyModel(criterion);
        config.setSuppressionLimit(1d);
        result = anonymizer.anonymize(data, config);
        System.out.println("DifferentialPrivacy----------------------------------------------");
    }


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
        Person person;
        while (transformed.hasNext()) {
            // Print results
            String[] transformedStrings = transformed.next();
            System.out.println(Arrays.toString(transformedStrings));
            // Set transformed values to person
            anonymized = Arrays.asList(transformedStrings);
            // Search person on db with ID
            person = repository.readById(Long.valueOf(anonymized.get(0)));
            // Override the values with the transformed ones
            person.setOrganisationName(checkNull(anonymized.get(1)));
            person.setOrganisationAdditionalName(checkNull(anonymized.get(2)));
            person.setDepartment(checkNull(anonymized.get(3)));
            person.setOfficialName(checkNull(anonymized.get(4)));
            person.setOriginalName(checkNull(anonymized.get(5)));
            person.setFirstName(checkNull(anonymized.get(6)));
            person.setDateOfBirth(parseArxDate(anonymized.get(7)));
            // Update person object on db
            repository.update(person);
            // Commit the update to the db immediately
            entityManager.flush();
        }
        persons = entityManager.createQuery(sqlP).getResultList();
        assertEquals(initialPersonCount, persons.size());
        assertEquals("nenad", persons.get(1).getFirstName());
        assertEquals("jevdjenic", persons.get(1).getOfficialName());
    }

    private HierarchyBuilderRedactionBased<?> createHierarchy() {
        HierarchyBuilderRedactionBased<?> builderOfficialName = HierarchyBuilderRedactionBased
                .create(Order.RIGHT_TO_LEFT, Order.RIGHT_TO_LEFT, ' ', generateRandomString());
        return builderOfficialName;
    }

    private char generateRandomString() {
        Random r = new Random();
        char c = (char) (r.nextInt(26) + 'a');
        return c;
    }

    private String checkNull(String input) {
        if (input.equalsIgnoreCase("null")) {
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
