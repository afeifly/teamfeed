 docker run --name my-postgres -e POSTGRES_PASSWORD=xuffei -p 15432:5432 -d postgres:15
docker exec -it my-postgres psql -U postgres -d postgres -h 127.0.0.1 -c "create DATABASE selffeed;"


 ng build --configuration production


 src:
https://chinadigitaltimes.net/chinese/feed
https://rsshub.exmm.top/t66y/7/


```bash
echo "build web application"

cd src/main/angular \
  && yarn \
  && ng build --configuration production --output-path ../resources/static \
  && cd ../../../

echo "copy web application to resources"

cd src/main/resources/static \
  && mkdir error \
  && cp index.html error/404.html \
  && cd ../../../../


```
