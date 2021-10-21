package jam.codedred.halloween.tasks;

public abstract class PluginScheduler {
	
	public final int delay;
	private boolean sync = false;
	private int tickCount = 0;
	public PluginScheduler(int delay) {
		this.delay = delay;
	}
	
	public void registerSyncScheduler() {
		MainScheduler.syncSchedulers.add(this);
		sync = true;
	}
	
	public void registerAsyncScheduler() {
		MainScheduler.asyncSchedulers.add(this);
	}
	
	public void stop() {
		if (sync) {
			MainScheduler.syncSchedulers.remove(this);
		} else MainScheduler.asyncSchedulers.remove(this);
	}
	
	public abstract void run();
	
	protected void tick() {
		if (++tickCount >= delay) {
			run();
			tickCount = 0;
		}
	}
	
}