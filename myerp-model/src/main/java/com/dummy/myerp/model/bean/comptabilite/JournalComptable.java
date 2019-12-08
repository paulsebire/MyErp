package com.dummy.myerp.model.bean.comptabilite;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Bean représentant un Journal Comptable
 */
@NoArgsConstructor
@Getter
@Setter
public class JournalComptable {

    // ==================== Attributs ====================
    /** code */
    @NotNull
    @Size(min = 1, max = 5)
    private String code;

    /** libelle */
    @NotNull
    @Size(min = 1, max = 150)
    private String libelle;

    @Valid
    private final List<SequenceEcritureComptable> listSequenceEcriture  = new ArrayList<>();


    // ==================== Constructeur ====================
    public JournalComptable(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
    }


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

        return (vBean != null && Objects.equals(vBean.getCode(), pCode) );
    }
}
