package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.SpringRegistry;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ComptabiliteManagerImplITest {


    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    private EcritureComptable vEcriture ;
    private ComptabiliteDaoImpl dao = ComptabiliteDaoImpl.getInstance();
    private TransactionManager trManager = TransactionManager.getInstance();

    private List<CompteComptable> compteComptableList = new ArrayList<>();
    private List<JournalComptable> journalComptableList= new ArrayList<>();
    private JournalComptable journal;


    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private Date dateAddReference;
    private Date dateRG6;

    private LigneEcritureComptable createLigne(Integer pLigneId, String pLigneEcritureLibelle, Integer pCompteComptableNumero,String pCompteComptableLibelle, String pDebit, String pCredit) {

        CompteComptable pCompteComptable =  ObjectUtils.defaultIfNull(
                CompteComptable.getByNumero( compteComptableList, pCompteComptableNumero ),
                new CompteComptable( pCompteComptableNumero,pCompteComptableLibelle ) );

        BigDecimal vDebit = pDebit == null ?  null : new BigDecimal( pDebit );
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal( pCredit );


        LigneEcritureComptable vRetour = new LigneEcritureComptable(pLigneId, pCompteComptable, pLigneEcritureLibelle,vDebit,vCredit );
        return vRetour;
    }

    @Before
    public void initAll() throws ParseException {
        SpringRegistry.init();


        compteComptableList = dao.getListCompteComptable();
        journalComptableList = dao.getListJournalComptable();

        dateAddReference = formatter.parse("1900-01-01");
        dateRG6 = formatter.parse("2016-01-01");
        vEcriture = manager.getEcritureComptable();
        journal = ObjectUtils.defaultIfNull(
                JournalComptable.getByCode( journalComptableList, "AC" ),
                new JournalComptable( "AC","Achat" ) );

    }

    @Test
    public void addReference() {

        calendar.setTime( dateAddReference );

        vEcriture.setJournal( journal );
        vEcriture.setDate( dateAddReference );
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2",null, "10"));

        manager.addReference();
        Assert.assertEquals(vEcriture.getReference() ,manager.setReference(dao.getSequenceEcritureComptable("AC",calendar.get(Calendar.YEAR) ) ) );


        manager.addReference( );
        SequenceEcritureComptable pSeq = dao.getSequenceEcritureComptable("AC",calendar.get(Calendar.YEAR) );

        Assert.assertEquals( vEcriture.getReference(), manager.setReference(pSeq) );
        Assert.assertEquals("AC-1900/00002", manager.setReference(pSeq) );

        dao.deleteSequenceEcritureComptable( pSeq );
        Assert.assertNull(  dao.getSequenceEcritureComptable("AC",calendar.get(Calendar.YEAR) ) );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableContextRG6_New() throws FunctionalException {

        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal(journal );
        vEcriture.setReference("AC-2016/00011");
        vEcriture.setDate( dateRG6 );
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2",null, "10"));

        manager.checkEcritureComptable( vEcriture );

        vEcriture.setReference("AC-2016/00001");
        manager.checkEcritureComptable( vEcriture );


    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableContextRG6_Exist() throws FunctionalException {

        EcritureComptable vEcriture = dao.getEcritureComptable(-2);

        manager.checkEcritureComptable( vEcriture );
        vEcriture.setReference("VE-2016/00004");
        manager.checkEcritureComptable( vEcriture );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableContextRG6_isNoneEmpty() throws FunctionalException {

        EcritureComptable vEcriture = dao.getEcritureComptable(-2);
        vEcriture.setReference( null );
        manager.checkEcritureComptableContext( vEcriture );
        vEcriture.setReference("VE-2016/00004");
        manager.checkEcritureComptable( vEcriture );
    }


    @Test(expected = FunctionalException.class )
    public  void insertEcritureComptable() throws FunctionalException, NotFoundException {
        Date date = new Date();
        vEcriture.setJournal( new JournalComptable("AC","Achat") );
        vEcriture.setDate( date );
        vEcriture.setLibelle("Insertion nouvelle écriture");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2",null, "10"));

        manager.addReference();

        manager.insertEcritureComptable( vEcriture );

        EcritureComptable vECRef = dao.getEcritureComptableByRef(vEcriture.getReference());

        manager.deleteEcritureComptable( vECRef.getId() );

        SequenceEcritureComptable pSeq = dao.getSequenceEcritureComptable("AC",calendar.get(Calendar.YEAR) );
        dao.deleteSequenceEcritureComptable( pSeq );

        vEcriture.setReference("VE-2016/00004");

        manager.insertEcritureComptable( vEcriture );

    }

    @Test(expected = FunctionalException.class )
    public void updateEcritureComptable() throws FunctionalException, NotFoundException {

        vEcriture.setJournal( journal );
        vEcriture.setDate( new Date() );
        vEcriture.setLibelle("Insertion nouvelle écriture");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2",null, "10"));

        manager.addReference();

        manager.insertEcritureComptable( vEcriture );

        EcritureComptable vECRef = dao.getEcritureComptableByRef(vEcriture.getReference());

        vEcriture.setLibelle("Insertion nouvelle écriture");

        manager.updateEcritureComptable( vEcriture );

        manager.deleteEcritureComptable( vECRef.getId() );
        SequenceEcritureComptable pSeq = dao.getSequenceEcritureComptable("AC",calendar.get(Calendar.YEAR) );
        dao.deleteSequenceEcritureComptable( pSeq );

        vEcriture.setReference("VE-2016/00004");
        manager.updateEcritureComptable( vEcriture );

    }

    @Test
    public void deleteEcritureComptable() throws FunctionalException, NotFoundException {
        Date date = new Date();
        JournalComptable journalComptable = new JournalComptable("AC","Achat");
        CompteComptable compteComptable = new CompteComptable(411,"Clients");
        vEcriture.setJournal( journalComptable );
        vEcriture.setDate( date );
        vEcriture.setLibelle("Insertion nouvelle écriture");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2",null, "10"));

        manager.addReference();

        manager.insertEcritureComptable( vEcriture );

        EcritureComptable vECRef = dao.getEcritureComptableByRef(vEcriture.getReference());

        List<EcritureComptable> listEcritureComptable = manager.getListEcritureComptable();
        Assert.assertNotNull( EcritureComptable.getById( listEcritureComptable,vECRef.getId() ) );

        List<CompteComptable> listCompteComptable = manager.getListCompteComptable();
        Assert.assertNotNull(CompteComptable.getByNumero(listCompteComptable,compteComptable.getNumero() ) );

        boolean isCodeJournal = false;
        List<JournalComptable> listJournalComptable = manager.getListJournalComptable();
        for (JournalComptable jComptable : listJournalComptable )
            if( jComptable.getCode().equals( journalComptable.getCode() ) ) {
                isCodeJournal = true;
                break;
            }

        Assert.assertTrue( isCodeJournal );

        manager.deleteEcritureComptable( vECRef.getId() );

        listEcritureComptable = manager.getListEcritureComptable();
        Assert.assertNull( EcritureComptable.getById( listEcritureComptable,vECRef.getId() ) );

        listCompteComptable = manager.getListCompteComptable();
        Assert.assertNotNull(CompteComptable.getByNumero(listCompteComptable,compteComptable.getNumero() ) );

        isCodeJournal = false;
        listJournalComptable = manager.getListJournalComptable();
        for (JournalComptable jComptable : listJournalComptable )
            if( jComptable.getCode().equals( journalComptable.getCode() ) ) {
                isCodeJournal = true;
                break;
            }

        Assert.assertTrue( isCodeJournal );

        SequenceEcritureComptable pSeq = dao.getSequenceEcritureComptable("AC",calendar.get(Calendar.YEAR) );
        dao.deleteSequenceEcritureComptable( pSeq );
    }

    @Test
    public void transactionManagerStatus(){
        TransactionStatus vTS = null;
        try {
            trManager.commitMyERP(vTS);
            Assert.assertNull( vTS );
        } finally {
            vTS = trManager.beginTransactionMyERP();
            Assert.assertNotNull( vTS );
            trManager.rollbackMyERP(vTS);
        }

        vTS = trManager.beginTransactionMyERP();
        try {
            trManager.commitMyERP(vTS);
            Assert.assertNotNull( vTS );
            vTS = null;
        } finally {
            Assert.assertNull( vTS );
            trManager.rollbackMyERP(vTS);
        }

    }

}