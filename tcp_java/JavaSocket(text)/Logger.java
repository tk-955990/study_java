import java.util.*; // Collectionに必要

public interface Logger {
  public void writeEntry(Collection entry); // 行のリストを書き込む
  public void writeEntry(String entry);     // 1行だけを書き込む
}
