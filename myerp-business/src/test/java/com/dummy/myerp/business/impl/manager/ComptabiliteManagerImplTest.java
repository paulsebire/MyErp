package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Date;

import com.dummy.myerp.model.bean.comptabilite.*;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;

public class ComptabiliteManagerImplTest {

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
/*
    @Test
    public void addReference(){

    }
*/
    @Test
    public void setReference(){
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable("AL",2019,1);

        Assert.assertEquals("AL-2019/00001",manager.setReference( sequenceEcritureComptable ) );

        Assert.assertNotEquals("AL-2019/0001",manager.setReference( sequenceEcritureComptable ) );

        sequenceEcritureComptable = new SequenceEcritureComptable("AL",2016,1);
        Assert.assertNotEquals("AL-2019/00001",manager.setReference( sequenceEcritureComptable ) );
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkConstraintValid() throws Exception {
        EcritureComptable ecritureComptable = new EcritureComptable();
        manager.checkConstraintValid( ecritureComptable );
    }

    @Test(expected = FunctionalException.class)
    public void isEquilibree() throws Exception{
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2,"Compte 2", null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","1", "2"));
        manager.isEquilibree( vEcriture );
    }

    @Test(expected = FunctionalException.class)
    public void isNumberValidEcritureComptable()throws Exception{
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de débit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
         manager.isNumberValidEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
         manager.isNumberValidEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de débit/crédit ");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
         manager.isNumberValidEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2  lignes de débit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "101", null));
         manager.isNumberValidEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2 lignes de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "30"));
        manager.isNumberValidEcritureComptable( vEcriture );
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



}
