package org.scapemod.accessor;

public interface IPlayer extends ICharacter {

    String getName();

    IModel getModel();

    IPlayerDefinition getDefinition();
}