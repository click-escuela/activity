#!/bin/bash
chmod +x /home/ec2-user/server/activity/logs
chmod +x /home/ec2-user/server/activity/logs/error.log
chmod +x /home/ec2-user/server/activity/logs/debug.log
var="$(cat /home/ec2-user/server/activity/activity-service.pid)"
sudo kill $var
sudo rm -rf /home/ec2-user/server/activity/activity-service.pid