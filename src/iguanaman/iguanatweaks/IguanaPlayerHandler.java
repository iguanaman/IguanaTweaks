package iguanaman.iguanatweaks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class IguanaPlayerHandler implements IPlayerTracker {

	@Override
   public void onPlayerLogin(EntityPlayer entityplayer) {
   }

	@Override
	public void onPlayerLogout(EntityPlayer player) {
	}
	
	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
	}
	
	@Override
	public void onPlayerRespawn(EntityPlayer entityplayer) {
		
		// set health
	   int respawnHealth = IguanaConfig.respawnHealth;
	   
	   if (IguanaConfig.respawnHealthDifficultyScaling) {
		   if (entityplayer.worldObj.difficultySetting == 3) 
		   {
			   respawnHealth = Math.max(Math.round((float)respawnHealth / 2f), 1);
		   }
		   else if (entityplayer.worldObj.difficultySetting <= 1) 
		   {
			   respawnHealth = Math.min(respawnHealth * 2, 20);
		   }
	   }

		entityplayer.setHealth((float)respawnHealth);
		
		
		if (IguanaConfig.destroyBedOnRespawn)
		{
		    // destroy nearest bed block (4 block radius)
			World world = entityplayer.worldObj;
			
			double posmod = 0d;
			if(entityplayer instanceof EntityClientPlayerMP) posmod = 1.62d;
			
			int x = (int)entityplayer.posX;
			int y = (int)(entityplayer.posY - posmod - 1d);
			int z = (int)entityplayer.posZ;
			if (x < 0) --x;
			if (y < 0) --y;
			if (z < 0) --z;
			
	        for (int testX = x - 3; testX <= x + 3; ++testX)
	        {
	            for (int testY = y - 1; testY <= y + 1; ++testY)
	            {
	                for (int testZ = z - 3; testZ <= z + 3; ++testZ)
	                {
	                    if (world.getBlockId(testX, testY, testZ) == Block.bed.blockID)
	                    {
	                    	int meta = world.getBlockMetadata(testX, testY, testZ);
	                    	if (!((BlockBed)Block.bed).isBlockHeadOfBed(meta)) 
	                    	{
	                    		world.destroyBlock(testX, testY, testZ, false);
	                    		entityplayer.setSpawnChunk(null, false);
	                    	    entityplayer.addChatMessage("You awake to find your bed smashed to pieces");
	                    		break;
	                    	}
	                    }
	                }
	            }
	        }
		}
		
	}
}
