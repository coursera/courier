import { Simple } from "./org.coursera.records.test.Simple";

export type WithCustomTypesArrayUnion = WithCustomTypesArrayUnion.IntMember | WithCustomTypesArrayUnion.StringMember | WithCustomTypesArrayUnion.SimpleMember;
export module WithCustomTypesArrayUnion {
  export interface WithCustomTypesArrayUnionMember {
    [key: string]: number | string | Simple;
  }
  export interface IntMember extends WithCustomTypesArrayUnionMember {
    "int": number;
  }
  export interface StringMember extends WithCustomTypesArrayUnionMember {
    "string": string;
  }
  export interface SimpleMember extends WithCustomTypesArrayUnionMember {
    "org.coursera.records.test.Simple": Simple;
  }
  export function unpack(union: WithCustomTypesArrayUnion) {
    return {
      int: union["int"] as number,
      string$: union["string"] as string,
      simple: union["org.coursera.records.test.Simple"] as Simple
    };
  }
}
