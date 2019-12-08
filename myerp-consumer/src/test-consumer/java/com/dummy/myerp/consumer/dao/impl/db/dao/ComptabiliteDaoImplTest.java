package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.SpringRegistry;
import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class ComptabiliteDaoImplTest {

    private ComptabiliteDaoImpl dao = ComptabiliteDaoImpl.getInstance();

    private List<CompteComptable> compteComptableList = new ArrayList<>();
    private List<JournalComptable> journalComptableList= new ArrayList<>();


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
    public void init() {
        SpringRegistry.init();
        compteComptableList = dao.getListCompteComptable();
        journalComptableList = dao.getListJournalComptable();
    }

    @Test
    public void isDataSourceConfig(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://192.168.99.101:9032/db_myerp");
        ds.setUsername("root");
        ds.setPassword("root");

        Map<DataSourcesEnum, DataSource> vMapDataSource = new HashMap<>(DataSourcesEnum.values().length);
        vMapDataSource.put(DataSourcesEnum.MYERP,ds );

        Assert.assertFalse( AbstractDbConsumer.isDataSourceConfig(ds,vMapDataSource,DataSourcesEnum.MYERP) );
        Assert.assertTrue( AbstractDbConsumer.isDataSourceConfig(ds,vMapDataSource,null) );
        Assert.assertTrue( AbstractDbConsumer.isDataSourceConfig(null,vMapDataSource,DataSourcesEnum.MYERP) );
        Assert.assertTrue( AbstractDbConsumer.isDataSourceConfig(null,vMapDataSource,null) );

    }

    @Test(expected = UnsatisfiedLinkError.class )
    public void isDataSourceIsNull()throws UnsatisfiedLinkError{
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://192.168.99.101:9032/db_myerp");
        ds.setUsername("root");
        ds.setPassword("root");

        Map<DataSourcesEnum, DataSource> vMapDataSource = new HashMap<>(DataSourcesEnum.values().length);
        vMapDataSource.put(DataSourcesEnum.MYERP,ds );
         AbstractDbConsumer.isDataSourceIsNull(ds,DataSourcesEnum.MYERP) ;
         AbstractDbConsumer.isDataSourceIsNull(null,DataSourcesEnum.MYERP);
    }



    @Test
    public void crudSequenceEcritureComptable(){
        JournalComptable journal = ObjectUtils.defaultIfNull(
                JournalComptable.getByCode( journalComptableList, "VE" ),
                new JournalComptable( "VE","Vente" ) );
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(journal,1900,1);
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
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"Facture c1",411,"Compte 1", "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "Facture c2",401,"Compte 2",null, "10"));


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