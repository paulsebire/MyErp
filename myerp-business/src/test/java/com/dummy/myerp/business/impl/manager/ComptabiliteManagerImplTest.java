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
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "Compte 1","200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "Compte 1","100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
        Assert.assertTrue(manager.isEquilibree(vEcriture));

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2,"Compte 2", null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","1", "2"));
        Assert.assertFalse( manager.isEquilibree(vEcriture));
    }



    @Test(expected = FunctionalException.class )
    public void checkEcritureComptable_constraint() throws FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptable(vEcritureComptable);

    }

    /**
     * isequilibre
     * @throws FunctionalException
     */
    @Test(expected = FunctionalException.class )
    public void checkEcritureComptable_isnotequilibree() throws FunctionalException {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Non equilibre");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "Compte 1","200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "Compte 1","100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));


    }


    @Test
    public void checkEcritureComptableUnit() throws FunctionalException {

        EcritureComptable pEcritureComptable = new EcritureComptable();
        thrown.expect(FunctionalException.class);


        manager.checkEcritureComptableUnit(pEcritureComptable);
    }

    @Test
    public void checkConstraintValid() throws FunctionalException {
        EcritureComptable ecritureComptable = new EcritureComptable();
        thrown.expect(FunctionalException.class);
        manager.checkConstraintValid( ecritureComptable );
    }



    @Test
    public void isNumberValidEcritureComptable()throws FunctionalException{
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de débit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        thrown.expect(FunctionalException.class);
        manager.isNumberValidEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        thrown.expect(FunctionalException.class);
        manager.isNumberValidEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 1 seule ligne de débit/crédit ");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2","40", "7"));
        thrown.expect(FunctionalException.class);
        manager.isNumberValidEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2  lignes de débit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Compte 1", "101", null));
        thrown.expect(FunctionalException.class);
        manager.isNumberValidEcritureComptable( vEcriture );

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Nombre écriture non valide : 2 lignes de crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Compte 2",null, "30"));
        thrown.expect(FunctionalException.class);
        manager.isNumberValidEcritureComptable( vEcriture );
    }





}
