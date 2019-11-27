package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.dummy.myerp.model.validation.constraint.MontantComptable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Bean représentant une Ligne d'écriture comptable.
 */
@NoArgsConstructor
@RequiredArgsConstructor
public @Data class LigneEcritureComptable {

    // ==================== Attributs ====================
    /** Compte Comptable */
    @NonNull
    private CompteComptable compteComptable;

    /** The Libelle. */
    @Size(max = 200)
    @NonNull
    private String libelle;

    /** The Debit. */
    @MontantComptable
    @NonNull
    private BigDecimal debit;

    /** The Credit. */
    @MontantComptable
    @NonNull
    private BigDecimal credit;
}
