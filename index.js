import { NativeModules } from "react-native";

console.log("RnMobilePay is", NativeModules.RnMobilePay);
console.log("And native modules are", NativeModules);

function helper() {
  console.log("Helper here, RnMobilepay is", NativeModules.RnMobilePay);
  console.log("Helper here, NativeModules", NativeModules);
}

module.exports = {
  helper,
  MobilePay: NativeModules.RnMobilePay
};
