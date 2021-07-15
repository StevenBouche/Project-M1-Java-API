package com.miage.share.game.model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaseTypeTest {
    static Case caseTest1;
    static Case caseTest2;

    @BeforeAll
    static void init(){
        caseTest1 = new Case();
        caseTest2= new Case();

        caseTest2.setValue('L');
        caseTest2.setType(CaseType.LD);
    }

    @Test
    void TestType() {
        getCaseType();
        getTypeTest();
        setTypeTest();
        getIdTest();
        setIdTest();
    }

    /**
     * Test du type d'une case à partir de l'id du type
     */
    void getCaseType(){
        int BA = 0;
        int LD = 1;
        int LT = 2;
        int MD = 3;
        int MT = 4;
        int ST = 5;

        // Test case basique
        CaseType res = CaseType.getCaseType(0);
        System.out.println(res);
        assertEquals("BA",res.toString());

        //Test lettre compte double
        CaseType res2 = CaseType.getCaseType(1);
        System.out.println(res2);
        assertEquals("LD",res2.toString());

        //Test Lettre compte triple
        CaseType res3 = CaseType.getCaseType(2);
        System.out.println(res3);
        assertEquals("LT",res3.toString());

        //Test mot compte double
        CaseType res4 = CaseType.getCaseType(3);
        System.out.println(res4);
        assertEquals("MD",res4.toString());

        //Test mot compte triple
        CaseType res5 = CaseType.getCaseType(4);
        System.out.println(res5);
        assertEquals("MT",res5.toString());

        //Test cas de départ
        CaseType res6 = CaseType.getCaseType(5);
        System.out.println(res6);
        assertEquals("ST",res6.toString());

        // Test case mauvais type
        CaseType resFalse = CaseType.getCaseType(12);
        System.out.println(resFalse);
        assertNull(resFalse);
    }


    /**
     * Test pour avoir le type d'une CaseType
     */
    void getTypeTest(){
        CaseType result = CaseType.LD;

        System.out.println(result);
        assertEquals("LD",result.getType());
    }

    /**
     * Test pour mettre à jour le type d'une case
     */

    void setTypeTest(){
        CaseType result = CaseType.LD;
        result.setType("ST");

        assertEquals("ST",result.getType());
    }

    /**
     * Test pour avoir l'ID du type de la case
     */
    void getIdTest(){
        CaseType result = CaseType.LD;

        assertEquals(1,result.getId());
    }

    /**
     * Test pour mettre à jour l'ID dy type d'une case
     */

    void setIdTest(){
        CaseType result = CaseType.LD;
        result.setId(4);

        assertEquals(4,result.getId());
    }


}