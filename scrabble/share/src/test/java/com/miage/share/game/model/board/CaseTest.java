package com.miage.share.game.model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaseTest {

    static Case caseTestEmpty;
    static Case caseTestFull;


    @BeforeAll
    static void init(){
        caseTestEmpty = new Case();
        caseTestFull = new Case();

        caseTestFull.setValue('L');
        caseTestFull.setType(CaseType.LD);

    }

    @Test
    void TestCase() {
        Case c = new Case(CaseType.LT);
        assertEquals("LT",c.getType().toString());
        isEmptyTest();
        getValueTest();
        setValueTest();
        getTypeByIdTest();
        setTypeTest();
    }

    /**
     * Test si une case est vide ou non
     */
    void isEmptyTest(){

        boolean res = caseTestEmpty.isEmpty();
        System.out.println(res);
        assertEquals(true,res);

        boolean res2 = caseTestFull.isEmpty();
        System.out.println(res2);
        assertEquals(false,res2);

    }

    /**
     * Test pour obtenir la lettre d'une case (renvoie un char)
     */
    void getValueTest(){

        assertEquals('L',caseTestFull.getValue());
        assertEquals(' ',caseTestEmpty.getValue());
    }

    /**
     * Test pour remplir une case vide
     */
    void setValueTest(){

        Case caseTest = new Case();
        caseTest.setValue('F');

        assertEquals('F',caseTest.getValue());

    }

    /**
     * Test pour obtenir le type de la case (renvoie une value de CaseType)
     */
    void getTypeByIdTest(){
        assertEquals("LD",caseTestFull.getType().toString());
    }

    /**
     * Test pour donner un type à la case (renvoie un type de CaseType)
     */
    void setTypeTest(){

        Case caseTest = new Case();
        caseTest.setType(CaseType.LT);

        assertEquals("LT",caseTest.getType().toString());

    }

}