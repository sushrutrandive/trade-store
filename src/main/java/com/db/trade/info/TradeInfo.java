package com.db.trade.info;

import java.time.LocalDate;




public class TradeInfo {
	
	private String tradeId;
	private int version;
	private String counterPartyId;
	private String bookId;
	private LocalDate createdDate;
	private LocalDate maturityDate;
	private boolean expired;
	
	public static Builder builder() {		
		return new Builder();
		
	}
	
	
	
	private TradeInfo(String tradeId, int version, String counterPartyId, String bookId, LocalDate createdDate,
			LocalDate maturityDate,boolean expired) {
		super();
		this.tradeId = tradeId;
		this.version = version;
		this.counterPartyId = counterPartyId;
		this.bookId = bookId;
		this.createdDate = createdDate;
		this.maturityDate = maturityDate;
		this.expired = expired;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tradeId == null) ? 0 : tradeId.hashCode());
		result = prime * result + version;
		return result;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeInfo other = (TradeInfo) obj;
		if (tradeId == null) {
			if (other.tradeId != null)
				return false;
		} else if (!tradeId.equals(other.tradeId))
			return false;
		if (version != other.version)
			return false;
		return true;
	}
	
	
	
	
	@Override
	public String toString() {
		return "TradeInfo [tradeId=" + tradeId + ", version=" + version + ", counterPartyId=" + counterPartyId
				+ ", bookId=" + bookId + ", createdDate=" + createdDate + ", maturityDate=" + maturityDate
				+ ", expired=" + expired + "]";
	}


	public String getTradeId() {
		return tradeId;
	}


	public int getVersion() {
		return version;
	}


	public String getCounterPartyId() {
		return counterPartyId;
	}


	public String getBookId() {
		return bookId;
	}


	public LocalDate getCreatedDate() {
		return createdDate;
	}


	public LocalDate getMaturityDate() {
		return maturityDate;
	}
	
	public void markAsExpire() {
		this.expired = true;
	}




	public boolean isExpired() {
		return expired;
	}




	public static class Builder{
		private String tradeId;
		private int version;
		private String counterPartyId;
		private String bookId;
		private LocalDate createdDate;
		private LocalDate maturityDate;
		private boolean expired;
		
		
		public Builder withTradeId(String tradeId) {
			this.tradeId = tradeId;
			return this;
		}

		public Builder withVersion(int version) {
			this.version = version;
			return this;
		}

		public Builder withCounterPartyId(String counterPartyId) {
			this.counterPartyId = counterPartyId;
			return this;
		}

		public Builder withBookId(String bookId) {
			this.bookId = bookId;
			return this;
		}

		public Builder withCreatedDate(LocalDate createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public Builder withMaturityDate(LocalDate maturityDate) {
			this.maturityDate = maturityDate;
			return this;
		}
		public Builder withExpired(boolean expired) {
			this.expired = expired;
			return this;
		}
		public TradeInfo build() {
			return new TradeInfo(tradeId, version, counterPartyId, bookId, createdDate, maturityDate,expired);
		}
	}
	
	
	

}
