package com.dummy.myerp.model.bean.comptabilite;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JournalComptableTest {



    private SequenceEcritureComptable createSequence(JournalComptable journalComptable,Integer year,Integer lastValue ){
        return new SequenceEcritureComptable( journalComptable,year,lastValue );
    }

    @Test
    public void getByCode() {
        List<JournalComptable> journalComptableList =new ArrayList<JournalComptable>();
        JournalComptable journalComptable = new JournalComptable();
        journalComptable.setCode("AC");
        journalComptable.setLibelle("Achat");
        journalComptable.getListSequenceEcriture().add(this.createSequence(journalComptable,2016,1) );

        journalComptableList.add(  journalComptable );
        Assert.assertNotNull(JournalComptable.getByCode( journalComptableList,"AC" ) );
        Assert.assertEquals(journalComptable.getLibelle(),JournalComptable.getByCode( journalComptableList,"AC" ).getLibelle() );
        Assert.assertNull(JournalComptable.getByCode( journalComptableList,"ACC" ) );
    }

    @Test
    public void isJournalComptableExist() {
        JournalComptable journalComptable = new JournalComptable( "AC","Achat") ;
        Assert.assertTrue( JournalComptable.isJournalComptableExist( journalComptable,"AC") );
        Assert.assertFalse( JournalComptable.isJournalComptableExist( journalComptable,"ACc" ) );
        Assert.assertFalse( JournalComptable.isJournalComptableExist( null,"AC") );
    }
}
