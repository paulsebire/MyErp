package com.dummy.myerp.model.bean.comptabilite;


import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
@NoArgsConstructor
@Getter
@Setter
public class SequenceEcritureComptable {

    // ==================== Attributs ====================
    @NotNull
    private JournalComptable journalComptable;

    /** L'année */
    @NotNull
    private Integer annee;

    /** La dernière valeur utilisée */
    @NotNull
    private Integer derniereValeur;

// ==================== Constructeur ====================

    public SequenceEcritureComptable(JournalComptable journalComptable, Integer annee, Integer derniereValeur) {
        this.journalComptable = journalComptable;
        this.annee = annee;
        this.derniereValeur = derniereValeur;
    }


    // ==================== Méthodes STATIC ====================
    /**
     * Renvoie le {@link SequenceEcritureComptable} de code {@code pCode} s'il est présent dans la liste
     *
     * @param pList la liste où chercher le {@link SequenceEcritureComptable}
     * @param pCode le code du {@link SequenceEcritureComptable} à chercher
     * @param  pYear l'année du {@link SequenceEcritureComptable} à chercher
     * @return {@link SequenceEcritureComptable} ou {@code null}
     */
    public static SequenceEcritureComptable getByCodeAndYear(List<? extends SequenceEcritureComptable> pList, String pCode,Integer pYear) {
        SequenceEcritureComptable vRetour = null;
        for (SequenceEcritureComptable vBean : pList) {
            if (isSequenceEcritureComptableExist( vBean,  pCode, pYear )) {
                vRetour = vBean;
                break;
            }
        }
        return vRetour;
    }

    public static boolean isSequenceEcritureComptableExist(SequenceEcritureComptable vBean, String pCode, Integer pYear){
        return (vBean != null && Objects.equals(vBean.getJournalComptable().getCode(), pCode) && Objects.equals(vBean.getAnnee(), pYear));
    }

}
