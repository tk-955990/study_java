import java.io.*; // PrintWriter�AFileWriter�ɕK�v
import java.util.*; // Collection�AIterator�ɕK�v

class FileLogger implements Logger {

  PrintWriter out; // ���O�t�@�C��


  public FileLogger(String filename) throws IOException {
	out = new PrintWriter(new FileWriter(filename), true); // ���O�t�@�C�����쐬����
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
