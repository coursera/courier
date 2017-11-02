import { Map } from "./CourierRuntime";

export interface WithTypedKeyMap {
  
  ints : Map<string>;
  
  longs : Map<string>;
  
  floats : Map<string>;
  
  doubles : Map<string>;
  
  booleans : Map<string>;
  
  strings : Map<string>;
  
  bytes : Map<string>;
  
  record : Map<string>;
  
  array : Map<string>;
  
  "enum" : Map<string>;
  
  custom : Map<string>;
  
  fixed : Map<string>;
  
  samePackageEnum ?: Map<string>;
}
