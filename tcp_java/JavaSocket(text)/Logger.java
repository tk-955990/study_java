import java.util.*; // Collection��ɬ��

public interface Logger {
  public void writeEntry(Collection entry); // �ԤΥꥹ�Ȥ�񤭹���
  public void writeEntry(String entry);     // 1�Ԥ�����񤭹���
}
