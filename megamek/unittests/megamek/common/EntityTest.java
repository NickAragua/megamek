/*
 * MegaMek - Copyright (C) 2000,2001,2002,2003,2004,2005 Ben Mazur
 * (bmazur@sev.org)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package megamek.common;

import junit.framework.TestCase;
import megamek.common.Entity;
import megamek.common.MechFileParser;
import megamek.common.options.OptionsConstants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @version $Id$
 * @lastEditBy Deric "Netzilla" Page (deric dot page at usa dot net)
 * @since 11/3/13 8:48 AM
 */
@RunWith(JUnit4.class)
public class EntityTest {

    private Entity setupGunEmplacement() {
        Entity testEntity = Mockito.mock(GunEmplacement.class);
        Mockito.when(testEntity.calculateBattleValue()).thenCallRealMethod();
        Mockito.when(testEntity.calculateBattleValue(Mockito.anyBoolean(), Mockito.anyBoolean())).thenCallRealMethod();
        Mockito.when(testEntity.getTotalArmor()).thenReturn(100);
        ArrayList<Mounted> equipment = new ArrayList<Mounted>(2);
        WeaponType ppcType = Mockito.mock(WeaponType.class);
        Mockito.when(ppcType.getBV(Mockito.any(Entity.class))).thenReturn(50.0);
        Mounted ppc = Mockito.mock(Mounted.class);
        Mockito.when(ppc.getType()).thenReturn(ppcType);
        Mockito.when(ppc.isDestroyed()).thenReturn(false);
        equipment.add(ppc);
        equipment.add(ppc);
        Mockito.when(testEntity.getEquipment()).thenReturn(equipment);
        Mockito.when(testEntity.getWeaponList()).thenReturn(equipment);
        Mockito.when(testEntity.getAmmo()).thenReturn(new ArrayList<Mounted>(0));
        return testEntity;
    }

    @Test
    public void testCalculateBattleValue() {
        // Test a gun emplacement.
        Entity testEntity = setupGunEmplacement();
        Mockito.when(testEntity.useGeometricMeanBV()).thenReturn(false);
        int expected = 94;
        int actual = testEntity.calculateBattleValue(true, true);
        TestCase.assertEquals(expected, actual);
        Mockito.when(testEntity.useGeometricMeanBV()).thenReturn(true);
        expected = 94;
        actual = testEntity.calculateBattleValue(true, true);
        TestCase.assertEquals(expected, actual);
        Mockito.when(testEntity.getTotalArmor()).thenReturn(0); // Gun Emplacement with no armor.
        Mockito.when(testEntity.useGeometricMeanBV()).thenReturn(false);
        expected = 44;
        actual = testEntity.calculateBattleValue(true, true);
        TestCase.assertEquals(expected, actual);
        Mockito.when(testEntity.useGeometricMeanBV()).thenReturn(true);
        expected = 44;
        actual = testEntity.calculateBattleValue(true, true);
        TestCase.assertEquals(expected, actual);
    }
    
    @Test
    public void testCalculateWeight() {
        File f; 
        MechFileParser mfp;
        Entity e;
        int expectedWeight, computedWeight;
        
        // Test 1/1
        try {
            f = new File("data/mechfiles/mechs/3050U/Exterminator EXT-4A.mtf");
            mfp  = new MechFileParser(f);
            e = mfp.getEntity();
            expectedWeight = 65;
            computedWeight = (int)e.getWeight();
            TestCase.assertEquals(expectedWeight, computedWeight);
        } catch (Exception exc){
            TestCase.fail(exc.getMessage());
        }
    }
    
    @Test
    public void testGetRunMPMascChargerSpeedDemon() {
        File f; 
        MechFileParser mfp;
        Entity e;
        
        // get 
        try {
            // this is a 5/8 (13) unit with a MASC and supercharger
            f = new File("data/mechfiles/mechs/3145/Steiner/Gauntlet GTL-1O.mtf");
            mfp  = new MechFileParser(f);
            e = mfp.getEntity();
            e.setCrew(new Crew(CrewType.SINGLE));
            e.getCrew().getOptions().initialize();

            int expectedRunMPNormal = 8;
            int expectedRunMPSpeedDemon = 9;
            int expectedSprintMPNormal = 10;
            int expectedSprintMPSpeedDemon = 12;
            int expectedRunMPMascSuperCharger = 13;
            int expectedRunMPMascSuperChargerSpeedDemon = 14;
            int expectedSprintMPMascSuperCharger = 15;
            int expectedSprintMPMascSuperChargerSpeedDemon = 17;

            
            TestCase.assertEquals(expectedRunMPNormal, e.getRunMPwithoutMASC());
            TestCase.assertEquals(expectedRunMPMascSuperCharger, e.getRunMP());
            TestCase.assertEquals(expectedSprintMPNormal, e.getSprintMPwithoutMASC());
            TestCase.assertEquals(expectedSprintMPMascSuperCharger, e.getSprintMP());
            
            e.setUsingSpeedDemon(true);
            
            TestCase.assertEquals(expectedRunMPSpeedDemon, e.getRunMPwithoutMASC());
            TestCase.assertEquals(expectedRunMPMascSuperChargerSpeedDemon, e.getRunMP());
            TestCase.assertEquals(expectedSprintMPSpeedDemon, e.getSprintMPwithoutMASC());
            TestCase.assertEquals(expectedSprintMPMascSuperChargerSpeedDemon, e.getSprintMP());
            
        } catch (Exception exc){
            TestCase.fail(exc.getMessage());
        }
    }
    
    @Test
    public void testGetRunMPProtoMechSpeedDemon() {
        File f; 
        MechFileParser mfp;
        Entity e;
        
        // get 
        try {
            // this is a 6/9 protomech
            f = new File("data/mechfiles/protomechs/3060/Centaur 2.blk");
            mfp  = new MechFileParser(f);
            e = mfp.getEntity();
            e.setCrew(new Crew(CrewType.SINGLE));
            e.getCrew().getOptions().initialize();

            int expectedRunMPNormal = 9;
            int expectedRunMPSpeedDemon = 10;
            // it's a trick, protomechs can't sprint
            int expectedSprintMPNormal = 9;
            int expectedSprintMPNormalSpeedDemon = 10;
            
            TestCase.assertEquals(expectedRunMPNormal, e.getRunMPwithoutMASC());
            TestCase.assertEquals(expectedSprintMPNormal, e.getSprintMPwithoutMASC());
            
            e.setUsingSpeedDemon(true);
            
            TestCase.assertEquals(expectedRunMPSpeedDemon, e.getRunMPwithoutMASC());
            TestCase.assertEquals(expectedSprintMPNormalSpeedDemon, e.getSprintMPwithoutMASC());
            
        } catch (Exception exc){
            TestCase.fail(exc.getMessage());
        }
    }
    
    @Test
    public void testGetRunMPTankSpeedDemon() {
        File f; 
        MechFileParser mfp;
        Entity e;
        
        // get 
        try {
            // this is an 11/17 (22) tank with a supercharger
            f = new File("data/mechfiles/vehicles/3145/Republic/Scapha Hovertank (Primary).blk");
            mfp  = new MechFileParser(f);
            e = mfp.getEntity();
            e.setCrew(new Crew(CrewType.CREW));
            e.getCrew().getOptions().initialize();
            e.setGame(new Game());
            e.getGame().getOptions().getOption(OptionsConstants.ADVGRNDMOV_VEHICLE_ADVANCED_MANEUVERS).setValue(true);

            int expectedRunMPNormal = 17;
            int expectedRunMPSpeedDemon = 18;
            int expectedSprintMPNormal = 22;
            int expectedSprintMPSpeedDemon = 24;
            int expectedRunMPMascSuperCharger = 22;
            int expectedRunMPMascSuperChargerSpeedDemon = 23;
            int expectedSprintMPMascSuperCharger = 28;
            int expectedSprintMPMascSuperChargerSpeedDemon = 30;

            
            TestCase.assertEquals(expectedRunMPNormal, e.getRunMPwithoutMASC());
            TestCase.assertEquals(expectedRunMPMascSuperCharger, e.getRunMP());
            TestCase.assertEquals(expectedSprintMPNormal, e.getSprintMPwithoutMASC());
            TestCase.assertEquals(expectedSprintMPMascSuperCharger, e.getSprintMP());
            
            e.setUsingSpeedDemon(true);
            
            TestCase.assertEquals(expectedRunMPSpeedDemon, e.getRunMPwithoutMASC());
            TestCase.assertEquals(expectedRunMPMascSuperChargerSpeedDemon, e.getRunMP());
            TestCase.assertEquals(expectedSprintMPSpeedDemon, e.getSprintMPwithoutMASC());
            TestCase.assertEquals(expectedSprintMPMascSuperChargerSpeedDemon, e.getSprintMP());
            
        } catch (Exception exc){
            TestCase.fail(exc.getMessage());
        }
    }
}
