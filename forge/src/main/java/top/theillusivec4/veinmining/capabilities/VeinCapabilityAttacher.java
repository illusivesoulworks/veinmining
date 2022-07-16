package top.theillusivec4.veinmining.capabilities;

import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VeinCapabilityAttacher {
	
	private static class VeinCapabilityProvider implements ICapabilityProvider {
		
		public static final ResourceLocation IDENTIFIER = new ResourceLocation("veinmining", "veincap");
		
		private final IVeinCapability filler = new VeinCapabilityImpl();
		private final LazyOptional<IVeinCapability> optionalData = LazyOptional.of(() -> filler);
		
	    @Override
	    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
	    	
	    	return VeinCapability.INSTANCE.orEmpty(cap, this.optionalData);
	    	
	    }
	    
	}
	
	@SubscribeEvent
	public static void attach(final AttachCapabilitiesEvent<Entity> evt) {
		
		if (!(evt.getObject() instanceof Player)) {
			return;
		}
	    	
	    final VeinCapabilityProvider provider = new VeinCapabilityProvider();

	    evt.addCapability(VeinCapabilityProvider.IDENTIFIER, provider);
	        
	}
	
	private VeinCapabilityAttacher() {
		
    }
	
}
