package top.theillusivec4.veinmining.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class VeinCapability {
	
	 public static final Capability<IVeinCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
	 
	 private VeinCapability() {
		 
	 }
	
}
