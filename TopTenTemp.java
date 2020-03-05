/*
* TopTenTemp calculates the ten highest temperatures
* in the dataset by ZIP code, year, and month. 
*/

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TopTenTemp extends Configured implements Tool {

	public static class MyMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
		private static int STATION_ID = 0;
		private static int ZIPCODE = 1;
		private static int LAT = 2;
		private static int LONG = 3;
		private static int TEMP = 4;
		private static int PERCIP = 5;
		private static int HUMID = 6;
		private static int YEAR = 7;
		private static int MONTH = 8;

		TreeMap<Double, String> localTopTen;

		protected void setup(Context context) throws IOException, InterruptedException {
			localTopTen = new TreeMap<Double, String>(Collections.reverseOrder());
		}

		public void map(LongWritable inputKey, Text inputValue, Context context)
				throws IOException, InterruptedException {

			String[] element = new String[9];
			StringTokenizer st = new StringTokenizer(inputValue.toString());

			for (int i = 0; i < element.length; i++) {
				if (st.hasMoreTokens()) {
					element[i] = st.nextToken();
					if (i == 0 && element[i].equals("StationID"))
						return;
				}
			}

			String entryInfo = element[ZIPCODE] + "\t" + element[YEAR] + "\t" + element[MONTH];
			double entryTemp = Double.parseDouble(element[TEMP]);

			localTopTen.put(entryTemp, entryInfo);

			if (localTopTen.size() > 10) {
				localTopTen.remove(localTopTen.lastKey());
			}

		}// end map

		protected void cleanup(Context context) throws IOException, InterruptedException {
			for (Entry<Double, String> entry : localTopTen.entrySet()) {
				double temp = entry.getKey();
				String info = entry.getValue();
				context.write(new Text(info), new DoubleWritable(temp));
			}
		}

	}// end MyMapper

	public static class MyReducer extends Reducer<Text, DoubleWritable, Text, Text> {
		TreeMap<Double, String> globalTopTen;

		protected void setup(Context context) throws IOException, InterruptedException {
			globalTopTen = new TreeMap<Double, String>(Collections.reverseOrder());
		}

		public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
				throws IOException, InterruptedException {
			double maxVal = -100;

			for (DoubleWritable val : values) {
				if (maxVal < Double.parseDouble(val.toString())) {
					maxVal = Double.parseDouble(val.toString());
				}
			}

			globalTopTen.put(maxVal, key.toString());

			if (globalTopTen.size() > 10) {
				globalTopTen.remove(globalTopTen.lastKey());
			}

		}
		// end reduce

		protected void cleanup(Context context) throws IOException, InterruptedException {
			for (Entry<Double, String> entry : globalTopTen.entrySet()) {
				double temp = entry.getKey();
				String info = entry.getValue();
				context.write(new Text(info), new Text(Double.toString(temp)));
			}
		}

	}// end MyReducer

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new TopTenTemp(), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println(
					"Usage: TopTenTemp.jar TopTenTemp <input directory> <ouput directory> <number of reduces>");
			System.out.println("args length incorrect, length: " + args.length);
			return -1;
		}

		int numReduces;

		Path inputPath = new Path(args[0]);
		Path outputPath = new Path(args[1]);

		try {
			numReduces = new Integer(args[2]);
			System.out.println("number reducers set to: " + numReduces);
		} catch (NumberFormatException e) {
			System.out.println(
					"Usage: TopTenTemp.jar TopTenTemp <input directory> <ouput directory> <number of reduces>");
			System.out.println("Error: number of reduces not a type integer");
			return -1;
		}

		Configuration conf = new Configuration();

		FileSystem fs = FileSystem.get(conf);

		if (!fs.exists(inputPath)) {
			System.out.println(
					"Usage: TopTenTemp.jar TopTenTemp <input directory> <ouput directory> <number of reduces>");
			System.out.println("Error: Input Directory Does Not Exist");
			System.out.println("Invalid input Path: " + inputPath.toString());
			return -1;
		}

		if (fs.exists(outputPath)) {
			System.out.println(
					"Usage: TopTenTemp.jar TopTenTemp <input directory> <ouput directory> <number of reduces>");
			System.out.println("Error: Output Directory Already Exists");
			System.out.println("Please delete or specifiy different output directory");
			return -1;
		}

		Job job = new Job(conf, "Top Ten Temperatures by ZIP. Year, and Month");

		job.setNumReduceTasks(numReduces);
		job.setJarByClass(TopTenTemp.class);

		// sets mapper class
		job.setMapperClass(MyMapper.class);

		// sets map output key/value types
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(DoubleWritable.class);

		// Set Reducer class
		job.setReducerClass(MyReducer.class);

		// specify output types
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// sets Input format
		job.setInputFormatClass(TextInputFormat.class);

		// specify input and output DIRECTORIES (not files)
		FileInputFormat.addInputPath(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);

		job.waitForCompletion(true);
		return 0;
	}

}
