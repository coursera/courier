import { Map } from "./CourierRuntime";
import { StringId } from "./org.coursera.customtypes.StringId";
import { ByteId } from "./org.coursera.customtypes.ByteId";
import { DoubleId } from "./org.coursera.customtypes.DoubleId";
import { CaseClassCustomIntWrapper } from "./org.coursera.customtypes.CaseClassCustomIntWrapper";
import { BooleanId } from "./org.coursera.customtypes.BooleanId";
import { BoxedIntId } from "./org.coursera.customtypes.BoxedIntId";
import { ShortId } from "./org.coursera.customtypes.ShortId";
import { IntId } from "./org.coursera.customtypes.IntId";
import { CharId } from "./org.coursera.customtypes.CharId";
import { LongId } from "./org.coursera.customtypes.LongId";
import { FloatId } from "./org.coursera.customtypes.FloatId";
import { CaseClassStringIdWrapper } from "./org.coursera.customtypes.CaseClassStringIdWrapper";

export interface WithCaseClassCustomType {
  
  short : ShortId;
  
  byte : ByteId;
  
  char : CharId;
  
  int : IntId;
  
  long : LongId;
  
  float : FloatId;
  
  double : DoubleId;
  
  "string" : StringId;
  
  "boolean" : BooleanId;
  
  boxedInt : BoxedIntId;
  
  map : Map<IntId>;
  
  mapKeys : Map<string>;
  
  array : Array<IntId>;
  
  chained : CaseClassStringIdWrapper;
  
  chainedToCoercer : CaseClassCustomIntWrapper;
}
