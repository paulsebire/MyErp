package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.SpringRegistry;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ComptabiliteDaoImplTest {

    ComptabiliteDaoImpl dao = ComptabiliteDaoImpl.getInstance();


    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String vLibelle, String pComteComptableLibelle, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ?  BigDecimal.ZERO : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? BigDecimal.ZERO : new BigDecimal(pCredit);
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero,pComteComptableLibelle),
                vLibelle,
                vDebit, vCredit);
        return vRetour;
    }

    @Before
    public void init() {
        SpringRegistry.init();
    }



    @Test
    public void crudSequenceEcritureComptable(){
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable("VE",1900,1);
        dao.insertSequenceEcritureComptable( sequenceEcritureComptable );

        sequenceEcritureComptable = dao.getSequenceEcritureComptable("VE",1900);
        assertNotNull( sequenceEcritureComptable );


        sequenceEcritureComptable.setDerniereValeur( 2 );
        dao.updateSequenceEcritureComptable( sequenceEcritureComptable );;


        sequenceEcritureComptable = dao.getSequenceEcritureComptable("VE",1900);
        assertTrue( sequenceEcritureComptable.getDerniereValeur().equals( 2 ) );

        dao.deleteSequenceEcritureComptable( sequenceEcritureComptable );

        sequenceEcritureComptable = dao.getSequenceEcritureComptable("VE",1900);
        assertNull( sequenceEcritureComptable );

    }


    @Test
    public void getListCompteComptable() {
        List<CompteComptable> listCompteComptable = dao.getListCompteComptable();
        assertNotNull( listCompteComptable);
    }

    @Test
    public void getListJournalComptable() {
        List<JournalComptable> listJournalComptable = dao.getListJournalComptable();
        assertNotNull( listJournalComptable);
    }

    @Test
    public void getListEcritureComptable() {
        List<EcritureComptable> listEcritureComptable = dao.getListEcritureComptable();
        assertNotNull(listEcritureComptable );
    }

    @Test
    public void getEcritureComptable() {
        EcritureComptable ecritureComptable = dao.getEcritureComptable( -1);
        assertNotNull( ecritureComptable);
        ecritureComptable =dao.getEcritureComptable( 0);
        assertNull(ecritureComptable);

    }

    @Test(expected = NotFoundException.class )
    public void getEcritureComptableByRef() throws NotFoundException {
        EcritureComptable ecritureComptable = dao.getEcritureComptableByRef("AC-2016/00001");
        assertNotNull( ecritureComptable);
        dao.getEcritureComptableByRef("AC-2016/00000");

    }



    @Test(expected = NotFoundException.class )
    public void crudEcritureComptable() throws NotFoundException {
        Date date = new Date();
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setJournal( new JournalComptable("AC","Achat") );
        vEcriture.setDate( date );
        vEcriture.setReference("AL-1900/00000");
        vEcriture.setLibelle("Insertion nouvelle écriture");
        vEcriture.getListLigneEcriture().add(this.createLigne(411,"Facture c1","Clients", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(411, "Facture c2","Clients",null, "10"));


        dao.insertEcritureComptable( vEcriture );

        EcritureComptable vECRef = dao.getEcritureComptableByRef(vEcriture.getReference());

        assertNotNull( vECRef);

        vECRef.setReference("AL-1900/00002");

        dao.updateEcritureComptable( vECRef );

        vECRef = dao.getEcritureComptableByRef( vECRef.getReference() );

        assertNotNull( vECRef);

        dao.deleteEcritureComptable( vECRef.getId() );

        dao.getEcritureComptableByRef( vECRef.getReference() );

    }


}