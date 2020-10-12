package com.github.arxintegration.sample.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

/**
 * 
 * <p>
 * Title: Persons
 * </p>
 * 
 * <p>
 * Description: Domain Object describing a Persons entity
 * </p>
 * 
 */
@Entity
@Table(name = "PERSON_ARX")
public class PersonArx {
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "ORGANISATION_NAME", length = 50)
    private String organisationName;

    @Column(name = "ORGANISATION_ADDITIONAL_NAME", length = 50)
    private String organisationAdditionalName;
    
    @Column(name = "DEPARTMENT", length = 50)
    private String department;
    
    @Column(name = "OFFICIAL_NAME", length = 30)
    private String officialName;

    @Column(name = "ORIGINAL_NAME", length = 30)
    private String originalName;
    
    @Column(name = "FIRST_NAME", length = 30)
    private String firstName;
    
    @Column(name = "DATE_OF_BIRTH")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    @Column(name = "PLACE_OF_ORIGIN_NAME", length = 30)
    private String placeOfOriginName;
    
    @Column(name = "SECOND_PLACE_OF_ORIGIN_NAME", length = 30)
    private String secondPlaceOfOriginName;

    @Column(name = "PLACE_OF_BIRTH_COUNTRY", length = 30)
    private String placeOfBirthCountry;

    @Column(name = "SEX", length = 10)
    @Enumerated(EnumType.STRING)
    private SexEnum sex;
    
    @Column(name = "LANGUAGE", length = 1)
    private String language;
    
    @Column(name = "NATIONALITY", length = 4)
    private String nationality;
    
    @Column(name = "COUNTRY_OF_ORIGIN", length = 4)
    private String countryOfOrigin;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OF_DEATH")
    private Date dateOfDeath;

    @Column(name = "REMARK", length = 50)
    private String remark;
    
    @Column(name = "LAST_MEDICAL_CHECKUP")
    @Temporal(TemporalType.DATE)
    private Date lastMedicalCheckup;

    @Column(name = "NEXT_MEDICAL_CHECKUP")
    @Temporal(TemporalType.DATE)
    private Date nextMedicalCheckup;

    @Column(name = "PHONE_NUMBER", length = 15)
    private String phoneNumber;

    @Column(name = "CELL_NUMBER", length = 15)
    private String cellNumber;

    @Column(name = "EMAIL", length = 250)
    private String email;
    
    @Column(name = "GUARDIANSHIP", length = 1)
    @Type(type = "true_false")
    private Boolean guardianship;
    
    @Column(name = "CURRENT_ZIP_CODE", precision = 7, scale = 0)
    private Integer currentZipCode;
    
    @Column(name = "CURRENT_TOWN", length = 30)
    private String currentTown;

    @Column(name = "MANDATOR", length = 2)
    private String mandator;
    
    /**
     * Default constructor.
     */
    public PersonArx() {
    }

    public Date getDateOfBirth() {
        return prepareDate(dateOfBirth);
    }

    public Date getDateOfDeath() {
        return prepareDate(dateOfDeath);
    }

    public String getDepartment() {
        return department;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLanguage() {
        return language;
    }

    public Date getLastMedicalCheckup() {
        return prepareDate(lastMedicalCheckup);
    }

    public String getNationality() {
        return nationality;
    }

    public Date getNextMedicalCheckup() {
        return prepareDate(nextMedicalCheckup);
    }

    public String getOfficialName() {
        return officialName;
    }

    public String getOrganisationAdditionalName() {
        return organisationAdditionalName;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public String getPlaceOfBirthCountry() {
        return placeOfBirthCountry;
    }

    public String getPlaceOfOriginName() {
        return placeOfOriginName;
    }

    public String getRemark() {
        return remark;
    }

    public String getSecondPlaceOfOriginName() {
        return secondPlaceOfOriginName;
    }

    public SexEnum getSex() {
        return sex;
    }

    public Long getId() {
		return id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getCellNumber() {
		return cellNumber;
	}

	public String getEmail() {
		return email;
	}

	public Integer getCurrentZipCode() {
		return currentZipCode;
	}

	public String getCurrentTown() {
		return currentTown;
	}

	public String getMandator() {
		return mandator;
	}

	public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = prepareDate(dateOfBirth);
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = prepareDate(dateOfDeath);
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLastMedicalCheckup(Date lastMedicalCheckup) {
        this.lastMedicalCheckup = prepareDate(lastMedicalCheckup);
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setNextMedicalCheckup(Date nextMedicalCheckup) {
        this.nextMedicalCheckup = prepareDate(nextMedicalCheckup);
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public void setOrganisationAdditionalName(String organisationAdditionalName) {
        this.organisationAdditionalName = organisationAdditionalName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setPlaceOfBirthCountry(String placeOfBirthCountry) {
        this.placeOfBirthCountry = placeOfBirthCountry;
    }

    public void setPlaceOfOriginName(String placeOfOriginName) {
        this.placeOfOriginName = placeOfOriginName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSecondPlaceOfOriginName(String secondPlaceOfOriginName) {
        this.secondPlaceOfOriginName = secondPlaceOfOriginName;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public Boolean getGuardianship() {
        return guardianship;
    }

    public void setGuardianship(Boolean guardianship) {
        this.guardianship = guardianship;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setCellNumber(String cellNumber) {
		this.cellNumber = cellNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCurrentZipCode(Integer currentZipCode) {
		this.currentZipCode = currentZipCode;
	}

	public void setCurrentTown(String currentTown) {
		this.currentTown = currentTown;
	}

	public void setMandator(String mandator) {
		this.mandator = mandator;
	}
	
    public static Date prepareDate(Date date) {
        return date == null ? null : (Date) date.clone();
    }
}
