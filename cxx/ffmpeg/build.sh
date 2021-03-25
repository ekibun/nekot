#!/bin/bash
starttime=`date +'%Y-%m-%d %H:%M:%S'`

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

ARCS=(
  "android arm"
  "android arm64"
  "android x86"
  "android x86_64"
  "win32 x86_64"
)

for arc in "${ARCS[@]}" 
do
  $DIR/build.ffmpeg.sh $arc
done

endtime=`date +'%Y-%m-%d %H:%M:%S'`
start_seconds=$(date --date="$starttime" +%s)
end_seconds=$(date --date="$endtime" +%s)
echo "build finish in "$((end_seconds-start_seconds))"s"