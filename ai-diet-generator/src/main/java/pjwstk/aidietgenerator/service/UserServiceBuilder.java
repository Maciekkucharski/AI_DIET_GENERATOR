package pjwstk.aidietgenerator.service;

import javax.persistence.EntityManager;

public class UserServiceBuilder {
    private EntityManager entityManager;

    public UserServiceBuilder setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        return this;
    }

    public UserService createUserService() {
        return new UserService(entityManager);
    }
}