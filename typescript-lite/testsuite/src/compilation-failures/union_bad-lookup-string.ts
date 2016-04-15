import { Union } from "./tslite-bindings/org.coursera.typerefs.Union";

const a: Union = {
  "org.coursera.records.Messag": { // should be "Message" not "Messag"
    "title": "title",
    "body": "Hello, Courier."
  }
};
