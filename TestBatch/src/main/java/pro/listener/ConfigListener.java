package pro.listener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigListener implements JobExecutionListener {
	String previous = System.getProperty("user.dir") + "/src/main/resources/input/previous.csv";
	String current = System.getProperty("user.dir") + "/src/main/resources/input/current.csv";
	String currentNew = System.getProperty("user.dir") + "/src/main/resources/input/currentNew.csv";

	@Override
	public void beforeJob(JobExecution jobExecution) {
		ArrayList<String> prevAl = new ArrayList<>();
		ArrayList<String> currAl = new ArrayList<>();

		String prevCsvRow;
		try {
			BufferedReader prevCSV = new BufferedReader(new FileReader(previous));
			prevCsvRow = prevCSV.readLine();
			while (prevCsvRow != null) {
				String[] rowArray = prevCsvRow.split(",");
				for (String item1 : rowArray) {
					prevAl.add(item1);
				}

				prevCsvRow = prevCSV.readLine();
			}

			prevCSV.close();

			BufferedReader currCSV = new BufferedReader(new FileReader(current));
			String currCsvRow = currCSV.readLine();
			while (currCsvRow != null) {
				String[] dataArray2 = currCsvRow.split(",");
				for (String item2 : dataArray2) {
					currAl.add(item2);

				}
				currCsvRow = currCSV.readLine();
			}
			currCSV.close();

			for (String sA : prevAl) {
				currAl.remove(sA);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int size = currAl.size();
		try {
			FileWriter writer = new FileWriter(currentNew, false);
			writer.append("gender");
			writer.append(",");
			writer.append("mark");
			writer.append(",");
			writer.append("class");
			writer.append(",");
			writer.append("name");
			writer.append(",");
			writer.append("id");
			writer.append('\n');
			while (size != 0) {
				size--;
				writer.append("" + currAl.get(size));
				if (size % 5 == 0)
					writer.append('\n');
				else
					writer.append(",");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {

	}

}
