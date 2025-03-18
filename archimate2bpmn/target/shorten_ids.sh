#!/bin/bash

# Prüfe, ob eine Datei als Argument übergeben wurde
if [ $# -ne 1 ]; then
    echo "Nutzung: $0 datei.xml"
    exit 1
fi

FILE="$1"

# Prüfe, ob die Datei existiert
if [ ! -f "$FILE" ]; then
    echo "Fehler: Datei $FILE nicht gefunden!"
    exit 1
fi

# IDs kürzen und direkt die Originaldatei überschreiben
sed -E -i 's/(id-[a-fA-F0-9]{6})[a-fA-F0-9]{26}/\1/g' "$FILE"

# Erfolgsmeldung
echo "IDs wurden erfolgreich gekürzt in: $FILE"
