package com.dummy.myerp.model.bean.comptabilite;


import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Bean représentant un Compte Comptable
 */
@NoArgsConstructor
@Getter
@ToString
public class CompteComptable {
    // ==================== Attributs ====================
    /** The Numero. */
    @NotNull
    private Integer numero;

    /** The Libelle. */
    @NotNull
    @Size(min = 1, max = 150)
    private String libelle;

    @Valid
    private final List<LigneEcritureComptable> ligneEcritureComptableList = new ArrayList<>();

    // ==================== Getter / Setter ====================
    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    // ==================== Constructeur ====================

    public CompteComptable(Integer numero, String libelle) {
        this.numero = numero;
        this.libelle = libelle;
    }


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
