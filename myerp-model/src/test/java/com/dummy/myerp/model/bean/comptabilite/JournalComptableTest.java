package com.dummy.myerp.model.bean.comptabilite;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class JournalComptableTest {

    private  SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();

    @Test
    public void getByCode() {
        List<JournalComptable> journalComptableList =new ArrayList<JournalComptable>();
        journalComptableList.add(  new JournalComptable( "AC","Achat ") );
        journalComptableList.add(  new JournalComptable( "CC","Courantt ") );

        Assert.assertEquals( new JournalComptable( "AC","Achat "),JournalComptable.getByCode(journalComptableList, "AC" ) );

        Assert.assertNotEquals(  new JournalComptable( "AC","Achat "),JournalComptable.getByCode(journalComptableList, "ACC" ));

    }

    @Test
    public void isJournalComptableExist() {

        JournalComptable journalComptable = new JournalComptable( "AC","Achat ") ;
        Assert.assertTrue( JournalComptable.isJournalComptableExist( journalComptable,"AC") );
        Assert.assertFalse( JournalComptable.isJournalComptableExist( journalComptable,"ACc") );
        Assert.assertFalse( JournalComptable.isJournalComptableExist( null,"AC") );
    }
}
