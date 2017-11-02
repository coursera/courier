import { Map } from "./CourierRuntime";
import { CustomRecord } from "./org.coursera.customtypes.CustomRecord";

export interface WithCustomRecord {
  
  custom : CustomRecord;
  
  customArray : Array<CustomRecord>;
  
  customMap : Map<CustomRecord>;
}
