package com.dummy.myerp.business;


import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import lombok.Data;


/**
 * Classe mère des classes de test d'intégration de la couche Business
 */
public abstract @Data class BusinessTestCase {

    static {
        SpringRegistry.init();
    }

    /** {@link BusinessProxy} */
    private static final BusinessProxy BUSINESS_PROXY = SpringRegistry.getBusinessProxy();
    /** {@link TransactionManager} */
    private static final TransactionManager TRANSACTION_MANAGER = SpringRegistry.getTransactionManager();

}
