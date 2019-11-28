package com.dummy.myerp.model.bean.comptabilite;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
@NoArgsConstructor
@RequiredArgsConstructor
public @Data class SequenceEcritureComptable {

    // ==================== Attributs ====================
    @NonNull
    private String journalCode;

    /** L'année */
    @NonNull
    private Integer annee;

    /** La dernière valeur utilisée */
    @NonNull
    private Integer derniereValeur;

}
