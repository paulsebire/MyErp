package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompteComptableTest {



    @Test
    public void getByNumero(){
        List<CompteComptable> compteComptableList = new  ArrayList<CompteComptable>();
        compteComptableList.add(  new CompteComptable(11223344,"Compte courant") );
        Assert.assertNotNull( CompteComptable.getByNumero(compteComptableList,11223344) );
        Assert.assertNull( CompteComptable.getByNumero(compteComptableList,11223345));
    }

    @Test
    public void isCompteComptableExist(){
        CompteComptable compteComptable = new CompteComptable();
        compteComptable.setNumero( 11223344 );
        compteComptable.setLibelle("Compte courant");


        Assert.assertTrue( CompteComptable.isCompteComptableExist( compteComptable,11223344) );
        Assert.assertFalse( CompteComptable.isCompteComptableExist( compteComptable,11223346) );
        Assert.assertFalse( CompteComptable.isCompteComptableExist( null,11223344) );

    }
}
