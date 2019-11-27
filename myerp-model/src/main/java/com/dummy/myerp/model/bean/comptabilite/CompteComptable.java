package com.dummy.myerp.model.bean.comptabilite;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Size;


/**
 * Bean représentant un Compte Comptable
 */
@NoArgsConstructor
@RequiredArgsConstructor
public @Data class CompteComptable {
    // ==================== Attributs ====================
    /** The Numero. */
    @NonNull
    private Integer numero;

    /** The Libelle. */
    @NonNull
    @Size(min = 1, max = 150)
    private String libelle;

    // ==================== Méthodes STATIC ====================
    /**
     * Renvoie le {@link CompteComptable} de numéro {@code pNumero} s'il est présent dans la liste
     *
     * @param pList la liste où chercher le {@link CompteComptable}
     * @param pNumero le numero du {@link CompteComptable} à chercher
     * @return {@link CompteComptable} ou {@code null}
     */
    public static CompteComptable getByNumero(List<? extends CompteComptable> pList, Integer pNumero) {
        CompteComptable vRetour = null;
        for (CompteComptable vBean : pList)
            if (isCompteComptableExist( vBean,  pNumero) ) {
                vRetour = vBean;
                break;
            }

        return vRetour;
    }

    public static boolean isCompteComptableExist(CompteComptable vBean, Integer pNumero){

        return (vBean != null && Objects.equals(vBean.getNumero(), pNumero) );
    }
}
