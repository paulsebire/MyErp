package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Date;

import com.dummy.myerp.model.bean.comptabilite.*;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.rules.ExpectedException;

public class ComptabiliteManagerImplTest {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero,String pComteComptableLibelle, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ?  BigDecimal.ZERO : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? BigDecimal.ZERO : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero,pComteComptableLibelle),
                vLibelle,
                vDebit, vCredit);
        return vRetour;
    }

    @Test
    public void setReference(){
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable("AL",2019,1);

        Assert.assertEquals("AL-2019/00001",manager.setReference( sequenceEcritureComptable ) );

        Assert.assertNotEquals("AL-2019/0001",manager.setReference( sequenceEcritureComptable ) );

        sequenceEcritureComptable = new SequenceEcritureComptable("AL",2016,1);
        Assert.assertNotEquals("AL-2019/00001",manager.setReference( sequenceEcritureComptable ) );
    }

    @Test
    public void isNumberValidEcritureComptable(){
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        Assert.assertTrue(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture valide : au moins 2 lignes de débit/crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 1","45", "5"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
        Assert.assertTrue(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture valide :  1 ligne de débit et 1 ligne de débit/crédit ");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
        Assert.assertTrue(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture valide :  1 ligne de crédit et 1 seule ligne de débit/crédit ");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
        Assert.assertTrue(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de débit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        Assert.assertFalse(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        Assert.assertFalse(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de débit/crédit ");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
        Assert.assertFalse(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2 lignes de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "30", null));
        Assert.assertFalse(manager.isNumberValidEcritureComptable( vEcriture ) );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2 lignes de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", null, "10"));
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
        Date date = new Date();
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( new JournalComptable("AC","Achat") );
        vEcriture.setReference("AC-2019/00001");
        vEcriture.setDate( date );
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", null, "10"));
        manager.checkEcritureComptable( vEcriture );
        vEcriture.getListLigneEcriture().clear();
        manager.checkEcritureComptable( vEcriture );


    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableUnitRG2()throws FunctionalException{
        Date date = new Date();
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( new JournalComptable("AC","Achat") );
        vEcriture.setReference("AC-2019/00001");
        vEcriture.setDate( date );
        vEcriture.setLibelle("équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", null, "10"));
        manager.checkEcritureComptable( vEcriture );
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().clear();
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "20", "1"));
        manager.checkEcritureComptable( vEcriture );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableUnitRG3()throws FunctionalException{
        Date date = new Date();
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( new JournalComptable("AC","Achat") );
        vEcriture.setReference("AC-2019/00001");
        vEcriture.setDate( date );
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "10"));
        manager.checkEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2 lignes de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", null, null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", null, null));
        manager.checkEcritureComptable( vEcriture );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableUnitRG5()throws FunctionalException{
        Date date = new Date();
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( new JournalComptable("AC","Achat") );
        vEcriture.setReference("AC-2019/00001");
        vEcriture.setDate( date );
        vEcriture.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "10"));

        manager.checkEcritureComptable( vEcriture );

        vEcriture.setReference("AC-2016/00001");
        manager.checkEcritureComptable( vEcriture );
    }


















}
