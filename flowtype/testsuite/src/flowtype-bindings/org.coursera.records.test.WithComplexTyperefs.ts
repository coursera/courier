import { UnionTyperef } from "./org.coursera.typerefs.UnionTyperef";
import { MapTyperef } from "./org.coursera.typerefs.MapTyperef";
import { EnumTyperef } from "./org.coursera.typerefs.EnumTyperef";
import { ArrayTyperef } from "./org.coursera.typerefs.ArrayTyperef";
import { RecordTyperef } from "./org.coursera.typerefs.RecordTyperef";

export interface WithComplexTyperefs {
  
  "enum" : EnumTyperef;
  
  record : RecordTyperef;
  
  map : MapTyperef;
  
  array : ArrayTyperef;
  
  union : UnionTyperef;
}
