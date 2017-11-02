import { BooleanTyperef } from "./org.coursera.records.test.BooleanTyperef";
import { StringTyperef } from "./org.coursera.records.test.StringTyperef";
import { BytesTyperef } from "./org.coursera.records.test.BytesTyperef";
import { FloatTyperef } from "./org.coursera.records.test.FloatTyperef";
import { DoubleTyperef } from "./org.coursera.records.test.DoubleTyperef";
import { LongTyperef } from "./org.coursera.records.test.LongTyperef";
import { IntTyperef } from "./org.coursera.records.test.IntTyperef";

export interface WithPrimitiveTyperefs {
  
  intField : IntTyperef;
  
  longField : LongTyperef;
  
  floatField : FloatTyperef;
  
  doubleField : DoubleTyperef;
  
  booleanField : BooleanTyperef;
  
  stringField : StringTyperef;
  
  bytesField : BytesTyperef;
}
