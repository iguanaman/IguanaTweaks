package iguanaman.iguanatweaks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class IguanaConfig {

	public static boolean damageSlowdownDifficultyScaling;
    public static boolean addDebugText;
    public static boolean addHudText;
    public static boolean detailedHudText;
    public static boolean lessObviousSilverfish;
    public static boolean alterPoison;
    public static int blockStackSizeDividerMin;
    public static int blockStackSizeDividerMax;
    public static int itemStackSizeDivider;
    public static int maxCarryWeight;
    public static int torchesPerCoal;
	public static double armorWeight;
	public static int newSlowdownPotionId;
	public static int damageSlowdownDuration;
	public static int terrainSlowdownPercentage;
	public static int miningExhaustionPercentage;
    public static List<String> restrictedDrops = new ArrayList<String>();
    public static double hardnessMultiplier;
    public static boolean hardnessBlockListIsWhitelist;
    public static List<Integer> hardnessBlockList = new ArrayList<Integer>();
    
	public static void Init(File file)
	{
        Configuration config = new Configuration(file);
        config.load();
        
        // modules
		ConfigCategory modulesCategory = config.getCategory("modules");
        
        Property damageSlowdownDifficultyScalingProperty = config.get("modules", "damageSlowdownDifficultyScaling", true);
        damageSlowdownDifficultyScalingProperty.comment = "Is the duration of the slowdown dependant on difficulty?";
        damageSlowdownDifficultyScaling = damageSlowdownDifficultyScalingProperty.getBoolean(true);
        
        Property lessObviousSilverfishProperty = config.get("modules", "lessObviousSilverfish", true);
        lessObviousSilverfishProperty.comment = "Silverfish blocks are less easy to spot";
        lessObviousSilverfish = lessObviousSilverfishProperty.getBoolean(true);
        
        Property alterPoisonProperty = config.get("modules", "alterPoison", true);
        alterPoisonProperty.comment = "Poison causes damage less often but is now deadly";
        alterPoison = alterPoisonProperty.getBoolean(true);
        
        Property addDebugTextProperty = config.get("modules", "addDebugText", true);
        addDebugTextProperty.comment = "Shows weight text in the debug (F3) details";
        addDebugText = addDebugTextProperty.getBoolean(true);
        
        Property addHudTextProperty = config.get("modules", "addHudText", true);
        addHudTextProperty.comment = "Shows weight text on the HUD when carrying too much";
        addHudText = addHudTextProperty.getBoolean(true);
        
        Property detailedHudTextProperty = config.get("modules", "detailedHudText", false);
        detailedHudTextProperty.comment = "Weight text on the HUD will be more detailed, showing numbers";
        detailedHudText = detailedHudTextProperty.getBoolean(false);
        
		ConfigCategory modifiersCategory = config.getCategory("modifiers");
        
        Property armorWeightProperty = config.get("modifiers", "armorWeight", 0.5d);
        armorWeightProperty.comment = "Percentage of slowdown for each point (half-shield) of armor (0 to disable)";
        armorWeight = armorWeightProperty.getDouble(0.5d);
        if (armorWeight < 0d) {
        	armorWeight = 0d;
        	armorWeightProperty.set(armorWeight);
        	}
        
        Property itemStackSizeDividerProperty = config.get("modifiers", "itemStackSizeDivider", 2);
        itemStackSizeDividerProperty.comment = "Max stack size divider";
        itemStackSizeDivider = itemStackSizeDividerProperty.getInt(2);
        
        if (itemStackSizeDivider < 1) {
        	itemStackSizeDivider = 1;
        	itemStackSizeDividerProperty.set(itemStackSizeDivider);
        	}
        
        Property blockStackSizeDividerMaxProperty = config.get("modifiers", "blockStackSizeDividerMax", 8);
        blockStackSizeDividerMaxProperty.comment = "Max stack size divider";
        blockStackSizeDividerMax = blockStackSizeDividerMaxProperty.getInt(8);
        
        if (blockStackSizeDividerMax < 1) {
        	blockStackSizeDividerMax = 1;
        	blockStackSizeDividerMaxProperty.set(blockStackSizeDividerMax);
        	}
        
        Property blockStackSizeDividerMinProperty = config.get("modifiers", "blockStackSizeDividerMin", 2);
        blockStackSizeDividerMinProperty.comment = "Min stack size divider";
        blockStackSizeDividerMin = blockStackSizeDividerMinProperty.getInt(2);
        
        if (blockStackSizeDividerMin < 1) {
        	blockStackSizeDividerMin = 1;
        	blockStackSizeDividerMinProperty.set(blockStackSizeDividerMin);
        	}
        
        if (blockStackSizeDividerMin > blockStackSizeDividerMax) {
        	blockStackSizeDividerMin = blockStackSizeDividerMax;
        	blockStackSizeDividerMinProperty.set(blockStackSizeDividerMin);
        	}
        
        Property torchesPerCoalProperty = config.get("modifiers", "torchesPerCoal", 1);
        torchesPerCoalProperty.comment = "Torches given from torch recipe";
        torchesPerCoal = torchesPerCoalProperty.getInt(1);
        
        if (torchesPerCoal < 1) {
        	torchesPerCoal = 1;
        	torchesPerCoalProperty.set(torchesPerCoal);
        	}
        
        Property maxCarryWeightProperty = config.get("modifiers", "maxCarryWeight", 512);
        maxCarryWeightProperty.comment = "Maximum carry weight (default 256) (Set 0 to disable)";
        maxCarryWeight = maxCarryWeightProperty.getInt(515);
        
        if (maxCarryWeight < 0) {
        	maxCarryWeight = 0;
        	maxCarryWeightProperty.set(maxCarryWeight);
        	}
        
        Property damageSlowdownDurationProperty = config.get("modifiers", "damageSlowdownDuration", 5);
        damageSlowdownDurationProperty.comment = "Number of ticks each heart of damage slows you down for (default 5) (Set 0 to disable)";
        damageSlowdownDuration = damageSlowdownDurationProperty.getInt(5);
        
        if (damageSlowdownDuration < 0) {
        	damageSlowdownDuration = 0;
        	damageSlowdownDurationProperty.set(damageSlowdownDuration);
        	}
        
        Property terrainSlowdownPercentageProperty = config.get("modifiers", "terrainSlowdownPercentage", 100);
        terrainSlowdownPercentageProperty.comment = "Amount that terrain affects movement speed (Set 0 to disable)";
        terrainSlowdownPercentage = terrainSlowdownPercentageProperty.getInt(100);
        
        if (terrainSlowdownPercentage < 0) {
        	terrainSlowdownPercentage = 0;
        	terrainSlowdownPercentageProperty.set(terrainSlowdownPercentage);
        	}
        
        Property miningExhaustionPercentageProperty = config.get("modifiers", "miningExhaustionPercentage", 100);
        miningExhaustionPercentageProperty.comment = "Modifier on the exhaustion given when breaking blocks (0 disables feature)";
        miningExhaustionPercentage = miningExhaustionPercentageProperty.getInt(100);
        
        if (miningExhaustionPercentage < 0) {
        	miningExhaustionPercentage = 0;
        	terrainSlowdownPercentageProperty.set(miningExhaustionPercentage);
        	}
        
        
        Property newSlowdownPotionIdProperty = config.get("modifiers", "newSlowdownPotionId", 29);
        newSlowdownPotionIdProperty.comment = "Potion ID for the new slowdown effect (maximum 31)";
        newSlowdownPotionId = newSlowdownPotionIdProperty.getInt(29);
        
        if (newSlowdownPotionId < 0) {
        	newSlowdownPotionId = 0;
        	newSlowdownPotionIdProperty.set(newSlowdownPotionId);
        	}

        
        // modules
		ConfigCategory hardnessCategory = config.getCategory("hardness");
        
        Property hardnessMultiplierProperty = config.get("hardness", "hardnessMultiplier", 4d);
        hardnessMultiplierProperty.comment = "Multiplier applied to the hardness of blocks (set to 1 to disable feature)";
        hardnessMultiplier = hardnessMultiplierProperty.getDouble(4d);
        
        if (hardnessMultiplier < 0d) {
        	hardnessMultiplier = 1d;
        	hardnessMultiplierProperty.set(hardnessMultiplier);
        	}
        
        Property hardnessBlockListIsWhitelistProperty = config.get("hardness", "hardnessBlockListIsWhitelist", false);
        hardnessBlockListIsWhitelistProperty.comment = "Whether hardness multiplier only affects blocks on the list (true) or if all blocks are affect except those on the list (false)";
        hardnessBlockListIsWhitelist = hardnessBlockListIsWhitelistProperty.getBoolean(false);
		
        Property hardnessBlockListProperty = config.get("hardness", "hardnessBlockList", new int[] {});
        hardnessBlockListProperty.comment = "Block ids (each on seperate line) for the hardness whitelist/blacklist";
        for (int i : hardnessBlockListProperty.getIntList()) hardnessBlockList.add(i);
        
		
		// restrictions
		ConfigCategory restrictionsCategory = config.getCategory("restrictions");

        Property restrictedDropsProperty = config.get("restrictions", "restrictedDrops", new String[] {});
        restrictedDropsProperty.comment = "List of items/blocks to restrict from mob drops (separated by new line, format id:meta)";
        for (String i : restrictedDropsProperty.getStringList()) restrictedDrops.add(i);
        

        config.save();
	}
}
