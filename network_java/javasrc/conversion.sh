
#!/bin/bash
for file in /home/tk955990/Downloads/javasrc/*; do
    if [ -f "$file" ]; then  # �t�@�C���ł��邱�Ƃ��m�F
        iconv -f UTF-8 -t SHIFT-JIS "$file" > "$file.tmp"  # �ꎞ�t�@�C���ɕϊ����ʂ�ۑ�
        mv "$file.tmp" "$file"  # ���̃t�@�C�����㏑��
    fi
done

