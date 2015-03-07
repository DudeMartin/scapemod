package org.scapemod.accessor;

import java.awt.Canvas;

public interface IClient {

    Canvas getCanvas();
    
    GameKeyboard getKeyboard();

    GameMouse getMouse();
    
    int getLoginState();

    int getConnectionState();

    String getUsername();

    String getPassword();

    int[] getCurrentStats();

    int[] getBaseStats();

    int[] getStatExperiences();

    IInterfaceComponent[][] getInterfaceComponents();

    IHashTable getInterfaceComponentNodes();

    INpc[] getNpcs();

    IPlayer[] getPlayers();

    ICollisionMap[] getCollisionMaps();

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

    IPlayer getLocalPlayer();

    int getBaseX();

    int getBaseY();

    int getEnergy();

    int getWeight();

    int[][][] getTileHeights();

    byte[][][] getTileSettings();

    int getMapScale();

    int getMapAngle();

    int getMapOffset();

    IRegion getRegion();

    String[] getMenuActions();

    String[] getMenuOptions();
    
    int[] getMenuOpcodes();
    
    int[] getMenuParams0();
    
    int[] getMenuParams1();
    
    int[] getMenuParams2();

    int getMenuSize();

    boolean isMenuOpen();

    int getMenuX();

    int getMenuY();

    int getMenuWidth();

    int getMenuHeight();

    int getDestinationX();

    int getDestinationY();
    
    int[] getGameSettings();
}