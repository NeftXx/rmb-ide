@echo off

REM Tener agregado Cup en variables de entorno

cd ..
cd ..

set PATH_ORIGEN=src\com\neftxx\analyzer\olc
set PATH_DESTINO=src\com\neftxx\olc

set OPTIONS_PARSER=-destdir %PATH_DESTINO% -locations -interface -parser Parser -symbols Sym
set PARSER_FILE=%PATH_ORIGEN%\Parser.cup

color 0A
echo "Ejecutando CUP"
call cup %OPTIONS_PARSER% %PARSER_FILE%
echo.
pause
exit