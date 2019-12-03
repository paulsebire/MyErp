package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CompteComptableTest {




    @Test
    public void getByNumeroTest(){

        List<CompteComptable> compteComptableList =new  ArrayList<CompteComptable>();
        compteComptableList.add(  new CompteComptable(11223344,"Compte courant") );
        compteComptableList.add(new CompteComptable(11223366,"Compte stock") );

        Assert.assertEquals(  new CompteComptable(11223344,"Compte courant"),CompteComptable.getByNumero(compteComptableList, 11223344 ) );

        Assert.assertNotEquals(  new CompteComptable(11223344,"Compte courant"),CompteComptable.getByNumero(compteComptableList, 112233477 ));

    }

    @Test
    public void isCompteComptableExistTest(){
        CompteComptable compteComptable = new CompteComptable(11223344,"Compte courant");
        Assert.assertTrue( CompteComptable.isCompteComptableExist( compteComptable,11223344) );
        Assert.assertFalse( CompteComptable.isCompteComptableExist( compteComptable,11223346) );
        Assert.assertFalse( CompteComptable.isCompteComptableExist( null,11223344) );

    }
}
