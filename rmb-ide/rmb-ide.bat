@echo off
java --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web --add-exports javafx.graphics/com.sun.javafx.geom=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.text=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.scene.text=ALL-UNNAMED --add-opens javafx.graphics/javafx.scene.text=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.text=ALL-UNNAMED -Xss512m -Xms200m -Xmx1536m -jar rmb-ide.jar
exit
