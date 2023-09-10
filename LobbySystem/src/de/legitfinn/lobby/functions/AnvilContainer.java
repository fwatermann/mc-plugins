package de.legitfinn.lobby.functions;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.EntityHuman;

public class AnvilContainer
  extends ContainerAnvil
{
  public AnvilContainer(EntityHuman entity)
  {
    super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    return true;
  }
}