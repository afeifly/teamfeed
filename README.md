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

## Debug
1. run springboot project
2. ng server --proxy-config proxy.conf.json


## Some source
```json
[
{
    "title": "云海臻府 3栋",
    "preSellId": 127569,
    "ysProjectId": 29042,
    "fybId": 47820,
    "buildBranch": "二单元"
},

{
  "title": "云海臻府 2栋",
  "preSellId": 127569,
  "ysProjectId": 29042,
  "fybId": 47832,
  "buildBranch": ""
},
  {
    "title": "云上润府 3栋",
    "preSellId": 127650,
    "ysProjectId": 29382,
    "fybId": 48170,
    "buildBranch": "四单元"
  }
  ,
  {
    "title": "云上润府 3栋",
    "preSellId": 127650,
    "ysProjectId": 29382,
    "fybId": 48170,
    "buildBranch": "三单元"
  }
,
  {
    "title": "会展湾雍境 6栋",
    "preSellId": 128686,
    "ysProjectId": 29925,
    "fybId": 48892,
    "buildBranch": ""
  }
,
  {
    "title": "会展湾雍境 9栋",
    "preSellId": 128686,
    "ysProjectId": 29925,
    "fybId": 48895,
    "buildBranch": ""
  }
,
  {
    "title": "凤凰里二期 1栋",
    "preSellId": 128803,
    "ysProjectId": 29990,
    "fybId": 48923,
    "buildBranch": ""
  }
  ,
    {
    "title": "拾悦城楠园 1栋",
    "preSellId": 129340,
    "ysProjectId": 30186,
    "fybId": 49294,
    "buildBranch": ""
  }

]
```
