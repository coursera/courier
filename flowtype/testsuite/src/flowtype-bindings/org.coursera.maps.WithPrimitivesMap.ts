import { Map } from "./CourierRuntime";

export interface WithPrimitivesMap {
  
  ints : Map<number>;
  
  longs : Map<number>;
  
  floats : Map<number>;
  
  doubles : Map<number>;
  
  booleans : Map<boolean>;
  
  strings : Map<string>;
  
  bytes : Map<string>;
}
