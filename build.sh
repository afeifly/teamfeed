echo "build web application"
 
cd src/main/agl-web \
  && yarn \
  && ng build --configuration production --output-path ../resources/static \
  && cd ../../../

echo "copy web application to resources"

cd src/main/resources/static \
  && mkdir error \
  && cp index.html error/404.html \
  && cd ../../../../

mvn clean install -Dlicense.skip=true -DskipTests -e
