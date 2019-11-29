package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.dummy.myerp.model.bean.comptabilite.*;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
@NoArgsConstructor
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================
    private SequenceEcritureComptable sequenceEcritureComptable;

    private Integer lastValueSequence;


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }

    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    @Override
    public SequenceEcritureComptable getSequenceEcritureComptable(String pJournalCode, Integer pAnnee) throws NotFoundException {
        return getDaoProxy().getComptabiliteDao().getSequenceEcritureComptable(pJournalCode, pAnnee );
    }

    @Override
    public void insertSequenceEcritureComptable(SequenceEcritureComptable sequenceEcritureComptable) {
        getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable( sequenceEcritureComptable );
    }

    @Override
    public void updateSequenceEcritureComtpable(SequenceEcritureComptable sequenceEcritureComptable) {
        getDaoProxy().getComptabiliteDao().updateSequenceEcritureComtpable( sequenceEcritureComptable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {
        Date date = pEcritureComptable.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String journalCode = pEcritureComptable.getJournal().getCode();
        Integer annee = calendar.get(Calendar.YEAR );


        try {
            sequenceEcritureComptable = getSequenceEcritureComptable( journalCode, annee );
            lastValueSequence =  sequenceEcritureComptable.getDerniereValeur() + 1 ;
            sequenceEcritureComptable.setDerniereValeur( lastValueSequence );

            updateSequenceEcritureComtpable( sequenceEcritureComptable );

        }catch (NotFoundException nf) {
            sequenceEcritureComptable = new SequenceEcritureComptable(journalCode,annee,1);
            insertSequenceEcritureComptable( sequenceEcritureComptable );
        }
        try {
            pEcritureComptable.setReference( setReference( sequenceEcritureComptable ) );
            updateEcritureComptable( pEcritureComptable );
        }catch (FunctionalException fe ){
            fe.printStackTrace();
        }
    }

    /**
     * @param sequenceEcritureComptable
     * @return
     */
    public String setReference(SequenceEcritureComptable sequenceEcritureComptable){
        String reference = sequenceEcritureComptable.getJournalCode() + "-";
        reference += sequenceEcritureComptable.getAnnee()  + "/";
        reference +=String.format("%05d", sequenceEcritureComptable.getDerniereValeur() );

        return  reference;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    // TODO tests à compléter
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {

        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        this.checkConstraintValid (pEcritureComptable ) ;

        // ===== RG_Compta_2
        if (!isEquilibree( pEcritureComptable ) )throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");

        // ===== RG_Compta_3
        this.isNumberValidEcritureComptable( pEcritureComptable);

        // TODO ===== RG_Compta_5 : Format et contenu de la référence
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
    }

    /**
     *
     * @param pEcritureComptable
     * @throws FunctionalException
     */
    public void checkConstraintValid( EcritureComptable pEcritureComptable ) throws FunctionalException {
        Set< ConstraintViolation< EcritureComptable > > vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty() ) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                    new ConstraintViolationException(
                            "L'écriture comptable ne respecte pas les contraintes de validation",
                            vViolations));
        }
    }

    /**
     * RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
     * @param pEcritureComptable
     * @return
     * @throws FunctionalException
     */
    public boolean isEquilibree( EcritureComptable pEcritureComptable)  {
       return   ( pEcritureComptable.isEquilibree() );
    }

    /**
     * RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
     *
     * @param pEcritureComptable
     * @return
     */
    public void isNumberValidEcritureComptable( EcritureComptable pEcritureComptable ) throws FunctionalException{

        if (pEcritureComptable.getListLigneEcriture().size() < 2 ) throw new FunctionalException(
                "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");


        int vNbrCredit = 0;
        int vNbrDebit = 0;

        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture() ) {

            if (isAmountExist( vLigneEcritureComptable.getCredit() ) )
                vNbrCredit++;

            if (isAmountExist( vLigneEcritureComptable.getDebit() ))
                vNbrDebit++;
        }
         if (!( vNbrCredit > 0 && vNbrDebit > 0) ) throw new FunctionalException(
                "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
    }

    /**
     *
     * @param amount
     * @return
     */
    public boolean isAmountExist(BigDecimal amount){
        return ( BigDecimal.ZERO.compareTo( ObjectUtils.defaultIfNull(amount, BigDecimal.ZERO )  ) != 0 );
    }

    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                    pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}
