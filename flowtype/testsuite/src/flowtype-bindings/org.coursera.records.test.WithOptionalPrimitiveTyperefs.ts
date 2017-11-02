import { OptionalBooleanTyperef } from "./org.coursera.records.test.OptionalBooleanTyperef";
import { OptionalIntTyperef } from "./org.coursera.records.test.OptionalIntTyperef";
import { OptionalStringTyperef } from "./org.coursera.records.test.OptionalStringTyperef";
import { OptionalFloatTyperef } from "./org.coursera.records.test.OptionalFloatTyperef";
import { OptionalLongTyperef } from "./org.coursera.records.test.OptionalLongTyperef";
import { OptionalDoubleTyperef } from "./org.coursera.records.test.OptionalDoubleTyperef";
import { OptionalBytesTyperef } from "./org.coursera.records.test.OptionalBytesTyperef";

export interface WithOptionalPrimitiveTyperefs {
  
  intField ?: OptionalIntTyperef;
  
  longField ?: OptionalLongTyperef;
  
  floatField ?: OptionalFloatTyperef;
  
  doubleField ?: OptionalDoubleTyperef;
  
  booleanField ?: OptionalBooleanTyperef;
  
  stringField ?: OptionalStringTyperef;
  
  bytesField ?: OptionalBytesTyperef;
}
