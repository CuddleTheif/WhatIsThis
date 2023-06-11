set background="rgba(0, 0, 0, 0.09)"
set topdir=..

set dir=block_font\
set font="..\block_font.ttf"
set width=76
set height=255
set pointsize=96
set color="rgb(255, 255, 255)"
set stroke="rgba(0, 0, 0, 0.2)"

cd %dir%
set chars=a b c d e f g h i j k l m n o p q r s t u v w x y z 1 2 3 4 5 6 7 8 9 0 ` ~ ! @ # $ "%%" "&" "(" ")" "-" "_" "=" "+" "[" "]" "{" "}" ";" "'" "." ","
For %%X in (%chars%) do (convert -font %font% -pointsize %pointsize% -size %width%x%height% -background %background% -stroke %stroke% -fill %color% label:%%X "%%~X.png")
set chars=A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
For %%X in (%chars%) do (convert -font %font% -pointsize %pointsize% -size %width%x%height% -background %background% -stroke %stroke% -fill %color% label:%%X "up_%%~X.png")
convert -font %font% -pointsize %pointsize% -size %width%x%height% -background %background% -stroke "rgba(0, 0, 0, 0)" -fill "rgba(0, 0, 0, 0)" label:a "space.png"
convert -font %font% -pointsize %pointsize% -size %width%x%height% -background %background% -stroke %stroke% -fill %color% label:? "blank.png"
set chars=a b c d e f g h i j k l m n o p q r s t u v w x y z
For %%X in (%chars%) do (rename "up_%%~X.png" "up_%%~X.png")
cd %topdir%

set dir=resource_font\
set font="..\resource_font.ttf"
set width=76
set height=122
set pointsize=96
set color="rgb(138,43,226)"
set stroke="rgba(108,13,196, 0.2)"

cd %dir%
set chars=a b c d e f g h i j k l m n o p q r s t u v w x y z 1 2 3 4 5 6 7 8 9 0 ` ~ ! @ # $ "%%" "&" "(" ")" "-" "_" "=" "+" "[" "]" "{" "}" ";" "'" "." ","
For %%X in (%chars%) do (convert -font %font% -pointsize %pointsize% -size %width%x%height% -background %background% -stroke %stroke% -fill %color% label:%%X "%%~X.png")
set chars=A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
For %%X in (%chars%) do (convert -font %font% -pointsize %pointsize% -size %width%x%height% -background %background% -stroke %stroke% -fill %color% label:%%X "up_%%~X.png")
convert -font %font% -pointsize %pointsize% -size %width%x%height% -background %background% -stroke "rgba(0, 0, 0, 0)" -fill "rgba(0, 0, 0, 0)" label:a "space.png"
convert -font %font% -pointsize %pointsize% -size %width%x%height% -background %background% -stroke %stroke% -fill %color% label:? "blank.png"
set chars=a b c d e f g h i j k l m n o p q r s t u v w x y z
For %%X in (%chars%) do (rename "up_%%~X.png" "up_%%~X.png")
cd %topdir%

PAUSE
EXIT