package com.dummy.myerp.business;

import com.dummy.myerp.tests.business.BusinessTestCase;
import com.dummy.myerp.tests.business.SpringRegistry;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * Classe de test de l'initialisation du contexte Spring
 */
public class InitSpringTest extends BusinessTestCase {

    /**
     * Constructeur.
     */
    public InitSpringTest() {
        super();
    }


    /**
     * Teste l'initialisation du contexte Spring
     */
    @Test
    public void testInit() {

        SpringRegistry.init();
        assertNotNull(SpringRegistry.getBusinessProxy());
        assertNotNull(SpringRegistry.getTransactionManager());


    }
}
