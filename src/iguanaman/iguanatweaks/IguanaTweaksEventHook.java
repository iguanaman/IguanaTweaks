package iguanaman.iguanatweaks;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.client.entity.*;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IguanaTweaksEventHook {

	@ForgeSubscribe
    public void onLivingUpdate(LivingUpdateEvent event) {
		
		EntityLivingBase entity = event.entityLiving;
		World world = entity.worldObj;
		
		if (world.isRemote && entity != null)
		{
			boolean isCreative = false;
			double slownessArmour = (double)entity.getTotalArmorValue() * IguanaConfig.armorWeight;
			int slownessHurt = 0;
			int slownessTerrain = 0;
			double slownessWeight = 0d;
			boolean onIce = false;
			boolean jumping = false;
			
			if (entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)entity;
				int lastJump = 0;
				
				if (IguanaTweaks.playerLastJump.containsKey(player.username))
				{
					lastJump  = IguanaTweaks.playerLastJump.get(player.username) - 1;
				}
				
				if (player.jumpTicks == 10) lastJump = 10;
				
				if (lastJump <= 0) 
				{
					IguanaTweaks.playerLastJump.remove(player.username);
				}
				else
				{
					IguanaTweaks.playerLastJump.put(player.username, lastJump);
					jumping = true;
				}
				
				//if (jumping) FMLLog.warning("lastjump" + lastJump);
			}
			
			//Stun effect
			PotionEffect currentEffect = entity.getActivePotionEffect(IguanaTweaks.slowdownNew);
			if (currentEffect != null)
			{
				slownessHurt += Math.round(100f * ((float)currentEffect.duration / (float)currentEffect.getAmplifier()));
			}
			
			//Walking block
			if (!entity.isInWater() && IguanaConfig.terrainSlowdownPercentage > 0)
			{
				double posmod = 0d;

				if(entity instanceof EntityClientPlayerMP) posmod = 1.62d;
				
				int posX = (int)entity.posX;
				int posY = (int)(entity.posY - posmod - 1d);
				int posZ = (int)entity.posZ;
				if (posX < 0) --posX;
				if (posY < 0) --posY;
				if (posZ < 0) --posZ;
				
				/*
				if (entity instanceof EntityPlayer) 
					FMLLog.warning("posY" + Double.toString(entity.posY - posmod) + " class" + entity.getClass().toString());
					//FMLLog.warning("posX" + posX + " posY" + posY + " posZ" + posZ);
				*/
				
				Material blockOnMaterial = world.getBlockMaterial(posX, posY, posZ);
				Material blockInMaterial = world.getBlockMaterial(posX, posY + 1, posZ);
	
		        if (blockOnMaterial == Material.grass || blockOnMaterial == Material.ground) slownessTerrain = 5; 
		        else if (blockOnMaterial == Material.sand) slownessTerrain = 10;
		        else if (blockOnMaterial == Material.leaves || blockOnMaterial == Material.plants 
		        		|| blockOnMaterial == Material.vine || blockOnMaterial == Material.ice 
		        		|| blockOnMaterial == Material.snow) slownessTerrain = 20;
		        
		        if (blockInMaterial == Material.snow) slownessTerrain += 20;
				else if (blockInMaterial == Material.vine || blockOnMaterial == Material.plants ) slownessTerrain += 5;
		        
		        if (blockOnMaterial == Material.ice) onIce = true;
		        
		        slownessTerrain = Math.round((float)slownessTerrain * ((float)IguanaConfig.terrainSlowdownPercentage / 100f));
			}
			
			
			if (entity instanceof EntityPlayer && IguanaConfig.maxCarryWeight > 0) 
			{
				EntityPlayer player = (EntityPlayer)entity;
				if (player.capabilities.isCreativeMode) isCreative = true;
	
				if (world.getWorldTime() % 10 == 0 || !IguanaTweaks.playerWeights.containsKey(player.username)) {
					double weight = 0d;
					for (Object slotObject : player.inventoryContainer.inventorySlots) 
					{
						Slot slot = (Slot)slotObject;
						if (slot.getHasStack())
						{
					        double toAdd = 0d;	
					        
							ItemStack stack = slot.getStack();
							Material blockMaterial = null;
					        try {
					        	blockMaterial = Block.blocksList[stack.getItem().itemID].blockMaterial;
					        } catch (Exception e) {
					        }
	
					        if (blockMaterial != null) 
					        {
						        if (blockMaterial == Material.rock) toAdd = 1d;
						        if (blockMaterial == Material.grass || blockMaterial == Material.ground 
						        		|| blockMaterial == Material.sand || blockMaterial == Material.snow 
						        		|| blockMaterial == Material.wood || blockMaterial == Material.glass 
						        		|| blockMaterial == Material.ice || blockMaterial == Material.tnt) toAdd = 0.5d;
						        if (blockMaterial == Material.iron || blockMaterial == Material.anvil) toAdd = 1.5d;
						        if (blockMaterial == Material.leaves || blockMaterial == Material.plants 
						        		|| blockMaterial == Material.vine) toAdd = 1.0d / 16d;
						        if (blockMaterial == Material.cloth) toAdd = 1.0d / 4d;
					        } else if (stack.getItem().itemID < Block.blocksList.length) { //is block
					        	toAdd = 1d / 32d;
					        } else { //is item
					        	toAdd = 1d / 64d;
					        }
					        weight += toAdd * (double)stack.stackSize;
						}
					}
					
					// Calculate max weight
					double maxWeight = (double)IguanaConfig.maxCarryWeight;
					
					slownessWeight = (weight / maxWeight) *  100d;
	
					IguanaTweaks.playerWeights.put(player.username, new WeightValues(weight, maxWeight, slownessWeight, slownessArmour));
				}
				else
				{
					slownessWeight = IguanaTweaks.playerWeights.get(player.username).encumberance;
				}
	
	        	if (slownessWeight > 0) player.addExhaustion(0.00001F * Math.round(slownessWeight));
			}
	
	    	if (slownessArmour > 100d) slownessArmour = 100d;
	    	if (slownessHurt > 100) slownessHurt = 100;
	    	if (slownessTerrain > 100) slownessTerrain = 100;
	    	if (slownessWeight > 100d) slownessWeight = 100d;
	    	double speedModifierArmour = (100d - slownessArmour) / 100d;
	    	double speedModifierHurt = (100d - (double)slownessHurt) / 100d;
	    	double speedModifierTerrain = (100d - (double)slownessTerrain) / 100d;
	    	double speedModifierWeight = (100d - slownessWeight) / 100d;
	
			//if (entity instanceof EntityPlayer) FMLLog.warning("speedModifier" + Double.toString(speedModifier));
			//if (world.isRemote && entity instanceof EntityPlayer) FMLLog.warning("ssp" + Double.toString(speedModifier));
			//if (!world.isRemote && entity instanceof EntityPlayer) FMLLog.warning("smp" + Double.toString(speedModifier));
			
			if (!isCreative)
			{
				double speedModifier = speedModifierArmour * speedModifierHurt * speedModifierTerrain * speedModifierWeight;
				
				if (jumping) speedModifier = 0.75d + (speedModifier / 4d);
				
				if (entity instanceof EntityPlayerSP) 
				{
					EntityPlayerSP playerSP = (EntityPlayerSP)entity;
					if (playerSP.moveForward < 0f) speedModifier *= 0.5d;
				}
				if (onIce) speedModifier = 0.8d + (speedModifier / 5d);
				
				//if (entity instanceof EntityPlayer) FMLLog.warning("onIce" + Boolean.toString(onIce) + " mod" + Double.toString(speedModifier));
				
		    	speedModifier = (2d * speedModifier) - 1d;
				entity.motionX *= speedModifier;
		    	entity.motionZ *= speedModifier;
			}
		}
	}
	
	@ForgeSubscribe
	public void LivingHurt(LivingHurtEvent event)
	{
		if (IguanaConfig.damageSlowdownDuration > 0 && !event.entityLiving.worldObj.isRemote)
		{
			float difficultyEffect = 1;
			if (IguanaConfig.damageSlowdownDifficultyScaling)
			{
				int difficulty = event.entityLiving.worldObj.difficultySetting;
				if (!(event.entityLiving instanceof EntityPlayer)) difficulty = (difficulty - 3) * -1;
				difficultyEffect = Math.max(difficulty - 1, 0);
			}
			
			int duration = Math.max(Math.min(Math.round(event.ammount * 5f * difficultyEffect), 255), 0);
			int effect = duration;
			
			/*
			if (event.entityLiving instanceof EntityPlayer) 
				FMLLog.warning("a" + Float.toString(event.ammount) + " d" + duration + " e" + effect + " dif" + Float.toString(difficultyEffect));
			*/
			
			if (duration > 0)
			{
	        	PotionEffect currentEffect = event.entityLiving.getActivePotionEffect(IguanaTweaks.slowdownNew);
	        	if (currentEffect != null) {
	        		duration = Math.min(duration + currentEffect.duration, 255);
	        		effect = Math.min(effect + currentEffect.getAmplifier(), 255);
	    			
	        		/*
	    			if (event.entityLiving instanceof EntityPlayer) 
	    				FMLLog.warning("cd" + currentEffect.duration + " da" + duration + " ce" + currentEffect.getAmplifier() + " ea" + effect);
	    			*/
	        	}
	        	event.entityLiving.addPotionEffect(new PotionEffect(IguanaTweaks.slowdownNew.id, duration, effect, true));
			}
		}
	}
    
	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void LivingDrops(LivingDropsEvent event)
	{
		if (IguanaConfig.restrictedDrops.size() > 0 && event.entity != null && !(event.entity instanceof EntityPlayer))
		{
			Iterator<EntityItem> i = event.drops.iterator();
			while (i.hasNext()) {
			   EntityItem eitem = i.next();

				if (eitem != null)
				{
					if (eitem.getEntityItem() != null)
					{
						ItemStack item = eitem.getEntityItem();
						
						if (IguanaConfig.restrictedDrops.contains(Integer.toString(item.itemID))
								|| IguanaConfig.restrictedDrops.contains(Integer.toString(item.itemID) + ":" + Integer.toString(item.getItemDamage())))
						{
							i.remove();
						}
					}
				}
			}
		}
	}
	
    @SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
    	
		if (IguanaConfig.maxCarryWeight > 0 || IguanaConfig.armorWeight > 0d) 
		{
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.thePlayer;
			
			if (IguanaTweaks.playerWeights.containsKey(player.username))
			{
				WeightValues playerWeightValues = IguanaTweaks.playerWeights.get(player.username);

				if (mc.gameSettings.showDebugInfo && IguanaConfig.addDebugText) {
					event.left.add("");
					event.left.add("Weight: " + Double.toString(playerWeightValues.currentWeight) + " / " + Double.toString(playerWeightValues.maxWeight) 
							+ " (" + Double.toString(playerWeightValues.encumberance) + "%)");
				} else if (!player.isDead && !player.capabilities.isCreativeMode && IguanaConfig.addHudText) {
					String color = "\u00A7f";
					
					String line = "";
					
					if (IguanaConfig.detailedHudText)
					{
						if (playerWeightValues.encumberance >= 30) color = "\u00A74";
						else if (playerWeightValues.encumberance >= 20) color = "\u00A76";
						else if (playerWeightValues.encumberance >= 10) color = "\u00A7e";
						
						line = "Weight: " + Double.toString(Math.round(playerWeightValues.currentWeight)) + " / " + Double.toString(Math.round(playerWeightValues.maxWeight)) 
								+ " (" + Double.toString(playerWeightValues.encumberance) + "%)";
					}
					else
					{
						double totalEncumberance = playerWeightValues.encumberance + playerWeightValues.armour;
						
						if (totalEncumberance >= 30) color = "\u00A74";
						else if (totalEncumberance >= 20) color = "\u00A76";
						else if (totalEncumberance >= 10) color = "\u00A7e";
						
						if (totalEncumberance >= 30) {
							line = "Greatly encumbered";
						} else if (totalEncumberance >= 20) {
							line = "Encumbered";
						} else if (totalEncumberance >= 10) {
							line = "Slightly encumbered";
						} 
					}
					
					if (!line.equals("")) event.right.add(color + line + "\u00A7r");
				}
			}
			
		}
	}
    
    
    @ForgeSubscribe
    public void playerSleep(PlayerSleepInBedEvent event)
    {
        if (event.entityPlayer.worldObj.provider.dimensionId == 0)
        {
            event.result = EnumStatus.OTHER_PROBLEM;
            event.entityPlayer.setSpawnChunk(new ChunkCoordinates(event.x, event.y, event.z), false, 0);
    	    event.entityPlayer.addChatMessage("Your spawn location has been set, enjoy the night");
        }
    }
	
}
