package org.scapemod.accessor;

import java.awt.Canvas;

public interface Client {

    Canvas getCanvas();

    int getLoginState();

    int getConnectionState();

    String getUsername();

    String getPassword();

    int[] getCurrentStats();

    int[] getBaseStats();

    int[] getStatExperiences();

    InterfaceComponent[][] getInterfaceComponents();

    HashTable getInterfaceComponentNodes();

    Npc[] getNpcs();

    Player[] getPlayers();

    CollisionMap[] getCollisionMaps();

    int[] getInterfaceComponentWidths();

    int[] getInterfaceComponentHeights();

    int[] getInterfaceComponentXPositions();

    int[] getInterfaceComponentYPositions();

    int getCameraX();

    int getCameraY();

    int getCameraZ();

    int getPlane();

    int getCameraPitch();

    int getCameraYaw();

    Player getLocalPlayer();

    int getBaseX();

    int getBaseY();

    int getEnergy();

    int getWeight();

    int[][][] getTileHeights();

    byte[][][] getTileBytes();

    int getMapScale();

    int getMapAngle();

    int getMapOffset();

    Region getRegion();

    String[] getMenuActions();

    String[] getMenuOptions();

    int getMenuSize();

    boolean getMenuOpen();
}