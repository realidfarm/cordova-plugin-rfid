var exec = require('cordova/exec');

exports.openDevice = function(success, error) {
    exec(success, error, "Rfid", "openDevice", []);
};

exports.closeDevice = function(success, error) {
    exec(success, error, "Rfid", "closeDevice", []);
};

exports.scanCycle = function(success, error) {
    exec(success, error, "Rfid", "scanCycle", []);
};

exports.scanCycleStop = function(success, error) {
    exec(success, error, "Rfid", "scanCycleStop", []);
};