var exec = require('cordova/exec');

exports.getRfid = function(success, error) {
    exec(success, error, "Rfid", "openDevice", []);
};

exports.getRfid = function(success, error) {
    exec(success, error, "Rfid", "closeDevice", []);
};

exports.getRfid = function(success, error) {
    exec(success, error, "Rfid", "scanCycle", []);
};

exports.getRfid = function(success, error) {
    exec(success, error, "Rfid", "scanCycleStop", []);
};