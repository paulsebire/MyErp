package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.dummy.myerp.model.validation.constraint.MontantComptable;
import lombok.*;


/**
 * Bean représentant une Ligne d'écriture comptable.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public  class LigneEcritureComptable {

    // ==================== Attributs ====================

    private Integer id;

    private EcritureComptable ecritureComptable;

    /** Compte Comptable */
    @NotNull
    private CompteComptable compteComptable;

    /** The Libelle. */
    @Size(max = 200)
    private String libelle;

    /** The Debit. */
    @MontantComptable
    private BigDecimal debit;

    /** The Credit. */
    @MontantComptable
    private BigDecimal credit;

// ==================== Constructeur ====================
    /**
     * Instantiates a new Ligne ecriture comptable.
     *
     * @param pCompteComptable the Compte Comptable
     * @param pLibelle the libelle
     * @param pDebit the debit
     * @param pCredit the credit
     */
    public LigneEcritureComptable(Integer pId, CompteComptable pCompteComptable, String pLibelle,
                                  BigDecimal pDebit, BigDecimal pCredit) {
        id = pId;
        compteComptable = pCompteComptable;
        libelle = pLibelle;
        debit = pDebit;
        credit = pCredit;
    }



    // ==================== Méthodes STATIC ====================
    /**
     * Renvoie le {@link EcritureComptable} de code {@code pCode} s'il est présent dans la liste
     *
     * @param pList la liste où chercher le {@link LigneEcritureComptable}
     * @param pId le id de l'écriture du {@link LigneEcritureComptable} à chercher
     * @return {@link LigneEcritureComptable} ou {@code null}
     */
    public static LigneEcritureComptable getById(List<? extends LigneEcritureComptable> pList, Integer pId) {
        LigneEcritureComptable vRetour = null;
        for (LigneEcritureComptable vBean : pList) {
            if (isLigneEcritureComptableExist( vBean,  pId )) {
                vRetour = vBean;
                break;
            }
        }
        return vRetour;
    }

    public static boolean isLigneEcritureComptableExist(LigneEcritureComptable vBean, Integer pId ){

        return (vBean != null && Objects.equals(vBean.getEcritureComptable().getId(), pId) );
    }




}
