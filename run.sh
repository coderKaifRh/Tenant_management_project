#!/usr/bin/env bash

echo "==================================================="
echo "    TAK Limited - Tenant Management System"
echo "==================================================="
echo ""

if ! command -v java &> /dev/null; then
    echo "[ERROR] Java is not detected in your PATH."
    echo "Please install JDK 21 or higher."
    exit 1
fi

echo "[OK] Java detected:"
java -version
echo ""

echo "Launching application via Maven Wrapper..."
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
chmod +x "$SCRIPT_DIR/mvnw"
"$SCRIPT_DIR/mvnw" javafx:run -f "$SCRIPT_DIR/pom.xml"
