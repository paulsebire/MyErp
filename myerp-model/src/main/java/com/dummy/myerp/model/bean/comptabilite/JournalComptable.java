package com.dummy.myerp.model.bean.comptabilite;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Size;


/**
 * Bean représentant un Journal Comptable
 */
@NoArgsConstructor
@RequiredArgsConstructor
public @Data class JournalComptable {

    // ==================== Attributs ====================
    /** code */
    @NonNull
    @Size(min = 1, max = 5)
    private String code;

    /** libelle */
    @NonNull
    @Size(min = 1, max = 150)
    private String libelle;

    private final List<SequenceEcritureComptable> listSequenceEcritureComptable  = new ArrayList<>();



    // ==================== Méthodes STATIC ====================
    /**
     * Renvoie le {@link JournalComptable} de code {@code pCode} s'il est présent dans la liste
     *
     * @param pList la liste où chercher le {@link JournalComptable}
     * @param pCode le code du {@link JournalComptable} à chercher
     * @return {@link JournalComptable} ou {@code null}
     */
    public static JournalComptable getByCode(List<? extends JournalComptable> pList, String pCode) {
        JournalComptable vRetour = null;
        for (JournalComptable vBean : pList) {
            if (isJournalComptableExist( vBean,  pCode )) {
                vRetour = vBean;
                break;
            }
        }
        return vRetour;
    }

    public static boolean isJournalComptableExist(JournalComptable vBean, String pCode ){

        return (vBean != null && Objects.equals(vBean.getCode(), pCode));
    }
}
