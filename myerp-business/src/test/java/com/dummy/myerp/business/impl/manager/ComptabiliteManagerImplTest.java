package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dummy.myerp.business.BusinessTestCase;
import com.dummy.myerp.business.SpringRegistry;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.rules.ExpectedException;

public class ComptabiliteManagerImplTest {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    private List<CompteComptable> compteComptableList = new ArrayList<>();
    private List<JournalComptable> journalComptableList= new ArrayList<>();

    private  JournalComptable journal;

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
    public void init(){

        ComptabiliteManagerImpl.setTransactionManager( BusinessTestCase.getTransactionManager() );
        ComptabiliteManagerImpl.setDaoProxy( BusinessTestCase.getDaoProxy() );
        ComptabiliteManagerImpl.setBusinessProxy( BusinessTestCase.getBusinessProxy() );


        compteComptableList.add(new CompteComptable(401,"Fournisseurs" ) );
        compteComptableList.add(new CompteComptable(411,"Clients" ) );
        compteComptableList.add(new CompteComptable(4456,"Taxes sur le chiffre d'affaires déductibles" ) );
        compteComptableList.add(new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par l'entreprise" ) );
        compteComptableList.add(new CompteComptable(512,"Banque" ) );
        compteComptableList.add(new CompteComptable(606,"Achats non stockés de matières et fournitures" ) );
        compteComptableList.add(new CompteComptable(706,"Prestations de services" ) );

        journalComptableList.add(new JournalComptable("AC","Achat") );
        journalComptableList.add(new JournalComptable("VE","Vente") );
        journalComptableList.add(new JournalComptable("BQ","Banque") );
        journalComptableList.add(new JournalComptable("OD","Opérations Diverses") );

        journal = ObjectUtils.defaultIfNull(
                JournalComptable.getByCode( journalComptableList, "AC" ),
                new JournalComptable( "AL","Achat Libre" ) );
    }
        @Test(expected = UnsatisfiedLinkError.class )
        public void testInit()throws UnsatisfiedLinkError{
           //

        Assert.assertEquals(BusinessProxyImpl.getInstance(SpringRegistry.getDaoProxy(),BusinessTestCase.getTransactionManager() ), BusinessProxyImpl.getInstance() );
        Assert.assertNotEquals(BusinessProxyImpl.getInstance(null,BusinessTestCase.getTransactionManager() ), BusinessProxyImpl.getInstance() );

        }
    @Test
    public void setReference(){

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(journal,2019,1);

        Assert.assertEquals("AC-2019/00001",manager.setReference( sequenceEcritureComptable ) );

        Assert.assertNotEquals("AL-2019/0001",manager.setReference( sequenceEcritureComptable ) );

        sequenceEcritureComptable = new SequenceEcritureComptable(journal,2016,1);
        Assert.assertNotEquals("AV-2019/00001",manager.setReference( sequenceEcritureComptable ) );


    }

    @Test
    public void isNumberValidEcritureComptable(){
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2",null, "301"));
        Assert.assertTrue(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture valide : au moins 2 lignes de débit/crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1","45", "5"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2","40", "7"));
        Assert.assertTrue(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture valide :  1 ligne de débit et 1 ligne de débit/crédit ");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2","40", "7"));
        Assert.assertTrue(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture valide :  1 ligne de crédit et 1 seule ligne de débit/crédit ");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2","40", "7"));
        Assert.assertTrue(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de débit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        Assert.assertFalse(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1",null, "301"));
        Assert.assertFalse(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de débit/crédit ");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1","40", "7"));
        Assert.assertFalse(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2 lignes de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1","10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2", "30", null));
        Assert.assertFalse(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2 lignes de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2", null, "10"));
        Assert.assertFalse(manager.isNumberValidEcritureComptable( vEcriture ) );
    }

    @Test
    public void isAmountExist(){
        Assert.assertTrue(manager.isAmountExist( new BigDecimal("1") ));
        Assert.assertTrue(manager.isAmountExist( new BigDecimal("-1") ));
        Assert.assertTrue(manager.isAmountExist( new BigDecimal("1.1") ));
        Assert.assertTrue(manager.isAmountExist( new BigDecimal("-1.1") ));

        Assert.assertFalse(manager.isAmountExist( BigDecimal.ZERO) );
        Assert.assertFalse(manager.isAmountExist( new BigDecimal("0")) );
        Assert.assertFalse(manager.isAmountExist( new BigDecimal("0.00")) );
        Assert.assertFalse(manager.isAmountExist( null ) );
    }

    @Test
    public void isReferenceValid(){
        Date date = new Date();
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( new JournalComptable("AC","Achat") );

        vEcriture.setReference("AC-2019/00001");
        vEcriture.setDate( date );
        Assert.assertTrue( manager.isReferenceValid( vEcriture ) );

        vEcriture.setReference("AL-2019/00001");
        Assert.assertFalse( manager.isReferenceValid( vEcriture ) );

        vEcriture.setReference("AL-2016/00001");
        Assert.assertFalse( manager.isReferenceValid( vEcriture ) );

        vEcriture.setReference("AC-2016/00001");
        Assert.assertFalse( manager.isReferenceValid( vEcriture ) );

        vEcriture.setReference("AC-2016/0000");
        Assert.assertFalse( manager.isReferenceValid( vEcriture ) );

        vEcriture.setReference("AC-2019/0000");
        Assert.assertFalse( manager.isReferenceValid( vEcriture ) );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableUnitConstaint()throws FunctionalException{

        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( journal );
        vEcriture.setReference("AC-2019/00001");
        vEcriture.setDate( new Date() );
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2", null, "10"));
        manager.checkEcritureComptable( vEcriture );
        vEcriture.getListLigneEcriture().clear();
        manager.checkEcritureComptable( vEcriture );


    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableUnitRG2()throws FunctionalException{

        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( journal );
        vEcriture.setReference("AC-2019/00001");
        vEcriture.setDate( new Date());
        vEcriture.setLibelle("équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2", null, "10"));
        manager.checkEcritureComptable( vEcriture );
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().clear();
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1","10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2", "20", "1"));
        manager.checkEcritureComptable( vEcriture );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableUnitRG3()throws FunctionalException{

        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( journal );
        vEcriture.setReference("AC-2019/00001");
        vEcriture.setDate( new Date() );
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2",null, "10"));
        manager.checkEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2 lignes de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", null, null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2", null, null));
        manager.checkEcritureComptable( vEcriture );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableUnitRG5()throws FunctionalException{

        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( journal );
        vEcriture.setReference("AC-2019/00001");
        vEcriture.setDate( new Date() );
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2",null, "10"));

        manager.checkEcritureComptable( vEcriture );

        vEcriture.setReference("AC-2016/00001");
        manager.checkEcritureComptable( vEcriture );
    }


















}
