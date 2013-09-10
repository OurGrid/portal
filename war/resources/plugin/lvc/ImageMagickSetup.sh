#! /bin/sh
tar xvzf ImageMagick-6.7.6-5.tar.gz
mkdir imagick
export MAGICKDIR="$PWD/imagick"
cd ImageMagick-6.7.6-5
./configure --prefix=$MAGICKDIR
make
make install
cd ../
$MAGICKDIR/bin/mogrify -paint 20 $1
