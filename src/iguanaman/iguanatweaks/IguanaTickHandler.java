package iguanaman.iguanatweaks;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.Item;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class IguanaTickHandler implements ITickHandler {

	public IguanaTickHandler() {
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Iterator<Map.Entry<UUID, EntityData>> i = IguanaTweaks.entityDataMap.entrySet().iterator();
		while (i.hasNext())
		{
			Map.Entry<UUID, EntityData> entry = i.next();
        	EntityData data = entry.getValue();
        	if (++data.age >= IguanaConfig.tickRateEntityUpdate) i.remove();
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return null;
	}

}
