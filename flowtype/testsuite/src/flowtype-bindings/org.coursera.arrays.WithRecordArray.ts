import { Fruits } from "./org.coursera.enums.Fruits";
import { Empty } from "./org.coursera.records.test.Empty";

export interface WithRecordArray {
  
  empties : Array<Empty>;
  
  fruits : Array<Fruits>;
}
