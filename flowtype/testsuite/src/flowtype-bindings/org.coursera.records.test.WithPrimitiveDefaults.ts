import { Fruits } from "./org.coursera.enums.Fruits";

export interface WithPrimitiveDefaults {
  
  intWithDefault : number;
  
  longWithDefault : number;
  
  floatWithDefault : number;
  
  doubleWithDefault : number;
  
  booleanWithDefault : boolean;
  
  stringWithDefault : string;
  
  bytesWithDefault : string;
  
  enumWithDefault : Fruits;
}
