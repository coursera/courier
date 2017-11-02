import { Map } from "./CourierRuntime";
import { Fruits } from "./org.coursera.enums.Fruits";
import { Empty } from "./org.coursera.records.test.Empty";
import { WithComplexTypesMapUnion } from "./org.coursera.maps.WithComplexTypesMapUnion";
import { Fixed8 } from "./org.coursera.fixed.Fixed8";
import { Simple } from "./org.coursera.records.test.Simple";

export interface WithComplexTypesMap {
  
  empties : Map<Empty>;
  
  fruits : Map<Fruits>;
  
  arrays : Map<Array<Simple>>;
  
  maps : Map<Map<Simple>>;
  
  unions : Map<WithComplexTypesMapUnion>;
  
  fixed : Map<Fixed8>;
}
