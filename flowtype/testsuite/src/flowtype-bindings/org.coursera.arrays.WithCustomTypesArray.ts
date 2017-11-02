import { Map } from "./CourierRuntime";
import { WithCustomTypesArrayUnion } from "./org.coursera.arrays.WithCustomTypesArrayUnion";
import { CustomInt } from "./org.coursera.customtypes.CustomInt";
import { Fixed8 } from "./org.coursera.fixed.Fixed8";
import { Simple } from "./org.coursera.records.test.Simple";

export interface WithCustomTypesArray {
  
  ints : Array<CustomInt>;
  
  arrays : Array<Array<Simple>>;
  
  maps : Array<Map<Simple>>;
  
  unions : Array<WithCustomTypesArrayUnion>;
  
  fixed : Array<Fixed8>;
}
