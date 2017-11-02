import { Simple } from "./org.coursera.records.test.Simple";

export type WithComplexTypesMapUnion = WithComplexTypesMapUnion.IntMember | WithComplexTypesMapUnion.StringMember | WithComplexTypesMapUnion.SimpleMember;
export module WithComplexTypesMapUnion {
  export interface WithComplexTypesMapUnionMember {
    [key: string]: number | string | Simple;
  }
  export interface IntMember extends WithComplexTypesMapUnionMember {
    "int": number;
  }
  export interface StringMember extends WithComplexTypesMapUnionMember {
    "string": string;
  }
  export interface SimpleMember extends WithComplexTypesMapUnionMember {
    "org.coursera.records.test.Simple": Simple;
  }
  export function unpack(union: WithComplexTypesMapUnion) {
    return {
      int: union["int"] as number,
      string$: union["string"] as string,
      simple: union["org.coursera.records.test.Simple"] as Simple
    };
  }
}
