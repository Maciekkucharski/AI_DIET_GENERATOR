package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class UserServiceBuilder {
    private EntityManager entityManager;

    @Autowired
    public UserServiceBuilder setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        return this;
    }

    public UserService createUserService() {
        return new UserService(entityManager);
    }
}