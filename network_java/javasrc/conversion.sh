
#!/bin/bash
for file in /home/tk955990/Downloads/javasrc/*; do
    if [ -f "$file" ]; then  # ファイルであることを確認
        iconv -f UTF-8 -t SHIFT-JIS "$file" > "$file.tmp"  # 一時ファイルに変換結果を保存
        mv "$file.tmp" "$file"  # 元のファイルを上書き
    fi
done

