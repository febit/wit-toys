#!/data/data/com.termux/files/usr/bin/sh
set -e

CLASS_PATH=$CLASS_PATH:./:classes.dex
MAIN_CLASS=org.febit.wit.toy.console.Main

dalvikvm -cp "$CLASS_PATH" $MAIN_CLASS
