package distributed;

public class SimulationControlJSON {
	
	public static class RateJSON {
		private Float rate;
		
		public RateJSON(float f) {
			rate = f;
		}
		
		public Float getRate() {
			return rate;
		}
	}
	
	public static class CountJSON {

		private Integer count;
		
		public CountJSON(int count) {
			this.count = count;
		}
		
		public Integer getCount() {
			return this.count;
		}
	}
}
