package com.dummy.myerp.consumer.dao.impl.cache;

import java.util.List;

import com.dummy.myerp.consumer.ConsumerHelper;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import lombok.NoArgsConstructor;


/**
 * Cache DAO de {@link CompteComptable}
 */
@NoArgsConstructor
public class CompteComptableDaoCache {

    // ==================== Attributs ====================
    /** The List compte comptable. */
    private List<CompteComptable> listCompteComptable;


    // ==================== Méthodes ====================
    /**
     * Gets by numero.
     *
     * @param pNumero the numero
     * @return {@link CompteComptable} ou {@code null}
     */
    public CompteComptable getByNumero(Integer pNumero) {
        if (listCompteComptable == null)
            listCompteComptable = ConsumerHelper.getDaoProxy().getComptabiliteDao().getListCompteComptable();

        return CompteComptable.getByNumero(listCompteComptable, pNumero);

    }
}
