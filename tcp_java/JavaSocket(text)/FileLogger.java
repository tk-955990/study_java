import java.io.*; // PrintWriter、FileWriterに必要
import java.util.*; // Collection、Iteratorに必要

class FileLogger implements Logger {

  PrintWriter out; // ログファイル


  public FileLogger(String filename) throws IOException {
	out = new PrintWriter(new FileWriter(filename), true); // ログファイルを作成する
  }

  public synchronized void writeEntry(Collection entry) {
	for (Iterator line = entry.iterator(); line.hasNext();)
	  out.println(line.next());
	out.println();
  }

  public synchronized void writeEntry(String entry) {
	out.println(entry);
	out.println();
  }
}
