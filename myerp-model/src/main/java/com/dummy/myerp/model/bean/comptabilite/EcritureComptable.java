package com.dummy.myerp.model.bean.comptabilite;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;

/**
 * Bean représentant une Écriture Comptable
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EcritureComptable {

    // ==================== Attributs ====================
    /** The Id. */
    private Integer id;

    /** Journal comptable */
    @NotNull
    private JournalComptable journal;

    /** The Reference. */
   // @Pattern(regexp = "\\d{1,5}-\\d{4}/\\d{5}")
    @Pattern(regexp = "[A-Z]{2}-[0-9]{4}/[0-9]{5}")
    private String reference;

    /** The Date. */
    @NotNull
    private Date date;

    /** The Libelle. */
    @NotNull
    @Size(min = 1, max = 200)
    private String libelle;

    /** La liste des lignes d'écriture comptable. */
    @Valid
    @Size(min = 2)
    private final List<LigneEcritureComptable> listLigneEcriture = new ArrayList<>();


    /**
     * Calcul et renvoie le total des montants au débit des lignes d'écriture
     *
     * @return {@link BigDecimal}, {@link BigDecimal#ZERO} si aucun montant au débit
     */
    public BigDecimal getTotalDebit() {
        BigDecimal vRetour = BigDecimal.ZERO;
        for (LigneEcritureComptable vLigneEcritureComptable : listLigneEcriture) {
            if (isAmountNotNull( vLigneEcritureComptable.getDebit() ) ) {
                vRetour = vRetour.add(vLigneEcritureComptable.getDebit());
            }
        }
        return vRetour.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcul et renvoie le total des montants au crédit des lignes d'écriture
     *
     * @return {@link BigDecimal}, {@link BigDecimal#ZERO} si aucun montant au crédit
     */
    public BigDecimal getTotalCredit() {
        BigDecimal vRetour = BigDecimal.ZERO;
        for (LigneEcritureComptable vLigneEcritureComptable : listLigneEcriture) {
            if ( isAmountNotNull( vLigneEcritureComptable.getCredit() ) ) {
                vRetour = vRetour.add(vLigneEcritureComptable.getCredit());
            }
        }
        return vRetour.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
     * Renvoie si l'écriture est équilibrée (TotalDebit = TotalCrédit)
     * @return boolean
     */
    public boolean isEquilibree() {
        return this.getTotalDebit().equals( this.getTotalCredit() );
    }

    public boolean isAmountNotNull(BigDecimal amount){
        return  (amount != null);
    }

    // ==================== Méthodes STATIC ====================
    /**
     * Renvoie le {@link EcritureComptable} de code {@code pCode} s'il est présent dans la liste
     *
     * @param pList la liste où chercher le {@link EcritureComptable}
     * @param pId le id de l'écriture du {@link EcritureComptable} à chercher
     * @return {@link EcritureComptable} ou {@code null}
     */
    public static EcritureComptable getById(List<? extends EcritureComptable> pList, Integer pId) {
        EcritureComptable vRetour = null;
        for (EcritureComptable vBean : pList) {
            if (isEcritureComptableExist( vBean,  pId )) {
                vRetour = vBean;
                break;
            }
        }
        return vRetour;
    }

    public static boolean isEcritureComptableExist(EcritureComptable vBean, Integer pId ){

        return (vBean != null && Objects.equals(vBean.getId(), pId));
    }
}
