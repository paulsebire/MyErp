package com.dummy.myerp.consumer.dao.impl.cache;

import java.util.List;

import com.dummy.myerp.consumer.ConsumerHelper;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import lombok.NoArgsConstructor;


/**
 * Cache DAO de {@link JournalComptable}
 */
@NoArgsConstructor
public class JournalComptableDaoCache {

    // ==================== Attributs ====================
    /** The List compte comptable. */
    private List<JournalComptable> listJournalComptable;

    // ==================== Méthodes ====================
    /**
     * Gets by code.
     *
     * @param pCode le code du {@link JournalComptable}
     * @return {@link JournalComptable} ou {@code null}
     */
    public JournalComptable getByCode(String pCode) {
        if (listJournalComptable == null)
            listJournalComptable = ConsumerHelper.getDaoProxy().getComptabiliteDao().getListJournalComptable();

        return JournalComptable.getByCode(listJournalComptable, pCode);
    }
}
