package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.SpringRegistry;
import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComptabiliteManagerImplITest {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    private EcritureComptable vEcriture ;
    private ComptabiliteDaoImpl dao = ComptabiliteDaoImpl.getInstance();
    private TransactionManager trManager = TransactionManager.getInstance();

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private Date dateAddReference;
    private Date dateRG6;



    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero,String vLibelle, String pComteComptableLibelle, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ?  BigDecimal.ZERO : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? BigDecimal.ZERO : new BigDecimal(pCredit);
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero,pComteComptableLibelle),
                vLibelle,
                vDebit, vCredit);
        return vRetour;
    }


    @Before
    public void initAll() throws ParseException {
        SpringRegistry.init();
        ComptabiliteManagerImpl.setTransactionManager(SpringRegistry.getTransactionManager());
        ComptabiliteManagerImpl.setDaoProxy(SpringRegistry.getDaoProxy());
        ComptabiliteManagerImpl.setBusinessProxy(SpringRegistry.getBusinessProxy());

        dateAddReference = formatter.parse("1900-01-01");
        dateRG6 = formatter.parse("2016-01-01");
        vEcriture = manager.getEcritureComptable();

    }

    @Test
    public void addReference() {
        calendar.setTime( dateAddReference );

        vEcriture.setJournal( new JournalComptable("AC","Achat") );
        vEcriture.setDate( dateAddReference );
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1","Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2","Compte 2",null, "10"));

        manager.addReference();
        SequenceEcritureComptable pSeq = dao.getSequenceEcritureComptable("AC",calendar.get(Calendar.YEAR) );
        Assert.assertEquals("AC-1900/00001",manager.setReference(pSeq) );


        manager.addReference( );
        pSeq = dao.getSequenceEcritureComptable("AC",calendar.get(Calendar.YEAR) );
        Assert.assertEquals("AC-1900/00002",manager.setReference( pSeq ) );

        dao.deleteSequenceEcritureComptable( pSeq );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableContextRG6_New() throws FunctionalException {

        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( new JournalComptable("AC","Achat") );
        vEcriture.setReference("AC-2016/00011");
        vEcriture.setDate( dateRG6 );
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1","Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2","Compte 2",null, "10"));

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
        vEcriture.getListLigneEcriture().add(this.createLigne(411,"Facture c1","Clients", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(411, "Facture c2","Clients",null, "10"));
        manager.addReference();

        manager.insertEcritureComptable( vEcriture );

        EcritureComptable vECRef = dao.getEcritureComptableByRef(vEcriture.getReference());

        manager.deleteEcritureComptable( vECRef.getId() );

        vEcriture.setReference("VE-2016/00004");

        manager.insertEcritureComptable( vEcriture );

    }

    @Test(expected = FunctionalException.class )
    public void updateEcritureComptable() throws FunctionalException, NotFoundException {

        Date date = new Date();
        vEcriture.setJournal( new JournalComptable("AC","Achat") );
        vEcriture.setDate( date );
        vEcriture.setLibelle("Insertion nouvelle écriture");
        vEcriture.getListLigneEcriture().add(this.createLigne(411,"Facture c1","Clients", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(411, "Facture c2","Clients",null, "10"));
        manager.addReference();

        manager.insertEcritureComptable( vEcriture );

        EcritureComptable vECRef = dao.getEcritureComptableByRef(vEcriture.getReference());

        vEcriture.setLibelle("Insertion nouvelle écriture");

        manager.updateEcritureComptable( vEcriture );

        manager.deleteEcritureComptable( vECRef.getId() );
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
        vEcriture.getListLigneEcriture().add(this.createLigne(411,"Facture c1","Clients", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(411, "Facture c2","Clients",null, "10"));
        manager.addReference();

        manager.insertEcritureComptable( vEcriture );

        EcritureComptable vECRef = dao.getEcritureComptableByRef(vEcriture.getReference());

        List<EcritureComptable> listEcritureComptable = manager.getListEcritureComptable();
        Assert.assertTrue( listEcritureComptable.contains( vECRef ) );


        List<CompteComptable> listCompteComptable = manager.getListCompteComptable();
        Assert.assertTrue( listCompteComptable.contains( compteComptable ) );

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
        Assert.assertFalse( listEcritureComptable.contains( vECRef ) );

        listCompteComptable = manager.getListCompteComptable();
        Assert.assertTrue( listCompteComptable.contains( compteComptable ) );

        isCodeJournal = false;
        listJournalComptable = manager.getListJournalComptable();
        for (JournalComptable jComptable : listJournalComptable )
            if( jComptable.getCode().equals( journalComptable.getCode() ) ) {
                isCodeJournal = true;
                break;
            }

        Assert.assertTrue( isCodeJournal );


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