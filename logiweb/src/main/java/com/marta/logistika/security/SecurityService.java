package com.marta.logistika.security;

public interface SecurityService {

    /**
     * If user with given username does not exist, creates a new user with the ROLE_DRIVER authority.
     * If user already exists, checks his authorities and adds the ROLE_DRIVER authority if absent.
     *
     * @param username user name
     */
    void ensureUserWithDriverRole(String username);

    /**
     * Removers ROLE_DRIVER from the user. User is not deleted.
     *
     * @param username user name
     */
    void removeDriverRoleFromUser(String username);
}
