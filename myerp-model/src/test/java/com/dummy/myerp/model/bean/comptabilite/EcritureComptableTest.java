package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;


public class EcritureComptableTest {



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
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "Compte 1","200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "Compte 1","100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2,"Compte 2", null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","1", "2"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }

    @Test
    public void getTotalDebit() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();


        vEcriture.setLibelle("Total Débit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "Compte 1","201", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "100", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
        Assert.assertEquals(new BigDecimal( "341.00"),vEcriture.getTotalDebit() );

        Assert.assertNotEquals(new BigDecimal( "341"),vEcriture.getTotalDebit());
    }

    @Test
    public void getTotalCredit(){
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();


        vEcriture.setLibelle("Total Crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "201", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "100", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2,"Compte 2", null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
        Assert.assertEquals(new BigDecimal( "341.00"),vEcriture.getTotalCredit() );

        Assert.assertNotEquals(new BigDecimal( "341"),vEcriture.getTotalCredit());
    }

    @Test
    public void isAmountNotNull(){
        EcritureComptable ecritureComptable = new EcritureComptable();

        Assert.assertTrue( ecritureComptable.isAmountNotNull( new BigDecimal( "341.00") ));
        Assert.assertTrue( ecritureComptable.isAmountNotNull( new BigDecimal( "341") ));
        Assert.assertFalse( ecritureComptable.isAmountNotNull( null ) );

    }

}
