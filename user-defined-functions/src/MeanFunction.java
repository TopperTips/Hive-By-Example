import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.hive.ql.metadata.HiveException;
/**
 * 
 * @author Bigdata Training
 * This is a sample program for User Defined Aggregate Function
 */
public class MeanFunction extends UDAF {

	public static class MeanUDAFEvaluator implements UDAFEvaluator {

		/**
		 * Use Column class to serialize intermediate computation This is our
		 * groupByColumn
		 */
		public static class Column {
			double sum = 0;
			int count = 0;
		}

		private Column col = null;

		public MeanUDAFEvaluator() {
			super();
			init();
		}

		// A - Initalize evaluator - indicating that no values have been
		// aggregated yet.

		public void init() {
			col = new Column();
		}

		// B- Iterate every time there is a new value to be aggregated
		public boolean iterate(double value) throws HiveException {
			if (col == null)
				throw new HiveException("Item is not initialized");
			col.sum = col.sum + value;
			col.count = col.count + 1;
			return true;
		}

		// C - Called when Hive wants partially aggregated results.
		public Column terminatePartial() {
			return col;
		}

		// D - Called when Hive decides to combine one partial aggregation with
		// another
		public boolean merge(Column other) {
			if (other == null) {
				return true;
			}
			col.sum += other.sum;
			col.count += other.count;
			return true;
		}

		// E - Called when the final result of the aggregation needed.
		public double terminate() {
			return col.sum / col.count;
		}

	}
}