import { Fruits } from "./org.coursera.enums.Fruits";
import { Map } from "./CourierRuntime";
import { Empty } from "./org.coursera.records.test.Empty";
import { Simple } from "./org.coursera.records.test.Simple";

export interface WithComplexTypesUnion {
  
  union : WithComplexTypesUnion.Union;
}
export module WithComplexTypesUnion {
  
  export type Union = Union.EmptyMember | Union.FruitsMember | Union.ArrayMember | Union.MapMember;
  export module Union {
    export interface UnionMember {
      [key: string]: Empty | Fruits | Array<Simple> | Map<Simple>;
    }
    export interface EmptyMember extends UnionMember {
      "org.coursera.records.test.Empty": Empty;
    }
    export interface FruitsMember extends UnionMember {
      "org.coursera.enums.Fruits": Fruits;
    }
    export interface ArrayMember extends UnionMember {
      "array": Array<Simple>;
    }
    export interface MapMember extends UnionMember {
      "map": Map<Simple>;
    }
    export function unpack(union: Union) {
      return {
        empty: union["org.coursera.records.test.Empty"] as Empty,
        fruits: union["org.coursera.enums.Fruits"] as Fruits,
        arraySimple: union["array"] as Array<Simple>,
        mapSimple: union["map"] as Map<Simple>
      };
    }
  }
}
