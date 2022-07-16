package top.theillusivec4.veinmining.capabilities;

public class VeinCapabilityImpl implements IVeinCapability {
	
	private boolean b;
	
	@Override
	public boolean isVeining() {
		return this.b;
	}
	
	@Override
	public void setVeining(boolean b) {
		this.b = b;
	}
	
}
