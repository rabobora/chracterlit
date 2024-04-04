# AWS S3/Lambda 메뉴얼

### 1. S3 버킷 생성

- aws s3서비스에서 로그인 후 버킷을 생성
- 생성된 버킷 안에 2개의 폴더를생성
    
    images : 이미지 업로드용 폴더
    
    resized-images : 썸네일 자동 생성 이미지 저장용 폴더
    
- AWS 콘솔의 IAM으로 이동
- 액세스 관리 - 역할(Role) 메뉴로 이동하여 "**역할 만들기**"를 클릭.
    
    Role Name : S3_Image_Resize
    

권한 정책에는 다음 3가지를 추가

- AmazonS3FullAccess (S3 Access 권한)
- AWSLambdaBasicExecutionRole (Clowd Watch 로그 작성 권한)
- AWSLambda_FullAccess (Lambda Access 권한)

### 2. 리사이즈용 함수 작성(python)

```python
import boto3
import os
import sys
import uuid
from urllib.parse import unquote_plus
from PIL import Image
import mimetypes  

s3_client = boto3.client('s3')

def resize_image(image_path, resized_path):
    base_width = 300
    with Image.open(image_path) as image:
        w_percent = (base_width / float(image.size[0]))
        h_size = int((float(image.size[1]) * float(w_percent)))
        image = image.resize((base_width, h_size), Image.ANTIALIAS)
        image.save(resized_path)

def get_content_type(file_name):
    mime_type, _ = mimetypes.guess_type(file_name)
    if mime_type is None:
        mime_type = 'application/octet-stream'
    return mime_type

def lambda_handler(event, context):
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = unquote_plus(record['s3']['object']['key'])
        tmpkey = key.replace('/', '')
        download_path = '/tmp/{}{}'.format(uuid.uuid4(), tmpkey)
        upload_path = '/tmp/resized-{}'.format(tmpkey)

        s3_client.download_file(bucket, key, download_path)
        resize_image(download_path, upload_path)

        content_type = get_content_type(key)  
        s3_client.upload_file(upload_path, bucket, 'resized-{}'.format(key),
                              ExtraArgs={'ContentType': content_type, 'Metadata': {}})

```

- pillow 라이브러리를 package 폴더에 설치

```python
pip install --target ./package pillow
```

- template.yaml 파일을 루트폴더에 생성

```python
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Resources:
  CreateThumbnail:
    Type: AWS::Serverless::Function
    Properties:
      Handler: lambda_function.lambda_handler
      Runtime: python3.9
      Timeout: 10
      Policies: AWSLambdaExecute
      Events:
        CreateThumbnailEvent:
          Type: S3
          Properties:
            Bucket: !Ref SrcBucket
            Events: s3:ObjectCreated:*

  SrcBucket:
    Type: AWS::S3::Bucket
```

- requirements.txt 파일을 루트폴더에 생성

```python
Pillow == 9.5.0

```

### 3. 프로젝트를 sam을 활용해 빌드,배포

- 도커 설치 및 실행
- aws cli 설치:[https://docs.aws.amazon.com/ko_kr/cli/latest/userguide/getting-started-install.html](https://docs.aws.amazon.com/ko_kr/cli/latest/userguide/getting-started-install.html)
- sam cli 설치: [https://docs.aws.amazon.com/ko_kr/serverless-application-model/latest/developerguide/install-sam-cli.html](https://docs.aws.amazon.com/ko_kr/serverless-application-model/latest/developerguide/install-sam-cli.html)

```python
// 빌드
sam build --use-container

// 배포
sam deploy --guided
```

### 4. AWS Lambda 서비스에서 트리거 설정

- 트리거 추가
- 버킷 설정 → 아까 만들어 둔 버킷 추가
- 접두사 : images/
- 재귀호출 체크

### → 해당 버킷 images 폴더에 업로드 하면 resized-images에 리사이즈 된 이미지 자동생성