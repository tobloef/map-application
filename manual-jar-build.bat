set PATH_TO_FX=C:\Program Files\javafx-sdk-11.0.2\lib

rmdir /s /q out

dir /s /b src\main\java\bfst19\osmdrawing\*.java > sources.txt
javac --module-path "%PATH_TO_FX%" --add-modules=javafx.controls,javafx.fxml -d out @sources.txt
del sources.txt

cd out
jar xf "%PATH_TO_FX%\javafx.base.jar"
jar xf "%PATH_TO_FX%\javafx.graphics.jar"
jar xf "%PATH_TO_FX%\javafx.controls.jar"
jar xf "%PATH_TO_FX%\javafx.fxml.jar"
cd ..
copy "%PATH_TO_FX%\..\bin\prism*.dll" out
copy "%PATH_TO_FX%\..\bin\javafx*.dll" out
copy "%PATH_TO_FX%\..\bin\glass.dll" out
copy "%PATH_TO_FX%\..\bin\decora_sse.dll" out
echo D|xcopy /s src\main\resources\bfst19\osmdrawing out
del out\META-INF\MANIFEST.MF
del out\module-info.class

mkdir build\libs
jar --create --file=build/libs/BFST19.jar --main-class=bfst19.danmarkskort.Launcher -C out .

java -jar build/libs/BFST19.jar