package com.dummy.myerp.consumer.dao.impl;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import lombok.Getter;
import lombok.Setter;


/**
 * <p>Implémentation du Proxy d'accès à la couche DAO.</p>
 */
@Getter
@Setter
public final class DaoProxyImpl implements DaoProxy {

    // ==================== Attributs ====================
    /** {@link ComptabiliteDao} */
    private ComptabiliteDao comptabiliteDao;


    // ==================== Constructeurs ====================
    /** Instance unique de la classe (design pattern Singleton) */
    private static final DaoProxyImpl INSTANCE = new DaoProxyImpl();

    /**
     * Renvoie l'instance unique de la classe (design pattern Singleton).
     *
     * @return {@link DaoProxyImpl}
     */
    protected static DaoProxyImpl getInstance() {
        return DaoProxyImpl.INSTANCE;
    }

    /**
     * Constructeur.
     */
    private DaoProxyImpl() {
        super();
    }


}
