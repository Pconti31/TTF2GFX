#!/bin/sh
SCRIPT_NAME=$(basename "$0")
APP_NAME=${SCRIPT_NAME%.sh}

DIR="${0%/*}"



"$DIR/bin/java" $CDS_JVM_OPTS  -p "$DIR/../app" -m TTF2GFX/ttf2gfx.Ttf2GfxApp  "$@"
