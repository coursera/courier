#!/bin/bash
script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $script_dir

printf 'Building docker image for running python3 test-suite...'
docker build --tag courier-python3-test . > /dev/null
printf 'Done\n\n'

docker run -v --rm $script_dir:/app courier-python3-test
