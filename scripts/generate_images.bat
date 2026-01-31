@echo off
chcp 65001 >nul
echo ========================================
echo CloudItemApp Image Generator
echo ========================================
echo.

REM Check if OpenAI library is installed
python -c "import openai" 2>nul
if errorlevel 1 (
    echo Installing required packages...
    pip install -r requirements.txt
    if errorlevel 1 (
        echo Failed to install packages. Please run: pip install -r requirements.txt
        pause
        exit /b 1
    )
)

REM Check if API key is set
if "%OPENAI_API_KEY%"=="" (
    echo.
    echo WARNING: OPENAI_API_KEY environment variable is not set!
    echo.
    echo Please set your API key first using:
    echo    set OPENAI_API_KEY=sk-your-key-here
    echo.
    echo Or you can enter it when prompted by the script.
    echo.
    pause
)

:menu
echo.
echo ========================================
echo Select an option:
echo ========================================
echo 1. Generate ALL images (235 items)
echo 2. Generate Animals only (40 items)
echo 3. Generate Fruits only (30 items)
echo 4. Generate Vegetables only (30 items)
echo 5. Generate Transportation only (35 items)
echo 6. Generate Daily Items only (40 items)
echo 7. Generate Nature only (20 items)
echo 8. Generate Food and Drink only (25 items)
echo 9. Generate Body Parts only (15 items)
echo 10. Test mode (dry run - no images generated)
echo 0. Exit
echo.
set /p choice="Enter your choice (0-10): "

if "%choice%"=="1" goto all
if "%choice%"=="2" goto animals
if "%choice%"=="3" goto fruits
if "%choice%"=="4" goto vegetables
if "%choice%"=="5" goto transportation
if "%choice%"=="6" goto daily
if "%choice%"=="7" goto nature
if "%choice%"=="8" goto food
if "%choice%"=="9" goto body
if "%choice%"=="10" goto test
if "%choice%"=="0" goto exit
goto menu

:all
echo Generating ALL images...
python generate_images.py
pause
goto menu

:animals
echo Generating Animals...
python generate_images.py --category animals
pause
goto menu

:fruits
echo Generating Fruits...
python generate_images.py --category fruits
pause
goto menu

:vegetables
echo Generating Vegetables...
python generate_images.py --category vegetables
pause
goto menu

:transportation
echo Generating Transportation...
python generate_images.py --category transportation
pause
goto menu

:daily
echo Generating Daily Items...
python generate_images.py --category daily_items
pause
goto menu

:nature
echo Generating Nature...
python generate_images.py --category nature
pause
goto menu

:food
echo Generating Food and Drink...
python generate_images.py --category food_drink
pause
goto menu

:body
echo Generating Body Parts...
python generate_images.py --category body_parts
pause
goto menu

:test
echo Running test mode (dry run)...
python generate_images.py --dry-run
pause
goto menu

:exit
echo Goodbye!
exit /b 0