# rfid Cordova Plugin

## 集成步骤

- 直接通过 url 安装：

        cordova plugin add https://github.com/realidfarm/cordova-plugin-rfid.git

- 或下载到本地安装：

        cordova plugin add Your_Plugin_Path

# API

## Methods

- [rfid.openDevice](#openDevice)
- [rfid.closeDevice](#closeDevice)
- [rfid.scanCycle](#scanCycle)

## openDevice

打开串口.

    rfid.openDevice();

## closeDevice

关闭串口.

    rfid.closeDevice();

## scanCycle

读取标签.

    rfid.scanCycle();


完整调用demo:
```js
    rfid.openDevice(function(data) {
        rfidscan = $interval(function() {
            rfid.scanCycle(function(data) {
                if (data) {
                    var tagid = data.split(":");
                    $scope.Marking.SlugNo = tagid[1];
                }
            }, function(error) {
                alert("error:" + error);
            });
        }, 400);
    }, function(error) {
        alert("error:" + error);
    });
```