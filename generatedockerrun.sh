echo -e "{
  \"AWSEBDockerrunVersion\": \"1\",
  \"Image\": {
    \"Name\": \"bitbyte01/cargaraage:${CIRCLE_BUILD_NUM}\",
    \"Update\": \"true\"
  },
  \"Ports\": [
    {
      \"ContainerPort\": 8080
    }
  ]
}" > Dockerrun.aws.json