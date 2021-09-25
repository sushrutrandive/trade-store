package com.db.trade;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import com.db.trade.info.TradeInfo;

public class TradeProducer implements Producer<TradeInfo>, Runnable{
	
	private BlockingQueue<TradeInfo> tradeQueue;
	private List<TradeInfo> tradeList;
	
	public TradeProducer(BlockingQueue<TradeInfo> queue, List<TradeInfo> tradeList) {
		this.tradeQueue = queue;
		this.tradeList = tradeList;
	}
	
	@Override
	public void run() {
		for(TradeInfo tradeInfo : this.tradeList ) {
			this.publish(tradeInfo);
		}		
	}
	
	@Override
	public void publish(TradeInfo t) {

		if(Objects.nonNull(t)) {
			try {
				
				this.tradeQueue.put(t);
				Thread.currentThread().sleep(500); // Sleep for 0.5 sec to mimic async operation 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
