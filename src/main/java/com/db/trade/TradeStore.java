package com.db.trade;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.db.trade.info.TradeInfo;

public class TradeStore {
	
	private Map<String, Set<TradeInfo>> tradeMap=  new HashMap<>();
	private Map<String, Integer> versionMap=  new HashMap<>();
	
	public void store(TradeInfo trade) {		
		if(Objects.isNull(trade))
			throw new RuntimeException("NULL_OBJECT");
		// check if trade has maturity date less than current date
		if(!trade.getMaturityDate().isBefore(LocalDate.now())) {
			if(LocalDate.now().isEqual(trade.getMaturityDate()))
				trade.markAsExpire();
			Set<TradeInfo> set = tradeMap.get(trade.getTradeId()) ;// Check if store has
			if(set==null) {
				set = new HashSet<TradeInfo>();
				tradeMap.put(trade.getTradeId(), set) ;
			}
			
			if(set.contains(trade)) { // duplicate 
				set.remove(trade);
				set.add(trade); // override existing trade
				versionMap.put(trade.getTradeId(), trade.getVersion());
				tradeMap.put(trade.getTradeId(), set) ;
			}else {
				// pull latest version for given trade id
				Integer version = versionMap.get(trade.getTradeId());
				if(Objects.nonNull(version) && trade.getVersion() < version) {
					throw new RuntimeException("LOWER_VERSION_TRADE");
				}else {
					set.add(trade); 
					versionMap.put(trade.getTradeId(), trade.getVersion());
					tradeMap.put(trade.getTradeId(), set) ;
				}
			}
		}
		
		
	}
	
	public List<TradeInfo>  getStoreSnapshot() {
		List<TradeInfo> list = this.tradeMap.values()
					.stream()
					.flatMap(Collection<TradeInfo>::stream)
					 .collect(Collectors.toList());
		return list;
	}
	


}
