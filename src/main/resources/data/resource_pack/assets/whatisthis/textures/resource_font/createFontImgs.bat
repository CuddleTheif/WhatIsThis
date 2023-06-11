set chars=a b c d e f g h i j k l m n o p q r s t u v w x y z 1 2 3 4 5 6 7 8 9 0 ` ~ ! @ # $ "%%" "^" "&" "(" ")" "-" "_" "=" "+" "[" "]" "{" "}" ";" "'" "," "."
For %%X in (%chars%) do (convert -font font.ttf -pointsize 32 -size 30x40 -background "rgba(0, 0, 0, 0.01)" -stroke "rgb(0, 0, 0)" -fill "rgb(255, 255, 255)" label:%%X %%X.png)
set chars=A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
For %%X in (%chars%) do (convert -font font.ttf -pointsize 32 -size 30x40 -background "rgba(0, 0, 0, 0.01)" -stroke "rgb(0, 0, 0)" -fill "rgb(255, 255, 255)" label:%%X up_%%X.png)
PAUSE
EXIT