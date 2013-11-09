package iguanaman.iguanatweaks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class StackSizeTweaks {

	public static void init() 
	{
    	// BLOCKS
    	if (IguanaConfig.blockStackSizeDividerMax > 1) 
    	{
    		IguanaLog.log("Reducing block stack sizes");
        	for (Block block : Block.blocksList)
        	{
        		
        		Item item = null;
        		try {
        			item = Item.itemsList[block.blockID];
        		} catch (Exception e) {
        			
        		}

        		if (item != null)
        		{
        			Material blockMaterial = block.blockMaterial;
        			float materialModifier = 0.0F;
			        if (blockMaterial == Material.rock) materialModifier = 1.0F;
			        if (blockMaterial == Material.grass || blockMaterial == Material.ground 
			        		|| blockMaterial == Material.sand || blockMaterial == Material.snow 
			        		|| blockMaterial == Material.wood || blockMaterial == Material.glass 
			        		|| blockMaterial == Material.ice || blockMaterial == Material.tnt) materialModifier = 0.5F;
			        if (blockMaterial == Material.iron || blockMaterial == Material.anvil) materialModifier = 1.5F;
			        if (blockMaterial == Material.leaves || blockMaterial == Material.plants 
			        		|| blockMaterial == Material.vine) materialModifier = 1.0F / 16F;
			        if (blockMaterial == Material.cloth) materialModifier = 0.25F;
			        
			        int size = 0;
			        
			        if (materialModifier > 0.0F) 
			        {
		        		size = Math.round((float) item.maxStackSize / ((float) IguanaConfig.blockStackSizeDividerMax * materialModifier));
		        		if (size > item.maxStackSize / IguanaConfig.blockStackSizeDividerMin) size = item.maxStackSize / IguanaConfig.blockStackSizeDividerMin;
			        } else {
		        		size = Math.round((float)item.maxStackSize / (float)IguanaConfig.blockStackSizeDividerMin);
			        }

	        		if (size < 1) size = 1;
	        		if (size > 64) size = 64;
	        		if (size < item.maxStackSize) 
	        		{
	            		if (IguanaConfig.logHardnessChanges) IguanaLog.log("Reducing stack size of block " + item.getUnlocalizedName()  + " to " + size);
	        			item.setMaxStackSize(size);
	        		}
        		}
        	}
    	}
    	
    	// ITEMS
    	if (IguanaConfig.itemStackSizeDivider > 1) 
    	{
    		IguanaLog.log("Reducing item stack sizes");
        	for (Item item : Item.itemsList)
        	{
        		if (item != null)
        		{
        			if (item.itemID >= Block.blocksList.length) 
        			{
        				int size = item.maxStackSize / IguanaConfig.itemStackSizeDivider;
		        		if (size < 1) size = 1;
		        		if (size > 64) size = 64;
		        		if (size < item.maxStackSize) 
		        		{
		        			if (IguanaConfig.logHardnessChanges) IguanaLog.log("Reducing stack size of item " + item.getUnlocalizedName()  + " to " + size);
		        			item.setMaxStackSize(size);
		        		}
        			}
        		}
        	}
    	}
	}
	
}
