@echo off

REM Tener agregado JFlex en variables de entorno

cd ..
cd ..

set PATH_ORIGEN=src\com\neftxx\analyzer\olc
set PATH_DESTINO=src\com\neftxx\olc

set OPTIONS_LEXER=-d %PATH_DESTINO%
set LEXER_FILE=%PATH_ORIGEN%\Lexer.jflex

color 0A
echo "Ejecutando JFLEX"
call jflex %OPTIONS_LEXER% %LEXER_FILE%
echo.
pause
exit