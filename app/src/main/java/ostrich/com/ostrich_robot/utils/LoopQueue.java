package ostrich.com.ostrich_robot.utils;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class LoopQueue<T> {
	
	private LinkedBlockingQueue<T> commands;
	private volatile boolean runningLoop;
	private volatile T current;
	private Executor executor;
	
	public LoopQueue(int capacity){
		commands = new LinkedBlockingQueue<>(capacity);
		executor = Executors.newSingleThreadExecutor();
	}

	public void startLoop() {
		if(!runningLoop){
			runningLoop = true;
			executor.execute(loop);
		}
	}
	
	public void cancelLoop() {
		if (runningLoop) {
			runningLoop = false;
		}
	}
	
	public void remove(T command){
		if(command != null && commands.contains(command)){
			try {
				commands.take();
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void removeAll(){
		for(T command : commands){
			remove(command);
		}
	}
	
	public void add(LinkedList<T> list){
		if(commands != null && commands.size() > 0){
			for(T command : list){
				add(command);
			}
		}
	}
	
	public void add(T object){
		if(object != null){
			try {
				commands.put(object);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getSize(){
		return commands.size();
	}
	
	private Runnable loop = new Runnable() {
		public void run() {
			while(runningLoop){
					try {
						current = commands.take();
						if(current!=null){
							loop(current);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		}
	};
	
	public abstract void loop(T object);
}
