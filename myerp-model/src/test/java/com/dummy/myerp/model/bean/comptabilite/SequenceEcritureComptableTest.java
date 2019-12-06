package com.dummy.myerp.model.bean.comptabilite;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SequenceEcritureComptableTest {

    private List<JournalComptable > journalComptableList= new ArrayList<JournalComptable>();

    @Before
    public void init(){
        journalComptableList.add(new JournalComptable("AC","Achat") );
        journalComptableList.add(new JournalComptable("VE","Vente") );
        journalComptableList.add(new JournalComptable("BQ","Banque") );
        journalComptableList.add(new JournalComptable("OD","Opérations Diverses") );
    }

    @Test
    public void getByCodeAndYear(){
        List<SequenceEcritureComptable> sequenceEcritureComptableList = new ArrayList<SequenceEcritureComptable>();
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();

        JournalComptable journal = ObjectUtils.defaultIfNull(
                JournalComptable.getByCode( journalComptableList, "AC" ),
                new JournalComptable( "AC","Achat" ) );

        sequenceEcritureComptable.setJournalComptable( journal );
        sequenceEcritureComptable.setAnnee(2016);
        sequenceEcritureComptable.setDerniereValeur(1);

        sequenceEcritureComptableList.add( sequenceEcritureComptable );
        Assert.assertNotNull(SequenceEcritureComptable.getByCodeAndYear(sequenceEcritureComptableList,"AC",2016) );
        Assert.assertEquals(sequenceEcritureComptable.getDerniereValeur(),SequenceEcritureComptable.getByCodeAndYear(sequenceEcritureComptableList,"AC",2016).getDerniereValeur());
        Assert.assertNull(SequenceEcritureComptable.getByCodeAndYear(sequenceEcritureComptableList,"AC",2000) );

    }

    @Test
    public void isSequenceEcritureComptableExist(){
        JournalComptable journal = ObjectUtils.defaultIfNull(
                JournalComptable.getByCode( journalComptableList, "AC" ),
                new JournalComptable( "AC","Achat" ) );

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(journal,2016,1);

        Assert.assertTrue(SequenceEcritureComptable.isSequenceEcritureComptableExist(sequenceEcritureComptable,"AC",2016) );

        Assert.assertFalse(SequenceEcritureComptable.isSequenceEcritureComptableExist(sequenceEcritureComptable,"A",2016) );
        Assert.assertFalse(SequenceEcritureComptable.isSequenceEcritureComptableExist(sequenceEcritureComptable,"AC",2000) );
        Assert.assertFalse(SequenceEcritureComptable.isSequenceEcritureComptableExist(null,"AC",2016) );
        Assert.assertFalse(SequenceEcritureComptable.isSequenceEcritureComptableExist(null,"AC",2000) );

    }

}