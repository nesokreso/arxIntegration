package com.github.arxintegration.sample.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.arxintegration.sample.entity.PersonArx;

@Service
@Transactional
public class PersonService {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<PersonArx> find(String name) {
		Query query = this.entityManager.createNamedQuery("PersonArx.find");
		query.setParameter("official_name", "%" + name + "%");
		return query.getResultList();
	}

	public void remove(int personId) {
		PersonArx person = this.entityManager.find(PersonArx.class, personId);
		this.entityManager.remove(person);
	}

	public PersonArx create(PersonArx entity) {
		entityManager.persist(entity);
		return entity;
	}
	
	public void delete(PersonArx entity) {
        if (entity != null) {
            entityManager.remove(entity);
            entityManager.flush();
        }
    }
	
	public PersonArx update(PersonArx entity) throws OptimisticLockException {
        final PersonArx merged;
        if (entityManager.contains(entity)) {
            merged = entity;
        } else {
            merged = entityManager.merge(entity);
        }
        return merged;
    }

    public void deleteById(Long id) {
        PersonArx entity = readById(id);
        delete(entity);
    }

    public PersonArx readById(Long id) {
        return entityManager.find(PersonArx.class, id);
    }
}
