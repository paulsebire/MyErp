package com.dummy.myerp.model.bean.comptabilite;

import com.dummy.myerp.model.validation.constraint.MontantComptable;
import lombok.NonNull;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LigneEcritureComptableTest {



    @Test
    public void getByIdEcritureAndLigneId(){

        List<LigneEcritureComptable> ligneEcritureComptableList = new ArrayList<LigneEcritureComptable>();
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable();



        JournalComptable journal = new JournalComptable("AC","Achat");
        EcritureComptable ecritureComptable = new EcritureComptable( );
        ecritureComptable.setId( -1 );
        ecritureComptable.setJournal( journal );
        ecritureComptable.setReference("AC-2016/00001");
        ecritureComptable.setDate( new Date() );
        ecritureComptable.setLibelle("Ecriture 1");

        // EcritureComptable ecritureComptable;
        ligneEcritureComptable.setId(1);
        ligneEcritureComptable.setCompteComptable( new CompteComptable(411,"Clients") );
        ligneEcritureComptable.setLibelle( "Facture c1" );
        ligneEcritureComptable.setDebit( null );
        ligneEcritureComptable.setCredit(new BigDecimal("500.00"));
        ligneEcritureComptable.setEcritureComptable( ecritureComptable );
        ligneEcritureComptableList.add( ligneEcritureComptable );

        Assert.assertNotNull( LigneEcritureComptable.getById(ligneEcritureComptableList,-1) );
        Assert.assertEquals(ligneEcritureComptable.getId(), LigneEcritureComptable.getById( ligneEcritureComptableList,-1).getId());
        Assert.assertNull( LigneEcritureComptable.getById(ligneEcritureComptableList,-10) );
    }

    @Test
    public void isLigneEcritureComptableExist(){
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setId( -1 );
        ecritureComptable.setJournal(new JournalComptable("AC","Achat") );
        ecritureComptable.setDate(new Date() );
        ecritureComptable.setLibelle("Ecriture 1");

        ecritureComptable.setReference("AC-2016/00001");

        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable(
                1, new CompteComptable(411,"Clients"),
                "Facture c1",
                null,new BigDecimal("500.00"));

        ligneEcritureComptable.setEcritureComptable( ecritureComptable );

        Assert.assertTrue(LigneEcritureComptable.isLigneEcritureComptableExist(ligneEcritureComptable,-1));
        Assert.assertFalse(LigneEcritureComptable.isLigneEcritureComptableExist(ligneEcritureComptable,-10) );
        Assert.assertFalse(LigneEcritureComptable.isLigneEcritureComptableExist(null,-1) );
        Assert.assertFalse(LigneEcritureComptable.isLigneEcritureComptableExist(null,-10) );

    }
}