import { Union } from "./tslite-bindings/org.coursera.typerefs.Union";

const a: Union = {
  "org.coursera.records.Messag": {
    "titl": "title", // should be "title" not "titl"
    "body": "Hello, Courier."
  }
};
