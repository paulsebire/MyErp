package com.dummy.myerp.business;


import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import lombok.Data;
import lombok.Getter;


/**
 * Classe mère des classes de test d'intégration de la couche Business
 */

public abstract class BusinessTestCase {

    static {
        SpringRegistry.init();
    }

    private static final DaoProxy DAO_PROXY = SpringRegistry.getDaoProxy();

    /** {@link BusinessProxy} */
    private static final BusinessProxy BUSINESS_PROXY = SpringRegistry.getBusinessProxy();
    /** {@link TransactionManager} */
    private static final TransactionManager TRANSACTION_MANAGER = SpringRegistry.getTransactionManager();

    // ==================== Constructeurs ====================
    /**
     * Constructeur.
     */
    public BusinessTestCase() {}

    // ==================== Getters/Setters ====================
    public static BusinessProxy getBusinessProxy() { return BUSINESS_PROXY; }

    public static TransactionManager getTransactionManager() {
        return TRANSACTION_MANAGER;
    }

    public static DaoProxy getDaoProxy() { return DAO_PROXY; }
}
