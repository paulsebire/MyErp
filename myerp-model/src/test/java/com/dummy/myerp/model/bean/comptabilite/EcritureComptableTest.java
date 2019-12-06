package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class EcritureComptableTest {


    private List<CompteComptable> compteComptableList = new ArrayList<CompteComptable>();
    private List<JournalComptable >journalComptableList= new ArrayList<JournalComptable>();

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


    }

    @Test
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Ecriture 1", 411,"Compte 1","200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Ecriture 2",401, "Compte 2","100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(3, "Ecriture 3",401,"Compte 2",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(4, "Ecriture 4",412,"Nouveau compte","40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Ecriture 1", 411,"Compte 1","200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Ecriture 2",401, "Compte 2","100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(3, "Ecriture 3",401,"Compte 2",null, "301"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }

    @Test
    public void getTotalDebit() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();


        vEcriture.setLibelle("Total Débit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Ecriture 1", 411,"Compte 1","201", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Ecriture 2",401, "Compte 2", "100", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(3, "Ecriture 3",401,"Compte 2",null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(4, "Ecriture 4",412,"Nouveau compte","40", "7"));
        Assert.assertEquals(new BigDecimal( "341.00"),vEcriture.getTotalDebit() );

        Assert.assertNotEquals(new BigDecimal( "341"),vEcriture.getTotalDebit());
    }

    @Test
    public void getTotalCredit(){
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Total Crédit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Ecriture 1", 411,"Compte 1", "201", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Ecriture 2",401, "Compte 2", "100", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(3, "Ecriture 3",401,"Compte 2", null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(4, "Ecriture 4",412,"Nouveau compte","40", "7"));
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

    @Test
    public void getById(){

        List<EcritureComptable> ecritureComptableList = new ArrayList<EcritureComptable>();

        JournalComptable journal = ObjectUtils.defaultIfNull(
                JournalComptable.getByCode( journalComptableList, "AC" ),
                new JournalComptable( "AC","Achat" ) );

        EcritureComptable ecritureComptable = new EcritureComptable( );
        ecritureComptable.setId( 1 );
        ecritureComptable.setJournal( journal );
        ecritureComptable.setReference("AC-2016/00001");
        ecritureComptable.setDate( new Date() );
        ecritureComptable.setLibelle("Ecriture 1");
        ecritureComptableList.add( ecritureComptable );

       Assert.assertNotNull( EcritureComptable.getById( ecritureComptableList,1) );

       Assert.assertNull( EcritureComptable.getById( ecritureComptableList,2 ) );



    }
    @Test
    public void isEcritureComptableExist(){


        JournalComptable journal = ObjectUtils.defaultIfNull(
                JournalComptable.getByCode( journalComptableList, "AD" ),
                new JournalComptable( "AC","Achat" ) );
        EcritureComptable ecritureComptable = new EcritureComptable( );
        ecritureComptable.setId( 1 );
        ecritureComptable.setJournal( journal );
        ecritureComptable.setReference("AC-2016/00001");
        ecritureComptable.setDate( new Date() );
        ecritureComptable.setLibelle("Ecriture 1");

        Assert.assertTrue(EcritureComptable.isEcritureComptableExist( ecritureComptable,1) );
        Assert.assertFalse(EcritureComptable.isEcritureComptableExist(ecritureComptable,2) );
        Assert.assertFalse(EcritureComptable.isEcritureComptableExist(null,1) );
    }

}
